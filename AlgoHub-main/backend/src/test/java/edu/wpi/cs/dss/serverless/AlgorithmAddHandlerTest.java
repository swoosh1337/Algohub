package edu.wpi.cs.dss.serverless;

import com.google.gson.Gson;
import edu.wpi.cs.dss.serverless.algorithms.AlgorithmAddHandler;
import edu.wpi.cs.dss.serverless.algorithms.http.AlgorithmAddRequest;
import edu.wpi.cs.dss.serverless.algorithms.http.AlgorithmAddResponse;
import edu.wpi.cs.dss.serverless.generic.GenericResponse;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class AlgorithmAddHandlerTest extends LambdaTest  {

    void testSuccessInput(String incoming) throws IOException {
        AlgorithmAddHandler handler = new AlgorithmAddHandler();
        AlgorithmAddRequest req = new Gson().fromJson(incoming, AlgorithmAddRequest.class);
        AlgorithmAddResponse response = (AlgorithmAddResponse) handler.handleRequest(req, createContext("add"));


        Assert.assertEquals(req.getName(),response.getName());
        Assert.assertEquals(req.getAuthorId(),response.getAuthorId());
        Assert.assertEquals(req.getDescription(),response.getDescription());
        Assert.assertEquals(req.getClassificationId(),response.getClassificationId());
        Assert.assertEquals(new Integer(200), response.getStatusCode());
    }

    void testFailInput(String incoming) throws IOException {
        AlgorithmAddHandler handler = new AlgorithmAddHandler();
        AlgorithmAddRequest req = new Gson().fromJson(incoming, AlgorithmAddRequest.class);
        GenericResponse response = handler.handleRequest(req, createContext("add"));


        Assert.assertEquals(new Integer(400), response.getStatusCode());
    }


    @Test
    public void testSuccessInput(){
        String sample_name = "Quick Sort";
        String description = "Cool sorting algorithm";
        String classification_id = "1b53c044-5bb3-11ec-933c-16c4115dd1ff";
        String author_id = "john_smith_sr";

        AlgorithmAddRequest req = new AlgorithmAddRequest(sample_name,description,classification_id,author_id);
        String input = new Gson().toJson(req);

        try {
            testSuccessInput(input);
        } catch (IOException ioe) {
            Assert.fail("Invalid:" + ioe.getMessage());
        }

    }

    @Test
    public void testFailInput() {
        String sample_name = "Quick Sort";
        String description = "Cool sorting algorithm";
        String classification_id = "60";
        String author_id = "john_smith_sr";

        AlgorithmAddRequest req = new AlgorithmAddRequest(sample_name, description, classification_id, author_id);
        String input = new Gson().toJson(req);

        try {
            testFailInput(input);
        } catch (IOException ioe) {
            Assert.fail("Invalid:" + ioe.getMessage());
        }

    }


}
