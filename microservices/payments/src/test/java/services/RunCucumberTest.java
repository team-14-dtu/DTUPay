package services;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

// @author : Søren
@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = "summary",
        snippets = CucumberOptions.SnippetType.CAMELCASE
)
public class RunCucumberTest {
}
