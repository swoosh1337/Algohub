package edu.wpi.cs.dss.serverless.users;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import edu.wpi.cs.dss.serverless.generic.GenericResponse;
import edu.wpi.cs.dss.serverless.users.http.UserGetAllRequest;
import edu.wpi.cs.dss.serverless.users.http.UserGetAllResponse;
import edu.wpi.cs.dss.serverless.util.DataSource;
import edu.wpi.cs.dss.serverless.util.ErrorMessage;
import edu.wpi.cs.dss.serverless.util.HttpStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserGetAllHandler implements RequestHandler<UserGetAllRequest, GenericResponse> {

    private LambdaLogger logger;

    @Override
    public GenericResponse handleRequest(UserGetAllRequest request, Context context) {
        logger = context.getLogger();
        logger.log("Received a get users request from AWS Lambda: \n" + request);

        // find problem instance by id
        final GenericResponse response = getAllUsers();
        logger.log("Sent a get users response to AWS Lambda:\n" + response);

        return response;
    }

    private GenericResponse getAllUsers() {
        final String query = "SELECT author_id FROM classification" +
                " UNION " +
                "SELECT author_id FROM algorithm" +
                " UNION " +
                "SELECT author_id FROM implementation" +
                " UNION " +
                "SELECT author_id FROM benchmark";

        try (final Connection connection = DataSource.getConnection(logger);
             final PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            logger.log("Successfully connected to db!");

            final List<String> users = new ArrayList<>();
            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                while(resultSet.next()) {
                    final String authorId = resultSet.getString(1);
                    users.add(authorId);
                }
            }

            return UserGetAllResponse.builder()
                    .statusCode(HttpStatus.SUCCESS.getValue())
                    .users(users)
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