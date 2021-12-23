package edu.wpi.cs.dss.serverless.benchmarks.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BenchmarkGetByImplementationRequestTest {

    private static final String ID_PARAM = "id";

    private BenchmarkGetByImplementationRequest benchmarkGetByImplementationRequest;

    @Before
    public void init() {
        benchmarkGetByImplementationRequest = new BenchmarkGetByImplementationRequest();
        benchmarkGetByImplementationRequest.setId(ID_PARAM);
    }

    @Test
    public void testGetters() {
        assertEquals(ID_PARAM, benchmarkGetByImplementationRequest.getId());
    }

    @Test
    @SneakyThrows
    public void testToString() {
        final ObjectWriter objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();
        final String expected = objectWriter.writeValueAsString(benchmarkGetByImplementationRequest);
        assertEquals(expected, benchmarkGetByImplementationRequest.toString());
    }
}
