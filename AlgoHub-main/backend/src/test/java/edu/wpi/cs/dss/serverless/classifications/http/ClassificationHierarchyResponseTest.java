package edu.wpi.cs.dss.serverless.classifications.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class ClassificationHierarchyResponseTest {

    private static final String ERROR_PARAM = "error";
    private static final Integer STATUS_CODE_PARAM = 200;

    private ClassificationHierarchyResponse classificationHierarchyResponse;

    @Before
    public void init() {
        classificationHierarchyResponse = ClassificationHierarchyResponse.builder()
                .hierarchy(Collections.emptyList())
                .statusCode(STATUS_CODE_PARAM)
                .error(ERROR_PARAM)
                .build();
    }

    @Test
    public void testGetters() {
        assertEquals(0, classificationHierarchyResponse.getHierarchy().size());
        assertEquals(ERROR_PARAM, classificationHierarchyResponse.getError());
        assertEquals(STATUS_CODE_PARAM, classificationHierarchyResponse.getStatusCode());
    }

    @Test
    @SneakyThrows
    public void testToString() {
        final ObjectWriter objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();
        final String expected = objectWriter.writeValueAsString(classificationHierarchyResponse);
        assertEquals(expected, classificationHierarchyResponse.toString());
    }
}
