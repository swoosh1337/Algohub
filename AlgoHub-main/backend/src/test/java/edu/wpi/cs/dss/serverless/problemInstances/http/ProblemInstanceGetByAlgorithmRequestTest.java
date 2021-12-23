package edu.wpi.cs.dss.serverless.problemInstances.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ProblemInstanceGetByAlgorithmRequestTest {

    private static final String ID_PARAM = "id";

    private ProblemInstanceGetByAlgorithmRequest problemInstanceGetByAlgorithmRequest;

    @Before
    public void init() {
        problemInstanceGetByAlgorithmRequest = new ProblemInstanceGetByAlgorithmRequest();
        problemInstanceGetByAlgorithmRequest.setId(ID_PARAM);
    }

    @Test
    public void testGetters() {
        assertEquals(ID_PARAM, problemInstanceGetByAlgorithmRequest.getId());
    }

    @Test
    @SneakyThrows
    public void testToString() {
        final ObjectWriter objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();
        final String expected = objectWriter.writeValueAsString(problemInstanceGetByAlgorithmRequest);
        assertEquals(expected, problemInstanceGetByAlgorithmRequest.toString());
    }
}
