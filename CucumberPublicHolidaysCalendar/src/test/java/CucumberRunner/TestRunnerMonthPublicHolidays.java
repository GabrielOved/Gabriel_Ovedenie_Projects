package CucumberRunner;

// imports needed to run Cucumber tests
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

/**
 * This class serves as the main Test Runner for executing Cucumber tests.
 * It uses JUnit to trigger Cucumber feature execution.
 */
@RunWith(Cucumber.class) // Tells JUnit to run using Cucumber
@CucumberOptions(
        features = "src/test/resources/CucumberFeatures/BehaviorMonthPublicHolidays.feature", // path to the feature file(s) containing Gherkin scenarios
        glue = "CucumberSteps", // package where step definition classes are located
        plugin = {"pretty", "html:target/cucumber-report/cucumberMonthTestReport.html"}, // pretty: prints readable console output, html: generates an HTML report in the target folder
        monochrome = true // makes console output cleaner and easier to read
)
public class TestRunnerMonthPublicHolidays {
}