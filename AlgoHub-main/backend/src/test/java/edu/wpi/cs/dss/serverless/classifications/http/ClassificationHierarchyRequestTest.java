package edu.wpi.cs.dss.serverless.classifications.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ClassificationHierarchyRequestTest {

    private ClassificationHierarchyRequest classificationHierarchyRequest;

    @Before
    public void init() {
        classificationHierarchyRequest = new ClassificationHierarchyRequest();
    }

    @Test
    @SneakyThrows
    public void testToString() {
        final ObjectWriter objectWriter = new ObjectMapper().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false).writerWithDefaultPrettyPrinter();
        final String expected = objectWriter.writeValueAsString(this);
        assertEquals(expected, classificationHierarchyRequest.toString());
    }
}
