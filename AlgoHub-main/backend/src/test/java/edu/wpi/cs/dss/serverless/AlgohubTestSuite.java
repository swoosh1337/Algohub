package edu.wpi.cs.dss.serverless;

import edu.wpi.cs.dss.serverless.benchmarks.BenchmarkTestSuite;
import edu.wpi.cs.dss.serverless.problemInstances.ProblemInstanceTestSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        BenchmarkTestSuite.class,
        ProblemInstanceTestSuite.class
})
public class AlgohubTestSuite {
}
