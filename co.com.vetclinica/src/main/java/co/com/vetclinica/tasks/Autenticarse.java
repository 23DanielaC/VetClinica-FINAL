package co.com.vetclinica.tasks;

import co.com.vetclinica.interactions.EsperarElemento;
import co.com.vetclinica.models.Credenciales;
import co.com.vetclinica.userinterfaces.LoginUI;
import co.com.vetclinica.userinterfaces.MenuPrincipal;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.actions.Clear;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;

import java.time.Duration;
import java.util.List;

/**
 * Ingresa las credenciales y envia el formulario de inicio de sesion,
 * esperando a que aparezca el layout autenticado.
 */
public class Autenticarse implements Task {

    private final Credenciales credenciales;

    public Autenticarse(Credenciales credenciales) {
        this.credenciales = credenciales;
    }

    /** Recibe la lista mapeada desde la DataTable y usa la primera fila. */
    public static Autenticarse con(List<Credenciales> credenciales) {
        if (credenciales == null || credenciales.isEmpty()) {
            throw new IllegalArgumentException("Se requiere al menos una fila de credenciales");
        }
        return Tasks.instrumented(Autenticarse.class, credenciales.get(0));
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                EsperarElemento.seaVisible(LoginUI.INPUT_CORREO),
                Click.on(LoginUI.INPUT_CORREO),
                Clear.field(LoginUI.INPUT_CORREO),
                Enter.theValue(credenciales.getCorreo()).into(LoginUI.INPUT_CORREO),
                Click.on(LoginUI.INPUT_CLAVE),
                Clear.field(LoginUI.INPUT_CLAVE),
                Enter.theValue(credenciales.getClave()).into(LoginUI.INPUT_CLAVE),
                Click.on(LoginUI.BTN_INICIAR_SESION),
                // El login es asincrono (~500 ms) y redirige al dashboard
                EsperarElemento.seaVisible(MenuPrincipal.BTN_CERRAR_SESION, Duration.ofSeconds(20))
        );
    }
}
