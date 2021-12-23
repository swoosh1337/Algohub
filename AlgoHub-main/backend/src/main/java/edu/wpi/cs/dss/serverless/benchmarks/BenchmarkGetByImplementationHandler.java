package edu.wpi.cs.dss.serverless.benchmarks;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import edu.wpi.cs.dss.serverless.benchmarks.http.BenchmarkGetByImplementationRequest;
import edu.wpi.cs.dss.serverless.benchmarks.http.BenchmarkGetByImplementationResponse;
import edu.wpi.cs.dss.serverless.benchmarks.model.Benchmark;
import edu.wpi.cs.dss.serverless.generic.GenericResponse;
import edu.wpi.cs.dss.serverless.util.DataSource;
import edu.wpi.cs.dss.serverless.util.ErrorMessage;
import edu.wpi.cs.dss.serverless.util.HttpStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BenchmarkGetByImplementationHandler implements RequestHandler<BenchmarkGetByImplementationRequest, GenericResponse> {

    private LambdaLogger logger;

    @Override
    public GenericResponse handleRequest(BenchmarkGetByImplementationRequest request, Context context) {
        logger = context.getLogger();
        logger.log("Received a get benchmark request from AWS Lambda: \n" + request);

        // find algorithm by id
        final GenericResponse response = findById(request);
        logger.log("Sent a get benchmark response to AWS Lambda:\n" + response);

        return response;
    }

    private GenericResponse findById(BenchmarkGetByImplementationRequest request) {
        final String implementationId = request.getId();

        //create a sql query
        final String query =
                "SELECT benchmark.*, problem_instance.problem_type as problemType, problem_instance.dataset_size as datasetSize " +
                "FROM benchmark " +
                "INNER JOIN problem_instance ON problem_instance.id = benchmark.problem_instance_id " +
                "WHERE implementation_id = ?";

        try (final Connection connection = DataSource.getConnection(logger);
             final PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            logger.log("Successfully connected to db!");

            preparedStatement.setString(1, implementationId);

            final List<Benchmark> benchmarks = new ArrayList<>();
            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    final String id = resultSet.getString(1);
                    final int memory = resultSet.getInt(2);
                    final String cpuName = resultSet.getString(3);
                    final int cpuThreads = resultSet.getInt(4);
                    final int cpuCores = resultSet.getInt(5);
                    final int cpuL1Cache = resultSet.getInt(6);
                    final int cpuL2Cache = resultSet.getInt(7);
                    final int cpuL3Cache = resultSet.getInt(8);
                    final String executionDate = resultSet.getString(9);
                    final int executionTime = resultSet.getInt(10);
                    final int memoryUsage = resultSet.getInt(11);
                    final String implId = resultSet.getString(12);
                    final String problemInstanceId = resultSet.getString(13);
                    final String authorId = resultSet.getString(14);
                    final String problemType = resultSet.getString(15);
                    final int datasetSize = resultSet.getInt(16);

                    benchmarks.add(new Benchmark(
                            id,
                            implId,
                            problemInstanceId,
                            memory,
                            cpuName,
                            cpuThreads,
                            cpuCores,
                            cpuL1Cache,
                            cpuL2Cache,
                            cpuL3Cache,
                            executionTime,
                            executionDate,
                            memoryUsage,
                            authorId,
                            problemType,
                            datasetSize
                    ));
                }

                return BenchmarkGetByImplementationResponse.builder()
                        .statusCode(HttpStatus.SUCCESS.getValue())
                        .benchmarks(benchmarks)
                        .build();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            logger.log(ErrorMessage.SQL_EXECUTION_EXCEPTION.getValue());
            return BenchmarkGetByImplementationResponse.builder()
                    .statusCode(HttpStatus.BAD_REQUEST.getValue())
                    .error(ErrorMessage.SQL_EXECUTION_EXCEPTION.getValue())
                    .build();
        }
    }
}
