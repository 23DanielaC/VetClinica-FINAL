package co.com.vetclinica.questions;

import co.com.vetclinica.userinterfaces.VeterinariosUI;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;

/**
 * Verifica que el directorio de veterinarios este visible tras la consulta.
 */
public class VeterinariosVisibles implements Question<Boolean> {

    public static VeterinariosVisibles enElDirectorio() {
        return new VeterinariosVisibles();
    }

    @Override
    public Boolean answeredBy(Actor actor) {
        try {
            return VeterinariosUI.TITULO_DIRECTORIO.resolveFor(actor).isVisible()
                    && VeterinariosUI.CONTADOR.resolveFor(actor).isVisible();
        } catch (RuntimeException e) {
            return false;
        }
    }
}
