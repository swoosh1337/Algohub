package edu.wpi.cs.dss.serverless.implementation;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import edu.wpi.cs.dss.serverless.generic.GenericResponse;
import edu.wpi.cs.dss.serverless.util.DataSource;
import edu.wpi.cs.dss.serverless.implementation.http.ImplementationGetRequest;
import edu.wpi.cs.dss.serverless.implementation.http.ImplementationGetResponse;
import edu.wpi.cs.dss.serverless.util.ErrorMessage;
import edu.wpi.cs.dss.serverless.util.HttpStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ImplementationGetHandler implements RequestHandler<ImplementationGetRequest, GenericResponse> {

    private LambdaLogger logger;

    @Override
    public GenericResponse handleRequest(ImplementationGetRequest request, Context context) {
        logger = context.getLogger();
        logger.log("Received a get implementation request from AWS Lambda: \n" + request);

        // find algorithm by id
        final GenericResponse response = findById(request);
        logger.log("Sent a get implementation response to AWS Lambda:\n" + response);

        return response;
    }

    private GenericResponse findById(ImplementationGetRequest request) {
        // extracting implementation id from get implementation request
        final String id = request.getId();

        //create a sql query
        final String query = "SELECT * FROM implementation WHERE id = ?";

        try (final Connection connection = DataSource.getConnection(logger);
             final PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            logger.log("Successfully connected to db!");

            preparedStatement.setString(1, id);

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    final String programmingLanguage = resultSet.getString(2);
                    final String filename = resultSet.getString(3);
                    final String algorithmId = resultSet.getString(4);
                    final String authorId = resultSet.getString(5);

                    return ImplementationGetResponse.builder()
                            .statusCode(HttpStatus.SUCCESS.getValue())
                            .programmingLanguage(programmingLanguage)
                            .filename(filename)
                            .algorithmId(algorithmId)
                            .authorId(authorId)
                            .build();
                }
            }

            return GenericResponse.builder()
                    .statusCode(HttpStatus.BAD_REQUEST.getValue())
                    .error(ErrorMessage.RESOURCE_NOT_FOUND_EXCEPTION.getValue())
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
