package edu.wpi.cs.dss.serverless.algorithms;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import edu.wpi.cs.dss.serverless.algorithms.http.AlgorithmAddRequest;
import edu.wpi.cs.dss.serverless.algorithms.http.AlgorithmAddResponse;
import edu.wpi.cs.dss.serverless.generic.GenericResponse;
import edu.wpi.cs.dss.serverless.util.DataSource;
import edu.wpi.cs.dss.serverless.util.ErrorMessage;
import edu.wpi.cs.dss.serverless.util.HttpStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class AlgorithmAddHandler implements RequestHandler<AlgorithmAddRequest, GenericResponse> {

    private LambdaLogger logger;

    @Override
    public GenericResponse handleRequest(AlgorithmAddRequest request, Context context) {
        logger = context.getLogger();
        logger.log("Received an add algorithm request from AWS Lambda:\n" + request);

        // save algorithm to the database
        final GenericResponse response = save(request);
        logger.log("Sent an add algorithm response to AWS Lambda:\n" + response);

        return response;
    }

    private GenericResponse save(AlgorithmAddRequest request) {
        final String name = request.getName();
        final String authorId = request.getAuthorId();
        final String id = UUID.randomUUID().toString();
        final String description = request.getDescription();
        final String classificationId = request.getClassificationId();

        // creating a sql query
        final String query = "INSERT INTO algorithm (id, name, description, classification_id, author_id) VALUES (?, ?, ?, ?, ?)";

        try (final Connection connection = DataSource.getConnection(logger);
             final PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            logger.log("Successfully connected to db!");

            preparedStatement.setString(1, id);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, description);
            preparedStatement.setString(4, classificationId);
            preparedStatement.setString(5, authorId);

            final int rowsAffected = preparedStatement.executeUpdate();
            logger.log("Insert algorithm statement has affected " + rowsAffected + " rows!");

            return AlgorithmAddResponse.builder()
                    .statusCode(HttpStatus.SUCCESS.getValue())
                    .id(id)
                    .name(name)
                    .description(description)
                    .authorId(authorId)
                    .classificationId(classificationId)
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
