package co.com.vetclinica.userinterfaces;

import net.serenitybdd.annotations.DefaultUrl;
import net.serenitybdd.core.pages.PageObject;

/**
 * Pagina de entrada de la SPA VetClinica. La URL usa hash routing (#/login).
 * El valor por defecto se sobreescribe con webdriver.base.url de serenity.conf.
 */
@DefaultUrl("http://localhost:5173/#/login")
public class Inicio extends PageObject {
}
