package edu.wpi.cs.dss.serverless.problemInstances.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ProblemInstanceGetRequestTest {

    private static final String ID_PARAM = "id";

    private ProblemInstanceGetRequest problemInstanceGetRequest;

    @Before
    public void init() {
        problemInstanceGetRequest = new ProblemInstanceGetRequest();
        problemInstanceGetRequest.setId(ID_PARAM);
    }

    @Test
    public void testGetters() {
        assertEquals(ID_PARAM, problemInstanceGetRequest.getId());
    }

    @Test
    @SneakyThrows
    public void testToString() {
        final ObjectWriter objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();
        final String expected = objectWriter.writeValueAsString(problemInstanceGetRequest);
        assertEquals(expected, problemInstanceGetRequest.toString());
    }
}
