package de.patst.process.testing;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.extension.process_test_coverage.junit.rules.TestCoverageProcessEngineRuleBuilder;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ProcessCoverageTest {

    @Autowired
    private ProcessEngine processEngine;

    @Rule
    @ClassRule
    public static ProcessEngineRule rule;

    @PostConstruct
    void initRule() {
        rule = TestCoverageProcessEngineRuleBuilder.create(processEngine).build();
    }

    /**
     * The testcase generates a file with the coverage report:
     * target/process-test-coverage/de.patst.process.testing.ProcessCoverageTest/testProcessCoverage_TestProcessCoverage.html
     */
    @Test
    @Deployment(resources = "testing_coverage.bpmn") // only required for process test coverage
    public void testProcessCoverage() {
        Map<String, Object> parameters = new HashMap<>();
        // Input must be set, otherwise there will be an exception
        // parameters.put("input", 1);
        parameters.put("input", 0);
        this.processEngine
                .getRuntimeService()
                .startProcessInstanceByMessage("TestProcessCoverageStartMessage", "", parameters);
    }
}
