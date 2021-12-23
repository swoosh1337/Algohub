package edu.wpi.cs.dss.serverless.benchmarks.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BenchmarkAddRequestTest {

    private static final Integer MEMORY_PARAM = 1;
    private static final Integer CPU_CORES_PARAM = 2;
    private static final Integer CPU_THREADS_PARAM = 3;
    private static final Integer CPU_L1_CACHE_PARAM = 4;
    private static final Integer CPU_L2_CACHE_PARAM = 5;
    private static final Integer CPU_L3_CACHE_PARAM = 6;
    private static final Integer MEMORY_USAGE_PARAM = 7;
    private static final Integer EXECUTION_TIME_PARAM = 8;

    private static final String CPU_NAME_PARAM = "cpuName";
    private static final String AUTHOR_ID_PARAM = "authorId";
    private static final String IMPLEMENTATION_ID_PARAM = "implementationId";
    private static final String PROBLEM_INSTANCE_ID_PARAM = "problemInstanceId";

    private BenchmarkAddRequest benchmarkAddRequest;

    @Before
    public void init() {
        benchmarkAddRequest = new BenchmarkAddRequest();
        benchmarkAddRequest.setMemory(MEMORY_PARAM);
        benchmarkAddRequest.setCpuCores(CPU_CORES_PARAM);
        benchmarkAddRequest.setCpuThreads(CPU_THREADS_PARAM);
        benchmarkAddRequest.setCpuL1Cache(CPU_L1_CACHE_PARAM);
        benchmarkAddRequest.setCpuL2Cache(CPU_L2_CACHE_PARAM);
        benchmarkAddRequest.setCpuL3Cache(CPU_L3_CACHE_PARAM);
        benchmarkAddRequest.setMemoryUsage(MEMORY_USAGE_PARAM);
        benchmarkAddRequest.setExecutiontime(EXECUTION_TIME_PARAM);
        benchmarkAddRequest.setCpuName(CPU_NAME_PARAM);
        benchmarkAddRequest.setAuthorId(AUTHOR_ID_PARAM);
        benchmarkAddRequest.setImplementationId(IMPLEMENTATION_ID_PARAM);
        benchmarkAddRequest.setProblemInstanceId(PROBLEM_INSTANCE_ID_PARAM);
    }

    @Test
    public void testGetters() {
        assertEquals(MEMORY_PARAM, benchmarkAddRequest.getMemory());
        assertEquals(CPU_CORES_PARAM, benchmarkAddRequest.getCpuCores());
        assertEquals(CPU_THREADS_PARAM, benchmarkAddRequest.getCpuThreads());
        assertEquals(CPU_L1_CACHE_PARAM, benchmarkAddRequest.getCpuL1Cache());
        assertEquals(CPU_L2_CACHE_PARAM, benchmarkAddRequest.getCpuL2Cache());
        assertEquals(CPU_L3_CACHE_PARAM, benchmarkAddRequest.getCpuL3Cache());
        assertEquals(MEMORY_USAGE_PARAM, benchmarkAddRequest.getMemoryUsage());
        assertEquals(EXECUTION_TIME_PARAM, benchmarkAddRequest.getExecutiontime());
        assertEquals(CPU_NAME_PARAM, benchmarkAddRequest.getCpuName());
        assertEquals(AUTHOR_ID_PARAM, benchmarkAddRequest.getAuthorId());
        assertEquals(IMPLEMENTATION_ID_PARAM, benchmarkAddRequest.getImplementationId());
        assertEquals(PROBLEM_INSTANCE_ID_PARAM, benchmarkAddRequest.getProblemInstanceId());
    }

    @Test
    @SneakyThrows
    public void testToString() {
        final ObjectWriter objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();
        final String expected = objectWriter.writeValueAsString(benchmarkAddRequest);
        assertEquals(expected, benchmarkAddRequest.toString());
    }
}
