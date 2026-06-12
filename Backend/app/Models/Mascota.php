<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Relations\BelongsTo;
use Illuminate\Database\Eloquent\Relations\HasMany;

class Mascota extends VetClinicaModel
{
    protected $table = 'mascota';

    protected $primaryKey = 'id_mascota';

    protected $fillable = [
        'nombre',
        'especie',
        'raza',
        'fecha_nacimiento',
        'id_cliente',
    ];

    protected function casts(): array
    {
        return [
            'fecha_nacimiento' => 'date',
        ];
    }

    public function cliente(): BelongsTo
    {
        return $this->belongsTo(Cliente::class, 'id_cliente', 'id_cliente');
    }

    public function turnos(): HasMany
    {
        return $this->hasMany(Turno::class, 'id_mascota', 'id_mascota');
    }

    public function vacunasAplicadas(): HasMany
    {
        return $this->hasMany(VacunaAplicada::class, 'id_mascota', 'id_mascota');
    }
}
