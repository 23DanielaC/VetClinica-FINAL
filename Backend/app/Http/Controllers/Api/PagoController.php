<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Http\Requests\Api\StorePagoRequest;
use App\Http\Resources\PagoResource;
use App\Models\Factura;
use App\Models\Pago;
use Illuminate\Http\JsonResponse;
use Symfony\Component\HttpFoundation\Response;

class PagoController extends Controller
{
    public function store(StorePagoRequest $request): JsonResponse
    {
        $factura = Factura::query()
            ->withSum('pagos', 'monto')
            ->findOrFail($request->integer('id_factura'));

        $totalPagado = (float) ($factura->pagos_sum_monto ?? 0);
        $nuevoMonto = (float) $request->input('monto');

        if (($totalPagado + $nuevoMonto) > (float) $factura->total) {
            return response()->json([
                'message' => 'El monto excede el saldo pendiente de la factura.',
                'saldo_pendiente' => (float) $factura->total - $totalPagado,
            ], Response::HTTP_UNPROCESSABLE_ENTITY);
        }

        $pago = Pago::query()->create($request->validated());
        $pago->load(['factura.cliente', 'tipoPago', 'metodoPago']);

        return (new PagoResource($pago))
            ->response()
            ->setStatusCode(201);
    }
}
