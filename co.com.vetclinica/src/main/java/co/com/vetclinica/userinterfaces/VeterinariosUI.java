package co.com.vetclinica.userinterfaces;

import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.By;

/**
 * Targets del modulo de Veterinarios (catalogo de solo lectura).
 */
public final class VeterinariosUI {

    public static final Target TITULO_DIRECTORIO = Target.the("subtitulo del directorio")
            .located(By.xpath("//*[normalize-space(.)='Directorio del equipo médico veterinario']"));

    public static final Target CONTADOR = Target.the("contador de veterinarios")
            .located(By.xpath("//*[contains(normalize-space(.),' veterinarios')]"));

    private VeterinariosUI() {
    }
}
