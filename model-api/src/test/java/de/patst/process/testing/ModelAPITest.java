package de.patst.process.testing;

import org.apache.ibatis.logging.LogFactory;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.repository.DeploymentWithDefinitions;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.Process;
import org.camunda.bpm.model.bpmn.instance.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ModelAPITest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModelAPITest.class);

    static {
        LogFactory.useSlf4jLogging(); // MyBatis
    }

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private RepositoryService repositoryService;

    @Test
    public void testReadModel() {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey("DelegateProcess")
                .singleResult();
        BpmnModelInstance bpmnModelInstance = repositoryService.getBpmnModelInstance(processDefinition.getId());

        // Get all Items by type
        for (ServiceTask serviceTask : bpmnModelInstance.getModelElementsByType(ServiceTask.class)) {
            LOGGER.info("\n Service Task: \n Name: {} \n DelegateExpression: {} \n Class: {} \n Implementation: {}",
                    serviceTask.getName(),
                    serviceTask.getCamundaDelegateExpression(),
                    serviceTask.getCamundaClass(),
                    serviceTask.getImplementation());
        }
    }

    @Test
    public void testCreateModel() {
        BpmnModelInstance modelInstance = Bpmn.createEmptyModel();
        Definitions definitions = modelInstance.newInstance(Definitions.class);
        definitions.setTargetNamespace("http://bpmn.io/schema/bpmn");
        modelInstance.setDefinitions(definitions);

        // Add a process to the model
        Process process = modelInstance.newInstance(Process.class);
        process.setId("example-id");
        definitions.addChildElement(process);

        // Create start, servicetask and end
        StartEvent startEvent = modelInstance.newInstance(StartEvent.class);
        startEvent.setId("startEvent");
        process.addChildElement(startEvent);

        ServiceTask serviceTask = modelInstance.newInstance(ServiceTask.class);
        serviceTask.setId("logServiceTask");
        serviceTask.setCamundaDelegateExpression("${logDelegate}");
        process.addChildElement(serviceTask);

        EndEvent endEvent = modelInstance.newInstance(EndEvent.class);
        endEvent.setId("endEvent");
        process.addChildElement(endEvent);

        // connect the three elements
        SequenceFlow startToServiceFlow = modelInstance.newInstance(SequenceFlow.class);
        startToServiceFlow.setId("startToServiceFlow");
        process.addChildElement(startToServiceFlow);
        startToServiceFlow.setSource(startEvent);
        startToServiceFlow.setTarget(serviceTask);

        SequenceFlow serviceToEndFlow = modelInstance.newInstance(SequenceFlow.class);
        serviceToEndFlow.setId("serviceToEndFlow");
        process.addChildElement(serviceToEndFlow);
        serviceToEndFlow.setSource(serviceTask);
        serviceToEndFlow.setTarget(endEvent);

        // Validate the model
        Bpmn.validateModel(modelInstance);

        String xmlString = Bpmn.convertToString(modelInstance);
        LOGGER.info("BPMN XML: \n" + xmlString);
    }

    @Test
    public void testCreateModelFluentAPI() {
        BpmnModelInstance modelInstance = Bpmn.createProcess("fluent-process-example")
                .name("Fluent API Example process")
                .executable()
                .startEvent("StartEvent")
                .serviceTask("ServiceTask")
                .camundaDelegateExpression("${logDelegate}")
                .endEvent()
                .done();

        // Validate the model
        Bpmn.validateModel(modelInstance);

        String xmlString = Bpmn.convertToString(modelInstance);
        LOGGER.info("BPMN XML: \n" + xmlString);
    }

    @Test
    public void testCreateModelFluentAPIGateways() {
        BpmnModelInstance modelInstance = Bpmn.createProcess("fluent-process-example")
                .name("Fluent API Example process")
                .executable()
                .startEvent("StartEvent")
                .message("startMessage")
                .parallelGateway("fork")
                .serviceTask("ServiceTask1")
                .camundaDelegateExpression("${logDelegate}")
                .parallelGateway("join")
                .moveToNode("fork")
                .serviceTask("ServiceTask2")
                .camundaDelegateExpression("${logDelegate}")
                .connectTo("join")
                .endEvent()
                .done();

        // Validate the model
        Bpmn.validateModel(modelInstance);

        String xmlString = Bpmn.convertToString(modelInstance);
        LOGGER.info("BPMN XML: \n" + xmlString);

        // Deploy the model using the repository service.
        DeploymentWithDefinitions deployment = repositoryService
                .createDeployment()
                .addModelInstance("fluent-process-example.bpmn", modelInstance) // must have .bpmn suffix
                .deployWithResult();
        assertEquals(1, deployment.getDeployedProcessDefinitions().size());
    }
}
