package de.patst.process.testing;

import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ExceptionHandlingTest {

    @Autowired
    private HistoryService historyService;

    @Autowired
    private RuntimeService runtimeService;

    /**
     * Test for the exception handling 1 process.
     * The service exception is caught and transformed into a
     * variable which stores the state if the service was successful or not.
     */
    @Test
    public void testExceptionHandlingExample1() {
        ProcessInstance processInstance = runtimeService
                .createMessageCorrelation("ExceptionHandling1StartMessage")
                .correlateStartMessage();

        assertThat(processInstance)
                .hasPassed("CaughtExceptionTask", "HandleExceptionTask")
                .isEnded();
    }

    /**
     * Example 2 throws a Service Error end event if the loan amount is bigger than 100.000(€).
     */
    @Test
    public void testExceptionHandlingExample2() {
        ProcessInstance processInstance = runtimeService
                .createMessageCorrelation("ExceptionHandling2StartMessage")
                .correlateStartMessage();

        assertThat(processInstance)
                .hasVariables("loanAmount")
                .hasPassed("CalculateLoanTask", "ServiceErrorEndEvent")
                .isEnded();
    }

    @Test
    public void testExceptionHandlingExample3() {
        ProcessInstance processInstance = runtimeService
                .createMessageCorrelation("ExceptionHandling3StartMessage")
                .correlateStartMessage();

        assertThat(processInstance)
                .hasPassed("ExceptionTask", "ErrorBoundaryEvent", "HandleBPMNErrorTask", "ErrorEndEvent")
                .isEnded();
    }
}
