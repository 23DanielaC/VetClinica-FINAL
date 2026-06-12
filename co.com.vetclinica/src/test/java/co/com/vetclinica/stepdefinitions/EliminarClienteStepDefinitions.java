package co.com.vetclinica.stepdefinitions;

import co.com.vetclinica.models.Cliente;
import co.com.vetclinica.questions.TextoDeAlerta;
import co.com.vetclinica.tasks.AgregarCliente;
import co.com.vetclinica.tasks.EliminarCliente;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static org.hamcrest.Matchers.is;

public class EliminarClienteStepDefinitions {

    @Dado("existe un cliente registrado con nombre {string}")
    public void existeUnClienteRegistradoConNombre(String nombre) {
        // Precondicion: garantiza que el cliente a eliminar exista en el listado
        theActorInTheSpotlight().attemptsTo(
                AgregarCliente.conLosDatos(new Cliente(nombre, "", "", "")));
    }

    @Cuando("intenta eliminar el cliente {string}")
    public void intentaEliminarElCliente(String nombre) {
        theActorInTheSpotlight().attemptsTo(EliminarCliente.porNombre(nombre));
    }

    @Entonces("el sistema muestra el mensaje {string}")
    public void elSistemaMuestraElMensaje(String mensajeEsperado) {
        theActorInTheSpotlight().should(
                seeThat("el mensaje del aviso", TextoDeAlerta.visible(), is(mensajeEsperado))
        );
    }
}
