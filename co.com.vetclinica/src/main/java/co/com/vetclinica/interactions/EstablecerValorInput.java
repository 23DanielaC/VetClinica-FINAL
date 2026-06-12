package co.com.vetclinica.interactions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

/**
 * Establece el valor de un input via JavaScript y dispara los eventos input y
 * change. Es la via fiable para inputs de tipo date/time, donde el envio de
 * teclas depende del locale del navegador y resulta inestable.
 */
public class EstablecerValorInput implements Interaction {

    private static final String SCRIPT =
            "arguments[0].value = arguments[1];"
                    + "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));"
                    + "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));";

    private final Target target;
    private final String valor;

    public EstablecerValorInput(Target target, String valor) {
        this.target = target;
        this.valor = valor;
    }

    public static EstablecerValorInput de(Target target, String valor) {
        return new EstablecerValorInput(target, valor);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        WebElement elemento = target.resolveFor(actor);
        JavascriptExecutor js = (JavascriptExecutor) BrowseTheWeb.as(actor).getDriver();
        js.executeScript(SCRIPT, elemento, valor);
    }
}
