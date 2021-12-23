package edu.wpi.cs.dss.serverless.problemInstances;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import edu.wpi.cs.dss.serverless.generic.GenericResponse;
import edu.wpi.cs.dss.serverless.problemInstances.http.ProblemInstanceAddRequest;
import edu.wpi.cs.dss.serverless.problemInstances.http.ProblemInstanceAddResponse;
import edu.wpi.cs.dss.serverless.util.DataSource;
import edu.wpi.cs.dss.serverless.util.ErrorMessage;
import edu.wpi.cs.dss.serverless.util.HttpStatus;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Base64;
import java.util.UUID;

public class ProblemInstanceAddHandler implements RequestHandler<ProblemInstanceAddRequest, GenericResponse> {

    private LambdaLogger logger;
    private static final String AMAZON_S3_BUCKET_NAME = "cs509-algohub-storage";
    private static final String AMAZON_S3_FOLDER_NAME = "datasets/";

    private final AmazonS3 amazonS3 = AmazonS3ClientBuilder.standard().withRegion(Regions.US_EAST_1).build();

    @Override
    public GenericResponse handleRequest(ProblemInstanceAddRequest request, Context context) {
        logger = context.getLogger();
        logger.log("Received an add problem instance request from AWS Lambda:\n" + request);

        // save problem instance to the database
        final GenericResponse response = saveProblemInstanceDataset(request);
        logger.log("Sent an add problem instance response to AWS Lambda:\n" + response);

        return response;
    }


    private GenericResponse saveProblemInstanceDataset(ProblemInstanceAddRequest request) {
        final String id = UUID.randomUUID().toString();
        final String datasetFilename = request.getDatasetFilename();
        final String datasetSize = request.getDatasetSize();
        final String problemType = request.getProblemType();
        final String algorithmId = request.getAlgorithmId();
        final String authorId = request.getAuthorId();

        // upload dataset
        if (!uploadToS3(datasetFilename, request.getSourceCodeBase64())) {
            return GenericResponse.builder()
                    .error(ErrorMessage.AWS_S3_UPLOAD_EXCEPTION.getValue())
                    .statusCode(HttpStatus.BAD_REQUEST.getValue())
                    .build();
        }

        // Update RDS
        final String query = "INSERT INTO problem_instance (id, dataset_filename, dataset_size, problem_type, algorithm_id, author_id) VALUES (?, ?, ?, ?, ?, ?)";

        try (final Connection connection = DataSource.getConnection(logger);
             final PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            logger.log("Successfully connected to db!\n");

            preparedStatement.setString(1, id);
            preparedStatement.setString(2, datasetFilename);
            preparedStatement.setString(3, datasetSize);
            preparedStatement.setString(4, problemType);
            preparedStatement.setString(5, algorithmId);
            preparedStatement.setString(6, authorId);

            final int rowsAffected = preparedStatement.executeUpdate();
            logger.log("Insert problem instance statement has affected " + rowsAffected + " rows!\n");

            return ProblemInstanceAddResponse.builder()
                    .statusCode(HttpStatus.SUCCESS.getValue())
                    .id(id)
                    .build();

        } catch (SQLException e) {
            e.printStackTrace();
            logger.log(ErrorMessage.SQL_EXECUTION_EXCEPTION.getValue());
            deleteFromS3(datasetFilename);
            return GenericResponse.builder()
                    .statusCode(HttpStatus.BAD_REQUEST.getValue())
                    .error(ErrorMessage.SQL_EXECUTION_EXCEPTION.getValue())
                    .build();
        }
    }

    // S3 Bucket I/O
    private boolean uploadToS3(String filename, String sourceCodeBase64) {
        final byte[] sourceCodeAsBytes = Base64.getDecoder().decode(sourceCodeBase64);
        final String sourceCode = new String(sourceCodeAsBytes, StandardCharsets.UTF_8);

        logger.log("Uploading a problem instance dataset to AWS S3 ...\n");

        try {
            amazonS3.putObject(AMAZON_S3_BUCKET_NAME, AMAZON_S3_FOLDER_NAME + filename, sourceCode);
            logger.log("Successfully uploaded an problem instance dataset to AWS S3 ...\n");
        } catch (Exception e) {
            logger.log(ErrorMessage.AWS_S3_UPLOAD_EXCEPTION.getValue());
            e.printStackTrace();
            return false;
        }

        return true;
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
