package edu.wpi.cs.dss.serverless.classifications.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ClassificationMergeRequestTest {

    private static final String SOURCE_ID_PARAM = "sourceId";
    private static final String TARGET_ID_PARAM = "targetId";

    private ClassificationMergeRequest classificationMergeRequest;

    @Before
    public void init() {
        classificationMergeRequest = new ClassificationMergeRequest();
        classificationMergeRequest.setSourceId(SOURCE_ID_PARAM);
        classificationMergeRequest.setTargetId(TARGET_ID_PARAM);
    }

    @Test
    public void testGetters() {
        assertEquals(SOURCE_ID_PARAM, classificationMergeRequest.getSourceId());
        assertEquals(TARGET_ID_PARAM, classificationMergeRequest.getTargetId());
    }

    @Test
    @SneakyThrows
    public void testToString() {
        final ObjectWriter objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();
        final String expected = objectWriter.writeValueAsString(classificationMergeRequest);
        assertEquals(expected, classificationMergeRequest.toString());
    }
}
