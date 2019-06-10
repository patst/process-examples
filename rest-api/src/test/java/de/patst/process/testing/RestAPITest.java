package de.patst.process.testing;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsonorg.JsonOrgModule;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.PostConstruct;
import java.util.UUID;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RestAPITest {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestAPITest.class);

    private static final String API_PATH = "/rest";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @LocalServerPort // short for: ${local.server.port}
    private int port;


    @PostConstruct
    public void initialize() {
        LOGGER.info("Running webserver on port " + port);
        // Needed for serializing/ deserializing org.json.JSONObject datatype
        objectMapper.registerModule(new JsonOrgModule());
    }

    @Test
    public void checkApiAvailable() {
        ResponseEntity<String> responseEntity = this.restTemplate.getForEntity(
                API_PATH + "/engine",
                String.class);
        LOGGER.info("Engine info: " + responseEntity.getBody());
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
    }

    @Test
    public void getProcessDefinitions() {
        ResponseEntity<String> responseEntity = this.restTemplate.getForEntity(
                API_PATH + "/process-definition",
                String.class);
        LOGGER.info("Process definitions: " + responseEntity.getBody());
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
    }

    @Test
    public void startProcessInstanceWithoutResponse() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("messageName", "RestApiProcessStartMessage");
        ResponseEntity<JSONObject> responseEntity = this.restTemplate.postForEntity(
                API_PATH + "/message",
                jsonObject,
                JSONObject.class);
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        // No response body is expected!
        assertFalse(responseEntity.hasBody());
    }

    /**
     * Starts a process instance and asks for the result to be returned (without variables.
     * <p>
     * Example output:
     * [{"execution":null,"processInstance":{"caseInstanceId":null,"businessKey":"c8620f8e-1598-44c7-856f-9b90fc3e6d98","ended":true,"tenantId":null,"links":[],"id":"6e0bf152-8b7d-11e9-9a38-00155d4b010e","suspended":false,"definitionId":"RestAPIProcess:1:6c881070-8b7d-11e9-9a38-00155d4b010e"},"resultType":"ProcessDefinition"}]
     */
    @Test
    public void startProcessInstanceWithResponse() {
        String businessKey = UUID.randomUUID().toString();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("messageName", "RestApiProcessStartMessage");
        jsonObject.put("businessKey", businessKey);
        jsonObject.put("resultEnabled", true);
        JSONObject variables = new JSONObject()
                .put(
                        "myVariableName",
                        new JSONObject()
                                .put("value", "myVariableValue")
                                .put("type", "String")
                )
                .put(
                        "myBooleanVariable",
                        new JSONObject()
                                .put("value", true)
                                .put("type", "Boolean")
                );
        jsonObject.put("processVariables", variables);
        ResponseEntity<JSONArray> responseEntity = this.restTemplate.postForEntity(
                API_PATH + "/message",
                jsonObject,
                JSONArray.class);
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        // No response body is expected!
        assertTrue(responseEntity.hasBody());
        assertEquals(1, responseEntity.getBody().length());
        LOGGER.info("Process instance: " + responseEntity.getBody().toString());
        // Check if the business key matches the process we started
        assertEquals(businessKey, responseEntity
                .getBody()
                .getJSONObject(0)
                .getJSONObject("processInstance")
                .get("businessKey"));
    }

}
