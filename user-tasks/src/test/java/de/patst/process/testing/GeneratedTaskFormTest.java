package de.patst.process.testing;

import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.assertThat;

import java.util.HashMap;
import java.util.Map;
import org.apache.ibatis.logging.LogFactory;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

@DirtiesContext
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class GeneratedTaskFormTest {

    static {
        LogFactory.useSlf4jLogging(); // MyBatis
    }

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;


    @Test
    public void testGeneratedAssignedUserTask() {
        ProcessInstance processInstance = runtimeService
            .startProcessInstanceByMessage("GeneratedTaskFormAssignedStartMessage");
        assertThat(processInstance).isWaitingAt("EnterPersonalDataAssignedTask");

        Task task = taskService.createTaskQuery()
            .processInstanceId(processInstance.getId())
            .taskAssignee("admin")
            .singleResult();
        Map<String, Object> variables = new HashMap<>();
        // Firstname is required but the validation rules only work using the tasklist ui
        // variables.put("firstname", "Max");
        variables.put("lastname", "Mustermann");
        taskService.complete(task.getId(), variables);
    }

}
