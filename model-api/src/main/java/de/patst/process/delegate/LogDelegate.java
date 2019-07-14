package de.patst.process.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.model.bpmn.instance.Documentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class LogDelegate implements JavaDelegate {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogDelegate.class);

    @Override
    public void execute(DelegateExecution execution) {
        LOGGER.info("--- Start Logging in LogDelegate bean:---");
        LOGGER.info("Process Variables:");
        execution.getVariables()
                .forEach((key, value) -> LOGGER.info("Variable Name: " + key + "; value=" + value));
        String documentation = execution.
                getBpmnModelElementInstance().
                getDocumentations()
                .stream()
                .map(Documentation::getTextContent)
                .collect(Collectors.joining());
        LOGGER.info("Documentation: " + documentation);
        LOGGER.info("--- End Logging in LogDelegate bean:---");
    }
}
