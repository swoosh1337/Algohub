package edu.wpi.cs.dss.serverless.problemInstances;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import edu.wpi.cs.dss.serverless.generic.GenericRemoveRequest;
import edu.wpi.cs.dss.serverless.generic.GenericResponse;
import edu.wpi.cs.dss.serverless.problemInstances.http.ProblemInstanceAddResponse;
import edu.wpi.cs.dss.serverless.util.DataSource;
import edu.wpi.cs.dss.serverless.util.ErrorMessage;
import edu.wpi.cs.dss.serverless.util.HttpStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProblemInstanceRemoveHandler implements RequestHandler<GenericRemoveRequest, GenericResponse> {

    private LambdaLogger logger;

    private static final String AMAZON_S3_BUCKET_NAME = "cs509-algohub-storage";
    private static final String AMAZON_S3_FOLDER_NAME = "datasets/";

    private final AmazonS3 amazonS3 = AmazonS3ClientBuilder.standard().withRegion(Regions.US_EAST_1).build();

    @Override
    public GenericResponse handleRequest(GenericRemoveRequest request, Context context) {
        logger = context.getLogger();
        logger.log("Received an add problem instance request from AWS Lambda:\n" + request);

        // save problem instance to the database
        final GenericResponse response = deleteProblemInstanceFromDB(request);
        logger.log("Sent an add problem instance response to AWS Lambda:\n" + response);

        return response;
    }

    private GenericResponse deleteProblemInstanceFromDB(GenericRemoveRequest request) {
        final String id = request.getId();
        final String deleteQuery = "DELETE FROM problem_instance WHERE id = ?";
        final String findQuery = "SELECT * FROM problem_instance WHERE id = ?";

        try (final Connection connection = DataSource.getConnection(logger);
             final PreparedStatement findPreparedStatement = connection.prepareStatement(findQuery);
             final PreparedStatement deletePreparedStatement = connection.prepareStatement(deleteQuery)
        ) {
            logger.log("Successfully connected to db!");

            findPreparedStatement.setString(1, id);

            String datasetFileName = null;
            try (final ResultSet resultSet = findPreparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    datasetFileName = resultSet.getString(2);
                }
            }

            deletePreparedStatement.setString(1, id);
            final int rowsAffected = deletePreparedStatement.executeUpdate();
            logger.log("Delete from problem instance table statement has affected " + rowsAffected + " rows!");

            // delete from S3 Bucket
            if (rowsAffected == 1) {
                deleteFromS3(datasetFileName);
            }

            return ProblemInstanceAddResponse.builder()
                    .statusCode(HttpStatus.SUCCESS.getValue())
                    .build();

        } catch (SQLException e) {
            e.printStackTrace();
            logger.log(ErrorMessage.SQL_EXECUTION_EXCEPTION.getValue());
            return GenericResponse.builder()
                    .statusCode(HttpStatus.BAD_REQUEST.getValue())
                    .error(ErrorMessage.SQL_EXECUTION_EXCEPTION.getValue())
                    .build();
        }
    }


    private void deleteFromS3(String filename) {
        logger.log("Deleting an uploaded problem instance dataset from AWS S3 ...\n");

        try {
            amazonS3.deleteObject(AMAZON_S3_BUCKET_NAME, AMAZON_S3_FOLDER_NAME + filename);
            logger.log("Successfully delete an uploaded problem instance dataset from AWS S3 ...\n");
        } catch (Exception e) {
            logger.log(ErrorMessage.AWS_S3_DELETE_EXCEPTION.getValue());
            e.printStackTrace();
        }
    }
}
