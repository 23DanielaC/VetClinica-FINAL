package co.com.vetclinica.stepdefinitions;

import co.com.vetclinica.models.Credenciales;
import co.com.vetclinica.questions.EstaAutenticado;
import co.com.vetclinica.tasks.AbrirPagina;
import co.com.vetclinica.tasks.Autenticarse;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;

import java.util.List;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static org.hamcrest.Matchers.is;

public class LoginStepDefinitions {

    @Dado("que el usuario está en la página de inicio de sesión")
    public void queElUsuarioEstaEnLaPaginaDeInicioDeSesion() {
        theActorInTheSpotlight().wasAbleTo(AbrirPagina.deInicioDeSesion());
    }

    @Cuando("inicia sesión con las siguientes credenciales")
    public void iniciaSesionConLasSiguientesCredenciales(List<Credenciales> credenciales) {
        theActorInTheSpotlight().attemptsTo(Autenticarse.con(credenciales));
    }

    @Entonces("debería visualizar el panel principal autenticado")
    public void deberiaVisualizarElPanelPrincipalAutenticado() {
        theActorInTheSpotlight().should(
                seeThat("la sesión está activa", EstaAutenticado.enElSistema(), is(true))
        );
    }
}
