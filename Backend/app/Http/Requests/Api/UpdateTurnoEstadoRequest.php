<?php

namespace App\Http\Requests\Api;

use Illuminate\Foundation\Http\FormRequest;
use Illuminate\Validation\Rule;

class UpdateTurnoEstadoRequest extends FormRequest
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
            'id_estado_turno' => ['required_without:estado', 'integer', Rule::exists('estado_turno', 'id_estado_turno')],
            'estado' => ['required_without:id_estado_turno', 'string', Rule::exists('estado_turno', 'nombre')],
        ];
    }
}
