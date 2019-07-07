package de.patst.process.testing;

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
public class BusinessErrorHandlingTest {

    @Autowired
    private RuntimeService runtimeService;

    /**
     * Handles a business error in the process.
     * Throws a Service Error end event if the loan amount is bigger than 100.000(€).
     * This is a business error which should be handled in the BPMN model.
     */
    @Test
    public void testBusinessErrorHandling1() {
        ProcessInstance processInstance = runtimeService
                .createMessageCorrelation("BusinessErrorHandling1StartMessage")
                .correlateStartMessage();

        assertThat(processInstance)
                .hasVariables("loanAmount")
                .hasPassed("CalculateLoanTask", "ServiceErrorEndEvent")
                .isEnded();
    }

}
