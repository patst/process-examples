package de.patst.process.delegate;

import org.camunda.bpm.client.ExternalTaskClient;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.Random;
import java.util.UUID;

@Component
public class CreateContractDelegate {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateContractDelegate.class);

    // THIS DOES NOT WORK!
    // The Process Engine is not running in the same JVM.
    //@Autowired
    //private ExternalTaskService externalTaskService;

    private ExternalTaskClient client;

    @Value("${app.engine.api}")
    private String apiUrl;

    @PostConstruct
    public void init() throws Exception {
        LOGGER.info("Registering external task worker");
        this.client = ExternalTaskClient.create()
                .baseUrl(apiUrl)
                .maxTasks(1)
                .workerId(UUID.randomUUID().toString())
                .build();
        this.client
                .subscribe("createContract")
                .lockDuration(10000) // lock tasks for 10 seconds
                .handler(((externalTask, externalTaskService) -> {
                    LOGGER.info("Task received: " + externalTask.getId());
                    try {
                        handleTask(externalTask, externalTaskService);
                        LOGGER.info("Task handled: " + externalTask.getId());
                    } catch (Exception e) {
                        LOGGER.info("Task handled with exception " + e.getMessage() + ": " + externalTask.getId());
                    }
                }))
                .processDefinitionKey("ExternalTaskProcess")
                //.variables(); Specific variables can be prefetched.
                .open();
    }

    private synchronized void handleTask(ExternalTask externalTask, ExternalTaskService externalTaskService) throws Exception {
        // Random sleep time to simulate execution time
        this.wait(new Random().nextInt(10000));

        Object contract = externalTask.getVariable("contract");
        if (contract == null) {
            LOGGER.info("no contract set.");
            externalTaskService.handleFailure(externalTask, "NO_CONTRACT", "No contract information present", 0, 0);
        } else {
            LOGGER.info("contract created : " + contract.toString());
            // here we could persist the contract into a database or whatever
            // as result we set a contract id
            externalTaskService.complete(externalTask, Collections.singletonMap("contractId", UUID.randomUUID().toString()));
        }


    }
}
