package co.com.vetclinica.stepdefinitions;

import co.com.vetclinica.models.Mascota;
import co.com.vetclinica.questions.MascotaRegistrada;
import co.com.vetclinica.tasks.AgregarMascota;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Entonces;

import java.util.List;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static org.hamcrest.Matchers.is;

public class AgregarMascotaStepDefinitions {

    @Cuando("registra una mascota con los siguientes datos")
    public void registraUnaMascotaConLosSiguientesDatos(List<Mascota> mascotas) {
        theActorInTheSpotlight().attemptsTo(AgregarMascota.conLosDatos(mascotas.get(0)));
    }

    @Entonces("la mascota {string} debería aparecer en el listado")
    public void laMascotaDeberiaAparecerEnElListado(String nombre) {
        theActorInTheSpotlight().should(
                seeThat("la mascota está en el listado", MascotaRegistrada.conNombre(nombre), is(true))
        );
    }
}
