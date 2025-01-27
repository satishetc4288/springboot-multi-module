package org.spring.batch.auto;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StepDefinitions {

    @Autowired
    private TestRestTemplate restTemplate;

    private ResponseEntity<String> response;

    @Given("the Spring Boot application is running")
    public void the_spring_boot_application_is_running() {
        // Application should be running as part of @SpringBootTest setup
    }

    @When("I send a GET request to {string}")
    public void i_send_a_get_request_to(String endpoint) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("admin", "admin");
        HttpEntity<String> request = new HttpEntity<>(headers);
        response =  restTemplate.exchange(endpoint, HttpMethod.GET, request, String.class);
    }

    @Then("the response should be {string}")
    public void the_response_should_be(String expectedResponse) {
        assertEquals(expectedResponse, response.getBody());
    }
}

