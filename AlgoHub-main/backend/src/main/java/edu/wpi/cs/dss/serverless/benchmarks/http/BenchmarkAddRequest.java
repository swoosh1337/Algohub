package edu.wpi.cs.dss.serverless.benchmarks.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;

@Getter
@Setter
@NoArgsConstructor
public class BenchmarkAddRequest {

    private String implementationId;
    private String problemInstanceId;
    private Integer memory;
    private String cpuName;
    private Integer cpuThreads;
    private Integer cpuCores;
    private Integer cpuL1Cache;
    private Integer cpuL2Cache;
    private Integer cpuL3Cache;
    private Integer executiontime;
    private Integer memoryUsage;
    private String authorId;

    @Override
    @SneakyThrows
    public String toString() {
        final ObjectWriter objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();
        return objectWriter.writeValueAsString(this);
    }
}
