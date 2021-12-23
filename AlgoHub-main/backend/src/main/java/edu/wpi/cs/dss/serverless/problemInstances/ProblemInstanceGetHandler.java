package edu.wpi.cs.dss.serverless.problemInstances;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import edu.wpi.cs.dss.serverless.generic.GenericResponse;
import edu.wpi.cs.dss.serverless.problemInstances.http.ProblemInstanceGetRequest;
import edu.wpi.cs.dss.serverless.problemInstances.http.ProblemInstanceGetResponse;
import edu.wpi.cs.dss.serverless.util.DataSource;
import edu.wpi.cs.dss.serverless.util.ErrorMessage;
import edu.wpi.cs.dss.serverless.util.HttpStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProblemInstanceGetHandler implements RequestHandler<ProblemInstanceGetRequest, GenericResponse> {

    private LambdaLogger logger;

    @Override
    public GenericResponse handleRequest(ProblemInstanceGetRequest request, Context context) {
        logger = context.getLogger();
        logger.log("Received a get problem instance request from AWS Lambda: \n" + request);

        // find problem instance by id
        final GenericResponse response = getProblemInstanceById(request);
        logger.log("Sent a get problem instance response to AWS Lambda:\n" + response);

        return response;
    }

    private GenericResponse getProblemInstanceById(ProblemInstanceGetRequest request) {
        // extracting problem instance id from get problem instance request
        final String id = request.getId();

        //create a sql query
        final String query = "SELECT * FROM problem_instance WHERE id = ?";

        try (final Connection connection = DataSource.getConnection(logger);
             final PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            logger.log("Successfully connected to db!");

            preparedStatement.setString(1, id);

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    final String datasetFileName = resultSet.getString(2);
                    final String datasetSize = resultSet.getString(3);
                    final String problemType = resultSet.getString(4);
                    final String algorithmId= resultSet.getString(5);
                    final String authorId = resultSet.getString(6);

                    return ProblemInstanceGetResponse.builder()
                            .statusCode(HttpStatus.SUCCESS.getValue())
                            .datasetFilename(datasetFileName)
                            .datasetSize(datasetSize)
                            .problemType(problemType)
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
