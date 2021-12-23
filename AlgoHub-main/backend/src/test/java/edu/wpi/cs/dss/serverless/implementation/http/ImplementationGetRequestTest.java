package edu.wpi.cs.dss.serverless.implementation.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ImplementationGetRequestTest {

    private static final String ID_PARAM = "id";

    private ImplementationGetRequest implementationGetRequest;

    @Before
    public void init() {
        implementationGetRequest = new ImplementationGetRequest();
        implementationGetRequest.setId(ID_PARAM);
    }

    @Test
    public void testGetters() {
        assertEquals(ID_PARAM, implementationGetRequest.getId());
    }

    @Test
    @SneakyThrows
    public void testToString() {
        final ObjectWriter objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();
        final String expected = objectWriter.writeValueAsString(implementationGetRequest);
        assertEquals(expected, implementationGetRequest.toString());
    }
}
