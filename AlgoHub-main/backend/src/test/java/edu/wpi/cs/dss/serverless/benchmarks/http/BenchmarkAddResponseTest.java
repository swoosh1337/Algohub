package edu.wpi.cs.dss.serverless.benchmarks.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BenchmarkAddResponseTest {

    private static final String ID_PARAM = "id";
    private static final String ERROR_PARAM = "error";
    private static final Integer STATUS_CODE_PARAM = 200;

    private BenchmarkAddResponse benchmarkAddResponse;

    @Before
    public void init() {
        benchmarkAddResponse = BenchmarkAddResponse.builder()
                .statusCode(STATUS_CODE_PARAM)
                .error(ERROR_PARAM)
                .id(ID_PARAM)
                .build();
    }

    @Test
    public void testGetters() {
        assertEquals(STATUS_CODE_PARAM, benchmarkAddResponse.getStatusCode());
        assertEquals(ERROR_PARAM, benchmarkAddResponse.getError());
        assertEquals(ID_PARAM, benchmarkAddResponse.getId());
    }

    @Test
    @SneakyThrows
    public void testToString() {
        final ObjectWriter objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();
        final String expected = objectWriter.writeValueAsString(benchmarkAddResponse);
        assertEquals(expected, benchmarkAddResponse.toString());
    }
}
