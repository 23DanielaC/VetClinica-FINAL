<?php

namespace App\Http\Resources;

use Illuminate\Http\Request;
use Illuminate\Http\Resources\Json\JsonResource;

class ConsultaResource extends JsonResource
{
    /**
     * @return array<string, mixed>
     */
    public function toArray(Request $request): array
    {
        return [
            'id_consulta' => $this->id_consulta,
            'diagnostico' => $this->diagnostico,
            'tratamiento' => $this->tratamiento,
            'observaciones' => $this->observaciones,
            'id_turno' => $this->id_turno,
            'turno' => new TurnoResource($this->whenLoaded('turno')),
            'medicamentos' => MedicamentoResource::collection($this->whenLoaded('medicamentos')),
            'factura' => new FacturaResource($this->whenLoaded('factura')),
        ];
    }
}
