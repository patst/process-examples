package de.patst.process.testing;

import org.apache.ibatis.logging.LogFactory;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class DelegatesTest {

    static {
        LogFactory.useSlf4jLogging(); // MyBatis
    }

    @Autowired
    private ProcessEngine processEngine;

    @Test
    public synchronized void testLogDelegates() throws Exception {
        ProcessInstance processInstance = this.processEngine
                .getRuntimeService()
                .startProcessInstanceByMessage("DelegatesStartMessage");
        this.wait(10_000);
        assertThat(processInstance).isEnded();
    }

}
