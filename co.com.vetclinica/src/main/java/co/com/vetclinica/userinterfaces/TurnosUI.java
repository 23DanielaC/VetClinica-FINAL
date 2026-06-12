package co.com.vetclinica.userinterfaces;

import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.By;

/**
 * Targets del modulo de Turnos (listado/agenda + modal de agendamiento).
 */
public final class TurnosUI {

    public static final Target BTN_NUEVO_TURNO = Target.the("boton Nuevo Turno")
            .located(By.xpath("//button[normalize-space(.)='Nuevo Turno']"));

    public static final Target SELECT_CLIENTE = Target.the("selector Cliente")
            .located(By.xpath("//div[label[normalize-space(.)='Cliente']]/select"));

    public static final Target SELECT_MASCOTA = Target.the("selector Mascota")
            .located(By.xpath("//div[label[normalize-space(.)='Mascota']]/select"));

    public static final Target SELECT_VETERINARIO = Target.the("selector Veterinario")
            .located(By.xpath("//div[label[normalize-space(.)='Veterinario']]/select"));

    public static final Target INPUT_HORA = Target.the("campo Hora")
            .located(By.xpath("//div[label[normalize-space(.)='Hora']]/input"));

    public static final Target INPUT_MOTIVO = Target.the("campo Motivo")
            .located(By.xpath("//input[@placeholder='Control anual, vacunación...']"));

    public static final Target BTN_GUARDAR = Target.the("boton Guardar del modal")
            .located(By.xpath("//button[normalize-space(.)='Guardar']"));

    /** Tarjeta de turno identificada por el nombre de la mascota (encabezado h3). */
    public static Target tarjetaTurnoDe(String mascota) {
        return Target.the("tarjeta de turno de " + mascota)
                .located(By.xpath("//h3[normalize-space(.)='" + mascota + "']"));
    }

    private TurnosUI() {
    }
}
