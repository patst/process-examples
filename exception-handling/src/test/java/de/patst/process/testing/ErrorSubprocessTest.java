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
public class ErrorSubprocessTest {

    @Autowired
    private HistoryService historyService;

    @Autowired
    private RuntimeService runtimeService;

    @Test
    public void testExceptionHandlingExample1() {
        ProcessInstance processInstance = runtimeService
                .createMessageCorrelation("ErrorHandlingSubprocessStartMessage")
                .correlateStartMessage();

        assertThat(processInstance)
                .hasPassed("ParallelGateway",
                        "ErrorEndEvent",
                        "ErrorSubprocessStartEvent",
                        "LogErrorTask")
                .isEnded();
    }

}
