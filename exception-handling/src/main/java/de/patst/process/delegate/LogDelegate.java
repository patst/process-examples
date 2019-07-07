package de.patst.process.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LogDelegate implements JavaDelegate {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogDelegate.class);

    @Override
    public void execute(DelegateExecution execution) {
        LOGGER.info("--- Start Logging in LogDelegate bean:---");
        LOGGER.info("Activity Name: " + execution.getCurrentActivityName());
        LOGGER.info("Process Variables:");
        execution.getVariables()
                .forEach((key, value) -> LOGGER.info("Variable Name: " + key + "; value=" + value));
        LOGGER.info("--- End Logging in LogDelegate bean:---");
    }
}
