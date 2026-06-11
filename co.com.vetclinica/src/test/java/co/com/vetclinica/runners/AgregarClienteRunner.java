package co.com.vetclinica.runners;

import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
        features = "src/test/resources/features/clientes/AgregarCliente.feature",
        glue = {"co.com.vetclinica.stepdefinitions", "co.com.vetclinica.utils.hooks"},
        snippets = CucumberOptions.SnippetType.CAMELCASE
)
public class AgregarClienteRunner {
}
