package co.com.vetclinica.userinterfaces;

import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.By;

/**
 * Targets del modulo de Mascotas (listado de tarjetas + modal de creacion +
 * modal de historial medico).
 */
public final class MascotasUI {

    public static final Target BTN_NUEVA_MASCOTA = Target.the("boton Nueva Mascota")
            .located(By.xpath("//button[normalize-space(.)='Nueva Mascota']"));

    public static final Target INPUT_NOMBRE = Target.the("campo Nombre de la mascota")
            .located(By.xpath("//div[label[normalize-space(.)='Nombre']]/input"));

    public static final Target SELECT_ESPECIE = Target.the("selector Especie")
            .located(By.xpath("//div[label[normalize-space(.)='Especie']]/select"));

    public static final Target INPUT_RAZA = Target.the("campo Raza")
            .located(By.xpath("//div[label[normalize-space(.)='Raza']]/input"));

    public static final Target SELECT_PROPIETARIO = Target.the("selector Propietario")
            .located(By.xpath("//div[label[normalize-space(.)='Propietario']]/select"));

    public static final Target BTN_GUARDAR = Target.the("boton Guardar del modal")
            .located(By.xpath("//button[normalize-space(.)='Guardar']"));

    /** Tarjeta de la mascota identificada por su nombre (encabezado h3). */
    public static Target tarjetaPorNombre(String nombre) {
        return Target.the("tarjeta de la mascota " + nombre)
                .located(By.xpath("//h3[normalize-space(.)='" + nombre + "']"));
    }

    /** Boton "Historial médico" de la tarjeta de una mascota. */
    public static Target botonHistorialDe(String nombre) {
        return Target.the("boton Historial medico de " + nombre)
                .located(By.xpath(
                        "//*[.//h3[normalize-space(.)='" + nombre + "']]"
                                + "//button[@title='Historial médico']"));
    }

    /** Encabezado del modal de historial ("Historial — <nombre>"). */
    public static Target tituloHistorialDe(String nombre) {
        return Target.the("titulo del historial de " + nombre)
                .located(By.xpath("//h3[contains(normalize-space(.),'Historial') "
                        + "and contains(normalize-space(.),'" + nombre + "')]"));
    }

    private MascotasUI() {
    }
}
