<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Http\Resources\MedicamentoResource;
use App\Http\Resources\VeterinarioResource;
use App\Models\EstadoTurno;
use App\Models\Medicamento;
use App\Models\MetodoPago;
use App\Models\TipoPago;
use App\Models\TipoVacuna;
use App\Models\Veterinario;
use Illuminate\Http\JsonResponse;

class CatalogoController extends Controller
{
    public function medicamentos(): JsonResponse
    {
        return response()->json([
            'data' => MedicamentoResource::collection(
                Medicamento::query()->orderBy('nombre')->get(),
            ),
        ]);
    }

    public function veterinarios(): JsonResponse
    {
        return response()->json([
            'data' => VeterinarioResource::collection(
                Veterinario::query()->orderBy('nombre')->get(),
            ),
        ]);
    }

    public function tiposVacuna(): JsonResponse
    {
        return response()->json([
            'data' => TipoVacuna::query()->orderBy('nombre')->get(),
        ]);
    }

    public function estadosTurno(): JsonResponse
    {
        return response()->json([
            'data' => EstadoTurno::query()->orderBy('id_estado_turno')->get(),
        ]);
    }

    public function metodosPago(): JsonResponse
    {
        return response()->json([
            'data' => MetodoPago::query()->orderBy('nombre')->get(),
        ]);
    }

    public function tiposPago(): JsonResponse
    {
        return response()->json([
            'data' => TipoPago::query()->orderBy('nombre')->get(),
        ]);
    }
}
