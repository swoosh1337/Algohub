package edu.wpi.cs.dss.serverless.benchmarks.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class BenchmarkTest {

    private static final Integer MEMORY_PARAM = 1;
    private static final Integer CPU_CORES_PARAM = 2;
    private static final Integer CPU_THREADS_PARAM = 3;
    private static final Integer CPU_L1_CACHE_PARAM = 4;
    private static final Integer CPU_L2_CACHE_PARAM = 5;
    private static final Integer CPU_L3_CACHE_PARAM = 6;
    private static final Integer MEMORY_USAGE_PARAM = 7;
    private static final Integer DATASET_SIZE_PARAM = 8;
    private static final Integer EXECUTION_TIME_PARAM = 9;


    private static final String ID_PARAM = "id";
    private static final String CPU_NAME_PARAM = "cpuName";
    private static final String AUTHOR_ID_PARAM = "authorId";
    private static final String PROBLEM_TYPE_PARAM = "problemType";
    private static final String EXECUTION_DATE_PARAM = "executionDate";
    private static final String IMPLEMENTATION_ID_PARAM = "implementationId";
    private static final String PROBLEM_INSTANCE_ID_PARAM = "problemInstanceId";

    private Benchmark benchmark;

    @Before
    public void init() {
        benchmark = new Benchmark(
                ID_PARAM,
                IMPLEMENTATION_ID_PARAM,
                PROBLEM_INSTANCE_ID_PARAM,
                MEMORY_PARAM,
                CPU_NAME_PARAM,
                CPU_THREADS_PARAM,
                CPU_CORES_PARAM,
                CPU_L1_CACHE_PARAM,
                CPU_L2_CACHE_PARAM,
                CPU_L3_CACHE_PARAM,
                EXECUTION_TIME_PARAM,
                EXECUTION_DATE_PARAM,
                MEMORY_USAGE_PARAM,
                AUTHOR_ID_PARAM,
                PROBLEM_TYPE_PARAM,
                DATASET_SIZE_PARAM
        );
    }

    @Test
    public void testGetters() {
        assertEquals(ID_PARAM, benchmark.getId());
        assertEquals(IMPLEMENTATION_ID_PARAM, benchmark.getImplementationId());
        assertEquals(PROBLEM_INSTANCE_ID_PARAM, benchmark.getProblemInstanceId());
        assertEquals(MEMORY_PARAM, benchmark.getMemory());
        assertEquals(CPU_NAME_PARAM, benchmark.getCpuName());
        assertEquals(CPU_THREADS_PARAM, benchmark.getCpuThreads());
        assertEquals(CPU_L1_CACHE_PARAM, benchmark.getCpuL1Cache());
        assertEquals(CPU_L2_CACHE_PARAM, benchmark.getCpuL2Cache());
        assertEquals(CPU_L3_CACHE_PARAM, benchmark.getCpuL3Cache());
        assertEquals(EXECUTION_TIME_PARAM, benchmark.getExecutionTime());
        assertEquals(EXECUTION_DATE_PARAM, benchmark.getExecutionDate());
        assertEquals(MEMORY_USAGE_PARAM, benchmark.getMemoryUsage());
        assertEquals(AUTHOR_ID_PARAM, benchmark.getAuthorId());
        assertEquals(PROBLEM_TYPE_PARAM, benchmark.getProblemType());
        assertEquals(DATASET_SIZE_PARAM, benchmark.getDatasetSize());
    }

    @Test
    public void testEquals() {
        assertNotEquals(benchmark, "");
        assertNotEquals(benchmark, null);
        assertEquals(benchmark, benchmark);
    }

    @Test
    public void testHashCode() {
        assertEquals(benchmark.hashCode(), benchmark.hashCode());
    }

    @Test
    @SneakyThrows
    public void testToString() {
        final ObjectWriter objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();
        final String expected = objectWriter.writeValueAsString(benchmark);
        assertEquals(expected, benchmark.toString());
    }
}
