package edu.wpi.cs.dss.serverless.problemInstances;

import edu.wpi.cs.dss.serverless.LambdaTest;
import edu.wpi.cs.dss.serverless.generic.GenericResponse;
import edu.wpi.cs.dss.serverless.problemInstances.http.ProblemInstanceAddRequest;
import edu.wpi.cs.dss.serverless.problemInstances.http.ProblemInstanceAddResponse;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class ProblemInstanceAddHandlerTest extends LambdaTest {
    void testInput(ProblemInstanceAddRequest incoming, ProblemInstanceAddResponse outgoing) throws IOException {
        ProblemInstanceAddHandler handler = new ProblemInstanceAddHandler();
        ProblemInstanceAddResponse response = (ProblemInstanceAddResponse) handler.handleRequest(
                incoming, createContext("add problem instance")
        );

        Assert.assertTrue(response.getId().length() > 0);
        Assert.assertEquals(new Integer(200), response.getStatusCode());
    }

    void testFailInput(ProblemInstanceAddRequest incoming, ProblemInstanceAddResponse outgoing) throws IOException {
        ProblemInstanceAddHandler handler = new ProblemInstanceAddHandler();
        GenericResponse response = handler.handleRequest(
                incoming, createContext("add")
        );

//        Assert.assertTrue(response.getId().length() > 0);
        Assert.assertEquals(new Integer(400), response.getStatusCode());
    }

    @Test
    public void testValidProblemInstanceAdd() {
        ProblemInstanceAddRequest sample_input = new ProblemInstanceAddRequest();
        sample_input.setDatasetFilename("junit-test-file-name-3");
        sample_input.setAuthorId("junit-test-authorId");
        sample_input.setDatasetSize("128");
        sample_input.setProblemType("junit-test-type");
        sample_input.setAlgorithmId("52cac455-5bb3-11ec-933c-16c4115dd1ff");
        sample_input.setSourceCodeBase64("YXNrZGxqZmthanNkZmxramFzbGtkamY=");

        try {
            testInput(sample_input, null);
        } catch (IOException ioe) {
            Assert.fail("Invalid get problem instance:" + ioe.getMessage());
        }
    }

    @Test
    public void testInvalidProblemInstanceAdd() {
        ProblemInstanceAddRequest sample_input = new ProblemInstanceAddRequest();
        sample_input.setDatasetFilename("junit-test-file-name-3");
        sample_input.setAuthorId("junit-test-authorId");
        sample_input.setDatasetSize("128");
        sample_input.setProblemType("junit-test-type");
        sample_input.setAlgorithmId("bad-algorithm-id"); // foreign key restriction
        sample_input.setSourceCodeBase64("YXNrZGxqZmthanNkZmxramFzbGtkamY=");

        try {
            testFailInput(sample_input, null);
        } catch (IOException ioe) {
            Assert.fail("Invalid get problem instance:" + ioe.getMessage());
        }
    }
}
