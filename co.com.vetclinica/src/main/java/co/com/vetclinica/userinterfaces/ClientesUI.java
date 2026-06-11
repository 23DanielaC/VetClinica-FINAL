package co.com.vetclinica.userinterfaces;

import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.By;

/**
 * Targets del modulo de Clientes (listado + modal CrudModal de creacion).
 * Los inputs del modal no tienen id; se localizan por su label asociado.
 */
public final class ClientesUI {

    public static final Target BTN_NUEVO_CLIENTE = Target.the("boton Nuevo Cliente")
            .located(By.xpath("//button[normalize-space(.)='Nuevo Cliente']"));

    public static final Target INPUT_NOMBRE = Target.the("campo Nombre completo")
            .located(By.xpath("//div[label[normalize-space(.)='Nombre completo']]/input"));

    public static final Target INPUT_TELEFONO = Target.the("campo Telefono")
            .located(By.xpath("//div[label[normalize-space(.)='Teléfono']]/input"));

    public static final Target INPUT_EMAIL = Target.the("campo Email")
            .located(By.xpath("//div[label[normalize-space(.)='Email']]/input"));

    public static final Target INPUT_DIRECCION = Target.the("campo Direccion")
            .located(By.xpath("//div[label[normalize-space(.)='Dirección']]/input"));

    public static final Target BTN_GUARDAR = Target.the("boton Guardar del modal")
            .located(By.xpath("//button[normalize-space(.)='Guardar']"));

    /** Boton Eliminar de la fila de un cliente identificado por su nombre. */
    public static Target botonEliminarDe(String nombre) {
        return Target.the("boton Eliminar del cliente " + nombre)
                .located(By.xpath(
                        "//tr[.//p[normalize-space(.)='" + nombre + "']]//button[@title='Eliminar']"));
    }

    /** Celda del listado que contiene el nombre del cliente. */
    public static Target filaPorNombre(String nombre) {
        return Target.the("fila del cliente " + nombre)
                .located(By.xpath("//p[normalize-space(.)='" + nombre + "']"));
    }

    private ClientesUI() {
    }
}
