package co.com.vetclinica.questions;

import co.com.vetclinica.userinterfaces.MenuPrincipal;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;

/**
 * Verifica que exista una sesion activa comprobando la presencia del boton
 * "Cerrar sesion" del layout autenticado.
 */
public class EstaAutenticado implements Question<Boolean> {

    public static EstaAutenticado enElSistema() {
        return new EstaAutenticado();
    }

    @Override
    public Boolean answeredBy(Actor actor) {
        try {
            return MenuPrincipal.BTN_CERRAR_SESION.resolveFor(actor).isVisible();
        } catch (RuntimeException e) {
            return false;
        }
    }
}
