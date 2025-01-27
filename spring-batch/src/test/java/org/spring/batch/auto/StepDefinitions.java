package org.spring.batch.auto;

import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StepDefinitions {

    @Autowired
    private TestRestTemplate testRestTemplate;

    private ResponseEntity<String> response;

    @Before
    public void before() {
        testRestTemplate = testRestTemplate.withBasicAuth("admin", "admin");
    }

    @Given("the Spring Boot application is running")
    public void the_spring_boot_application_is_running() {
        // Application should be running as part of @SpringBootTest setup
    }

    @When("I send a GET request to {string}")
    public void i_send_a_get_request_to(String endpoint) {
        response =  testRestTemplate.getForEntity(endpoint, String.class);
    }

    @Then("the response should be {string}")
    public void the_response_should_be(String expectedResponse) {
        assertEquals(expectedResponse, response.getBody());
    }

    @And("the statuscode should be {int}")
    public void the_statuscode_should_be(int expectedStatusCode) {
        assertEquals(expectedStatusCode, response.getStatusCode().value());
    }
}

