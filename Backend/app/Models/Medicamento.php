<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Relations\BelongsToMany;

class Medicamento extends VetClinicaModel
{
    protected $table = 'medicamento';

    protected $primaryKey = 'id_medicamento';

    protected $fillable = [
        'nombre',
        'descripcion',
        'precio',
    ];

    protected function casts(): array
    {
        return [
            'precio' => 'decimal:2',
        ];
    }

    public function consultas(): BelongsToMany
    {
        return $this->belongsToMany(
            Consulta::class,
            'consulta_medicamento',
            'id_medicamento',
            'id_consulta',
            'id_medicamento',
            'id_consulta',
        )->withPivot('cantidad');
    }
}
