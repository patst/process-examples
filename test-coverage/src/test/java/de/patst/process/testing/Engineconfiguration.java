package de.patst.process.testing;

import org.camunda.bpm.engine.impl.history.handler.HistoryEventHandler;
import org.camunda.bpm.extension.process_test_coverage.listeners.FlowNodeHistoryEventHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Engineconfiguration {

    @Bean
    public HistoryEventHandler customHistoryEventHandler() {
        return new FlowNodeHistoryEventHandler();
    }
}
