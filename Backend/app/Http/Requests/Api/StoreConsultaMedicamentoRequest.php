<?php

namespace App\Http\Requests\Api;

use Illuminate\Foundation\Http\FormRequest;
use Illuminate\Validation\Rule;

class StoreConsultaMedicamentoRequest extends FormRequest
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
            'medicamentos' => ['required', 'array', 'min:1'],
            'medicamentos.*.id_medicamento' => ['required', 'integer', Rule::exists('medicamento', 'id_medicamento')],
            'medicamentos.*.cantidad' => ['required', 'integer', 'min:1'],
        ];
    }
}
