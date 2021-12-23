package edu.wpi.cs.dss.serverless.algorithms;

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
import java.sql.SQLException;

public class AlgorithmRemoveHandler implements RequestHandler<GenericRemoveRequest, GenericResponse> {

    private LambdaLogger logger;

    @Override
    public GenericResponse handleRequest(GenericRemoveRequest request, Context context) {
        logger = context.getLogger();
        logger.log("Received a remove algorithm request from AWS Lambda:\n" + request);

        final GenericResponse response = remove(request);
        logger.log("Sent a remove algorithm response to AWS Lambda:\n" + response);

        return response;
    }

    private GenericResponse remove(GenericRemoveRequest request) {
        final String id = request.getId();
        final String query = "DELETE FROM algorithm WHERE id=?";

        try (final Connection connection = DataSource.getConnection(logger);
             final PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            logger.log("Successfully connected to db!");

            preparedStatement.setString(1, id);

            final int rowsAffected = preparedStatement.executeUpdate();
            logger.log("Delete algorithm statement has affected " + rowsAffected + " rows!");

            return GenericResponse.builder()
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
}
