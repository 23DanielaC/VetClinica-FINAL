package co.com.vetclinica.tasks;

import co.com.vetclinica.interactions.EsperarElemento;
import co.com.vetclinica.userinterfaces.ClientesUI;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.actions.Click;

/**
 * Intenta eliminar un cliente desde el listado.
 * La API no soporta el borrado, por lo que la aplicacion responde con un
 * window.alert() que se valida en la question correspondiente.
 */
public class EliminarCliente implements Task {

    private final String nombre;

    public EliminarCliente(String nombre) {
        this.nombre = nombre;
    }

    public static EliminarCliente porNombre(String nombre) {
        return Tasks.instrumented(EliminarCliente.class, nombre);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                EsperarElemento.seaClickeable(ClientesUI.botonEliminarDe(nombre)),
                Click.on(ClientesUI.botonEliminarDe(nombre))
        );
    }
}
