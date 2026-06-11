<?php

namespace App\Http\Resources;

use Illuminate\Http\Request;
use Illuminate\Http\Resources\Json\JsonResource;

class MascotaResource extends JsonResource
{
    /**
     * @return array<string, mixed>
     */
    public function toArray(Request $request): array
    {
        return [
            'id_mascota' => $this->id_mascota,
            'nombre' => $this->nombre,
            'especie' => $this->especie,
            'raza' => $this->raza,
            'fecha_nacimiento' => $this->fecha_nacimiento?->toDateString(),
            'id_cliente' => $this->id_cliente,
            'cliente' => new ClienteResource($this->whenLoaded('cliente')),
            'vacunas_aplicadas' => VacunaAplicadaResource::collection($this->whenLoaded('vacunasAplicadas')),
        ];
    }
}
