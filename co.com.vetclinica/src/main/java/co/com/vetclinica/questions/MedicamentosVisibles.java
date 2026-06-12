package co.com.vetclinica.questions;

import co.com.vetclinica.userinterfaces.MedicamentosUI;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;

/**
 * Verifica que el catalogo/inventario de medicamentos este visible.
 */
public class MedicamentosVisibles implements Question<Boolean> {

    public static MedicamentosVisibles enElInventario() {
        return new MedicamentosVisibles();
    }

    @Override
    public Boolean answeredBy(Actor actor) {
        try {
            return MedicamentosUI.TITULO_INVENTARIO.resolveFor(actor).isVisible()
                    && MedicamentosUI.CONTADOR.resolveFor(actor).isVisible();
        } catch (RuntimeException e) {
            return false;
        }
    }
}
