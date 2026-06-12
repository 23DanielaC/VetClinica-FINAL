#!/usr/bin/env bash
set -euo pipefail

ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT"

API="$ROOT/Backend/routes/api.php"

write_api() {
  cat > "$API" <<'PHP'
<?php

use App\Http\Controllers\Api\CatalogoController;
use App\Http\Controllers\Api\ClienteController;
use App\Http\Controllers\Api\ConsultaController;
use App\Http\Controllers\Api\FacturaController;
use App\Http\Controllers\Api\MascotaController;
use App\Http\Controllers\Api\PagoController;
use App\Http\Controllers\Api\TurnoController;
use App\Http\Controllers\Api\VacunaController;
use Illuminate\Support\Facades\Route;

Route::prefix('v1')->group(function (): void {
PHP
  while IFS= read -r line; do
    printf '%s\n' "$line" >> "$API"
  done
  cat >> "$API" <<'PHP'
});
PHP
}

commit() {
  git status --short
  git commit -m "$1"
}

# ── Commit 1: base Laravel + cliente Axios ────────────────────────────────
write_api <<'ROUTES'
ROUTES

git add Backend/
git reset Backend/app/Http/Controllers/Api/ 2>/dev/null || true
git reset Backend/app/Http/Requests/Api/ 2>/dev/null || true
git reset Backend/app/Http/Resources/ClienteResource.php 2>/dev/null || true
git reset Backend/app/Http/Resources/ConsultaResource.php 2>/dev/null || true
git reset Backend/app/Http/Resources/FacturaDetalleResource.php 2>/dev/null || true
git reset Backend/app/Http/Resources/FacturaResource.php 2>/dev/null || true
git reset Backend/app/Http/Resources/MascotaResource.php 2>/dev/null || true
git reset Backend/app/Http/Resources/MedicamentoResource.php 2>/dev/null || true
git reset Backend/app/Http/Resources/PagoResource.php 2>/dev/null || true
git reset Backend/app/Http/Resources/TurnoResource.php 2>/dev/null || true
git reset Backend/app/Http/Resources/VacunaAplicadaResource.php 2>/dev/null || true
git reset Backend/app/Http/Resources/VeterinarioResource.php 2>/dev/null || true
git reset Backend/app/Models/Cliente.php Backend/app/Models/Consulta.php Backend/app/Models/EstadoTurno.php 2>/dev/null || true
git reset Backend/app/Models/Factura.php Backend/app/Models/FacturaDetalle.php Backend/app/Models/Mascota.php 2>/dev/null || true
git reset Backend/app/Models/Medicamento.php Backend/app/Models/MetodoPago.php Backend/app/Models/Pago.php 2>/dev/null || true
git reset Backend/app/Models/TipoPago.php Backend/app/Models/TipoVacuna.php Backend/app/Models/Turno.php 2>/dev/null || true
git reset Backend/app/Models/VacunaAplicada.php Backend/app/Models/Veterinario.php 2>/dev/null || true
git reset Backend/database/sql-schemas/ Backend/tests/Feature/Api/ 2>/dev/null || true
git add vetnova/.env.example vetnova/.gitignore vetnova/src/services/api.js scripts/crear-commits-backend.sh
commit "Configurar backend Laravel y cliente HTTP Axios para la API"

# ── Commit 2: clientes ────────────────────────────────────────────────────
write_api <<'ROUTES'
    Route::get('clientes', [ClienteController::class, 'index']);
    Route::post('clientes', [ClienteController::class, 'store']);
    Route::get('clientes/{cliente}/mascotas', [ClienteController::class, 'mascotas']);
ROUTES
git add Backend/app/Models/Cliente.php
git add Backend/app/Http/Controllers/Api/ClienteController.php
git add Backend/app/Http/Requests/Api/StoreClienteRequest.php
git add Backend/app/Http/Resources/ClienteResource.php
git add Backend/routes/api.php
git add vetnova/src/services/clienteService.js vetnova/src/stores/clienteStore.js
commit "Implementar API REST de clientes con servicio Axios y store Pinia"

# ── Commit 3: mascotas ────────────────────────────────────────────────────
write_api <<'ROUTES'
    Route::get('clientes', [ClienteController::class, 'index']);
    Route::post('clientes', [ClienteController::class, 'store']);
    Route::get('clientes/{cliente}/mascotas', [ClienteController::class, 'mascotas']);

    Route::post('mascotas', [MascotaController::class, 'store']);
