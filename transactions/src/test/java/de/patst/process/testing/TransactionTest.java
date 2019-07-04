package de.patst.process.testing;

import org.apache.ibatis.logging.LogFactory;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.ProcessEngineException;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNull;

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

    // Transaction example 1 has no async after/ before configured.
    // This means the process instance 'vanishes' in case of an exception,
    // because the database transaction is rolled back.
    @Test
    public void testTransactionExample1() {
        ProcessInstance processInstance = null;
        try {
            processInstance = runtimeService
                    .startProcessInstanceByMessage
                            ("Transaction1StartMessage");
        } catch (ProcessEngineException pee) {
            LOGGER.error("ProcessEngineException thrown", pee);
        }
        assertNull(processInstance);

    }

}
