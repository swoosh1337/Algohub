package edu.wpi.cs.dss.serverless.problemInstances;

import com.google.gson.Gson;
import edu.wpi.cs.dss.serverless.LambdaTest;
import edu.wpi.cs.dss.serverless.problemInstances.http.ProblemInstanceGetByAlgorithmRequest;
import edu.wpi.cs.dss.serverless.problemInstances.http.ProblemInstanceGetByAlgorithmResponse;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class ProblemInstanceGetByAlgorithmHandlerTest extends LambdaTest {

    void testInput(String incoming, String outgoing) throws IOException {
        ProblemInstanceGetByAlgorithmHandler handler = new ProblemInstanceGetByAlgorithmHandler();
        ProblemInstanceGetByAlgorithmRequest request = new Gson().fromJson(incoming, ProblemInstanceGetByAlgorithmRequest.class);
        ProblemInstanceGetByAlgorithmResponse response = (ProblemInstanceGetByAlgorithmResponse) handler.handleRequest(
                request, createContext("get problem instance by algorithm")
        );

        Assert.assertTrue(response.getProblemInstances().size() > 0);
        Assert.assertEquals(new Integer(200), response.getStatusCode());
    }

    @Test
    public void testValidProblemInstanceGet() {
        String sample_input = "{\"id\": \"52cac455-5bb3-11ec-933c-16c4115dd1ff\"}";

        try {
            testInput(sample_input, "");
        } catch (IOException ioe) {
            Assert.fail("Invalid get problem instance:" + ioe.getMessage());
        }
    }

    void testFailInput(String incoming, String outgoing) throws IOException {
        ProblemInstanceGetByAlgorithmHandler handler = new ProblemInstanceGetByAlgorithmHandler();
        ProblemInstanceGetByAlgorithmRequest request = new Gson().fromJson(incoming, ProblemInstanceGetByAlgorithmRequest.class);
        ProblemInstanceGetByAlgorithmResponse response = (ProblemInstanceGetByAlgorithmResponse) handler.handleRequest(
                request, createContext("get problem instance by algorithm")
        );

        Assert.assertTrue(response.getProblemInstances().size() == 0);
        Assert.assertEquals(new Integer(200), response.getStatusCode());
    }

    @Test
    public void testInvalidProblemInstanceGetByAlgorithm() {
        String sample_input = "{\"id\": \"Bad Id\"}";

        try {
            testFailInput(sample_input, "");
        } catch (IOException ioe) {
            Assert.fail("Invalid get problem instance:" + ioe.getMessage());
        }
    }
}
