package co.com.vetclinica.tasks;

import co.com.vetclinica.interactions.EsperarElemento;
import co.com.vetclinica.userinterfaces.MascotasUI;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.actions.Click;

/**
 * Abre el modal de historial medico de una mascota desde su tarjeta.
 */
public class VerHistorialMascota implements Task {

    private final String nombre;

    public VerHistorialMascota(String nombre) {
        this.nombre = nombre;
    }

    public static VerHistorialMascota deLaMascota(String nombre) {
        return Tasks.instrumented(VerHistorialMascota.class, nombre);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                EsperarElemento.seaVisible(MascotasUI.tarjetaPorNombre(nombre)),
                EsperarElemento.seaClickeable(MascotasUI.botonHistorialDe(nombre)),
                Click.on(MascotasUI.botonHistorialDe(nombre)),
                EsperarElemento.seaVisible(MascotasUI.tituloHistorialDe(nombre))
        );
    }
}
