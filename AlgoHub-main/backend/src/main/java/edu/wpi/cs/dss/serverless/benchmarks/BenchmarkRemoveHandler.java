package edu.wpi.cs.dss.serverless.benchmarks;

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

public class BenchmarkRemoveHandler implements RequestHandler<GenericRemoveRequest, GenericResponse> {

    private LambdaLogger logger;

    @Override
    public GenericResponse handleRequest(GenericRemoveRequest request, Context context) {
        logger = context.getLogger();
        logger.log("Received a remove benchmark request from client:\n" + request);

        // save problem instance to the database
        final GenericResponse response = deleteBenchmarkFromDB(request);
        logger.log("Sent a remove benchmark response from AWS Lambda:\n" + response);

        return response;
    }

    private GenericResponse deleteBenchmarkFromDB(GenericRemoveRequest request) {
        final String id = request.getId();
        final String query = "DELETE FROM benchmark WHERE id = ?";

        try (final Connection connection = DataSource.getConnection(logger);
             final PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            logger.log("Successfully connected to db!");

            preparedStatement.setString(1, id);

            final int rowsAffected = preparedStatement.executeUpdate();
            logger.log("Delete from benchmark table statement has affected " + rowsAffected + " rows!");

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