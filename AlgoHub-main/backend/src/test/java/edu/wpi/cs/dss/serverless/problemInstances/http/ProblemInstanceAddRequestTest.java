package edu.wpi.cs.dss.serverless.problemInstances.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ProblemInstanceAddRequestTest {

    private static final String AUTHOR_ID_PARAM = "authorId";
    private static final String DATASET_SIZE_PARAM = "datasetSize";
    private static final String PROBLEM_TYPE_PARAM = "problemType";
    private static final String ALGORITHM_ID_PARAM = "algorithmId";
    private static final String DATASET_FILENAME_PARAM = "datasetFilename";
    private static final String SOURCE_CODE_BASE_64_PARAM = "sourceCodeBase64";

    private ProblemInstanceAddRequest problemInstanceAddRequest;

    @Before
    public void init() {
        problemInstanceAddRequest = new ProblemInstanceAddRequest();
        problemInstanceAddRequest.setAuthorId(AUTHOR_ID_PARAM);
        problemInstanceAddRequest.setDatasetSize(DATASET_SIZE_PARAM);
        problemInstanceAddRequest.setProblemType(PROBLEM_TYPE_PARAM);
        problemInstanceAddRequest.setAlgorithmId(ALGORITHM_ID_PARAM);
        problemInstanceAddRequest.setDatasetFilename(DATASET_FILENAME_PARAM);
        problemInstanceAddRequest.setSourceCodeBase64(SOURCE_CODE_BASE_64_PARAM);
    }

    @Test
    public void testGetters() {
        assertEquals(AUTHOR_ID_PARAM, problemInstanceAddRequest.getAuthorId());
        assertEquals(DATASET_SIZE_PARAM, problemInstanceAddRequest.getDatasetSize());
        assertEquals(PROBLEM_TYPE_PARAM, problemInstanceAddRequest.getProblemType());
        assertEquals(ALGORITHM_ID_PARAM, problemInstanceAddRequest.getAlgorithmId());
        assertEquals(DATASET_FILENAME_PARAM, problemInstanceAddRequest.getDatasetFilename());
        assertEquals(SOURCE_CODE_BASE_64_PARAM, problemInstanceAddRequest.getSourceCodeBase64());
    }

    @Test
    @SneakyThrows
    public void testToString() {
        final ObjectWriter objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();
        final String expected = objectWriter.writeValueAsString(problemInstanceAddRequest);
        assertEquals(expected, problemInstanceAddRequest.toString());
    }
}
