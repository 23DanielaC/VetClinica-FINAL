<?php

namespace App\Http\Resources;

use Illuminate\Http\Request;
use Illuminate\Http\Resources\Json\JsonResource;

class FacturaDetalleResource extends JsonResource
{
    /**
     * @return array<string, mixed>
     */
    public function toArray(Request $request): array
    {
        return [
            'id_factura_detalle' => $this->id_factura_detalle,
            'id_factura' => $this->id_factura,
            'id_medicamento' => $this->id_medicamento,
            'descripcion' => $this->descripcion,
            'cantidad' => $this->cantidad,
            'precio_unitario' => $this->precio_unitario,
            'subtotal' => $this->subtotal,
            'medicamento' => new MedicamentoResource($this->whenLoaded('medicamento')),
        ];
    }
}
