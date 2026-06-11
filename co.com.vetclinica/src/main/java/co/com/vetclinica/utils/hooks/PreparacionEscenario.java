package co.com.vetclinica.utils.hooks;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import net.serenitybdd.screenplay.actors.OnlineCast;

import static net.serenitybdd.screenplay.actors.OnStage.drawTheCurtain;
import static net.serenitybdd.screenplay.actors.OnStage.setTheStage;
import static net.serenitybdd.screenplay.actors.OnStage.theActorCalled;

/**
 * Hooks de ciclo de vida del escenario.
 * Monta el escenario Screenplay con un elenco en linea (cada actor obtiene
 * automaticamente la habilidad BrowseTheWeb) y crea el actor protagonista.
 */
public class PreparacionEscenario {

    private static final String ACTOR_PRINCIPAL = "Administrador";

    @Before
    public void prepararEscenario() {
        setTheStage(new OnlineCast());
        theActorCalled(ACTOR_PRINCIPAL);
    }

    @After
    public void cerrarEscenario() {
        drawTheCurtain();
    }
}
