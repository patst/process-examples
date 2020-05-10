package de.patst.process.complex;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import org.camunda.bpm.engine.RuntimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Class for mocking the delivery service. Accepts requests and then later sends a response to a process instance.
 * <p>
 * During response sending correlation must happen.
 */
@Service
public class DeliveryServiceMock {

  private static final Logger LOGGER = LoggerFactory.getLogger(DeliveryServiceMock.class);

  public static final Map<String, Double> DELIVERY_PRICE_MAP = new HashMap<>();

  // create some static prices for delivery services for demonstration purposes
  static {
    DELIVERY_PRICE_MAP.put("ServiceA", 100d);
    DELIVERY_PRICE_MAP.put("ServiceB", 200d);
    DELIVERY_PRICE_MAP.put("ServiceC", 300d);
    DELIVERY_PRICE_MAP.put("ServiceD", 400d);
    DELIVERY_PRICE_MAP.put("ServiceE", 500d);
    DELIVERY_PRICE_MAP.put("ServiceF", 600d);
  }

  private Map<String, Map<String, String>> openRequests = new HashMap<>();

  private RuntimeService runtimeService;

  @Autowired
  private DeliveryServiceMock(RuntimeService runtimeService) {
    this.runtimeService = runtimeService;
  }

  /**
   * Accepts the request for getting a delivery price. The request is later handled and a response is send asynchronuous.
   *
   * @param deliveryService to get a price from.
   */
  public void askForDeliveryPrice(String deliveryService, Map<String, String> correlationParams) {
    this.openRequests.put(deliveryService, correlationParams);
  }

  /**
   * Checks every second for open requests and randomly replies to one of the requests.
   */
  @Scheduled(fixedRate = 1000)
  protected void setResponsesAsync() {
    if (openRequests.isEmpty()) {
      LOGGER.debug("No open requests for correlation.");
    } else {
      // Pick a request randomly
      int requestNumber = new Random().nextInt(openRequests.size());
      Iterator<Entry<String, Map<String, String>>> iterator = openRequests.entrySet().iterator();
      int i = 0;
      while (i < requestNumber) {
        iterator.next();
        i++;
      }
      Entry<String, Map<String, String>> request = iterator.next();
      // Remove the handled request from the request map
      openRequests.remove(request.getKey());

      this.correlateRequest(DELIVERY_PRICE_MAP.get(request.getKey()), request.getValue());
    }
  }

  private void correlateRequest(Double price, Map<String, String> correlationParams) {
    // Do the correlation to a running process instance
    // The process instance id does not provide a unique result
    this.runtimeService
        .createMessageCorrelation("DeliveryServicePriceReceivedMessage")
        .setVariable("deliveryPrice", price)
        .processInstanceId(correlationParams.get("processInstanceId"))
        .correlateWithResult();

  }
}
