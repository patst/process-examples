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
public class TechnicalExceptionHandlingTest {

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
     * Example 2 catches Java Exception and transforms it into a BPMN Error.
     * The error is modelled with a BPMN Boundary Error Event.
     */
    @Test
    public void testExceptionHandlingExample2() {
        ProcessInstance processInstance = runtimeService
                .createMessageCorrelation("ExceptionHandling2StartMessage")
                .correlateStartMessage();

        assertThat(processInstance)
                .hasPassed("ExceptionTask", "ErrorBoundaryEvent", "HandleBPMNErrorTask", "ErrorEndEvent")
                .isEnded();
    }

}
