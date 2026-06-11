<?php

namespace App\Models;

class MetodoPago extends VetClinicaModel
{
    public $timestamps = false;

    protected $table = 'metodo_pago';

    protected $primaryKey = 'id_metodo_pago';

    protected $fillable = [
        'nombre',
    ];
}
