package edu.wpi.cs.dss.serverless.benchmarks.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import edu.wpi.cs.dss.serverless.benchmarks.model.Benchmark;
import edu.wpi.cs.dss.serverless.generic.GenericResponse;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@SuperBuilder
public class BenchmarkGetByImplementationResponse extends GenericResponse {

    private List<Benchmark> benchmarks;

    @Override
    @SneakyThrows
    public String toString() {
        final ObjectWriter objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();
        return objectWriter.writeValueAsString(this);
    }
}
