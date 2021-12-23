package edu.wpi.cs.dss.serverless.problemInstances;

import com.google.gson.Gson;
import edu.wpi.cs.dss.serverless.LambdaTest;
import edu.wpi.cs.dss.serverless.generic.GenericResponse;
import edu.wpi.cs.dss.serverless.problemInstances.ProblemInstanceGetHandler;
import edu.wpi.cs.dss.serverless.problemInstances.http.ProblemInstanceGetRequest;
import edu.wpi.cs.dss.serverless.problemInstances.http.ProblemInstanceGetResponse;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class ProblemInstanceGetHandlerTest extends LambdaTest {
    void testInput(String incoming, ProblemInstanceGetResponse outgoing) throws IOException {
        ProblemInstanceGetHandler handler = new ProblemInstanceGetHandler();
        ProblemInstanceGetRequest request = new Gson().fromJson(incoming, ProblemInstanceGetRequest.class);
        ProblemInstanceGetResponse response = (ProblemInstanceGetResponse) handler.handleRequest(
                request, createContext("get problem instance")
        );

        Assert.assertEquals(outgoing.toString(), response.toString());
        Assert.assertEquals(new Integer(200), response.getStatusCode());
    }

    void testFailInput(String incoming, String outgoing) throws IOException {
        ProblemInstanceGetHandler handler = new ProblemInstanceGetHandler();
        ProblemInstanceGetRequest request = new Gson().fromJson(incoming, ProblemInstanceGetRequest.class);
        GenericResponse response = handler.handleRequest(
                request, createContext("get problem instance")
        );

//        System.out.printf("bad input response: {}", response);

        Assert.assertEquals(new Integer(400), response.getStatusCode());
    }



    @Test
    public void testValidProblemInstanceGet() {
        String sample_input = "{\"id\": \"1\"}";
        ProblemInstanceGetResponse expected_output = (ProblemInstanceGetResponse) ProblemInstanceGetResponse.builder()
                .datasetFilename("test-file-name")
                .datasetSize("128")
                .problemType("test-type")
                .algorithmId("52cac455-5bb3-11ec-933c-16c4115dd1ff")
                .authorId("test-author-id")
                .statusCode(200)
                .build();

        try {
            testInput(sample_input, expected_output);
        } catch (IOException ioe) {
            Assert.fail("Invalid get problem instance:" + ioe.getMessage());
        }
    }

    @Test
    public void testInvalidProblemInstanceGet() {
        String sample_input = "{\"id\": \"Bad Id\"}";

        try {
            testFailInput(sample_input, "");
        } catch (IOException ioe) {
            Assert.fail("Invalid get problem instance:" + ioe.getMessage());
        }
    }
}
