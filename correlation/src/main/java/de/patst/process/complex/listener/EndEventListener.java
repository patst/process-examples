package de.patst.process.complex.listener;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class EndEventListener implements ExecutionListener {

  private static final Logger LOGGER = LoggerFactory.getLogger(EndEventListener.class);

  @Override
  public void notify(DelegateExecution execution) {
    LOGGER.info("--- Start logging Variables set for process instance {} ", execution.getProcessInstanceId());
    execution
        .getVariables()
        .forEach((key, value) -> LOGGER.info("Variable Name={} \t Value={}", key, value));
    LOGGER.info("--- End logging for process instance {}", execution.getProcessInstanceId());
  }
}
