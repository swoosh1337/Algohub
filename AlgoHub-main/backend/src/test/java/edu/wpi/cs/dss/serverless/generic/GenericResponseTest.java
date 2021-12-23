package edu.wpi.cs.dss.serverless.generic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GenericResponseTest {

    private static final String ERROR_PARAM = "error";
    private static final Integer STATUS_CODE_PARAM = 200;

    private GenericResponse genericResponse;

    @Before
    public void init() {
        genericResponse = GenericResponse.builder()
                .statusCode(STATUS_CODE_PARAM)
                .error(ERROR_PARAM)
                .build();
    }

    @Test
    public void testGetters() {
        assertEquals(ERROR_PARAM, genericResponse.getError());
        assertEquals(STATUS_CODE_PARAM, genericResponse.getStatusCode());
    }

    @Test
    @SneakyThrows
    public void testToString() {
        final ObjectWriter objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();
        final String expected = objectWriter.writeValueAsString(genericResponse);
        assertEquals(expected, genericResponse.toString());
    }
}
