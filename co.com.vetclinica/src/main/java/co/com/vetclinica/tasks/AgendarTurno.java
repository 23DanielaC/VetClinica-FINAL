package co.com.vetclinica.tasks;

import co.com.vetclinica.interactions.EsperarElemento;
import co.com.vetclinica.interactions.EstablecerValorInput;
import co.com.vetclinica.models.Turno;
import co.com.vetclinica.userinterfaces.TurnosUI;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.actions.SelectFromOptions;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Abre el modal "Nuevo Turno", selecciona cliente/mascota/veterinario, define
 * el motivo y agenda. El cliente se selecciona primero porque condiciona las
 * opciones de mascota disponibles (onClienteChange en la UI).
 */
public class AgendarTurno implements Task {

    private final Turno turno;

    public AgendarTurno(Turno turno) {
        this.turno = turno;
    }

    public static AgendarTurno conLosDatos(Turno turno) {
        return Tasks.instrumented(AgendarTurno.class, turno);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                EsperarElemento.seaClickeable(TurnosUI.BTN_NUEVO_TURNO),
                Click.on(TurnosUI.BTN_NUEVO_TURNO),
                EsperarElemento.seaVisible(TurnosUI.SELECT_CLIENTE)
        );

        if (turno.tieneCliente()) {
            actor.attemptsTo(SelectFromOptions.byVisibleText(turno.getCliente())
                    .from(TurnosUI.SELECT_CLIENTE));
        }
        if (turno.tieneMascota()) {
            actor.attemptsTo(SelectFromOptions.byVisibleText(turno.getMascota())
                    .from(TurnosUI.SELECT_MASCOTA));
        }
        if (turno.tieneVeterinario()) {
            actor.attemptsTo(SelectFromOptions.byVisibleText(turno.getVeterinario())
                    .from(TurnosUI.SELECT_VETERINARIO));
        }

        actor.attemptsTo(
                // El backend rechaza turnos del mismo veterinario en la misma fecha+hora,
                // por lo que se asigna una hora aleatoria para evitar conflictos entre corridas
                EstablecerValorInput.de(TurnosUI.INPUT_HORA, horaAleatoria()),
                Enter.theValue(turno.getMotivo()).into(TurnosUI.INPUT_MOTIVO),
                Click.on(TurnosUI.BTN_GUARDAR),
                EsperarElemento.seaVisible(
                        TurnosUI.tarjetaTurnoDe(turno.getMascota()), Duration.ofSeconds(20))
        );
    }

    /** Genera una hora valida (08:00 - 18:59) para minimizar colisiones de agenda. */
    private String horaAleatoria() {
        int hora = ThreadLocalRandom.current().nextInt(8, 19);
        int minuto = ThreadLocalRandom.current().nextInt(0, 60);
        return String.format("%02d:%02d", hora, minuto);
    }
}
