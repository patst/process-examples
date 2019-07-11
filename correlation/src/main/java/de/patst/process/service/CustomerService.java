package de.patst.process.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.ws.rs.QueryParam;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Class to simulate a external service.
 * Normally the customer service would be some remote API
 */
@RestController
public class CustomerService {


    @Autowired
    private TaskExecutor taskExecutor;

    @PutMapping("/remote/api/customer")
    public void createCustomer(
            @RequestBody String name,
            @QueryParam("processInstanceId") String processInstanceId,
            @QueryParam("respondTo") String respondTo) {
        taskExecutor.execute(() -> {
            this.createCustomerAsync(name, processInstanceId, respondTo);
        });
    }

    private synchronized void createCustomerAsync(String name,
                                                  String processInstanceId,
                                                  String respondTo) {
        Objects.requireNonNull(processInstanceId);
        Objects.requireNonNull(respondTo);

        // Simulate some time the call takes
        try {
            this.wait(1000);
        } catch (InterruptedException e) {
        }
        // Customer created somewhere in a database
        // return customerId
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> correlationVars = new HashMap<>();
        correlationVars.put("processInstanceId", processInstanceId);
        restTemplate.put(respondTo + "?processInstanceId={processInstanceId}",
                UUID.randomUUID().toString(), correlationVars);
    }
}
