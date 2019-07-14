package de.patst.process.testing;

import org.apache.ibatis.logging.LogFactory;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class LogDelegateTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogDelegateTest.class);

    static {
        LogFactory.useSlf4jLogging(); // MyBatis
    }

    @Autowired
    private RuntimeService runtimeService;

    @Test
    public void testReadModel() {
        ProcessInstance pi = runtimeService.createMessageCorrelation("ModelAPIStartMessage")
                .correlateStartMessage();
        assertThat(pi).isEnded();
        // There should be a log message with the documentation from the service task.
    }
}
