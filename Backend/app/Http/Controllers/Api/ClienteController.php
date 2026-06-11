<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Http\Requests\Api\StoreClienteRequest;
use App\Http\Resources\ClienteResource;
use App\Http\Resources\MascotaResource;
use App\Models\Cliente;
use Illuminate\Http\JsonResponse;
use Illuminate\Http\Resources\Json\AnonymousResourceCollection;

class ClienteController extends Controller
{
    public function index(): AnonymousResourceCollection
    {
        return ClienteResource::collection(
            Cliente::query()->orderBy('nombre')->get(),
        );
    }

    public function store(StoreClienteRequest $request): JsonResponse
    {
        $cliente = Cliente::query()->create($request->validated());

        return (new ClienteResource($cliente))
            ->response()
            ->setStatusCode(201);
    }

    public function mascotas(Cliente $cliente): AnonymousResourceCollection
    {
        return MascotaResource::collection(
            $cliente->mascotas()->orderBy('nombre')->get(),
        );
    }
}
