<?php

namespace App\Http\Resources;

use Illuminate\Http\Request;
use Illuminate\Http\Resources\Json\JsonResource;

class VacunaAplicadaResource extends JsonResource
{
    /**
     * @return array<string, mixed>
     */
    public function toArray(Request $request): array
    {
        return [
            'id_vacuna_aplicada' => $this->id_vacuna_aplicada,
            'fecha_aplicacion' => $this->fecha_aplicacion?->toDateString(),
            'fecha_proxima' => $this->fecha_proxima?->toDateString(),
            'id_mascota' => $this->id_mascota,
            'id_tipo_vacuna' => $this->id_tipo_vacuna,
            'mascota' => new MascotaResource($this->whenLoaded('mascota')),
            'tipo_vacuna' => $this->whenLoaded('tipoVacuna', fn () => [
                'id_tipo_vacuna' => $this->tipoVacuna->id_tipo_vacuna,
                'nombre' => $this->tipoVacuna->nombre,
            ]),
        ];
    }
}
