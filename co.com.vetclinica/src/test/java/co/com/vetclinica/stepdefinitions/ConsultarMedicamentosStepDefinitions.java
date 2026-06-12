package co.com.vetclinica.stepdefinitions;

import co.com.vetclinica.questions.MedicamentosVisibles;
import co.com.vetclinica.tasks.ConsultarMedicamentos;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Entonces;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static org.hamcrest.Matchers.is;

public class ConsultarMedicamentosStepDefinitions {

    @Cuando("consulta el inventario de medicamentos")
    public void consultaElInventarioDeMedicamentos() {
        theActorInTheSpotlight().attemptsTo(ConsultarMedicamentos.elInventario());
    }

    @Entonces("debería visualizar el catálogo de medicamentos")
    public void deberiaVisualizarElCatalogoDeMedicamentos() {
        theActorInTheSpotlight().should(
                seeThat("el inventario está visible", MedicamentosVisibles.enElInventario(), is(true))
        );
    }
}
