<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Relations\HasMany;

class Cliente extends VetClinicaModel
{
    protected $table = 'cliente';

    protected $primaryKey = 'id_cliente';

    protected $fillable = [
        'nombre',
        'telefono',
        'direccion',
    ];

    public function mascotas(): HasMany
    {
        return $this->hasMany(Mascota::class, 'id_cliente', 'id_cliente');
    }

    public function facturas(): HasMany
    {
        return $this->hasMany(Factura::class, 'id_cliente', 'id_cliente');
    }

    public function tieneMascotasConTurnosActivos(): bool
    {
        return $this->mascotas()
            ->whereHas('turnos', fn ($query) => $query->whereIn('id_estado_turno', [1, 2]))
            ->exists();
    }
}
