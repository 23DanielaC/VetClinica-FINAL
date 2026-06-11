package co.com.vetclinica.models;

import java.util.Objects;

/**
 * Modelo de turno mapeado desde la DataTable Gherkin.
 * Cliente, mascota y veterinario son opcionales: el formulario los preselecciona
 * con valores validos; si se indican, se eligen explicitamente en los combos.
 */
public class Turno {

    private final String cliente;
    private final String mascota;
    private final String veterinario;
    private final String motivo;

    public Turno(String cliente, String mascota, String veterinario, String motivo) {
        this.cliente = cliente == null ? "" : cliente.trim();
        this.mascota = mascota == null ? "" : mascota.trim();
        this.veterinario = veterinario == null ? "" : veterinario.trim();
        this.motivo = Objects.requireNonNull(motivo, "El motivo del turno es obligatorio").trim();
    }

    public String getCliente() {
        return cliente;
    }

    public String getMascota() {
        return mascota;
    }

    public String getVeterinario() {
        return veterinario;
    }

    public String getMotivo() {
        return motivo;
    }

    public boolean tieneCliente() {
        return !cliente.isEmpty();
    }

    public boolean tieneMascota() {
        return !mascota.isEmpty();
    }

    public boolean tieneVeterinario() {
        return !veterinario.isEmpty();
    }
}
