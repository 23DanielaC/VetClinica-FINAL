package co.com.vetclinica.utils.hooks;

import co.com.vetclinica.models.Cliente;
import co.com.vetclinica.models.Credenciales;
import co.com.vetclinica.models.Mascota;
import co.com.vetclinica.models.Turno;
import io.cucumber.java.DataTableType;

import java.util.Map;

/**
 * Transformadores que mapean las DataTables de Gherkin a los modelos de dominio.
 * Cucumber los descubre automaticamente porque el paquete utils.hooks esta en el glue.
 */
public class TransformadoresDataTable {

    @DataTableType
    public Credenciales credenciales(Map<String, String> fila) {
        return new Credenciales(fila.get("correo"), fila.get("clave"));
    }

    @DataTableType
    public Cliente cliente(Map<String, String> fila) {
        return new Cliente(
                fila.get("nombre"),
                fila.get("telefono"),
                fila.get("email"),
                fila.get("direccion"));
    }

    @DataTableType
    public Mascota mascota(Map<String, String> fila) {
        return new Mascota(
                fila.get("nombre"),
                fila.get("especie"),
                fila.get("raza"),
                fila.get("propietario"));
    }

    @DataTableType
    public Turno turno(Map<String, String> fila) {
        return new Turno(
                fila.get("cliente"),
                fila.get("mascota"),
                fila.get("veterinario"),
                fila.get("motivo"));
    }
}
