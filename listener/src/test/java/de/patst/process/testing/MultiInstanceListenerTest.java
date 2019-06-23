package de.patst.process.testing;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class MultiInstanceListenerTest {

    @Autowired
    private RuntimeService runtimeService;

    @Test
    public void testMultiInstanceListener() {
        ProcessInstance processInstance = runtimeService
                .createMessageCorrelation("MultiInstanceListenerStartMessage")
                .setVariable("names", Arrays.asList("Meyer", "Müller", "Schulz"))
                .correlateStartMessage();
        assertTrue(processInstance.isEnded());
    }

}
