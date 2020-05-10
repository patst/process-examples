package de.patst.process;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class CorrelationProcessApplication {

  public static void main(String[] args) {
    SpringApplication.run(CorrelationProcessApplication.class, args);
  }

}
