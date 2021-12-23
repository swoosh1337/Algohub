package edu.wpi.cs.dss.serverless.algorithms.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AlgorithmAddRequestTest {

    private static final String NAME_PARAM = "name";
    private static final String AUTHOR_ID_PARAM = "authorId";
    private static final String DESCRIPTION_PARAM = "description";
    private static final String CLASSIFICATION_ID_PARAM = "classificationId";

    private AlgorithmAddRequest algorithmAddRequest;

    @Before
    public void init() {
        algorithmAddRequest = new AlgorithmAddRequest();
        algorithmAddRequest.setName(NAME_PARAM);
        algorithmAddRequest.setAuthorId(AUTHOR_ID_PARAM);
        algorithmAddRequest.setDescription(DESCRIPTION_PARAM);
        algorithmAddRequest.setClassificationId(CLASSIFICATION_ID_PARAM);
    }

    @Test
    public void testGetters() {
        assertEquals(NAME_PARAM, algorithmAddRequest.getName());
        assertEquals(AUTHOR_ID_PARAM, algorithmAddRequest.getAuthorId());
        assertEquals(DESCRIPTION_PARAM, algorithmAddRequest.getDescription());
        assertEquals(CLASSIFICATION_ID_PARAM, algorithmAddRequest.getClassificationId());
    }

    @Test
    @SneakyThrows
    public void testToString() {
        final ObjectWriter objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();
        final String expected = objectWriter.writeValueAsString(algorithmAddRequest);
        assertEquals(expected, algorithmAddRequest.toString());
    }
}
