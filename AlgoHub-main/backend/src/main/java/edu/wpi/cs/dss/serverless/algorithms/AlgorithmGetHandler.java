package edu.wpi.cs.dss.serverless.algorithms;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import edu.wpi.cs.dss.serverless.algorithms.http.AlgorithmGetRequest;
import edu.wpi.cs.dss.serverless.algorithms.http.AlgorithmGetResponse;
import edu.wpi.cs.dss.serverless.generic.GenericResponse;
import edu.wpi.cs.dss.serverless.util.DataSource;
import edu.wpi.cs.dss.serverless.util.ErrorMessage;
import edu.wpi.cs.dss.serverless.util.HttpStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AlgorithmGetHandler implements RequestHandler<AlgorithmGetRequest, GenericResponse> {

    private LambdaLogger logger;

    @Override
    public GenericResponse handleRequest(AlgorithmGetRequest request, Context context) {
        logger = context.getLogger();
        logger.log("Received a get algorithm request from AWS Lambda: \n" + request);

        // find algorithm by id
        final GenericResponse response = findById(request);
        logger.log("Sent a get algorithm response to AWS Lambda:\n" + response);

        return response;
    }

    private GenericResponse findById(AlgorithmGetRequest request) {
        // extracting algorithm id from get algorithm request
        final String id = request.getId();

        // create sql query
        final String query = "SELECT * FROM algorithm WHERE id = ?";

        // execute query
        try (final Connection connection = DataSource.getConnection(logger);
             final PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            logger.log("Successfully connected to db!");

            preparedStatement.setString(1, id);

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    final String name = resultSet.getString(2);
                    final String description = resultSet.getString(3);
                    final String classificationId = resultSet.getString(4);
                    final String authorId = resultSet.getString(5);

                    return AlgorithmGetResponse.builder()
                            .statusCode(HttpStatus.SUCCESS.getValue())
                            .classificationId(classificationId)
                            .authorId(authorId)
                            .description(description)
                            .name(name)
                            .id(id)
                            .build();
                }
            }

            return GenericResponse.builder()
                    .error(ErrorMessage.RESOURCE_NOT_FOUND_EXCEPTION.getValue())
                    .statusCode(HttpStatus.BAD_REQUEST.getValue())
                    .build();

        } catch (SQLException e) {
            e.printStackTrace();
            logger.log(ErrorMessage.SQL_EXECUTION_EXCEPTION.getValue());
            return AlgorithmGetResponse.builder()
                    .statusCode(HttpStatus.BAD_REQUEST.getValue())
                    .error(ErrorMessage.SQL_EXECUTION_EXCEPTION.getValue())
                    .build();
        }
    }
}
