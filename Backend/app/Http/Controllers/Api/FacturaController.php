<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Http\Requests\Api\StoreFacturaRequest;
use App\Http\Resources\FacturaResource;
use App\Models\Factura;
use Illuminate\Http\JsonResponse;
use Illuminate\Support\Facades\DB;

class FacturaController extends Controller
{
    public function store(StoreFacturaRequest $request): JsonResponse
    {
        $factura = DB::transaction(function () use ($request) {
            $factura = Factura::query()->create($request->safe()->only([
                'fecha',
                'total',
                'id_cliente',
                'id_consulta',
            ]));

            if ($request->filled('detalles')) {
                $factura->detalles()->createMany($request->validated('detalles'));
            }

            return $factura;
        });

        $factura->load(['cliente', 'consulta', 'detalles.medicamento', 'pagos']);

        return (new FacturaResource($factura))
            ->response()
            ->setStatusCode(201);
    }
}
