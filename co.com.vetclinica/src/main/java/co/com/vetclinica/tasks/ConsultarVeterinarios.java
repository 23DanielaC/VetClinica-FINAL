package co.com.vetclinica.tasks;

import co.com.vetclinica.interactions.EsperarElemento;
import co.com.vetclinica.userinterfaces.VeterinariosUI;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;

/**
 * Navega al modulo de Veterinarios y espera a que cargue el directorio.
 */
public class ConsultarVeterinarios implements Task {

    public static ConsultarVeterinarios elDirectorio() {
        return Tasks.instrumented(ConsultarVeterinarios.class);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                NavegarA.elModulo("Veterinarios"),
                EsperarElemento.seaVisible(VeterinariosUI.TITULO_DIRECTORIO)
        );
    }
}
