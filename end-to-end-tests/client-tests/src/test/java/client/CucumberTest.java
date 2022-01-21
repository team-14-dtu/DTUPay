package client;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = "summary",
        snippets = CucumberOptions.SnippetType.CAMELCASE,
        features = "features")
public class CucumberTest {
}