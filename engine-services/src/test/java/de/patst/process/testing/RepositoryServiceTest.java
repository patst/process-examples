package de.patst.process.testing;

import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class RepositoryServiceTest {

    @Autowired
    private RepositoryService repositoryService;

    @Test
    public void testQueryProcessDefinition() {
        List<ProcessDefinition> definitions = this.repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey("ProcessEngineServicesProcess")
                .messageEventSubscriptionName("ProcessEngineServiceStartMessage")
                .list();
        assertEquals(1, definitions.size());
    }
}
