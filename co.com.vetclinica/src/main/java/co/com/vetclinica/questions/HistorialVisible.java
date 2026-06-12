package co.com.vetclinica.questions;

import co.com.vetclinica.userinterfaces.MascotasUI;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;

/**
 * Verifica que el modal de historial medico de una mascota este visible,
 * comprobando su encabezado "Historial — <nombre>".
 */
public class HistorialVisible implements Question<Boolean> {

    private final String nombre;

    public HistorialVisible(String nombre) {
        this.nombre = nombre;
    }

    public static HistorialVisible deLaMascota(String nombre) {
        return new HistorialVisible(nombre);
    }

    @Override
    public Boolean answeredBy(Actor actor) {
        try {
            return MascotasUI.tituloHistorialDe(nombre).resolveFor(actor).isVisible();
        } catch (RuntimeException e) {
            return false;
        }
    }
}
