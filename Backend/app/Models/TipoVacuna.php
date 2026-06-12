<?php

namespace App\Models;

class TipoVacuna extends VetClinicaModel
{
    public $timestamps = false;

    protected $table = 'tipo_vacuna';

    protected $primaryKey = 'id_tipo_vacuna';

    protected $fillable = [
        'nombre',
        'descripcion',
    ];
}
