<?php

namespace App\Http\Requests\Api;

use Illuminate\Foundation\Http\FormRequest;
use Illuminate\Validation\Rule;

class StoreConsultaRequest extends FormRequest
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
            'id_turno' => ['required', 'integer', Rule::exists('turno', 'id_turno')],
            'diagnostico' => ['required', 'string', 'max:300'],
            'tratamiento' => ['nullable', 'string', 'max:500'],
            'observaciones' => ['nullable', 'string', 'max:500'],
        ];
    }
}
