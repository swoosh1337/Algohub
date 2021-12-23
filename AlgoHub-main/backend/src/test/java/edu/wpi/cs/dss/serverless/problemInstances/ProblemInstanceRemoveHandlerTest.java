package edu.wpi.cs.dss.serverless.problemInstances;

import com.google.gson.Gson;
import edu.wpi.cs.dss.serverless.LambdaTest;
import edu.wpi.cs.dss.serverless.generic.GenericRemoveRequest;
import edu.wpi.cs.dss.serverless.generic.GenericResponse;
import edu.wpi.cs.dss.serverless.problemInstances.http.*;
import edu.wpi.cs.dss.serverless.util.HttpStatus;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.UUID;

public class ProblemInstanceRemoveHandlerTest extends LambdaTest {

    void testInput(String incoming, GenericResponse outgoing) throws IOException {
        // remove
        ProblemInstanceRemoveHandler handler = new ProblemInstanceRemoveHandler();
        GenericRemoveRequest request = new Gson().fromJson(incoming, GenericRemoveRequest.class);
        GenericResponse response = handler.handleRequest(
                request, createContext("remove problem instance")
        );

//        Assert.assertEquals(outgoing.toString(), response.toString());
        Assert.assertEquals(new Integer(200), response.getStatusCode());
    }

    @Test
    public void testValidProblemInstanceRemove() {
        // add before remove
        ProblemInstanceAddHandler addHandler = new ProblemInstanceAddHandler();
        ProblemInstanceAddRequest addRequest = new ProblemInstanceAddRequest();
        addRequest.setDatasetFilename(UUID.randomUUID().toString()); // random file name, avoid overlapping
        addRequest.setAuthorId("junit-test-authorId");
        addRequest.setDatasetSize("128");
        addRequest.setProblemType("junit-test-type");
        addRequest.setAlgorithmId("52cac455-5bb3-11ec-933c-16c4115dd1ff");
        addRequest.setSourceCodeBase64("YXNrZGxqZmthanNkZmxramFzbGtkamY=");
        ProblemInstanceAddResponse addResponse = (ProblemInstanceAddResponse) addHandler.handleRequest(
                addRequest, createContext("add")
        );
        Assert.assertTrue(addResponse.getId().length() > 0);
        Assert.assertEquals(new Integer(200), addResponse.getStatusCode());

        System.out.println("new problem instance created with id -> " + addResponse.getId());

        String sample_input = String.format("{\"id\": \"%s\"}", addResponse.getId());
        GenericResponse expected_output = GenericResponse.builder()
                .statusCode(HttpStatus.SUCCESS.getValue())
                .build();

        try {
            testInput(sample_input, expected_output);
        } catch (IOException ioe) {
            Assert.fail("Invalid get problem instance:" + ioe.getMessage());
        }
    }

    void testFailInput(String incoming, String outgoing) throws IOException {
        ProblemInstanceRemoveHandler handler = new ProblemInstanceRemoveHandler();
        GenericRemoveRequest request = new Gson().fromJson(incoming, GenericRemoveRequest.class);
        GenericResponse response = handler.handleRequest(
                request, createContext("remove problem instance")
        );

        // affected 0 rows but return 200
        Assert.assertEquals(new Integer(200), response.getStatusCode());
    }

    @Test
    public void testInvalidProblemInstanceRemove() {
        String sample_input = "{\"id\": \"Bad Id\"}";

        try {
            testFailInput(sample_input, "");
        } catch (IOException ioe) {
            Assert.fail("Invalid get problem instance:" + ioe.getMessage());
        }
    }
}
