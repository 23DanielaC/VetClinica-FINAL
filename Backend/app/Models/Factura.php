<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Relations\BelongsTo;
use Illuminate\Database\Eloquent\Relations\HasMany;

class Factura extends VetClinicaModel
{
    protected $table = 'factura';

    protected $primaryKey = 'id_factura';

    protected $fillable = [
        'fecha',
        'total',
        'id_cliente',
        'id_consulta',
    ];

    protected function casts(): array
    {
        return [
            'fecha' => 'date',
            'total' => 'decimal:2',
        ];
    }

    public function cliente(): BelongsTo
    {
        return $this->belongsTo(Cliente::class, 'id_cliente', 'id_cliente');
    }

    public function consulta(): BelongsTo
    {
        return $this->belongsTo(Consulta::class, 'id_consulta', 'id_consulta');
    }

    public function detalles(): HasMany
    {
        return $this->hasMany(FacturaDetalle::class, 'id_factura', 'id_factura');
    }

    public function pagos(): HasMany
    {
        return $this->hasMany(Pago::class, 'id_factura', 'id_factura');
    }

    public function totalPagado(): float
    {
        return (float) $this->pagos()->sum('monto');
    }

    public function saldoPendiente(): float
    {
        return (float) $this->total - $this->totalPagado();
    }
}
