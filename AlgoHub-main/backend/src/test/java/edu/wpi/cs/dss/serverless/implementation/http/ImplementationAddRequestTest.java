package edu.wpi.cs.dss.serverless.implementation.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ImplementationAddRequestTest {

    private static final String NAME_PARAM = "name";
    private static final String AUTHOR_ID_PARAM = "authorId";
    private static final String EXTENSION_PARAM = "extension";
    private static final String ALGORITHM_ID_PARAM = "algorithmId";
    private static final String ALGORITHM_NAME_PARAM = "algorithmName";
    private static final String SOURCE_CODE_BASE_64_PARAM = "sourceCodeBase64";

    private ImplementationAddRequest implementationAddRequest;

    @Before
    public void init() {
        implementationAddRequest = new ImplementationAddRequest();
        implementationAddRequest.setName(NAME_PARAM);
        implementationAddRequest.setAuthorId(AUTHOR_ID_PARAM);
        implementationAddRequest.setExtension(EXTENSION_PARAM);
        implementationAddRequest.setAlgorithmId(ALGORITHM_ID_PARAM);
        implementationAddRequest.setAlgorithmName(ALGORITHM_NAME_PARAM);
        implementationAddRequest.setSourceCodeBase64(SOURCE_CODE_BASE_64_PARAM);
    }

    @Test
    public void testGetters() {
        assertEquals(NAME_PARAM, implementationAddRequest.getName());
        assertEquals(AUTHOR_ID_PARAM, implementationAddRequest.getAuthorId());
        assertEquals(EXTENSION_PARAM, implementationAddRequest.getExtension());
        assertEquals(ALGORITHM_ID_PARAM, implementationAddRequest.getAlgorithmId());
        assertEquals(ALGORITHM_NAME_PARAM, implementationAddRequest.getAlgorithmName());
        assertEquals(SOURCE_CODE_BASE_64_PARAM, implementationAddRequest.getSourceCodeBase64());
    }

    @Test
    @SneakyThrows
    public void testToString() {
        final ObjectWriter objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();
        final String expected = objectWriter.writeValueAsString(implementationAddRequest);
        assertEquals(expected, implementationAddRequest.toString());
    }
}
