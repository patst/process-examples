package de.patst.process.listener;

import de.patst.process.delegate.LogDelegate;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LogListener implements ExecutionListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogDelegate.class);

    private static final String FORMAT = "%-36s%-40s%-15s%s";

    @Override
    public void notify(DelegateExecution execution) {
        LOGGER.info(String.format(FORMAT,
                "----- Execution Listener called:",
                execution.getCurrentActivityName(),
                execution.getCurrentTransitionId(),
                execution.getEventName()));
    }
}
