package de.patst.process.testing;

import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.assertThat;

import org.apache.ibatis.logging.LogFactory;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class DelegatesTest {

    static {
        LogFactory.useSlf4jLogging(); // MyBatis
    }

    @Autowired
    private RuntimeService runtimeService;

    @Test
    public synchronized void testLogDelegates() throws Exception {
        ProcessInstance processInstance = this.runtimeService
                .startProcessInstanceByMessage("DelegatesStartMessage");
        this.wait(10_000);
        assertThat(processInstance).isEnded();
    }

}
