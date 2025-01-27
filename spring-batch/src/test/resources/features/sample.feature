Feature: Sample Spring Boot API Test

  Scenario: Check the /batch/hello endpoint
    Given the Spring Boot application is running
    When I send a GET request to "/batch/hello"
    Then the response should be "Hello, World!"
    And the statuscode should be 200
