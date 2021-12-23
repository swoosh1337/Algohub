package edu.wpi.cs.dss.serverless;

import com.google.gson.Gson;
import edu.wpi.cs.dss.serverless.algorithms.AlgorithmAddHandler;
import edu.wpi.cs.dss.serverless.algorithms.http.AlgorithmAddRequest;
import edu.wpi.cs.dss.serverless.generic.GenericRemoveRequest;
import edu.wpi.cs.dss.serverless.generic.GenericResponse;

import edu.wpi.cs.dss.serverless.algorithms.AlgorithmRemoveHandler;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class AlgorithmRemoveHandlerTest extends  LambdaTest {

    void testSuccessInput(String incoming) throws IOException {
        AlgorithmRemoveHandler handler = new AlgorithmRemoveHandler();
        GenericRemoveRequest req = new Gson().fromJson(incoming, GenericRemoveRequest.class);
        GenericResponse response =  handler.handleRequest(req, createContext("remove"));
        Assert.assertEquals(new Integer(200), response.getStatusCode());
    }

    void testFailInput(String incoming) throws IOException {
        AlgorithmRemoveHandler handler = new AlgorithmRemoveHandler();
        GenericRemoveRequest req = new Gson().fromJson(incoming, GenericRemoveRequest.class);
        GenericResponse response =  handler.handleRequest(req, createContext("remove"));



        Assert.assertEquals(new Integer(200), response.getStatusCode());
    }

    @Test
    public void testSuccessInput(){
        String id = "16ac8e25-4a75-462f-a12e-f0d5395df9de";


        GenericRemoveRequest req = new GenericRemoveRequest(id);
        String input = new Gson().toJson(req);

        try {
            testSuccessInput(input);
        } catch (IOException ioe) {
            Assert.fail("Invalid:" + ioe.getMessage());
        }

    }

    @Test
    public void FailInput(){
        String id = "16ac5";
        GenericRemoveRequest req = new GenericRemoveRequest(id);
        String input = new Gson().toJson(req);

        try {
            testFailInput(input);
        } catch (IOException ioe) {
            Assert.fail("Invalid:" + ioe.getMessage());
        }

    }

}