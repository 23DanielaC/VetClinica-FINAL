package co.com.vetclinica.questions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.openqa.selenium.Alert;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * Lee el texto de un window.alert() nativo y lo acepta para no bloquear
 * los pasos posteriores. Usado para validar operaciones restringidas por la
 * API (p. ej. "La API no permite eliminar clientes.").
 */
public class TextoDeAlerta implements Question<String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TextoDeAlerta.class);
    private static final Duration TIMEOUT = Duration.ofSeconds(10);

    public static TextoDeAlerta visible() {
        return new TextoDeAlerta();
    }

    @Override
    public String answeredBy(Actor actor) {
        WebDriverWait wait = new WebDriverWait(
                BrowseTheWeb.as(actor).getDriver(), TIMEOUT);
        Alert alerta = wait.until(ExpectedConditions.alertIsPresent());
        String texto = alerta.getText().trim();
        LOGGER.info("Texto del alert capturado: {}", texto);
        // Se acepta para liberar el hilo de JS del navegador
        alerta.accept();
        return texto;
    }
}
