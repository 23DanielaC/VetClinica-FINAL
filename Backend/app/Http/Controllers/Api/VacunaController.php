<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Http\Requests\Api\StoreVacunaRequest;
use App\Http\Resources\VacunaAplicadaResource;
use App\Models\VacunaAplicada;
use Illuminate\Http\JsonResponse;
use Illuminate\Http\Request;
use Illuminate\Http\Resources\Json\AnonymousResourceCollection;

class VacunaController extends Controller
{
    public function proximas(Request $request): AnonymousResourceCollection
    {
        $dias = $request->integer('dias', 30) ?: 30;

        return VacunaAplicadaResource::collection(
            VacunaAplicada::query()->proximasAVencer($dias)->get(),
        );
    }

    public function store(StoreVacunaRequest $request): JsonResponse
    {
        $vacuna = VacunaAplicada::query()->create($request->validated());
        $vacuna->load(['mascota.cliente', 'tipoVacuna']);

        return (new VacunaAplicadaResource($vacuna))
            ->response()
            ->setStatusCode(201);
    }
}
