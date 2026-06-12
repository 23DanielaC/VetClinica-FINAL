package co.com.vetclinica.models;

import java.util.Objects;

/**
 * Modelo de mascota mapeado desde la DataTable Gherkin.
 * El propietario es opcional: si no se indica, se usa el preseleccionado por la UI.
 */
public class Mascota {

    private final String nombre;
    private final String especie;
    private final String raza;
    private final String propietario;

    public Mascota(String nombre, String especie, String raza, String propietario) {
        this.nombre = Objects.requireNonNull(nombre, "El nombre de la mascota es obligatorio").trim();
        this.especie = especie == null ? "" : especie.trim();
        this.raza = raza == null ? "" : raza.trim();
        this.propietario = propietario == null ? "" : propietario.trim();
    }

    public String getNombre() {
        return nombre;
    }

    public String getEspecie() {
        return especie;
    }

    public String getRaza() {
        return raza;
    }

    public String getPropietario() {
        return propietario;
    }

    public boolean tieneEspecie() {
        return !especie.isEmpty();
    }

    public boolean tienePropietario() {
        return !propietario.isEmpty();
    }
}
