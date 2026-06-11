package co.com.vetclinica.tasks;

import co.com.vetclinica.interactions.EsperarElemento;
import co.com.vetclinica.models.Cliente;
import co.com.vetclinica.userinterfaces.ClientesUI;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;

import java.time.Duration;

/**
 * Abre el modal "Nuevo Cliente", completa el formulario y guarda.
 * Los campos opcionales solo se diligencian si vienen informados en el modelo.
 */
public class AgregarCliente implements Task {

    private final Cliente cliente;

    public AgregarCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public static AgregarCliente conLosDatos(Cliente cliente) {
        return Tasks.instrumented(AgregarCliente.class, cliente);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                EsperarElemento.seaClickeable(ClientesUI.BTN_NUEVO_CLIENTE),
                Click.on(ClientesUI.BTN_NUEVO_CLIENTE),
                EsperarElemento.seaVisible(ClientesUI.INPUT_NOMBRE),
                Enter.theValue(cliente.getNombre()).into(ClientesUI.INPUT_NOMBRE)
        );

        if (!cliente.getTelefono().isEmpty()) {
            actor.attemptsTo(Enter.theValue(cliente.getTelefono()).into(ClientesUI.INPUT_TELEFONO));
        }
        if (!cliente.getEmail().isEmpty()) {
            actor.attemptsTo(Enter.theValue(cliente.getEmail()).into(ClientesUI.INPUT_EMAIL));
        }
        if (!cliente.getDireccion().isEmpty()) {
            actor.attemptsTo(Enter.theValue(cliente.getDireccion()).into(ClientesUI.INPUT_DIRECCION));
        }

        actor.attemptsTo(
                Click.on(ClientesUI.BTN_GUARDAR),
                // El registro se persiste via API; se espera a que aparezca en el listado
                EsperarElemento.seaVisible(
                        ClientesUI.filaPorNombre(cliente.getNombre()), Duration.ofSeconds(20))
        );
    }
}
