package co.com.vetclinica.stepdefinitions;

import co.com.vetclinica.models.Cliente;
import co.com.vetclinica.questions.ClienteRegistrado;
import co.com.vetclinica.tasks.AgregarCliente;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Entonces;

import java.util.List;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static org.hamcrest.Matchers.is;

public class AgregarClienteStepDefinitions {

    @Cuando("registra un cliente con los siguientes datos")
    public void registraUnClienteConLosSiguientesDatos(List<Cliente> clientes) {
        theActorInTheSpotlight().attemptsTo(AgregarCliente.conLosDatos(clientes.get(0)));
    }

    @Entonces("el cliente {string} debería aparecer en el listado")
    public void elClienteDeberiaAparecerEnElListado(String nombre) {
        theActorInTheSpotlight().should(
                seeThat("el cliente está en el listado", ClienteRegistrado.conNombre(nombre), is(true))
        );
    }
}
