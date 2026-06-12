<?php

namespace App\Http\Requests\Api;

use Illuminate\Foundation\Http\FormRequest;
use Illuminate\Validation\Rule;

class StoreVacunaRequest extends FormRequest
{
    public function authorize(): bool
    {
        return true;
    }

    /**
     * @return array<string, mixed>
     */
    public function rules(): array
    {
        return [
            'id_mascota' => ['required', 'integer', Rule::exists('mascota', 'id_mascota')],
            'id_tipo_vacuna' => ['required', 'integer', Rule::exists('tipo_vacuna', 'id_tipo_vacuna')],
            'fecha_aplicacion' => ['required', 'date'],
            'fecha_proxima' => ['required', 'date', 'after_or_equal:fecha_aplicacion'],
        ];
    }
}
