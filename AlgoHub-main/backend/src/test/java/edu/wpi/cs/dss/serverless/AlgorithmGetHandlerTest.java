package edu.wpi.cs.dss.serverless;


import edu.wpi.cs.dss.serverless.algorithms.AlgorithmGetHandler;
import edu.wpi.cs.dss.serverless.algorithms.http.AlgorithmGetRequest;
import edu.wpi.cs.dss.serverless.algorithms.http.AlgorithmGetResponse;
import edu.wpi.cs.dss.serverless.generic.GenericResponse;
import org.junit.Assert;
import com.google.gson.Gson;
import org.junit.Test;

import java.io.IOException;

public class AlgorithmGetHandlerTest extends LambdaTest {

    void testSuccessInput(String incoming,String name, String authorId, String description, String classificationId) throws IOException {
        AlgorithmGetHandler handler = new AlgorithmGetHandler();
        AlgorithmGetRequest req = new Gson().fromJson(incoming, AlgorithmGetRequest.class);
        AlgorithmGetResponse response = (AlgorithmGetResponse) handler.handleRequest(req, createContext("get"));

        Assert.assertEquals(req.getId(),response.getId());
        Assert.assertEquals(name,response.getName());
        Assert.assertEquals(authorId,response.getAuthorId());
        Assert.assertEquals(description,response.getDescription());
        Assert.assertEquals(classificationId,response.getClassificationId());
        Assert.assertEquals(new Integer(200), response.getStatusCode());
    }

    void testFailInput(String incoming) throws IOException {
        AlgorithmGetHandler handler = new AlgorithmGetHandler();
        AlgorithmGetRequest req = new Gson().fromJson(incoming, AlgorithmGetRequest.class);
        GenericResponse response = handler.handleRequest(req, createContext("get"));

//        Assert.assertEquals(outgoing, response.result);
        Assert.assertEquals(new Integer(400), response.getStatusCode());
    }
    @Test
    public void testAlgorithmGet(){
        String SAMPLE_INPUT_ID = "52cac455-5bb3-11ec-933c-16c4115dd1ff";

        AlgorithmGetRequest req = new AlgorithmGetRequest(SAMPLE_INPUT_ID);
        String input_id = new Gson().toJson(req);

        String SAMPLE_OUTPUT_NAME = "BFS";
        String SAMPLE_OUTPUT_DESCRIPTION = "Breadth-First Search";
        String SAMPLE_OUTPUT_AUTHORRID = "1b53c068-5bb3-11ec-933c-16c4115dd1ff";
        String SAMPLE_OUTPUT_CLASSIFICATIONID = "1b53c044-5bb3-11ec-933c-16c4115dd1ff";


        try {
            testSuccessInput(input_id,SAMPLE_OUTPUT_NAME,SAMPLE_OUTPUT_AUTHORRID,SAMPLE_OUTPUT_DESCRIPTION,SAMPLE_OUTPUT_CLASSIFICATIONID);
        } catch (IOException ioe) {
            Assert.fail("Invalid:" + ioe.getMessage());
        }
    }

    @Test
    public void testFailInput(){
        String SAMPLE_INPUT_ID = "16ac8e25-4a75-462f-a12e-f0d5395d909de";

        AlgorithmGetRequest req = new AlgorithmGetRequest(SAMPLE_INPUT_ID);
        String input_id = new Gson().toJson(req);

        try {
            testFailInput(input_id);
        } catch (IOException ioe) {
            Assert.fail("Invalid:" + ioe.getMessage());
        }
    }

}

