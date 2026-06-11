package co.com.vetclinica.models;

import java.util.Objects;

/**
 * Modelo de credenciales de inicio de sesion mapeado desde la DataTable Gherkin.
 */
public class Credenciales {

    private final String correo;
    private final String clave;

    public Credenciales(String correo, String clave) {
        // Validacion de inputs: las credenciales son obligatorias para autenticar
        this.correo = Objects.requireNonNull(correo, "El correo es obligatorio").trim();
        this.clave = Objects.requireNonNull(clave, "La clave es obligatoria");
    }

    public String getCorreo() {
        return correo;
    }

    public String getClave() {
        return clave;
    }
}
