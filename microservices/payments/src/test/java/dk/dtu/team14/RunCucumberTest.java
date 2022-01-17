package dk.dtu.team14;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin="summary",
        snippets = CucumberOptions.SnippetType.CAMELCASE
)
public class RunCucumberTest {
}
