package de.patst.process.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
public class CaughtExceptionDelegate implements JavaDelegate {


    @Override
    public void execute(DelegateExecution execution) throws Exception {
        try {
            // here is some service call which returns a exception
            throw new IllegalStateException("Oops, this went wrong");
        } catch (Exception e) {
            execution.setVariable("delegateException", e.getMessage());
        }

    }
}
