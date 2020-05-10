package de.patst.process.testing;

import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.assertThat;

import de.patst.process.complex.DeliveryServiceMock;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.ibatis.logging.LogFactory;
import org.camunda.bpm.engine.MismatchingMessageCorrelationException;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ComplexCorrelationTest {

  static {
    LogFactory.useSlf4jLogging(); // MyBatis
  }

  @Autowired
  private RuntimeService runtimeService;

  /**
   * The test demonstrates an example where the processInstanceId alone does not provide a unique result for message correlation. There are multiple process
   * executions waiting for a 'DeliveryServicePriceReceivedMessage' message.
   *
   * @throws MismatchingMessageCorrelationException because correlation to a single PI execution is not possible.
   * @throws InterruptedException                   because of Thread.sleep
   */
  @Test(expected = org.camunda.bpm.engine.MismatchingMessageCorrelationException.class)
  public synchronized void testComplexCorrelation() throws MismatchingMessageCorrelationException, InterruptedException {
    Collection<String> deliveryServices = DeliveryServiceMock.DELIVERY_PRICE_MAP.keySet();

    List<String> deliveryServicesList = new ArrayList<>(deliveryServices);

    ProcessInstance processInstance = runtimeService
        .createMessageCorrelation("ComplexCorrelationExampleStartMessage")
        .setVariable("deliveryServices", deliveryServicesList)
        .correlateStartMessage();

    //Wait to all async task to be finished
    this.wait(4_000);

    assertThat(processInstance)
        .isEnded();
  }

}
