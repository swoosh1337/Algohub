package edu.wpi.cs.dss.serverless.problemInstances;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ProblemInstanceGetHandlerTest.class,
        ProblemInstanceRemoveHandlerTest.class,
        ProblemInstanceGetByAlgorithmHandlerTest.class,
        ProblemInstanceAddHandlerTest.class
})
public class ProblemInstanceTestSuite {

}