ROUTES
git add Backend/app/Models/Mascota.php
git add Backend/app/Http/Controllers/Api/MascotaController.php
git add Backend/app/Http/Requests/Api/StoreMascotaRequest.php
git add Backend/app/Http/Resources/MascotaResource.php
git add Backend/routes/api.php
git add vetnova/src/services/mascotaService.js vetnova/src/stores/mascotaStore.js
commit "Desarrollar endpoint de mascotas con capa de servicios y store Pinia"

# ── Commit 4: turnos ──────────────────────────────────────────────────────
write_api <<'ROUTES'
    Route::get('clientes', [ClienteController::class, 'index']);
    Route::post('clientes', [ClienteController::class, 'store']);
    Route::get('clientes/{cliente}/mascotas', [ClienteController::class, 'mascotas']);

    Route::post('mascotas', [MascotaController::class, 'store']);

    Route::get('turnos', [TurnoController::class, 'index']);
    Route::post('turnos', [TurnoController::class, 'store']);
    Route::patch('turnos/{turno}/estado', [TurnoController::class, 'updateEstado']);
ROUTES
git add Backend/app/Models/Turno.php Backend/app/Models/EstadoTurno.php Backend/app/Models/Veterinario.php
git add Backend/app/Http/Controllers/Api/TurnoController.php
git add Backend/app/Http/Requests/Api/StoreTurnoRequest.php Backend/app/Http/Requests/Api/UpdateTurnoEstadoRequest.php
git add Backend/app/Http/Resources/TurnoResource.php Backend/app/Http/Resources/VeterinarioResource.php
git add Backend/routes/api.php
git add vetnova/src/services/turnoService.js vetnova/src/stores/turnoStore.js
commit "Implementar gestión de turnos en API Laravel y frontend Vue"

# ── Commit 5: consultas ───────────────────────────────────────────────────
write_api <<'ROUTES'
    Route::get('clientes', [ClienteController::class, 'index']);
    Route::post('clientes', [ClienteController::class, 'store']);
    Route::get('clientes/{cliente}/mascotas', [ClienteController::class, 'mascotas']);

    Route::post('mascotas', [MascotaController::class, 'store']);

    Route::get('turnos', [TurnoController::class, 'index']);
    Route::post('turnos', [TurnoController::class, 'store']);
    Route::patch('turnos/{turno}/estado', [TurnoController::class, 'updateEstado']);

    Route::post('consultas', [ConsultaController::class, 'store']);
    Route::post('consultas/{consulta}/medicamentos', [ConsultaController::class, 'storeMedicamentos']);
ROUTES
git add Backend/app/Models/Consulta.php Backend/app/Models/Medicamento.php
git add Backend/app/Http/Controllers/Api/ConsultaController.php
git add Backend/app/Http/Requests/Api/StoreConsultaRequest.php Backend/app/Http/Requests/Api/StoreConsultaMedicamentoRequest.php
git add Backend/app/Http/Resources/ConsultaResource.php Backend/app/Http/Resources/MedicamentoResource.php
git add Backend/routes/api.php
git add vetnova/src/services/consultaService.js vetnova/src/stores/consultaStore.js
commit "Desarrollar registro de consultas y medicamentos vía API y Pinia"

# ── Commit 6: vacunas ─────────────────────────────────────────────────────
write_api <<'ROUTES'
    Route::get('clientes', [ClienteController::class, 'index']);
    Route::post('clientes', [ClienteController::class, 'store']);
    Route::get('clientes/{cliente}/mascotas', [ClienteController::class, 'mascotas']);

    Route::post('mascotas', [MascotaController::class, 'store']);

    Route::get('turnos', [TurnoController::class, 'index']);
    Route::post('turnos', [TurnoController::class, 'store']);
    Route::patch('turnos/{turno}/estado', [TurnoController::class, 'updateEstado']);

    Route::post('consultas', [ConsultaController::class, 'store']);
    Route::post('consultas/{consulta}/medicamentos', [ConsultaController::class, 'storeMedicamentos']);

    Route::get('vacunas/proximas', [VacunaController::class, 'proximas']);
    Route::post('vacunas', [VacunaController::class, 'store']);
