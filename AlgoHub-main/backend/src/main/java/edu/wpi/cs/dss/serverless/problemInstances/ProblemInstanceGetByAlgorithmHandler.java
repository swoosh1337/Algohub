package edu.wpi.cs.dss.serverless.problemInstances;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import edu.wpi.cs.dss.serverless.generic.GenericResponse;
import edu.wpi.cs.dss.serverless.problemInstances.http.ProblemInstanceGetByAlgorithmRequest;
import edu.wpi.cs.dss.serverless.problemInstances.http.ProblemInstanceGetByAlgorithmResponse;
import edu.wpi.cs.dss.serverless.problemInstances.model.ProblemInstance;
import edu.wpi.cs.dss.serverless.util.DataSource;
import edu.wpi.cs.dss.serverless.util.ErrorMessage;
import edu.wpi.cs.dss.serverless.util.HttpStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProblemInstanceGetByAlgorithmHandler implements RequestHandler<ProblemInstanceGetByAlgorithmRequest, GenericResponse> {

    private LambdaLogger logger;

    @Override
    public GenericResponse handleRequest(ProblemInstanceGetByAlgorithmRequest request, Context context) {
        logger = context.getLogger();
        logger.log("Received a get problem instances by algorithm request from AWS Lambda: \n" + request);

        // find problem instance by id
        final GenericResponse response = getProblemInstanceByAlgorithmId(request);
        logger.log("Sent a get problem instances by algorithm response to AWS Lambda:\n" + response);

        return response;
    }

    private GenericResponse getProblemInstanceByAlgorithmId(ProblemInstanceGetByAlgorithmRequest request) {
        // extracting problem instance id from get problem instance request
        final String algorithmId = request.getId();

        //create a sql query
        final String query = "SELECT * FROM problem_instance WHERE algorithm_id = ?";

        try (final Connection connection = DataSource.getConnection(logger);
             final PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            logger.log("Successfully connected to db!");

            preparedStatement.setString(1, algorithmId);

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                final List<ProblemInstance> problemInstances = new ArrayList<>();

                while(resultSet.next()) {
                    final String id = resultSet.getString(1);
                    final String datasetFileName = resultSet.getString(2);
                    final Integer datasetSize = resultSet.getInt(3);
                    final String problemType = resultSet.getString(4);
                    final String authorId = resultSet.getString(6);

                    problemInstances.add(new ProblemInstance(id, authorId, problemType, algorithmId, datasetSize, datasetFileName));
                }

                return ProblemInstanceGetByAlgorithmResponse.builder()
                        .statusCode(HttpStatus.SUCCESS.getValue())
                        .problemInstances(problemInstances)
                        .build();
            }

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
