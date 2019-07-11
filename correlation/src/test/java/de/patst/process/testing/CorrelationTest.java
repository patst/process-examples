package de.patst.process.testing;

import de.patst.process.delegate.CreateCustomerDelegate;
import org.apache.ibatis.logging.LogFactory;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Objects;

import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CorrelationTest {

    static {
        LogFactory.useSlf4jLogging(); // MyBatis
    }

    @LocalServerPort
    private int localServerPort;

    @Autowired
    private RuntimeService runtimeService;

    @Test
    public synchronized void testCorrelation() throws Exception {
        CreateCustomerDelegate.PORT = localServerPort;

        ProcessInstance processInstance = runtimeService
                .createMessageCorrelation("CorrelationStartMessage")
                .setVariable("name", "Max Meier")
                .correlateStartMessage();
        this.wait(4_000);
        assertThat(processInstance)
                .hasVariables("customerId")
                .isEnded();
    }

}
