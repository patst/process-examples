package de.patst.process.delegate;

import org.camunda.bpm.engine.ExternalTaskService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.externaltask.LockedExternalTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ExternalTaskLogDelegate {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExternalTaskLogDelegate.class);

    @Autowired
    private ExternalTaskService externalTaskService;

    @Autowired
    private RuntimeService runtimeService;

    // Check every 2 seconds for new tasks
    @Scheduled(fixedDelay = 2_000)
    public void initialize() {
        // Check for new tasks and lock them for 10 seconds
        List<LockedExternalTask> tasks = this.externalTaskService
                .fetchAndLock(1, ExternalTaskLogDelegate.class.getCanonicalName())
                .topic("logDelegateTopic", 10_000)
                .processDefinitionKey("DelegateProcess")
                .execute();
        tasks.forEach(task -> {
            //Execute the task
            Map<String, Object> variables = runtimeService.getVariables(task.getExecutionId());
            LOGGER.info("--- Start Logging in ExternalTaskLogDelegate bean:---");
            LOGGER.info("Process Variables:");
            variables
                    .forEach((key, value) -> LOGGER.info("Variable Name: " + key + "; value=" + value));
            LOGGER.info("--- End Logging in ExternalTaskLogDelegate bean:---");

            // and set it done
            this.externalTaskService.complete(
                    task.getId(),
                    ExternalTaskLogDelegate.class.getCanonicalName());
        });
    }
}
