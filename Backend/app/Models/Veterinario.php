<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Relations\HasMany;

class Veterinario extends VetClinicaModel
{
    protected $table = 'veterinario';

    protected $primaryKey = 'id_veterinario';

    protected $fillable = [
        'nombre',
        'especialidad',
        'telefono',
        'correo',
    ];

    public function turnos(): HasMany
    {
        return $this->hasMany(Turno::class, 'id_veterinario', 'id_veterinario');
    }
}
