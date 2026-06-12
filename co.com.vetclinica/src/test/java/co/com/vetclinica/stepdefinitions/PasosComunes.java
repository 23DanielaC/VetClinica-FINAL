package co.com.vetclinica.stepdefinitions;

import co.com.vetclinica.tasks.IniciarSesion;
import co.com.vetclinica.tasks.NavegarA;
import io.cucumber.java.es.Dado;

import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;

/**
 * Pasos compartidos entre features. El login como administrador y la navegacion
 * por el menu se definen una unica vez aqui y se reutilizan como precondiciones.
 */
public class PasosComunes {

    @Dado("que el usuario inició sesión como administrador")
    public void queElUsuarioInicioSesionComoAdministrador() {
        theActorInTheSpotlight().wasAbleTo(IniciarSesion.comoAdministrador());
    }

    @Dado("navega al módulo {string}")
    public void navegaAlModulo(String modulo) {
        theActorInTheSpotlight().attemptsTo(NavegarA.elModulo(modulo));
    }
}
