package de.patst.process.complex.delegate;

import de.patst.process.complex.DeliveryServiceMock;
import java.util.HashMap;
import java.util.Map;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AskForDeliveryPrice implements JavaDelegate {

  private static final Logger LOGGER = LoggerFactory.getLogger(AskForDeliveryPrice.class);

  private final DeliveryServiceMock deliveryServiceMock;

  @Autowired
  private AskForDeliveryPrice(DeliveryServiceMock deliveryServiceMock) {
    this.deliveryServiceMock = deliveryServiceMock;
  }

  @Override
  public void execute(DelegateExecution execution) throws Exception {
    String deliveryService = execution.getVariable("deliveryService").toString();

    LOGGER.info("Creating delivery price request for processInstanceId {} and service {}", execution.getProcessInstanceId(), deliveryService);

    Map<String, String> correlationParams = new HashMap<>();
    correlationParams.put("processInstanceId", execution.getProcessInstanceId());
    this.deliveryServiceMock.askForDeliveryPrice(deliveryService, correlationParams);
  }
}
