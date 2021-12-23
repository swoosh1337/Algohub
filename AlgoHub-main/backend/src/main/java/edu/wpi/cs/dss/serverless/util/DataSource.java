package edu.wpi.cs.dss.serverless.util;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;
import java.util.stream.Stream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DataSource {

    private static final String AWS_RDS_URL;
    private static final String AWS_RDS_USERNAME;
    private static final String AWS_RDS_PASSWORD;
    private static final String AWS_RDS_PORT = ":3306";
    private static final String AWS_RDS_NAME = "/algohub";
    private static final String AWS_RDS_TEST = "/algohub_test";
    private static final String AWS_RDS_URL_ENV_KEY = "dbHost";
    private static final String AWS_RDS_USERNAME_ENV_KEY = "dbUsername";
    private static final String AWS_RDS_PASSWORD_ENV_KEY = "dbPassword";

    private static final String JDBC_TAG = "jdbc:mysql://";
    private static final String JDBC_CONNECTION_PROPERTY = "?allowMultiQueries=true";

    static {
        AWS_RDS_URL = System.getenv(AWS_RDS_URL_ENV_KEY);
        AWS_RDS_USERNAME = System.getenv(AWS_RDS_USERNAME_ENV_KEY);
        AWS_RDS_PASSWORD = System.getenv(AWS_RDS_PASSWORD_ENV_KEY);
    }

    public static Connection getConnection(LambdaLogger logger) throws SQLException {
        boolean useTestDB = true;

        final long envs = Stream.of(AWS_RDS_URL, AWS_RDS_USERNAME, AWS_RDS_PASSWORD)
                .filter(Objects::nonNull)
                .count();

        if (envs != 3) {
            logger.log("AWS RDS environment variables are not properly configured ...");
        }

        logger.log("Connecting to db ...");

        if (useTestDB){
            return DriverManager.getConnection(
                    JDBC_TAG + AWS_RDS_URL + AWS_RDS_PORT + AWS_RDS_TEST + JDBC_CONNECTION_PROPERTY,
                    AWS_RDS_USERNAME,
                    AWS_RDS_PASSWORD
            );
        }

        else{
            return DriverManager.getConnection(
                    JDBC_TAG + AWS_RDS_URL + AWS_RDS_PORT + AWS_RDS_NAME + JDBC_CONNECTION_PROPERTY,
                    AWS_RDS_USERNAME,
                    AWS_RDS_PASSWORD
            );
        }


    }
}
