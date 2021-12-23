package edu.wpi.cs.dss.serverless.benchmarks.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class BenchmarkGetByImplementationResponseTest {

    private static final String ERROR_PARAM = "error";
    private static final Integer STATUS_CODE_PARAM = 200;

    private BenchmarkGetByImplementationResponse benchmarkGetByImplementationResponse;

    @Before
    public void init() {
        benchmarkGetByImplementationResponse = BenchmarkGetByImplementationResponse.builder()
                .benchmarks(Collections.emptyList())
                .statusCode(STATUS_CODE_PARAM)
                .error(ERROR_PARAM)
                .build();
    }

    @Test
    public void testGetters() {
        assertEquals(STATUS_CODE_PARAM, benchmarkGetByImplementationResponse.getStatusCode());
        assertEquals(0, benchmarkGetByImplementationResponse.getBenchmarks().size());
        assertEquals(ERROR_PARAM, benchmarkGetByImplementationResponse.getError());
    }

    @Test
    @SneakyThrows
    public void testToString() {
        final ObjectWriter objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();
        final String expected = objectWriter.writeValueAsString(benchmarkGetByImplementationResponse);
        assertEquals(expected, benchmarkGetByImplementationResponse.toString());
    }
}
