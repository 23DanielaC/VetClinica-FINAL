<?php

namespace App\Http\Resources;

use Illuminate\Http\Request;
use Illuminate\Http\Resources\Json\JsonResource;

class TurnoResource extends JsonResource
{
    /**
     * @return array<string, mixed>
     */
    public function toArray(Request $request): array
    {
        return [
            'id_turno' => $this->id_turno,
            'fecha' => $this->fecha?->toDateString(),
            'hora' => $this->hora,
            'id_estado_turno' => $this->id_estado_turno,
            'estado' => $this->whenLoaded('estadoTurno', fn () => $this->estadoTurno->nombre),
            'id_mascota' => $this->id_mascota,
            'id_veterinario' => $this->id_veterinario,
            'mascota' => new MascotaResource($this->whenLoaded('mascota')),
            'veterinario' => new VeterinarioResource($this->whenLoaded('veterinario')),
            'consulta' => new ConsultaResource($this->whenLoaded('consulta')),
        ];
    }
}
