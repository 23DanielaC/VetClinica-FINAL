package co.com.vetclinica.userinterfaces;

import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.By;

/**
 * Targets del modulo de Medicamentos (catalogo/inventario de solo lectura).
 */
public final class MedicamentosUI {

    public static final Target SUBTITULO_INVENTARIO = Target.the("subtitulo Inventario de medicamentos")
            .located(By.xpath("//*[normalize-space(.)='Inventario de medicamentos']"));

    public static final Target TITULO_INVENTARIO = Target.the("titulo Inventario de Medicamentos")
            .located(By.xpath("//h2[normalize-space(.)='Inventario de Medicamentos']"));

    public static final Target CONTADOR = Target.the("contador de medicamentos registrados")
            .located(By.xpath("//*[contains(normalize-space(.),'medicamentos registrados')]"));

    private MedicamentosUI() {
    }
}
