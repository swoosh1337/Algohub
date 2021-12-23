package edu.wpi.cs.dss.serverless.benchmarks.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import lombok.Value;

@Value
@EqualsAndHashCode(of = "id")
public class Benchmark {
    String id;
    String implementationId;
    String problemInstanceId;
    Integer memory;
    String cpuName;
    Integer cpuThreads;
    Integer cpuCores;
    Integer cpuL1Cache;
    Integer cpuL2Cache;
    Integer cpuL3Cache;
    Integer executionTime;
    String executionDate;
    Integer memoryUsage;
    String authorId;
    String problemType;
    Integer datasetSize;

    @Override
    @SneakyThrows
    public String toString() {
        final ObjectWriter objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();
        return objectWriter.writeValueAsString(this);
    }
}
