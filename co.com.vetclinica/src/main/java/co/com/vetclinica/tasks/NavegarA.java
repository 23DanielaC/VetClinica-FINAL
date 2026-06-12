package co.com.vetclinica.tasks;

import co.com.vetclinica.interactions.EsperarElemento;
import co.com.vetclinica.userinterfaces.MenuPrincipal;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.actions.Click;

/**
 * Navega a un modulo del sistema a traves del menu lateral.
 * El nombre debe coincidir con el texto del enlace (Clientes, Mascotas, etc.).
 */
public class NavegarA implements Task {

    private final String modulo;

    public NavegarA(String modulo) {
        this.modulo = modulo;
    }

    public static NavegarA elModulo(String modulo) {
        return Tasks.instrumented(NavegarA.class, modulo);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                EsperarElemento.seaClickeable(MenuPrincipal.enlaceMenu(modulo)),
                Click.on(MenuPrincipal.enlaceMenu(modulo))
        );
    }
}
