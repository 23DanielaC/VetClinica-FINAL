<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Builder;
use Illuminate\Database\Eloquent\Relations\BelongsTo;

class VacunaAplicada extends VetClinicaModel
{
    protected $table = 'vacuna_aplicada';

    protected $primaryKey = 'id_vacuna_aplicada';

    protected $fillable = [
        'id_mascota',
        'id_tipo_vacuna',
        'fecha_aplicacion',
        'fecha_proxima',
    ];

    protected function casts(): array
    {
        return [
            'fecha_aplicacion' => 'date',
            'fecha_proxima' => 'date',
        ];
    }

    public function mascota(): BelongsTo
    {
        return $this->belongsTo(Mascota::class, 'id_mascota', 'id_mascota');
    }

    public function tipoVacuna(): BelongsTo
    {
        return $this->belongsTo(TipoVacuna::class, 'id_tipo_vacuna', 'id_tipo_vacuna');
    }

    public function scopeProximasAVencer(Builder $query, int $dias = 30): Builder
    {
        return $query
            ->whereBetween('fecha_proxima', [now()->toDateString(), now()->addDays($dias)->toDateString()])
            ->with(['mascota.cliente', 'tipoVacuna'])
            ->orderBy('fecha_proxima');
    }
}