ROUTES
git add Backend/app/Models/VacunaAplicada.php Backend/app/Models/TipoVacuna.php
git add Backend/app/Http/Controllers/Api/VacunaController.php
git add Backend/app/Http/Requests/Api/StoreVacunaRequest.php
git add Backend/app/Http/Resources/VacunaAplicadaResource.php
git add Backend/routes/api.php
git add vetnova/src/services/vacunaService.js vetnova/src/stores/vacunaStore.js
commit "Implementar control de vacunas con endpoints REST y store dedicado"

# ── Commit 7: facturas y pagos ────────────────────────────────────────────
write_api <<'ROUTES'
    Route::get('clientes', [ClienteController::class, 'index']);
    Route::post('clientes', [ClienteController::class, 'store']);
    Route::get('clientes/{cliente}/mascotas', [ClienteController::class, 'mascotas']);

    Route::post('mascotas', [MascotaController::class, 'store']);

    Route::get('turnos', [TurnoController::class, 'index']);
    Route::post('turnos', [TurnoController::class, 'store']);
    Route::patch('turnos/{turno}/estado', [TurnoController::class, 'updateEstado']);

    Route::post('consultas', [ConsultaController::class, 'store']);
    Route::post('consultas/{consulta}/medicamentos', [ConsultaController::class, 'storeMedicamentos']);

    Route::get('vacunas/proximas', [VacunaController::class, 'proximas']);
    Route::post('vacunas', [VacunaController::class, 'store']);

    Route::post('facturas', [FacturaController::class, 'store']);
    Route::post('pagos', [PagoController::class, 'store']);
ROUTES
git add Backend/app/Models/Factura.php Backend/app/Models/FacturaDetalle.php
git add Backend/app/Models/Pago.php Backend/app/Models/MetodoPago.php Backend/app/Models/TipoPago.php
git add Backend/app/Http/Controllers/Api/FacturaController.php Backend/app/Http/Controllers/Api/PagoController.php
git add Backend/app/Http/Requests/Api/StoreFacturaRequest.php Backend/app/Http/Requests/Api/StorePagoRequest.php
git add Backend/app/Http/Resources/FacturaResource.php Backend/app/Http/Resources/FacturaDetalleResource.php Backend/app/Http/Resources/PagoResource.php
git add Backend/routes/api.php
git add vetnova/src/services/facturaService.js vetnova/src/stores/facturaStore.js
git add vetnova/src/services/pagoService.js vetnova/src/stores/pagoStore.js
commit "Desarrollar facturación y pagos con servicios Axios y stores Pinia"

# ── Commit 8: catálogos, SQL y pruebas ────────────────────────────────────
write_api <<'ROUTES'
    Route::get('clientes', [ClienteController::class, 'index']);
    Route::post('clientes', [ClienteController::class, 'store']);
    Route::get('clientes/{cliente}/mascotas', [ClienteController::class, 'mascotas']);

    Route::post('mascotas', [MascotaController::class, 'store']);

    Route::get('turnos', [TurnoController::class, 'index']);
    Route::post('turnos', [TurnoController::class, 'store']);
    Route::patch('turnos/{turno}/estado', [TurnoController::class, 'updateEstado']);

    Route::post('consultas', [ConsultaController::class, 'store']);
    Route::post('consultas/{consulta}/medicamentos', [ConsultaController::class, 'storeMedicamentos']);

    Route::get('vacunas/proximas', [VacunaController::class, 'proximas']);
    Route::post('vacunas', [VacunaController::class, 'store']);

    Route::post('facturas', [FacturaController::class, 'store']);
    Route::post('pagos', [PagoController::class, 'store']);

    Route::get('catalogos/medicamentos', [CatalogoController::class, 'medicamentos']);
    Route::get('catalogos/veterinarios', [CatalogoController::class, 'veterinarios']);
    Route::get('catalogos/tipos-vacuna', [CatalogoController::class, 'tiposVacuna']);
    Route::get('catalogos/estados-turno', [CatalogoController::class, 'estadosTurno']);
    Route::get('catalogos/metodos-pago', [CatalogoController::class, 'metodosPago']);
    Route::get('catalogos/tipos-pago', [CatalogoController::class, 'tiposPago']);
ROUTES
git add Backend/app/Http/Controllers/Api/CatalogoController.php
git add Backend/database/sql-schemas/
git add Backend/tests/Feature/Api/
git add Backend/routes/api.php
git add vetnova/src/services/catalogoService.js vetnova/src/stores/catalogoStore.js
commit "Agregar catálogos, esquema SQL y pruebas de integración de la API"

echo ""
echo "✓ 8 commits creados:"
git log --oneline -8
