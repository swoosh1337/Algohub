package edu.wpi.cs.dss.serverless.classifications;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import edu.wpi.cs.dss.serverless.generic.GenericRemoveRequest;
import edu.wpi.cs.dss.serverless.generic.GenericResponse;
import edu.wpi.cs.dss.serverless.util.DataSource;
import edu.wpi.cs.dss.serverless.util.ErrorMessage;
import edu.wpi.cs.dss.serverless.util.HttpStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClassificationRemoveHandler implements RequestHandler<GenericRemoveRequest, GenericResponse> {

    private static final String EMPTY_STRING = "";

    private LambdaLogger logger;

    @Override
    public GenericResponse handleRequest(GenericRemoveRequest request, Context context) {
        logger = context.getLogger();
        logger.log("Received a remove classification request from AWS Lambda:\n" + request);

        final GenericResponse response = remove(request);
        logger.log("Sent a remove classification response to AWS Lambda:\n" + response);

        return response;
    }

    private GenericResponse remove(GenericRemoveRequest request) {
        final String id = request.getId();
        final String removeClassificationQuery = "DELETE FROM classification WHERE id=?";
        final String selectClassificationParentIdQuery = "SELECT parent_id FROM classification WHERE id=?";
        final String countAlgorithmsByClassificationIdQuery = "SELECT count(*) FROM algorithm WHERE classification_id=?";
        final String updateAlgorithmsByClassificationIdQuery = "UPDATE algorithm SET classification_id=? WHERE classification_id=?";

        try (final Connection connection = DataSource.getConnection(logger);
             final PreparedStatement removeClassificationPreparedStatement = connection.prepareStatement(removeClassificationQuery);
             final PreparedStatement selectClassificationParentIdPreparedStatement = connection.prepareStatement(selectClassificationParentIdQuery);
             final PreparedStatement countAlgorithmsByClassificationIdPreparedStatement = connection.prepareStatement(countAlgorithmsByClassificationIdQuery);
             final PreparedStatement updateAlgorithmsByClassificationIdPreparedStatement = connection.prepareStatement(updateAlgorithmsByClassificationIdQuery)
        ) {
            logger.log("Successfully connected to db!");

            final String parentId = getParentId(id, selectClassificationParentIdPreparedStatement);
            logger.log("Fetch parent id: " + parentId);

            if (EMPTY_STRING.equals(parentId)) {
                return GenericResponse.builder()
                        .error(ErrorMessage.RESOURCE_NOT_FOUND_EXCEPTION.getValue())
                        .statusCode(HttpStatus.BAD_REQUEST.getValue())
                        .build();
            }

            final long relatedAlgorithms = countRelatedAlgorithms(id, countAlgorithmsByClassificationIdPreparedStatement);
            logger.log("Amount of related algorithms = " + relatedAlgorithms);

            if (parentId == null && relatedAlgorithms > 0) {
                return GenericResponse.builder()
                        .error(ErrorMessage.TOP_LEVEL_CLASSIFICATION_DELETE_EXCEPTION.getValue())
                        .statusCode(HttpStatus.BAD_REQUEST.getValue())
                        .build();
            }

            // set values for update query
            updateAlgorithmsByClassificationIdPreparedStatement.setString(1, parentId);
            updateAlgorithmsByClassificationIdPreparedStatement.setString(2, id);

            final int updateAlgorithmRowsAffected = updateAlgorithmsByClassificationIdPreparedStatement.executeUpdate();
            logger.log("Update algorithm by classification id statement has affected " + updateAlgorithmRowsAffected + " rows!");

            // set values for remove query
            removeClassificationPreparedStatement.setString(1, id);

            final int removeClassificationRowsAffected = removeClassificationPreparedStatement.executeUpdate();
            logger.log("Delete classification statement has affected " + removeClassificationRowsAffected + " rows!");

            return GenericResponse.builder()
                    .statusCode(HttpStatus.SUCCESS.getValue())
                    .error("")
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

    private String getParentId(String id, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, id);

        final String parentId;
        try (final ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                parentId = resultSet.getString(1);
            } else {
                parentId = EMPTY_STRING;
            }
        }

        return parentId;
    }

    private long countRelatedAlgorithms(String id, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, id);

        final long relatedAlgorithms;
        try (final ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                relatedAlgorithms = resultSet.getLong(1);
            } else {
                relatedAlgorithms = 0L;
            }
        }

        return relatedAlgorithms;
    }
}
