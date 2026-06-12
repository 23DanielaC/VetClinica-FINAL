package co.com.vetclinica.questions;

import co.com.vetclinica.userinterfaces.TurnosUI;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Verifica que el turno de una mascota aparezca en la agenda tras agendarlo.
 * Usa una espera explicita porque la agenda se re-renderiza al confirmar el
 * turno contra la API (la reactividad de Vue puede dejar el nodo previo stale).
 */
public class TurnoAgendado implements Question<Boolean> {

    private static final Duration TIMEOUT = Duration.ofSeconds(15);

    private final String mascota;

    public TurnoAgendado(String mascota) {
        this.mascota = mascota;
    }

    public static TurnoAgendado paraLaMascota(String mascota) {
        return new TurnoAgendado(mascota);
    }

    @Override
    public Boolean answeredBy(Actor actor) {
        try {
            WebDriverWait wait = new WebDriverWait(
                    BrowseTheWeb.as(actor).getDriver(), TIMEOUT);
            wait.until(ExpectedConditions.visibilityOf(
                    TurnosUI.tarjetaTurnoDe(mascota).resolveFor(actor)));
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }
}
