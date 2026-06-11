<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Http\Requests\Api\StoreTurnoRequest;
use App\Http\Requests\Api\UpdateTurnoEstadoRequest;
use App\Http\Resources\TurnoResource;
use App\Models\EstadoTurno;
use App\Models\Turno;
use Illuminate\Http\JsonResponse;
use Illuminate\Http\Request;
use Illuminate\Http\Resources\Json\AnonymousResourceCollection;

class TurnoController extends Controller
{
    public function index(Request $request): AnonymousResourceCollection
    {
        $turnos = Turno::query()
            ->agenda(
                $request->query('fecha'),
                $request->integer('veterinario') ?: null,
            )
            ->when(
                $request->filled('estado'),
                fn ($query) => $query->whereHas(
                    'estadoTurno',
                    fn ($q) => $q->where('nombre', $request->query('estado')),
                ),
            )
            ->when(
                $request->filled('id_estado_turno'),
                fn ($query) => $query->where('id_estado_turno', $request->integer('id_estado_turno')),
            )
            ->get();

        return TurnoResource::collection($turnos);
    }

    public function store(StoreTurnoRequest $request): JsonResponse
    {
        $turno = Turno::query()->create([
            ...$request->validated(),
            'id_estado_turno' => $request->input('id_estado_turno', EstadoTurno::PENDIENTE),
        ]);

        $turno->load(['mascota.cliente', 'veterinario', 'estadoTurno']);

        return (new TurnoResource($turno))
            ->response()
            ->setStatusCode(201);
    }

    public function updateEstado(UpdateTurnoEstadoRequest $request, Turno $turno): TurnoResource
    {
        $idEstado = $request->input('id_estado_turno');

        if (! $idEstado && $request->filled('estado')) {
            $idEstado = EstadoTurno::query()
                ->where('nombre', $request->input('estado'))
                ->value('id_estado_turno');
        }

        $turno->update(['id_estado_turno' => $idEstado]);
        $turno->load(['mascota.cliente', 'veterinario', 'estadoTurno', 'consulta']);

        return new TurnoResource($turno);
    }
}
