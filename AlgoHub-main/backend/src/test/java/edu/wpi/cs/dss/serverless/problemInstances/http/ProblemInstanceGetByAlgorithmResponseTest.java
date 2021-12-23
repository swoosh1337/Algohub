package edu.wpi.cs.dss.serverless.problemInstances.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class ProblemInstanceGetByAlgorithmResponseTest {

    private ProblemInstanceGetByAlgorithmResponse problemInstanceGetByAlgorithmResponse;

    @Before
    public void init() {
        problemInstanceGetByAlgorithmResponse = ProblemInstanceGetByAlgorithmResponse.builder()
                .problemInstances(Collections.emptyList())
                .build();
    }

    @Test
    public void testGetters() {
        assertEquals(0, problemInstanceGetByAlgorithmResponse.getProblemInstances().size());
    }

    @Test
    @SneakyThrows
    public void testToString() {
        final ObjectWriter objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();
        final String expected = objectWriter.writeValueAsString(problemInstanceGetByAlgorithmResponse);
        assertEquals(expected, problemInstanceGetByAlgorithmResponse.toString());
    }
}
