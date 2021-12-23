package edu.wpi.cs.dss.serverless.benchmarks;

import com.google.gson.Gson;
import edu.wpi.cs.dss.serverless.LambdaTest;
import edu.wpi.cs.dss.serverless.benchmarks.http.BenchmarkGetByImplementationRequest;
import edu.wpi.cs.dss.serverless.benchmarks.http.BenchmarkGetByImplementationResponse;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class BenchmarkGetByImplementationHandlerTest extends LambdaTest {

    void testInput(String incoming, String outgoing) throws IOException {
        BenchmarkGetByImplementationHandler handler = new BenchmarkGetByImplementationHandler();
        BenchmarkGetByImplementationRequest request = new Gson().fromJson(incoming, BenchmarkGetByImplementationRequest.class);
        BenchmarkGetByImplementationResponse response = (BenchmarkGetByImplementationResponse) handler.handleRequest(
                request, createContext("get benchmarks by implementation")
        );

        Assert.assertTrue(response.getBenchmarks().size() > 0);
        Assert.assertEquals(new Integer(200), response.getStatusCode());
    }

    @Test
    public void testValidGetBenchmarkByImplementation() {
        String sample_input = "{\"id\": \"1\"}";

        try {
            testInput(sample_input, "");
        } catch (IOException ioe) {
            Assert.fail("Invalid get problem instance:" + ioe.getMessage());
        }
    }
}
