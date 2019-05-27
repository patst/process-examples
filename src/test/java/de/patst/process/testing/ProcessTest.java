package de.patst.process.testing;

import org.apache.ibatis.logging.LogFactory;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ProcessTest {

    @Autowired
    private ProcessEngine processEngine;

    static {
        LogFactory.useSlf4jLogging(); // MyBatis
    }

    @Test
    public void testHappyPath() {
        ProcessInstance processInstance = this.processEngine
                .getRuntimeService()
                .startProcessInstanceByMessage("TestProcessStartMessage");
        assertThat(processInstance).isEnded();
    }

    @Test
    public void testHappyPathWithVariable() {
        // Start the process
        String businessKey = UUID.randomUUID().toString();
        String variable = "myVar";
        ProcessInstance processInstance = this.processEngine
                .getRuntimeService()
                .startProcessInstanceByMessage(
                        "TestProcessStartMessage",
                        businessKey,
                        Stream.of(variable).collect(Collectors.toMap(Function.identity(), Function.identity())));

        // check the process state
        assertThat(processInstance)
                .isEnded()
                .hasVariables(variable)
                .hasPassed("SomeActivityTask");
    }
}
