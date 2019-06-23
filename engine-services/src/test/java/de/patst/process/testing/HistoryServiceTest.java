package de.patst.process.testing;

import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class HistoryServiceTest {

    @Autowired
    private HistoryService historyService;

    @Autowired
    private RuntimeService runtimeService;

    @Test
    public void testGetVariableValue() {
        ProcessInstance processInstance = runtimeService
                .createMessageCorrelation("ProcessEngineServiceStartMessage")
                .setVariable("myVar", "myValue")
                .correlateStartMessage();
        assertTrue(processInstance.isEnded());

        assertEquals("myValue",
                historyService
                        .createHistoricVariableInstanceQuery()
                        .processInstanceId(processInstance.getId())
                        .variableName("myVar")
                        .singleResult()
                        .getValue());
    }

}
