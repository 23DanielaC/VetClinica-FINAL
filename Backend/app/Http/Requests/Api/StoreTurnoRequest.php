<?php

namespace App\Http\Requests\Api;

use App\Models\EstadoTurno;
use App\Models\Turno;
use Illuminate\Foundation\Http\FormRequest;
use Illuminate\Validation\Rule;

class StoreTurnoRequest extends FormRequest
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
            'hora' => ['required', 'date_format:H:i:s'],
            'id_estado_turno' => ['sometimes', 'integer', Rule::exists('estado_turno', 'id_estado_turno')],
            'id_mascota' => ['required', 'integer', Rule::exists('mascota', 'id_mascota')],
            'id_veterinario' => ['required', 'integer', Rule::exists('veterinario', 'id_veterinario')],
        ];
    }

    public function withValidator($validator): void
    {
        $validator->after(function ($validator): void {
            if ($validator->errors()->isNotEmpty()) {
                return;
            }

            $exists = Turno::query()
                ->where('id_veterinario', $this->integer('id_veterinario'))
                ->whereDate('fecha', $this->input('fecha'))
                ->where('hora', $this->input('hora'))
                ->where('id_estado_turno', '!=', EstadoTurno::CANCELADO)
                ->exists();

            if ($exists) {
                $validator->errors()->add('hora', 'El veterinario ya tiene un turno en ese horario.');
            }
        });
    }
}
