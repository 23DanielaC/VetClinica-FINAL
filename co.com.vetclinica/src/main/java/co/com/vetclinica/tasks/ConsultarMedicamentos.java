package co.com.vetclinica.tasks;

import co.com.vetclinica.interactions.EsperarElemento;
import co.com.vetclinica.userinterfaces.MedicamentosUI;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;

/**
 * Navega al modulo de Medicamentos y espera a que cargue el inventario.
 */
public class ConsultarMedicamentos implements Task {

    public static ConsultarMedicamentos elInventario() {
        return Tasks.instrumented(ConsultarMedicamentos.class);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                NavegarA.elModulo("Medicamentos"),
                EsperarElemento.seaVisible(MedicamentosUI.TITULO_INVENTARIO)
        );
    }
}
