# External Task example process

This example demonstrates external task usage.
The example has three modules:

* process: contains the `.bpmn` file and runs the process engine
* contract-service: Is an external task client for the `create contract` topic
* customer-service: Is an external task client for the `create customer` topic

## Documentation

More architectural descriptions are available here: https://docs.camunda.org/manual/7.12/user-guide/process-engine/external-tasks/


## Run the example

1. Start the process-app with `mvn spring-boot:run` or click on the `ProcessApplication` and run the `main` method.
2. the Cockpit is accessible at http://localhost:8080 and process instances can be started 
    * add a variable named `customer` and `contract` to simulate the existence of these business objects. Otherwise a BPMN error is raised by the external tasks.
3. the process instance is visible in the cockpit bit the tasks are not executed at the moment
4. start the *contract-service* or *customer-service* (or both). The tasks should then be executed.
