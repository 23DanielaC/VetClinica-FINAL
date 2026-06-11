package co.com.vetclinica.userinterfaces;

import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.By;

/**
 * Targets de la pantalla de inicio de sesion.
 * Los inputs no exponen id/name, por lo que se localizan por tipo y placeholder.
 */
public final class LoginUI {

    public static final Target INPUT_CORREO = Target.the("campo correo electronico")
            .located(By.xpath("//input[@type='email' or @placeholder='correo@ejemplo.com']"));

    public static final Target INPUT_CLAVE = Target.the("campo contrasena")
            .located(By.xpath("//input[@placeholder='Tu contraseña']"));

    public static final Target BTN_INICIAR_SESION = Target.the("boton Iniciar Sesion")
            .located(By.xpath("//form//button[@type='submit']"));

    private LoginUI() {
    }
}
