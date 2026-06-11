package co.com.vetclinica.questions;

import co.com.vetclinica.userinterfaces.MascotasUI;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;

/**
 * Verifica que una mascota aparezca como tarjeta en el listado tras su creacion.
 */
public class MascotaRegistrada implements Question<Boolean> {

    private final String nombre;

    public MascotaRegistrada(String nombre) {
        this.nombre = nombre;
    }

    public static MascotaRegistrada conNombre(String nombre) {
        return new MascotaRegistrada(nombre);
    }

    @Override
    public Boolean answeredBy(Actor actor) {
        try {
            return MascotasUI.tarjetaPorNombre(nombre).resolveFor(actor).isVisible();
        } catch (RuntimeException e) {
            return false;
        }
    }
}
