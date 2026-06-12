package co.com.vetclinica.tasks;

import co.com.vetclinica.models.Credenciales;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;

import java.util.Collections;

/**
 * Precondicion reutilizable: abre la pagina e inicia sesion con el usuario
 * administrador por defecto. Se usa como Dado en los escenarios de los demas
 * modulos para no repetir el flujo de autenticacion.
 */
public class IniciarSesion implements Task {

    private static final String CORREO_ADMIN = "admin@vetclinica.com";
    private static final String CLAVE_ADMIN = "admin123";

    public static IniciarSesion comoAdministrador() {
        return Tasks.instrumented(IniciarSesion.class);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                AbrirPagina.deInicioDeSesion(),
                Autenticarse.con(Collections.singletonList(
                        new Credenciales(CORREO_ADMIN, CLAVE_ADMIN)))
        );
    }
}
