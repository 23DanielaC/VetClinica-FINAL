package co.com.vetclinica.tasks;

import co.com.vetclinica.interactions.EsperarElemento;
import co.com.vetclinica.models.Mascota;
import co.com.vetclinica.userinterfaces.MascotasUI;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.actions.SelectFromOptions;

import java.time.Duration;

/**
 * Abre el modal "Nueva Mascota", completa el formulario y guarda.
 * Especie y propietario tienen valores por defecto validos, por lo que solo se
 * seleccionan si vienen informados en el modelo.
 */
public class AgregarMascota implements Task {

    private final Mascota mascota;

    public AgregarMascota(Mascota mascota) {
        this.mascota = mascota;
    }

    public static AgregarMascota conLosDatos(Mascota mascota) {
        return Tasks.instrumented(AgregarMascota.class, mascota);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                EsperarElemento.seaClickeable(MascotasUI.BTN_NUEVA_MASCOTA),
                Click.on(MascotasUI.BTN_NUEVA_MASCOTA),
                EsperarElemento.seaVisible(MascotasUI.INPUT_NOMBRE),
                Enter.theValue(mascota.getNombre()).into(MascotasUI.INPUT_NOMBRE)
        );

        if (mascota.tieneEspecie()) {
            actor.attemptsTo(SelectFromOptions.byVisibleText(mascota.getEspecie())
                    .from(MascotasUI.SELECT_ESPECIE));
        }
        if (!mascota.getRaza().isEmpty()) {
            actor.attemptsTo(Enter.theValue(mascota.getRaza()).into(MascotasUI.INPUT_RAZA));
        }
        if (mascota.tienePropietario()) {
            actor.attemptsTo(SelectFromOptions.byVisibleText(mascota.getPropietario())
                    .from(MascotasUI.SELECT_PROPIETARIO));
        }

        actor.attemptsTo(
                Click.on(MascotasUI.BTN_GUARDAR),
                EsperarElemento.seaVisible(
                        MascotasUI.tarjetaPorNombre(mascota.getNombre()), Duration.ofSeconds(20))
        );
    }
}
