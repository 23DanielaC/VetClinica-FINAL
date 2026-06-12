<?php

namespace App\Http\Resources;

use Illuminate\Http\Request;
use Illuminate\Http\Resources\Json\JsonResource;

class PagoResource extends JsonResource
{
    /**
     * @return array<string, mixed>
     */
    public function toArray(Request $request): array
    {
        return [
            'id_pago' => $this->id_pago,
            'fecha' => $this->fecha?->toDateString(),
            'monto' => $this->monto,
            'id_tipo_pago' => $this->id_tipo_pago,
            'id_metodo_pago' => $this->id_metodo_pago,
            'id_factura' => $this->id_factura,
            'tipo_pago' => $this->whenLoaded('tipoPago', fn () => $this->tipoPago->nombre),
            'metodo_pago' => $this->whenLoaded('metodoPago', fn () => $this->metodoPago->nombre),
            'factura' => new FacturaResource($this->whenLoaded('factura')),
        ];
    }
}
