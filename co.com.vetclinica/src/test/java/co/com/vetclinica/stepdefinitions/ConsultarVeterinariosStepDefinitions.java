package co.com.vetclinica.stepdefinitions;

import co.com.vetclinica.questions.VeterinariosVisibles;
import co.com.vetclinica.tasks.ConsultarVeterinarios;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Entonces;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static org.hamcrest.Matchers.is;

public class ConsultarVeterinariosStepDefinitions {

    @Cuando("consulta el directorio de veterinarios")
    public void consultaElDirectorioDeVeterinarios() {
        theActorInTheSpotlight().attemptsTo(ConsultarVeterinarios.elDirectorio());
    }

    @Entonces("debería visualizar el directorio del equipo médico veterinario")
    public void deberiaVisualizarElDirectorioDelEquipoMedicoVeterinario() {
        theActorInTheSpotlight().should(
                seeThat("el directorio está visible", VeterinariosVisibles.enElDirectorio(), is(true))
        );
    }
}
