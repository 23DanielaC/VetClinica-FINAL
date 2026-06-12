package co.com.vetclinica.interactions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Acepta (cierra) el window.alert() nativo del navegador.
 * Requiere la capability unhandledPromptBehavior=ignore (ver serenity.conf)
 * para que el alert permanezca disponible en lugar de ser descartado.
 */
public class AceptarAlerta implements Interaction {

    private static final Duration TIMEOUT = Duration.ofSeconds(10);

    public static AceptarAlerta delSistema() {
        return new AceptarAlerta();
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        WebDriverWait wait = new WebDriverWait(
                BrowseTheWeb.as(actor).getDriver(), TIMEOUT);
        wait.until(ExpectedConditions.alertIsPresent());
        BrowseTheWeb.as(actor).getDriver().switchTo().alert().accept();
    }
}
