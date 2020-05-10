package de.patst.process.complex.listener;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DeliveryPriceReceived implements ExecutionListener {

  private static final Logger LOGGER = LoggerFactory.getLogger(DeliveryPriceReceived.class);

  @Override
  public void notify(DelegateExecution execution) throws Exception {
    String deliveryService = execution.getVariable("deliveryService").toString();
    Double price = (Double) execution.getVariable("deliveryPrice");
    LOGGER.info("Received delivery for processInstanceId {}: \tDeliveryService={} \t price={}",
        execution.getProcessInstanceId(),
        deliveryService,
        price);

  }
}
