<?php

namespace App\Http\Requests\Api;

use Illuminate\Foundation\Http\FormRequest;
use Illuminate\Validation\Rule;

class StoreMascotaRequest extends FormRequest
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
            'nombre' => ['required', 'string', 'max:100'],
            'especie' => ['required', 'string', 'max:50'],
            'raza' => ['nullable', 'string', 'max:50'],
            'fecha_nacimiento' => ['nullable', 'date'],
            'id_cliente' => ['required', 'integer', Rule::exists('cliente', 'id_cliente')],
        ];
    }
}
