package edu.wpi.cs.dss.serverless;

import com.google.gson.Gson;
import edu.wpi.cs.dss.serverless.algorithms.AlgorithmAddHandler;
import edu.wpi.cs.dss.serverless.algorithms.AlgorithmGetHandler;
import edu.wpi.cs.dss.serverless.algorithms.AlgorithmReclassifyHandler;
import edu.wpi.cs.dss.serverless.algorithms.http.AlgorithmAddRequest;
import edu.wpi.cs.dss.serverless.algorithms.http.AlgorithmGetRequest;
import edu.wpi.cs.dss.serverless.algorithms.http.AlgorithmGetResponse;
import edu.wpi.cs.dss.serverless.algorithms.http.AlgorithmReclassifyRequest;
import edu.wpi.cs.dss.serverless.generic.GenericResponse;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class AlgorithmReclassifyHandlerTest extends LambdaTest{

    void testSuccessInput(String incoming) throws IOException {
        AlgorithmReclassifyHandler handler = new AlgorithmReclassifyHandler ();
        AlgorithmReclassifyRequest req = new Gson().fromJson(incoming, AlgorithmReclassifyRequest.class);
        GenericResponse response = handler.handleRequest(req, createContext("reclassify"));

        AlgorithmGetHandler handler2 = new AlgorithmGetHandler ();
        AlgorithmGetRequest req2 = new AlgorithmGetRequest(req.getAlgorithmId());
        AlgorithmGetResponse response2 = (AlgorithmGetResponse) handler2.handleRequest(req2, createContext("get"));



        Assert.assertEquals(req.getAlgorithmId(),response2.getId());
        Assert.assertEquals(req.getNewClassificationId(),response2.getClassificationId());
        Assert.assertEquals(new Integer(200), response.getStatusCode());
    }

    void testFailInput(String incoming) throws IOException {
        AlgorithmAddHandler handler = new AlgorithmAddHandler();
        AlgorithmAddRequest req = new Gson().fromJson(incoming, AlgorithmAddRequest.class);
        GenericResponse response = handler.handleRequest(req, createContext("reclassify"));


        Assert.assertEquals(new Integer(400), response.getStatusCode());
    }

    @Test
    public void testSuccessInput(){
        String id = "52cac455-5bb3-11ec-933c-16c4115dd1ff";
        String classification_id = "1b53c044-5bb3-11ec-933c-16c4115dd1ff";

        AlgorithmReclassifyRequest req = new AlgorithmReclassifyRequest(id,classification_id);
        String input = new Gson().toJson(req);

        try {
            testSuccessInput(input);
        } catch (IOException ioe) {
            Assert.fail("Invalid:" + ioe.getMessage());
        }

    }

    @Test
    public void testFailInput() {
        String id = "52cac455-5bb3-11ec-933c-16c4115dd1ff";
        String classification_id = "1b53c044-5bb3-11ec-933c-16c4115dd134ff";

        AlgorithmReclassifyRequest req = new AlgorithmReclassifyRequest(id,classification_id);
        String input = new Gson().toJson(req);

        try {
            testFailInput(input);
        } catch (IOException ioe) {
            Assert.fail("Invalid:" + ioe.getMessage());
        }

    }

}
