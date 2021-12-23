package edu.wpi.cs.dss.serverless.classifications.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ClassificationAddRequestTest {

    private static final String NAME_PARAM = "name";
    private static final String AUTHOR_ID_PARAM = "authorId";
    private static final String PARENT_ID_PARAM = "parentId";

    private ClassificationAddRequest classificationAddRequest;

    @Before
    public void init() {
        classificationAddRequest = new ClassificationAddRequest();
        classificationAddRequest.setName(NAME_PARAM);
        classificationAddRequest.setAuthorId(AUTHOR_ID_PARAM);
        classificationAddRequest.setParentId(PARENT_ID_PARAM);
    }

    @Test
    public void testGetters() {
        assertEquals(NAME_PARAM, classificationAddRequest.getName());
        assertEquals(AUTHOR_ID_PARAM, classificationAddRequest.getAuthorId());
        assertEquals(PARENT_ID_PARAM, classificationAddRequest.getParentId());
    }

    @Test
    @SneakyThrows
    public void testToString() {
        final ObjectWriter objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();
        final String expected = objectWriter.writeValueAsString(classificationAddRequest);
        assertEquals(expected, classificationAddRequest.toString());
    }
}
