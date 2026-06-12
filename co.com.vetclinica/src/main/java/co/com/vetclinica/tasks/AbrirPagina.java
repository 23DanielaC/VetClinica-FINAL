package co.com.vetclinica.tasks;

import co.com.vetclinica.userinterfaces.Inicio;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.actions.Open;

/**
 * Abre el navegador en la pagina de inicio de sesion de VetClinica.
 */
public class AbrirPagina implements Task {

    public static AbrirPagina deInicioDeSesion() {
        return Tasks.instrumented(AbrirPagina.class);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(Open.browserOn(new Inicio()));
    }
}
