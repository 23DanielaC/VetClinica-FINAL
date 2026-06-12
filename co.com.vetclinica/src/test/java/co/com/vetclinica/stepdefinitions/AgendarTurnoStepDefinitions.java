package co.com.vetclinica.stepdefinitions;

import co.com.vetclinica.models.Turno;
import co.com.vetclinica.questions.TurnoAgendado;
import co.com.vetclinica.tasks.AgendarTurno;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Entonces;

import java.util.List;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static org.hamcrest.Matchers.is;

public class AgendarTurnoStepDefinitions {

    @Cuando("agenda un turno con los siguientes datos")
    public void agendaUnTurnoConLosSiguientesDatos(List<Turno> turnos) {
        theActorInTheSpotlight().attemptsTo(AgendarTurno.conLosDatos(turnos.get(0)));
    }

    @Entonces("el turno de la mascota {string} debería aparecer en la agenda")
    public void elTurnoDeLaMascotaDeberiaAparecerEnLaAgenda(String mascota) {
        theActorInTheSpotlight().should(
                seeThat("el turno está en la agenda", TurnoAgendado.paraLaMascota(mascota), is(true))
        );
    }
}
