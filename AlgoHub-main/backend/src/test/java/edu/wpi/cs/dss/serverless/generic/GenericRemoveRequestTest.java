package edu.wpi.cs.dss.serverless.generic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GenericRemoveRequestTest {

    private static final String ID_PARAM = "id";

    private GenericRemoveRequest request;

    @Before
    public void init() {
        request = new GenericRemoveRequest();
        request.setId(ID_PARAM);
    }

    @Test
    public void testGetters() {
        assertEquals(ID_PARAM, request.getId());
    }

    @Test
    @SneakyThrows
    public void testToString() {
        final ObjectWriter objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();
        final String expected = objectWriter.writeValueAsString(request);
        assertEquals(expected, request.toString());
    }
}
