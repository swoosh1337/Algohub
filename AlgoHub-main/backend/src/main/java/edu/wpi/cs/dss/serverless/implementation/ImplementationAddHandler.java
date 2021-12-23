package edu.wpi.cs.dss.serverless.implementation;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import edu.wpi.cs.dss.serverless.generic.GenericResponse;
import edu.wpi.cs.dss.serverless.util.DataSource;
import edu.wpi.cs.dss.serverless.implementation.http.ImplementationAddRequest;
import edu.wpi.cs.dss.serverless.implementation.http.ImplementationAddResponse;
import edu.wpi.cs.dss.serverless.util.ErrorMessage;
import edu.wpi.cs.dss.serverless.util.HttpStatus;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Base64;
import java.util.UUID;

public class ImplementationAddHandler implements RequestHandler<ImplementationAddRequest, GenericResponse> {

    private static final String AMAZON_S3_BUCKET_NAME = "cs509-algohub-storage";
    private static final String AMAZON_S3_FOLDER_NAME = "implementations/";

    private final AmazonS3 amazonS3 = AmazonS3ClientBuilder.standard().withRegion(Regions.US_EAST_1).build();

    private LambdaLogger logger;

    @Override
    public GenericResponse handleRequest(ImplementationAddRequest request, Context context) {
        logger = context.getLogger();
        logger.log("Received an add implementation request from AWS Lambda: \n" + request);

        // save implementation to the database and upload to the S3 bucket
        final GenericResponse response = save(request);
        logger.log("Sent an add implementation response to AWS Lambda:\n" + response);

        return response;
    }

    private GenericResponse save(ImplementationAddRequest request) {
        final String id = generateId();
        final String name = request.getName();
        final String authorId = request.getAuthorId();
        final String algorithmId = request.getAlgorithmId();
        final String filename = generateFilename(id, request);

        if (!uploadToS3(filename, request.getSourceCodeBase64())) {
            return GenericResponse.builder()
                    .error(ErrorMessage.AWS_S3_UPLOAD_EXCEPTION.getValue())
                    .statusCode(HttpStatus.BAD_REQUEST.getValue())
                    .build();
        }

        // creating a sql query
        final String query = "INSERT INTO implementation (id, name, filename, algorithm_id, author_id) VALUES (?,?,?,?,?)";

        try (final Connection connection = DataSource.getConnection(logger);
             final PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            logger.log("Successfully connected to db!");

            preparedStatement.setString(1, id);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, filename);
            preparedStatement.setString(4, algorithmId);
            preparedStatement.setString(5, authorId);

            final int rowsAffected = preparedStatement.executeUpdate();
            logger.log("Insert algorithm statement has affected " + rowsAffected + " rows!");

            return ImplementationAddResponse.builder()
                    .statusCode(HttpStatus.SUCCESS.getValue())
                    .id(id)
                    .filename(filename)
                    .build();

        } catch (SQLException e) {
            e.printStackTrace();
            logger.log(ErrorMessage.SQL_EXECUTION_EXCEPTION.getValue());
            deleteFromS3(filename);
            return GenericResponse.builder()
                    .statusCode(HttpStatus.BAD_REQUEST.getValue())
                    .error(ErrorMessage.SQL_EXECUTION_EXCEPTION.getValue())
                    .build();
        }
    }

    private String generateId() {
        return UUID.randomUUID().toString();
    }

    private String generateFilename(String id, ImplementationAddRequest request) {
        final String name = request.getName().toLowerCase();
        final String extension = request.getExtension().toLowerCase();
        final String algorithmName = request.getAlgorithmName().toLowerCase();

        return id + "_" + algorithmName + "_" + name + "." + extension;
    }

    private boolean uploadToS3(String filename, String sourceCodeBase64) {
        final byte[] sourceCodeAsBytes = Base64.getDecoder().decode(sourceCodeBase64);
        final String sourceCode = new String(sourceCodeAsBytes, StandardCharsets.UTF_8);

        logger.log("Uploading an implementation to AWS S3 ...");

        try {
            amazonS3.putObject(AMAZON_S3_BUCKET_NAME, AMAZON_S3_FOLDER_NAME + filename, sourceCode);
            logger.log("Successfully uploaded an implementation to AWS S3 ...");
        } catch (Exception e) {
            logger.log(ErrorMessage.AWS_S3_UPLOAD_EXCEPTION.getValue());
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private void deleteFromS3(String filename) {
        logger.log("Deleting an uploaded implementation from AWS S3 ...");

        try {
            amazonS3.deleteObject(AMAZON_S3_BUCKET_NAME, AMAZON_S3_FOLDER_NAME + filename);
            logger.log("Successfully delete an uploaded implementation from AWS S3 ...");
        } catch (Exception e) {
            logger.log(ErrorMessage.AWS_S3_DELETE_EXCEPTION.getValue());
            e.printStackTrace();
        }
    }
}
