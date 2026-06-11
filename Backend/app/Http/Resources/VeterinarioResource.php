<?php

namespace App\Http\Resources;

use Illuminate\Http\Request;
use Illuminate\Http\Resources\Json\JsonResource;

class VeterinarioResource extends JsonResource
{
    /**
     * @return array<string, mixed>
     */
    public function toArray(Request $request): array
    {
        return [
            'id_veterinario' => $this->id_veterinario,
            'nombre' => $this->nombre,
            'especialidad' => $this->especialidad,
            'telefono' => $this->telefono,
            'correo' => $this->correo,
        ];
    }
}
