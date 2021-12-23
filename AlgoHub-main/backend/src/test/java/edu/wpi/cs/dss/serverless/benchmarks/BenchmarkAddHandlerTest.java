package edu.wpi.cs.dss.serverless.benchmarks;

import edu.wpi.cs.dss.serverless.LambdaTest;
import edu.wpi.cs.dss.serverless.benchmarks.http.BenchmarkAddRequest;
import edu.wpi.cs.dss.serverless.benchmarks.http.BenchmarkAddResponse;
import edu.wpi.cs.dss.serverless.generic.GenericResponse;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class BenchmarkAddHandlerTest extends LambdaTest {

    void testInput(BenchmarkAddRequest incoming, BenchmarkAddResponse outgoing) throws IOException {
        BenchmarkAddHandler handler = new BenchmarkAddHandler();
        BenchmarkAddResponse response = (BenchmarkAddResponse) handler.handleRequest(
                incoming, createContext("add benchmark")
        );

        Assert.assertTrue(response.getId().length() > 0);
        Assert.assertEquals(new Integer(200), response.getStatusCode());
    }

    @Test
    public void testValidAddBenchmark() {
        BenchmarkAddRequest sample_input = new BenchmarkAddRequest();
        sample_input.setAuthorId("junit-add-benchmark-author-id");
        sample_input.setCpuCores(4);
        sample_input.setCpuL1Cache(1);
        sample_input.setCpuL2Cache(2);
        sample_input.setCpuL3Cache(3);
        sample_input.setCpuName("intel");
        sample_input.setCpuThreads(10);
        sample_input.setExecutiontime(1000);
        sample_input.setMemory(512);
        sample_input.setMemoryUsage(256);
        sample_input.setProblemInstanceId("1");
        sample_input.setImplementationId("1");

        try {
            testInput(sample_input, null);
        } catch (IOException ioe) {
            Assert.fail("Invalid get problem instance:" + ioe.getMessage());
        }
    }


    void testFailInput(BenchmarkAddRequest incoming, BenchmarkAddResponse outgoing) throws IOException {
        BenchmarkAddHandler handler = new BenchmarkAddHandler();
        GenericResponse response = handler.handleRequest(
                incoming, createContext("add benchmark fail")
        );

        Assert.assertEquals(new Integer(400), response.getStatusCode());
    }

    @Test
    public void testInvalidBenchmarkAdd() {
        // bad problem instance id
        BenchmarkAddRequest bad_input_1 = new BenchmarkAddRequest();

        bad_input_1.setAuthorId("junit-add-benchmark-author-id");
        bad_input_1.setCpuCores(4);
        bad_input_1.setCpuL1Cache(1);
        bad_input_1.setCpuL2Cache(2);
        bad_input_1.setCpuL3Cache(3);
        bad_input_1.setCpuName("intel");
        bad_input_1.setCpuThreads(10);
        bad_input_1.setExecutiontime(1000);
        bad_input_1.setMemory(512);
        bad_input_1.setMemoryUsage(256);
        bad_input_1.setProblemInstanceId("bad-problem-instance-id");
        bad_input_1.setImplementationId("1");

        // bad implementation id
        BenchmarkAddRequest bad_input_2 = new BenchmarkAddRequest();

        bad_input_2.setAuthorId("junit-add-benchmark-author-id");
        bad_input_2.setCpuCores(4);
        bad_input_2.setCpuL1Cache(1);
        bad_input_2.setCpuL2Cache(2);
        bad_input_2.setCpuL3Cache(3);
        bad_input_2.setCpuName("intel");
        bad_input_2.setCpuThreads(10);
        bad_input_2.setExecutiontime(1000);
        bad_input_2.setMemory(512);
        bad_input_2.setMemoryUsage(256);
        bad_input_2.setProblemInstanceId("1");
        bad_input_2.setImplementationId("bad-implementation-id");

        try {
            testFailInput(bad_input_1, null);
        } catch (IOException ioe) {
            Assert.fail("Invalid get problem instance:" + ioe.getMessage());
        }

        try {
            testFailInput(bad_input_2, null);
        } catch (IOException ioe) {
            Assert.fail("Invalid get problem instance:" + ioe.getMessage());
        }
    }
}
