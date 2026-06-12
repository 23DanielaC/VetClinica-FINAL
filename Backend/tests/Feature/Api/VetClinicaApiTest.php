<?php

namespace Tests\Feature\Api;

use App\Models\Consulta;
use App\Models\EstadoTurno;
use App\Models\Factura;
use App\Models\Mascota;
use App\Models\Turno;
use App\Models\VacunaAplicada;
use Illuminate\Foundation\Testing\DatabaseTransactions;
use Tests\TestCase;

class VetClinicaApiTest extends TestCase
{
    use DatabaseTransactions;

    public function test_lista_clientes(): void
    {
        $response = $this->getJson('/api/v1/clientes');

        $response
            ->assertOk()
            ->assertJsonStructure([
                'data' => [
                    '*' => ['id_cliente', 'nombre', 'telefono', 'direccion'],
                ],
            ]);
    }

    public function test_crea_cliente_y_mascota(): void
    {
        $clienteResponse = $this->postJson('/api/v1/clientes', [
            'nombre' => 'Cliente API Test',
            'telefono' => '3001234567',
            'direccion' => 'Calle 1',
        ]);

        $clienteResponse->assertCreated();
        $clienteId = $clienteResponse->json('data.id_cliente');

        $mascotaResponse = $this->postJson('/api/v1/mascotas', [
            'nombre' => 'Firulais Test',
            'especie' => 'Perro',
            'raza' => 'Mestizo',
            'fecha_nacimiento' => '2022-01-15',
            'id_cliente' => $clienteId,
        ]);

        $mascotaResponse
            ->assertCreated()
            ->assertJsonPath('data.nombre', 'Firulais Test');

        $this->getJson("/api/v1/clientes/{$clienteId}/mascotas")
            ->assertOk()
            ->assertJsonFragment(['nombre' => 'Firulais Test']);
    }

    public function test_agenda_turnos_filtra_por_fecha(): void
    {
        $response = $this->getJson('/api/v1/turnos?fecha=2025-06-01');

        $response
            ->assertOk()
            ->assertJsonStructure([
                'data' => [
                    '*' => ['id_turno', 'fecha', 'hora', 'id_estado_turno', 'estado'],
                ],
            ]);
    }

    public function test_no_permite_doble_reserva_de_turno(): void
    {
        $turno = Turno::query()->firstOrFail();

        $response = $this->postJson('/api/v1/turnos', [
            'fecha' => $turno->fecha->toDateString(),
            'hora' => $turno->hora,
            'id_mascota' => $turno->id_mascota,
            'id_veterinario' => $turno->id_veterinario,
        ]);

        $response->assertUnprocessable()
            ->assertJsonValidationErrors(['hora']);
    }

    public function test_consulta_requiere_turno_atendido(): void
    {
        $turno = Turno::query()->where('id_estado_turno', EstadoTurno::PENDIENTE)->first();

        if (! $turno) {
            $mascota = Mascota::query()->firstOrFail();
            $turno = Turno::query()->create([
                'fecha' => now()->addDay()->toDateString(),
                'hora' => '15:00:00',
                'id_estado_turno' => EstadoTurno::PENDIENTE,
                'id_mascota' => $mascota->id_mascota,
                'id_veterinario' => Turno::query()->value('id_veterinario'),
            ]);
        }

        $this->postJson('/api/v1/consultas', [
            'id_turno' => $turno->id_turno,
            'diagnostico' => 'Prueba',
        ])->assertUnprocessable()
            ->assertJsonPath('message', 'Solo los turnos atendidos pueden generar una consulta.');
    }

    public function test_vacunas_proximas_responde_correctamente(): void
    {
        VacunaAplicada::query()->update([
            'fecha_proxima' => now()->addDays(10)->toDateString(),
        ]);

        $response = $this->getJson('/api/v1/vacunas/proximas?dias=30');

        $response
            ->assertOk()
            ->assertJsonStructure([
                'data' => [
                    '*' => ['id_vacuna_aplicada', 'fecha_proxima', 'id_mascota', 'id_tipo_vacuna'],
                ],
            ]);
    }

    public function test_pago_no_puede_exceder_total_factura(): void
    {
        $factura = Factura::query()->withSum('pagos', 'monto')->firstOrFail();
        $saldo = (float) $factura->total - (float) ($factura->pagos_sum_monto ?? 0);

        $this->postJson('/api/v1/pagos', [
            'fecha' => now()->toDateString(),
            'monto' => $saldo + 1,
            'id_tipo_pago' => 2,
            'id_metodo_pago' => 1,
            'id_factura' => $factura->id_factura,
        ])->assertUnprocessable()
            ->assertJsonPath('message', 'El monto excede el saldo pendiente de la factura.');
    }

    public function test_prescribe_medicamentos_en_consulta(): void
    {
        $consulta = Consulta::query()->firstOrFail();

        $response = $this->postJson("/api/v1/consultas/{$consulta->id_consulta}/medicamentos", [
            'medicamentos' => [
                ['id_medicamento' => 1, 'cantidad' => 2],
            ],
        ]);

        $response
            ->assertOk()
            ->assertJsonStructure([
                'data' => ['id_consulta', 'medicamentos'],
            ]);
    }
}
