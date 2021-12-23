package edu.wpi.cs.dss.serverless.problemInstances.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ProblemInstanceTest {

    private static final String ID_PARAM = "id";
    private static final Integer DATASET_SIZE_PARAM = 200;
    private static final String AUTHOR_ID_PARAM = "authorId";
    private static final String PROBLEM_TYPE_PARAM = "problemType";
    private static final String ALGORITHM_ID_PARAM = "algorithmId";
    private static final String DATASET_FILENAME_PARAM = "datasetFilename";

    private ProblemInstance problemInstance;

    @Before
    public void init() {
        problemInstance = new ProblemInstance(
                ID_PARAM,
                AUTHOR_ID_PARAM,
                PROBLEM_TYPE_PARAM,
                ALGORITHM_ID_PARAM,
                DATASET_SIZE_PARAM,
                DATASET_FILENAME_PARAM
        );
    }

    @Test
    public void testGetters() {
        assertEquals(ID_PARAM, problemInstance.getId());
        assertEquals(AUTHOR_ID_PARAM, problemInstance.getAuthorId());
        assertEquals(PROBLEM_TYPE_PARAM, problemInstance.getProblemType());
        assertEquals(ALGORITHM_ID_PARAM, problemInstance.getAlgorithmId());
        assertEquals(DATASET_SIZE_PARAM, problemInstance.getDatasetSize());
        assertEquals(DATASET_FILENAME_PARAM, problemInstance.getDatasetFilename());
    }

    @Test
    public void testHashCode() {
        assertEquals(problemInstance.hashCode(), problemInstance.hashCode());
    }

    @Test
    public void testEquals() {
        assertNotEquals(problemInstance, "");
        assertNotEquals(problemInstance, null);
        assertEquals(problemInstance, problemInstance);
    }

    @Test
    @SneakyThrows
    public void testToString() {
        final ObjectWriter objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();
        final String expected = objectWriter.writeValueAsString(problemInstance);
        assertEquals(expected, problemInstance.toString());
    }
}
