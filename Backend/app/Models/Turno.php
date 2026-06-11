<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Relations\BelongsTo;
use Illuminate\Database\Eloquent\Relations\HasOne;

class Turno extends VetClinicaModel
{
    protected $table = 'turno';

    protected $primaryKey = 'id_turno';

    protected $fillable = [
        'fecha',
        'hora',
        'id_estado_turno',
        'id_mascota',
        'id_veterinario',
    ];

    protected function casts(): array
    {
        return [
            'fecha' => 'date',
        ];
    }

    public function mascota(): BelongsTo
    {
        return $this->belongsTo(Mascota::class, 'id_mascota', 'id_mascota');
    }

    public function veterinario(): BelongsTo
    {
        return $this->belongsTo(Veterinario::class, 'id_veterinario', 'id_veterinario');
    }

    public function estadoTurno(): BelongsTo
    {
        return $this->belongsTo(EstadoTurno::class, 'id_estado_turno', 'id_estado_turno');
    }

    public function consulta(): HasOne
    {
        return $this->hasOne(Consulta::class, 'id_turno', 'id_turno');
    }

    public function scopeAgenda($query, ?string $fecha = null, ?int $veterinario = null)
    {
        return $query
            ->when($fecha, fn ($q) => $q->whereDate('fecha', $fecha))
            ->when($veterinario, fn ($q) => $q->where('id_veterinario', $veterinario))
            ->with(['mascota.cliente', 'veterinario', 'estadoTurno'])
            ->orderBy('fecha')
            ->orderBy('hora');
    }
}
