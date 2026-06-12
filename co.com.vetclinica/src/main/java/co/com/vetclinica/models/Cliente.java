package co.com.vetclinica.models;

import java.util.Objects;

/**
 * Modelo de cliente mapeado desde la DataTable Gherkin.
 * Solo el nombre es obligatorio (regla de negocio del formulario de creacion).
 */
public class Cliente {

    private final String nombre;
    private final String telefono;
    private final String email;
    private final String direccion;

    public Cliente(String nombre, String telefono, String email, String direccion) {
        this.nombre = Objects.requireNonNull(nombre, "El nombre del cliente es obligatorio").trim();
        // Campos opcionales: se normalizan a cadena vacia si no vienen en la tabla
        this.telefono = telefono == null ? "" : telefono.trim();
        this.email = email == null ? "" : email.trim();
        this.direccion = direccion == null ? "" : direccion.trim();
    }

    public String getNombre() {
        return nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getEmail() {
        return email;
    }

    public String getDireccion() {
        return direccion;
    }
}
