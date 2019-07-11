package de.patst.process.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class CreateCustomerDelegate implements JavaDelegate {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateCustomerDelegate.class);

    public static int PORT = 8080;

    @Override
    public void execute(DelegateExecution execution) throws Exception {

        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("processInstanceId", execution.getProcessInstanceId());
        uriVariables.put("respondTo", "http://localhost:" + PORT + "/customer");

        restTemplate.put("http://localhost:" + PORT + "/remote/api/customer?processInstanceId={processInstanceId}&respondTo={respondTo}",
                execution.getVariable("name"),
                uriVariables);
        LOGGER.info("Request sent");
    }
}
