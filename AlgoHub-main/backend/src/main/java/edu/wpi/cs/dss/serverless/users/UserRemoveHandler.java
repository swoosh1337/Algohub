package edu.wpi.cs.dss.serverless.users;

import com.amazonaws.services.cognitoidp.model.*;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import edu.wpi.cs.dss.serverless.generic.GenericResponse;
import edu.wpi.cs.dss.serverless.users.http.UserRemoveRequest;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;

import edu.wpi.cs.dss.serverless.util.HttpStatus;

public class UserRemoveHandler implements RequestHandler<UserRemoveRequest, GenericResponse> {

    private LambdaLogger logger;

    @Override
    public GenericResponse handleRequest(UserRemoveRequest request, Context context) {
        logger = context.getLogger();
        logger.log("Received an delete user request from AWS Lambda:\n" + request);

        // save problem instance to the database
        final GenericResponse response = deleteUser(request);
        logger.log("Sent an delete user response to AWS Lambda:\n" + response);

        return response;
    }

    private GenericResponse deleteUser(UserRemoveRequest request) {
        final String username = request.getUsername();
        final String poolId = System.getenv("USER_POOL_ID");

        final AdminDeleteUserRequest adminDeleteUserRequest = new AdminDeleteUserRequest();
        adminDeleteUserRequest.setUsername(username);
        adminDeleteUserRequest.setUserPoolId(poolId);

        try {
            final AWSCognitoIdentityProvider provider = AWSCognitoIdentityProviderClientBuilder.defaultClient();
            provider.adminDeleteUser(adminDeleteUserRequest);

            return GenericResponse.builder()
                    .statusCode(HttpStatus.SUCCESS.getValue())
                    .error("")
                    .build();

        } catch (Exception e){
            e.printStackTrace();
            return GenericResponse.builder()
                    .statusCode(HttpStatus.BAD_REQUEST.getValue())
                    .error("Failed to delete user.")
                    .build();
        }
    }
}
