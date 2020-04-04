# REST API 

The module shows some examples how to setup a project which exposes the Camunda REST API.
The test case gives an example how to consume the APIs.

API descriptions can be found here: https://docs.camunda.org/manual/7.12/reference/rest/

The topic authorization and authentication is no covered in the examples. 

## Implementation

The REST-API is enabled with adding the starter-rest dependency:

``` 
<dependency>
    <groupId>org.camunda.bpm.springboot</groupId>
    <artifactId>camunda-bpm-spring-boot-starter-rest</artifactId>
    <version>${camunda.spring-boot.version}</version>
</dependency>
```

The base path for the REST-API is `/rest`, e.g. `http://localhost:8080/rest/process-definition` to get the process definitions.
