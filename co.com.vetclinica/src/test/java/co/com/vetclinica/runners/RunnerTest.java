package co.com.vetclinica.runners;

import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

/**
 * Runner unico que ejecuta los 8 escenarios E2E en el orden definido abajo.
 * El orden del array {@code features} es respetado por Cucumber en ejecucion secuencial.
 */
@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
        // Carpeta raiz: los subdirectorios 01_, 02_, ... garantizan orden lexico
        features = "src/test/resources/features",
        glue = {"co.com.vetclinica.stepdefinitions", "co.com.vetclinica.utils.hooks"},
        snippets = CucumberOptions.SnippetType.CAMELCASE
)
public class RunnerTest {
}
