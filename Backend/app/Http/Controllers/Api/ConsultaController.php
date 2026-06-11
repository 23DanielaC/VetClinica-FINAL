<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Http\Requests\Api\StoreConsultaMedicamentoRequest;
use App\Http\Requests\Api\StoreConsultaRequest;
use App\Http\Resources\ConsultaResource;
use App\Models\Consulta;
use App\Models\EstadoTurno;
use App\Models\Turno;
use Illuminate\Http\JsonResponse;
use Symfony\Component\HttpFoundation\Response;

class ConsultaController extends Controller
{
    public function store(StoreConsultaRequest $request): JsonResponse
    {
        $turno = Turno::query()
            ->with('consulta')
            ->findOrFail($request->integer('id_turno'));

        if ($turno->id_estado_turno !== EstadoTurno::ATENDIDO) {
            return response()->json([
                'message' => 'Solo los turnos atendidos pueden generar una consulta.',
            ], Response::HTTP_UNPROCESSABLE_ENTITY);
        }

        if ($turno->consulta) {
            return response()->json([
                'message' => 'Este turno ya tiene una consulta registrada.',
            ], Response::HTTP_UNPROCESSABLE_ENTITY);
        }

        $consulta = Consulta::query()->create($request->validated());
        $consulta->load(['turno.mascota.cliente', 'turno.veterinario']);

        return (new ConsultaResource($consulta))
            ->response()
            ->setStatusCode(201);
    }

    public function storeMedicamentos(StoreConsultaMedicamentoRequest $request, Consulta $consulta): ConsultaResource
    {
        $syncData = collect($request->validated('medicamentos'))
            ->mapWithKeys(fn (array $item): array => [
                $item['id_medicamento'] => ['cantidad' => $item['cantidad']],
            ])
            ->all();

        $consulta->medicamentos()->syncWithoutDetaching($syncData);
        $consulta->load(['medicamentos', 'turno.mascota', 'turno.veterinario']);

        return new ConsultaResource($consulta);
    }
}
