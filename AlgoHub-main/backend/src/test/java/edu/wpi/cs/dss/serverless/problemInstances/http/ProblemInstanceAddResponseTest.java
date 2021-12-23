package edu.wpi.cs.dss.serverless.problemInstances.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ProblemInstanceAddResponseTest {

    private static final String ID_PARAM = "id";
    private static final String ERROR_PARAM = "error";
    private static final Integer STATUS_CODE_PARAM = 200;

    private ProblemInstanceAddResponse response;

    @Before
    public void init() {
        response = ProblemInstanceAddResponse.builder()
                .statusCode(STATUS_CODE_PARAM)
                .error(ERROR_PARAM)
                .id(ID_PARAM)
                .build();
    }

    @Test
    public void testGetters() {
        assertEquals(ID_PARAM, response.getId());
        assertEquals(ERROR_PARAM, response.getError());
        assertEquals(STATUS_CODE_PARAM, response.getStatusCode());
    }

    @Test
    @SneakyThrows
    public void testToString() {
        final ObjectWriter objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();
        final String expected = objectWriter.writeValueAsString(response);
        assertEquals(expected, response.toString());
    }
}
