package edu.wpi.cs.dss.serverless.benchmarks;

import com.google.gson.Gson;
import edu.wpi.cs.dss.serverless.LambdaTest;
import edu.wpi.cs.dss.serverless.benchmarks.http.BenchmarkAddRequest;
import edu.wpi.cs.dss.serverless.benchmarks.http.BenchmarkAddResponse;
import edu.wpi.cs.dss.serverless.benchmarks.http.BenchmarkRemoveRequest;
import edu.wpi.cs.dss.serverless.generic.GenericRemoveRequest;
import edu.wpi.cs.dss.serverless.generic.GenericResponse;
import edu.wpi.cs.dss.serverless.util.HttpStatus;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class BenchmarkRemoveHandlerTest extends LambdaTest {

    void testInput(String incoming, GenericResponse outgoing) throws IOException {
        BenchmarkRemoveHandler handler = new BenchmarkRemoveHandler();
        GenericRemoveRequest request = new Gson().fromJson(incoming, GenericRemoveRequest.class);
        GenericResponse response = handler.handleRequest(
                request, createContext("remove benchmark")
        );

        Assert.assertEquals(outgoing.toString(), response.toString());
        Assert.assertEquals(new Integer(200), response.getStatusCode());
    }

    @Test
    public void testValidBenchmarkRemove() {
        // add before remove
        BenchmarkAddHandler addHandler = new BenchmarkAddHandler();
        BenchmarkAddRequest addRequest = new BenchmarkAddRequest();

        addRequest.setAuthorId("junit-add-benchmark-author-id");
        addRequest.setCpuCores(4);
        addRequest.setCpuL1Cache(1);
        addRequest.setCpuL2Cache(2);
        addRequest.setCpuL3Cache(3);
        addRequest.setCpuName("intel");
        addRequest.setCpuThreads(10);
        addRequest.setExecutiontime(1000);
        addRequest.setMemory(512);
        addRequest.setMemoryUsage(256);
        addRequest.setProblemInstanceId("1");
        addRequest.setImplementationId("1");

        BenchmarkAddResponse addResponse = (BenchmarkAddResponse) addHandler.handleRequest(
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
}
