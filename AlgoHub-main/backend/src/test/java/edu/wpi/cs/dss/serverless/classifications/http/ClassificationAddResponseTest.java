package edu.wpi.cs.dss.serverless.classifications.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ClassificationAddResponseTest {

    private static final String ID_PARAM = "id";
    private static final String NAME_PARAM = "name";
    private static final String ERROR_PARAM = "error";
    private static final Integer STATUS_CODE_PARAM = 200;
    private static final String AUTHOR_ID_PARAM = "authorId";
    private static final String PARENT_ID_PARAM = "parentId";

    private ClassificationAddResponse classificationAddResponse;

    @Before
    public void init() {
        classificationAddResponse = ClassificationAddResponse.builder()
                .statusCode(STATUS_CODE_PARAM)
                .parentId(PARENT_ID_PARAM)
                .authorId(AUTHOR_ID_PARAM)
                .error(ERROR_PARAM)
                .name(NAME_PARAM)
                .id(ID_PARAM)
                .build();
    }

    @Test
    public void testGetters() {
        assertEquals(ID_PARAM, classificationAddResponse.getId());
        assertEquals(NAME_PARAM, classificationAddResponse.getName());
        assertEquals(ERROR_PARAM, classificationAddResponse.getError());
        assertEquals(AUTHOR_ID_PARAM, classificationAddResponse.getAuthorId());
        assertEquals(PARENT_ID_PARAM, classificationAddResponse.getParentId());
        assertEquals(STATUS_CODE_PARAM, classificationAddResponse.getStatusCode());
    }

    @Test
    @SneakyThrows
    public void testToString() {
        final ObjectWriter objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();
        final String expected = objectWriter.writeValueAsString(classificationAddResponse);
        assertEquals(expected, classificationAddResponse.toString());
    }
}
