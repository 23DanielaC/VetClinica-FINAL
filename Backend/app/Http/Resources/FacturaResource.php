<?php

namespace App\Http\Resources;

use Illuminate\Http\Request;
use Illuminate\Http\Resources\Json\JsonResource;

class FacturaResource extends JsonResource
{
    /**
     * @return array<string, mixed>
     */
    public function toArray(Request $request): array
    {
        return [
            'id_factura' => $this->id_factura,
            'fecha' => $this->fecha?->toDateString(),
            'total' => $this->total,
            'id_cliente' => $this->id_cliente,
            'id_consulta' => $this->id_consulta,
            'cliente' => new ClienteResource($this->whenLoaded('cliente')),
            'consulta' => new ConsultaResource($this->whenLoaded('consulta')),
            'detalles' => FacturaDetalleResource::collection($this->whenLoaded('detalles')),
            'pagos' => PagoResource::collection($this->whenLoaded('pagos')),
            'total_pagado' => $this->when($this->relationLoaded('pagos'), fn () => $this->totalPagado()),
            'saldo_pendiente' => $this->when($this->relationLoaded('pagos'), fn () => $this->saldoPendiente()),
        ];
    }
}
