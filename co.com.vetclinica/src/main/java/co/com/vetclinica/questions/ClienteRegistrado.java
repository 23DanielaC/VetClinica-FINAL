package co.com.vetclinica.questions;

import co.com.vetclinica.userinterfaces.ClientesUI;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;

/**
 * Verifica que un cliente aparezca en el listado tras su creacion.
 */
public class ClienteRegistrado implements Question<Boolean> {

    private final String nombre;

    public ClienteRegistrado(String nombre) {
        this.nombre = nombre;
    }

    public static ClienteRegistrado conNombre(String nombre) {
        return new ClienteRegistrado(nombre);
    }

    @Override
    public Boolean answeredBy(Actor actor) {
        try {
            return ClientesUI.filaPorNombre(nombre).resolveFor(actor).isVisible();
        } catch (RuntimeException e) {
            return false;
        }
    }
}
