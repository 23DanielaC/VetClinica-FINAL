<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Relations\BelongsTo;
use Illuminate\Database\Eloquent\Relations\BelongsToMany;
use Illuminate\Database\Eloquent\Relations\HasOne;

class Consulta extends VetClinicaModel
{
    protected $table = 'consulta';

    protected $primaryKey = 'id_consulta';

    protected $fillable = [
        'diagnostico',
        'tratamiento',
        'observaciones',
        'id_turno',
    ];

    public function turno(): BelongsTo
    {
        return $this->belongsTo(Turno::class, 'id_turno', 'id_turno');
    }

    public function factura(): HasOne
    {
        return $this->hasOne(Factura::class, 'id_consulta', 'id_consulta');
    }

    public function medicamentos(): BelongsToMany
    {
        return $this->belongsToMany(
            Medicamento::class,
            'consulta_medicamento',
            'id_consulta',
            'id_medicamento',
            'id_consulta',
            'id_medicamento',
        )->withPivot('cantidad');
    }
}
