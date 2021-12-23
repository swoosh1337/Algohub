package edu.wpi.cs.dss.serverless.classifications;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import edu.wpi.cs.dss.serverless.classifications.http.ClassificationAddRequest;
import edu.wpi.cs.dss.serverless.classifications.http.ClassificationAddResponse;
import edu.wpi.cs.dss.serverless.generic.GenericResponse;
import edu.wpi.cs.dss.serverless.util.DataSource;
import edu.wpi.cs.dss.serverless.util.ErrorMessage;
import edu.wpi.cs.dss.serverless.util.HttpStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class ClassificationAddHandler implements RequestHandler<ClassificationAddRequest, GenericResponse> {

    private LambdaLogger logger;

    @Override
    public GenericResponse handleRequest(ClassificationAddRequest request, Context context) {
        logger = context.getLogger();
        logger.log("Received an add classification request from AWS Lambda:\n" + request);

        // save algorithm to the database
        final GenericResponse response = save(request);
        logger.log("Sent an add classification response to AWS Lambda:\n" + response);

        return response;
    }

    private GenericResponse save(ClassificationAddRequest request) {
        final String name = request.getName();
        final String parentId = request.getParentId();
        final String authorId = request.getAuthorId();
        final String id = UUID.randomUUID().toString();

        // creating a sql query
        final String query = "INSERT INTO classification (id, name, parent_id, author_id) VALUES (?, ?, ?, ?)";

        try (final Connection connection = DataSource.getConnection(logger);
             final PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            logger.log("Successfully connected to db!");

            preparedStatement.setString(1, id);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, parentId);
            preparedStatement.setString(4, authorId);

            final int rowsAffected = preparedStatement.executeUpdate();
            logger.log("Insert classification statement has affected " + rowsAffected + " rows!");

            return ClassificationAddResponse.builder()
                    .statusCode(HttpStatus.SUCCESS.getValue())
                    .id(id)
                    .name(name)
                    .parentId(parentId)
                    .authorId(authorId)
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
