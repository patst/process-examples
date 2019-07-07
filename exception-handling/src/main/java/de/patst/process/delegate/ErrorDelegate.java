package de.patst.process.delegate;

import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ErrorDelegate implements JavaDelegate {

    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorDelegate.class);

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        LOGGER.info("--- Throwing BPMN error from activity " + execution.getCurrentActivityName());
        throw new BpmnError("0815", "A service call failed");
    }
}
