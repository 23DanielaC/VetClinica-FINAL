<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Relations\BelongsTo;

class FacturaDetalle extends VetClinicaModel
{
    public $timestamps = false;

    protected $table = 'factura_detalle';

    protected $primaryKey = 'id_factura_detalle';

    protected $fillable = [
        'id_factura',
        'id_medicamento',
        'descripcion',
        'cantidad',
        'precio_unitario',
        'subtotal',
    ];

    protected function casts(): array
    {
        return [
            'precio_unitario' => 'decimal:2',
            'subtotal' => 'decimal:2',
        ];
    }

    public function factura(): BelongsTo
    {
        return $this->belongsTo(Factura::class, 'id_factura', 'id_factura');
    }

    public function medicamento(): BelongsTo
    {
        return $this->belongsTo(Medicamento::class, 'id_medicamento', 'id_medicamento');
    }
}
