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
public class CreateCustomerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateCustomerService.class);

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
                .subscribe("createCustomer")
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

        Object customer = externalTask.getVariable("customer");
        if(customer == null) {
            LOGGER.info("no customer set.");
            externalTaskService.handleFailure(externalTask, "NO_CUSTOMER", "No customer information present", 0, 0);
        } else {
            LOGGER.info("customer created : " + customer.toString());
            // here we could persist the customer into a database or whatever
            // as result we set a customer id
            externalTaskService.complete(externalTask, Collections.singletonMap("customerId", UUID.randomUUID().toString()));
        }


    }
}
