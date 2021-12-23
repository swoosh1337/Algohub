package edu.wpi.cs.dss.serverless.algorithms.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AlgorithmGetRequestTest {

    private static final String ID_PARAM = "id";

    private AlgorithmGetRequest algorithmGetRequest;

    @Before
    public void init() {
        algorithmGetRequest = new AlgorithmGetRequest();
        algorithmGetRequest.setId(ID_PARAM);
    }

    @Test
    public void testGetters() {
        assertEquals(ID_PARAM, algorithmGetRequest.getId());
    }

    @Test
    @SneakyThrows
    public void testToString() {
        final ObjectWriter objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();
        final String expected = objectWriter.writeValueAsString(algorithmGetRequest);
        assertEquals(expected, algorithmGetRequest.toString());
    }
}
