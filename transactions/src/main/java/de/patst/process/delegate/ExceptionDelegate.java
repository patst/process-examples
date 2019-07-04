package de.patst.process.delegate;

import org.camunda.bpm.application.ProcessApplicationExecutionException;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ExceptionDelegate implements JavaDelegate {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionDelegate.class);
    public static String EXCEPTION_MESSAGE = "Exception during process execution";
    private static Map<String, Integer> CALLS = new ConcurrentHashMap<>();

    @Override
    public void execute(DelegateExecution execution) throws ProcessApplicationExecutionException {
        CALLS.compute(execution.getProcessInstanceId(), (key, value) -> value == null ? 1 : ++value);

        LOGGER.info("--- Start Logging in ExceptionDelegate bean:---");
        LOGGER.info("ProcessInstanceId: " + execution.getProcessInstanceId());
        LOGGER.info("ExecutionId: " + execution.getId());
        LOGGER.info("Process Variables:");
        execution.getVariables()
                .forEach((key, value) -> LOGGER.info("Variable Name: " + key + "; value=" + value));
        LOGGER.info("Delegate called for processInstance " + CALLS.get(execution.getProcessInstanceId()) + " times");
        LOGGER.info("--- End Logging in ExceptionDelegate bean. ---");


        throw new ProcessApplicationExecutionException(EXCEPTION_MESSAGE);
    }
}
