package co.com.vetclinica.interactions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Espera explicita compatible con Serenity 4 (Targets + WebDriverWait).
 * Encapsula la sincronizacion para evitar fallos intermitentes por timing
 * en la SPA (Vue) que renderiza modales y tablas de forma asincrona.
 */
public class EsperarElemento implements Interaction {

    private static final Duration TIMEOUT_POR_DEFECTO = Duration.ofSeconds(15);

    private final Target target;
    private final Duration timeout;
    private final boolean clickeable;

    public EsperarElemento(Target target, Duration timeout, boolean clickeable) {
        this.target = target;
        this.timeout = timeout;
        this.clickeable = clickeable;
    }

    public static Interaction seaVisible(Target target) {
        return new EsperarElemento(target, TIMEOUT_POR_DEFECTO, false);
    }

    public static Interaction seaVisible(Target target, Duration timeout) {
        return new EsperarElemento(target, timeout, false);
    }

    public static Interaction seaClickeable(Target target) {
        return new EsperarElemento(target, TIMEOUT_POR_DEFECTO, true);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        WebDriverWait wait = new WebDriverWait(
                BrowseTheWeb.as(actor).getDriver(),
                timeout);
        if (clickeable) {
            wait.until(ExpectedConditions.elementToBeClickable(target.resolveFor(actor)));
        } else {
            wait.until(ExpectedConditions.visibilityOf(target.resolveFor(actor)));
        }
    }
}
