<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Relations\BelongsTo;

class Pago extends VetClinicaModel
{
    public $timestamps = false;

    protected $table = 'pago';

    protected $primaryKey = 'id_pago';

    protected $fillable = [
        'fecha',
        'monto',
        'id_tipo_pago',
        'id_metodo_pago',
        'id_factura',
    ];

    protected function casts(): array
    {
        return [
            'fecha' => 'date',
            'monto' => 'decimal:2',
        ];
    }

    public function factura(): BelongsTo
    {
        return $this->belongsTo(Factura::class, 'id_factura', 'id_factura');
    }

    public function tipoPago(): BelongsTo
    {
        return $this->belongsTo(TipoPago::class, 'id_tipo_pago', 'id_tipo_pago');
    }

    public function metodoPago(): BelongsTo
    {
        return $this->belongsTo(MetodoPago::class, 'id_metodo_pago', 'id_metodo_pago');
    }
}
