package edu.wpi.cs.dss.serverless.benchmarks;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import edu.wpi.cs.dss.serverless.benchmarks.http.BenchmarkAddRequest;
import edu.wpi.cs.dss.serverless.benchmarks.http.BenchmarkAddResponse;
import edu.wpi.cs.dss.serverless.generic.GenericResponse;
import edu.wpi.cs.dss.serverless.util.DataSource;
import edu.wpi.cs.dss.serverless.util.ErrorMessage;
import edu.wpi.cs.dss.serverless.util.HttpStatus;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class BenchmarkAddHandler implements RequestHandler<BenchmarkAddRequest, GenericResponse> {

    private LambdaLogger logger;

    @Override
    public GenericResponse handleRequest(BenchmarkAddRequest request, Context context) {
        logger = context.getLogger();
        logger.log("Received an add benchmark request from AWS Lambda:\n" + request);

        // save benchmark to the database
        final GenericResponse response = save(request);
        logger.log("Sent an add benchmark response to AWS Lambda:\n" + response);

        return response;
    }

    private GenericResponse save(BenchmarkAddRequest request) {
        final String implementationId = request.getImplementationId();
        final String problemInstanceId = request.getProblemInstanceId();
        final String authorId = request.getAuthorId();
        final Integer memory = request.getMemory();
        final String cpuName = request.getCpuName();
        final Integer cpuThreads = request.getCpuThreads();
        final Integer cpuCores = request.getCpuCores();
        final Integer cpuL1Cache = request.getCpuL1Cache();
        final Integer cpuL2Cache = request.getCpuL2Cache();
        final Integer cpuL3Cache = request.getCpuL3Cache();
        final Integer executionTime = request.getExecutiontime();
        final Integer memoryUsage = request.getMemoryUsage();

        final String id = UUID.randomUUID().toString();
        final Date date = new Date(System.currentTimeMillis());
        final String query = "INSERT INTO benchmark (id, memory, cpu_name, cpu_threads, cpu_cores, cpu_l1_cache, cpu_l2_cache, cpu_l3_cache, execution_date, execution_time, memory_usage, implementation_id, problem_instance_id, author_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (final Connection connection = DataSource.getConnection(logger);
             final PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            logger.log("Successfully connected to db!");

            preparedStatement.setString(1, id);
            preparedStatement.setInt(2, memory);
            preparedStatement.setString(3, cpuName);
            preparedStatement.setInt(4, cpuThreads);
            preparedStatement.setInt(5, cpuCores);
            preparedStatement.setInt(6, cpuL1Cache);
            preparedStatement.setInt(7, cpuL2Cache);
            preparedStatement.setInt(8, cpuL3Cache);
            preparedStatement.setDate(9, date);
            preparedStatement.setInt(10, executionTime);
            preparedStatement.setInt(11, memoryUsage);
            preparedStatement.setString(12, implementationId);
            preparedStatement.setString(13, problemInstanceId);
            preparedStatement.setString(14, authorId);

            final int rowsAffected = preparedStatement.executeUpdate();
            logger.log("Insert benchmark statement has affected " + rowsAffected + " rows!");

            return BenchmarkAddResponse.builder()
                    .statusCode(HttpStatus.SUCCESS.getValue())
                    .id(id)
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
