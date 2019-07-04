package de.patst.process.testing;

import org.apache.ibatis.logging.LogFactory;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.ManagementService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.assertThat;

@ActiveProfiles("jobexecutor")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class JobExecutorActiveTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobExecutorActiveTest.class);

    static {
        LogFactory.useSlf4jLogging();
    }

    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private ManagementService managementService;

    @Autowired
    private HistoryService historyService;

    // Async before configured at service task
    // camunda.bpm.job-execution.enabled must be set
    // This means, the instance will be retried and stay with an
    // incident in the camunda db
    @Test
    public synchronized void testTransactionExample2() throws Exception {
        ProcessInstance processInstance = this.runtimeService.startProcessInstanceByMessage("Transaction2StartMessage");
        while (this.historyService
                .createHistoricProcessInstanceQuery()
                .processInstanceId(processInstance.getId())
                .withIncidents()
                .list().size() == 0) {
            // Wait for completion
            LOGGER.info("Waiting for Process instance completion");
            Thread.sleep(1000);
        }
        assertThat(processInstance).isNotEnded();
    }
}
