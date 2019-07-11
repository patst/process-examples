package de.patst.process.delegate;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.MessageCorrelationResultType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.QueryParam;
import java.util.Objects;

@RestController
public class CustomerCreatedController {

    @Autowired
    private RuntimeService runtimeService;

    @PutMapping("/customer")
    public void createCustomer(
            @RequestBody String customerId,
            @QueryParam("processInstanceId") String processInstanceId) {
        Objects.requireNonNull(customerId);
        Objects.requireNonNull(processInstanceId);
        // Find the process instance to which we have to send a message
        MessageCorrelationResultType resultType = runtimeService
                .createMessageCorrelation("CustomerCreatedMessage")
                .setVariable("customerId", customerId)
                .processInstanceId(processInstanceId)
                .correlateWithResult().getResultType();
        Objects.requireNonNull(resultType);
    }
}
