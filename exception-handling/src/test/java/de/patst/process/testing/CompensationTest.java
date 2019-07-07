package de.patst.process.testing;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.assertThat;

@ActiveProfiles("jobexecutor")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompensationTest {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Test
    public synchronized void testCompensationExample1() throws InterruptedException {
        ProcessInstance processInstance = runtimeService
                .createMessageCorrelation("CompensationExampleStartMessage")
                .correlateStartMessage();
        this.wait(10000L);
        assertThat(processInstance)
                .hasPassed("OrderCarTask",
                        "ChargeCustomerTask",
                        "CancelOrderThrowEvent",
                        "OrderCancelledEndEvent",
                        "CancelCarOrderTask")
                .hasNotPassed("CommunicateDeliveryDateHumanTask",
                        "OrderSuccesfulEndEvent",
                        "DeliveryDateCommunicatedEndEvent");
        // The problem is, that the communicate delivery date human task is still active,
        // the processinstance is still running!
        assertThat(processInstance).isWaitingAt("CommunicateDeliveryDateHumanTask");
    }

    /**
     * Starts the process and checks if the processinstance is waiting at the 'Communicate delivery task'.
     * The compensation does not work, because the subprocess is not finished and therefore the
     * compensation event is not active.
     *
     * @throws Exception
     */
    @Test
    public synchronized void testCompensationSubprocess1() throws Exception {
        ProcessInstance processInstance = runtimeService
                .createMessageCorrelation("CompensationSubprocessExampleStartMessage")
                .correlateStartMessage();
        this.wait(10000L);
        assertThat(processInstance)
                .hasPassed("StartSubprocessEvent",
                        "OrderCarTask",
                        "ChargeCustomerTask",
                        "CancelOrderThrowEvent",
                        "OrderCancelledEndEvent")
                .hasNotPassed(
                        "CancelCarOrderTask",
                        "CommunicateDeliveryDateHumanTask",
                        "OrderSuccesfulEndEvent",
                        "DeliveryDateCommunicatedEndEvent");
        // The problem is, that the communicate delivery date human task is still active,
        // therefore the compensation does not work.
        // see https://docs.camunda.org/manual/7.11/reference/bpmn20/events/cancel-and-compensation-events/
        assertThat(processInstance).isWaitingAt("CommunicateDeliveryDateHumanTask");
    }

    /**
     * This examples completes the human task and therefore the compensation should work and the process instance
     * should be finished compensated.
     *
     * @throws Exception
     */
    @Test
    public synchronized void testCompensationSubprocessFinishedHumanTask() throws Exception {
        ProcessInstance processInstance = runtimeService
                .createMessageCorrelation("CompensationSubprocessExampleStartMessage")
                .correlateStartMessage();
        this.wait(100L);
        Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
        taskService.complete(task.getId());
        this.wait(10000L);
        assertThat(processInstance)
                .hasPassed("StartSubprocessEvent",
                        "OrderCarTask",
                        "ChargeCustomerTask",
                        "DeliveryDateCommunicatedEndEvent",
                        "CancelCarOrderTask",
                        "OrderCarSubprocess",
                        "CommunicateDeliveryDateHumanTask",
                        "CancelOrderThrowEvent",
                        "OrderCancelledEndEvent");
        // The problem is, that the communicate delivery date human task is still active,
        // the processinstance is still running!
        assertThat(processInstance).isEnded();
    }

}
