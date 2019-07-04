package de.patst.process.delegate;

import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
public class ExceptionDelegate implements JavaDelegate {


    @Override
    public void execute(DelegateExecution execution) throws Exception {
        // here is some service call which returns a exception
        throw new BpmnError("Oops, this went wrong");
    }
}
