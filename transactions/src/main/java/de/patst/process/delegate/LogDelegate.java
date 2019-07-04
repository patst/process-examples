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
public class LogDelegate implements JavaDelegate {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogDelegate.class);
    private static Map<String, Integer> CALLS = new ConcurrentHashMap<>();

    @Override
    public void execute(DelegateExecution execution) throws ProcessApplicationExecutionException {
        CALLS.compute(execution.getProcessInstanceId(), (key, value) -> value == null ? 1 : ++value);
        LOGGER.info("LogDelegate called with activity " + execution.getCurrentActivityName() + "  for processInstance " + CALLS.get(execution.getProcessInstanceId()) + " times");
    }
}
