<?php

namespace App\Models;

class EstadoTurno extends VetClinicaModel
{
    public const PENDIENTE = 1;

    public const ATENDIDO = 2;

    public const CANCELADO = 3;

    public $timestamps = false;

    protected $table = 'estado_turno';

    protected $primaryKey = 'id_estado_turno';

    protected $fillable = [
        'nombre',
    ];
}
