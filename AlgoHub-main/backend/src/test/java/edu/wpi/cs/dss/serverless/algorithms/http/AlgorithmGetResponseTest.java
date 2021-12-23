package edu.wpi.cs.dss.serverless.algorithms.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AlgorithmGetResponseTest {

    private static final String ID_PARAM = "id";
    private static final String NAME_PARAM = "name";
    private static final String ERROR_PARAM = "error";
    private static final String AUTHOR_ID = "authorId";
    private static final Integer STATUS_CODE_PARAM = 200;
    private static final String DESCRIPTION_PARAM = "description";
    private static final String CLASSIFICATION_ID_PARAM = "classificationId";

    private AlgorithmGetResponse algorithmGetResponse;

    @Before
    public void init() {
        algorithmGetResponse = AlgorithmGetResponse.builder()
                .classificationId(CLASSIFICATION_ID_PARAM)
                .description(DESCRIPTION_PARAM)
                .statusCode(STATUS_CODE_PARAM)
                .authorId(AUTHOR_ID)
                .error(ERROR_PARAM)
                .name(NAME_PARAM)
                .id(ID_PARAM)
                .build();
    }

    @Test
    public void testGetters() {
        assertEquals(ID_PARAM, algorithmGetResponse.getId());
        assertEquals(NAME_PARAM, algorithmGetResponse.getName());
        assertEquals(ERROR_PARAM, algorithmGetResponse.getError());
        assertEquals(AUTHOR_ID, algorithmGetResponse.getAuthorId());
        assertEquals(STATUS_CODE_PARAM, algorithmGetResponse.getStatusCode());
        assertEquals(DESCRIPTION_PARAM, algorithmGetResponse.getDescription());
        assertEquals(CLASSIFICATION_ID_PARAM, algorithmGetResponse.getClassificationId());
    }

    @Test
    @SneakyThrows
    public void testToString() {
        final ObjectWriter objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();
        final String expected = objectWriter.writeValueAsString(algorithmGetResponse);
        assertEquals(expected, algorithmGetResponse.toString());
    }
}
