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
    Route::get('clientes', [ClienteController::class, 'index']);
    Route::post('clientes', [ClienteController::class, 'store']);
    Route::get('clientes/{cliente}/mascotas', [ClienteController::class, 'mascotas']);

    Route::post('mascotas', [MascotaController::class, 'store']);

    Route::get('turnos', [TurnoController::class, 'index']);
    Route::post('turnos', [TurnoController::class, 'store']);
    Route::patch('turnos/{turno}/estado', [TurnoController::class, 'updateEstado']);

    Route::post('consultas', [ConsultaController::class, 'store']);
    Route::post('consultas/{consulta}/medicamentos', [ConsultaController::class, 'storeMedicamentos']);
});
