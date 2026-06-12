package co.com.vetclinica.userinterfaces;

import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.By;

/**
 * Targets del layout autenticado: menu lateral de navegacion y boton de salida.
 * El boton "Cerrar sesion" solo existe cuando hay una sesion activa, por lo que
 * sirve como indicador fiable de autenticacion.
 */
public final class MenuPrincipal {

    public static final Target BTN_CERRAR_SESION = Target.the("boton Cerrar sesion")
            .located(By.xpath("//button[contains(normalize-space(.),'Cerrar sesión')]"));

    /** Enlace del menu lateral por su texto visible (Clientes, Mascotas, etc.). */
    public static Target enlaceMenu(String nombre) {
        return Target.the("enlace de menu " + nombre)
                .located(By.xpath("//a[normalize-space(.)='" + nombre + "']"));
    }

    private MenuPrincipal() {
    }
}
