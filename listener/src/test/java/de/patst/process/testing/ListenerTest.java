package de.patst.process.testing;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ListenerTest {

    @Autowired
    private RuntimeService runtimeService;

    @Test
    public void testStartListener() {
        ProcessInstance processInstance = runtimeService
                .startProcessInstanceByMessage
                        ("ListenerStartMessage");
        assertTrue(processInstance.isEnded());
    }

}
