<?php

namespace App\Http\Requests\Api;

use Illuminate\Foundation\Http\FormRequest;
use Illuminate\Validation\Rule;

class StoreFacturaRequest extends FormRequest
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
            'fecha' => ['required', 'date'],
            'total' => ['required', 'numeric', 'min:0'],
            'id_cliente' => ['required', 'integer', Rule::exists('cliente', 'id_cliente')],
            'id_consulta' => ['required', 'integer', Rule::exists('consulta', 'id_consulta'), Rule::unique('factura', 'id_consulta')],
            'detalles' => ['sometimes', 'array', 'min:1'],
            'detalles.*.id_medicamento' => ['nullable', 'integer', Rule::exists('medicamento', 'id_medicamento')],
            'detalles.*.descripcion' => ['required_with:detalles', 'string', 'max:200'],
            'detalles.*.cantidad' => ['required_with:detalles', 'integer', 'min:1'],
            'detalles.*.precio_unitario' => ['required_with:detalles', 'numeric', 'min:0'],
            'detalles.*.subtotal' => ['required_with:detalles', 'numeric', 'min:0'],
        ];
    }
}
