package org.spring.batch.auto;

import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.springframework.boot.test.context.SpringBootTest;

@RunWith(Cucumber.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@CucumberOptions(
        features = "src/test/resources/features",    // Path to your feature files
        glue = "org.spring.batch.auto",    // Path to your step definitions
        plugin = {
                "pretty",                       // Output logs in a readable format
                "html:target/cucumber-report.html",  // Generate HTML report
                "json:target/cucumber-report.json"   // Generate JSON report
        }
)
@CucumberContextConfiguration
public class CucumberTest {

}
