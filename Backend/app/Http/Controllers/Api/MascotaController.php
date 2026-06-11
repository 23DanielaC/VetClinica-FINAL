<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Http\Requests\Api\StoreMascotaRequest;
use App\Http\Resources\MascotaResource;
use App\Models\Mascota;
use Illuminate\Http\JsonResponse;

class MascotaController extends Controller
{
    public function store(StoreMascotaRequest $request): JsonResponse
    {
        $mascota = Mascota::query()->create($request->validated());
        $mascota->load('cliente');

        return (new MascotaResource($mascota))
            ->response()
            ->setStatusCode(201);
    }
}
