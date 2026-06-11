<?php

namespace App\Http\Requests\Api;

use Illuminate\Foundation\Http\FormRequest;
use Illuminate\Validation\Rule;

class StorePagoRequest extends FormRequest
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
            'monto' => ['required', 'numeric', 'min:0.01'],
            'id_tipo_pago' => ['required', 'integer', Rule::exists('tipo_pago', 'id_tipo_pago')],
            'id_metodo_pago' => ['required', 'integer', Rule::exists('metodo_pago', 'id_metodo_pago')],
            'id_factura' => ['required', 'integer', Rule::exists('factura', 'id_factura')],
        ];
    }
}
