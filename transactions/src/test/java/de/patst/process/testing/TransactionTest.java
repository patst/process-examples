package de.patst.process.testing;

import de.patst.process.TransactionWatcher;
import org.apache.ibatis.logging.LogFactory;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class TransactionTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionTest.class);

    static {
        LogFactory.useSlf4jLogging();
    }

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private HistoryService historyService;

    // Transaction example 1 has no asnyc after/ before configured
    @Test
    public void testTransactionExample1() {
        TransactionWatcher.reset();
        ProcessInstance processInstance = runtimeService
                .startProcessInstanceByMessage
                        ("Transaction1StartMessage");
        assertTrue(processInstance.isEnded());

        LOGGER.info("Successful transactions: " + TransactionWatcher.getSuccessfulTransactions());
    }

    // Async before configured at service task
    // camunda.bpm.job-execution.enabled must be set
    @Test
    public synchronized void testTransactionExample2() throws Exception {
        TransactionWatcher.reset();
        ProcessInstance processInstance = this.runtimeService.startProcessInstanceByKey("TransactionExample2");
        while (this.historyService
                .createHistoricProcessInstanceQuery()
                .processInstanceId(processInstance.getId())
                .completed()
                .list().size() == 0) {
            // Wait for completion
            LOGGER.info("Waiting for Process instance completion");
            Thread.sleep(1000);
        }

        LOGGER.info("Successful transactions: " + TransactionWatcher.getSuccessfulTransactions());
    }
}
