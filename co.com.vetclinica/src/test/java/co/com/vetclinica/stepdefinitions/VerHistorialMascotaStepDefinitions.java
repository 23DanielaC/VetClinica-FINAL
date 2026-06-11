package co.com.vetclinica.stepdefinitions;

import co.com.vetclinica.models.Mascota;
import co.com.vetclinica.questions.HistorialVisible;
import co.com.vetclinica.tasks.AgregarMascota;
import co.com.vetclinica.tasks.VerHistorialMascota;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static org.hamcrest.Matchers.is;

public class VerHistorialMascotaStepDefinitions {

    @Dado("existe una mascota registrada con nombre {string}")
    public void existeUnaMascotaRegistradaConNombre(String nombre) {
        // Precondicion: crea la mascota cuyo historial se consultara
        theActorInTheSpotlight().attemptsTo(
                AgregarMascota.conLosDatos(new Mascota(nombre, "", "", "")));
    }

    @Cuando("consulta el historial médico de la mascota {string}")
    public void consultaElHistorialMedicoDeLaMascota(String nombre) {
        theActorInTheSpotlight().attemptsTo(VerHistorialMascota.deLaMascota(nombre));
    }

    @Entonces("debería visualizar el historial médico de {string}")
    public void deberiaVisualizarElHistorialMedicoDe(String nombre) {
        theActorInTheSpotlight().should(
                seeThat("el historial está visible", HistorialVisible.deLaMascota(nombre), is(true))
        );
    }
}
