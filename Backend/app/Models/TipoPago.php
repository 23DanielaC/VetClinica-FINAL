<?php

namespace App\Models;

class TipoPago extends VetClinicaModel
{
    public $timestamps = false;

    protected $table = 'tipo_pago';

    protected $primaryKey = 'id_tipo_pago';

    protected $fillable = [
        'nombre',
    ];
}
