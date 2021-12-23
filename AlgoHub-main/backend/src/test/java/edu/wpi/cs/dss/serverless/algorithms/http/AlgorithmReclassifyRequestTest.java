package edu.wpi.cs.dss.serverless.algorithms.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AlgorithmReclassifyRequestTest {

    private static final String ALGORITHM_ID_PARAM = "algorithmId";
    private static final String NEW_CLASSIFICATION_ID_PARAM = "newClassificationId";

    private AlgorithmReclassifyRequest algorithmReclassifyRequest;

    @Before
    public void init() {
        algorithmReclassifyRequest = new AlgorithmReclassifyRequest();
        algorithmReclassifyRequest.setAlgorithmId(ALGORITHM_ID_PARAM);
        algorithmReclassifyRequest.setNewClassificationId(NEW_CLASSIFICATION_ID_PARAM);
    }

    @Test
    public void testGetters() {
        assertEquals(ALGORITHM_ID_PARAM, algorithmReclassifyRequest.getAlgorithmId());
        assertEquals(NEW_CLASSIFICATION_ID_PARAM, algorithmReclassifyRequest.getNewClassificationId());
    }

    @Test
    @SneakyThrows
    public void testToString() {
        final ObjectWriter objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();
        final String expected = objectWriter.writeValueAsString(algorithmReclassifyRequest);
        assertEquals(expected, algorithmReclassifyRequest.toString());
    }
}
