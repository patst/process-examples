package de.patst.process.testing;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.Execution;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.assertThat;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class RuntimeServiceTest {

    @Autowired
    private RuntimeService runtimeService;

    @Test
    public void testStartProcess() {
        ProcessInstance processInstance = runtimeService
                .startProcessInstanceByMessage
                        ("ProcessEngineServiceStartMessage");
        assertTrue(processInstance.isEnded());
    }

    @Test
    public void testGetVariableValue() {
        ProcessInstance processInstance = runtimeService
                .createMessageCorrelation("ProcessEngineServiceStartMessage")
                .setVariable("myVar", "myValue")
                .correlateStartMessage();
        assertTrue(processInstance.isEnded());

        // DANGER: this does not work with the given process (no waits) -  "execution doesn't exist: execution is null"
        //assertEquals("myValue",
        //        runtimeService
        //               .getVariable(
        //                        processInstance.getProcessInstanceId(), "myVar"));
    }

    @Test
    public void testCorrelateMessageInProcess() {
        ProcessInstance processInstance = runtimeService
                .createMessageCorrelation("WaitForMessageStartMessage")
                .setVariable("myVar", "myValue")
                .correlateStartMessage();
        // Process instance is waiting for a message
        assertThat(processInstance)
                .isWaitingFor("MessageReceivedMessage");

        // Query the variable
        assertEquals("myValue",
                runtimeService
                        .getVariable(
                                processInstance.getProcessInstanceId(), "myVar"));

        Execution piAfterCorrelation = runtimeService.createMessageCorrelation("MessageReceivedMessage")
                .correlateWithResult()
                //.getProcessInstance(); --> Would return NULL because it wasn't correlated to a new ProcessInstance!
                .getExecution();
        assertTrue(piAfterCorrelation.isEnded());
        // DANGER: THIS IS STILL FALSE!
        assertFalse(processInstance.isEnded());
    }


}
