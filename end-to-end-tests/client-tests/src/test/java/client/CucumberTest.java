package client;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin="summary",
        snippets = CucumberOptions.SnippetType.CAMELCASE,
        features="features")
public class CucumberTest {
}