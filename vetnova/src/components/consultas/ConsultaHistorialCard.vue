<template>
  <article
    class="group bg-white rounded-2xl border border-slate-100 p-4 lg:p-5
           shadow-[0_1px_2px_rgba(0,0,0,0.04),0_4px_16px_rgba(0,0,0,0.05)]
           hover:shadow-[0_8px_28px_rgba(0,0,0,0.1)] hover:-translate-y-0.5
           transition-all duration-300"
  >
    <!-- Horizontal desktop -->
    <div class="hidden md:flex items-center gap-5 lg:gap-6">
      <!-- Fecha + avatar -->
      <div class="flex items-center gap-4 shrink-0 w-44 lg:w-48">
        <div
          :class="['w-14 h-14 rounded-2xl bg-gradient-to-br flex items-center justify-center text-2xl shadow-md ring-2 ring-white shrink-0', avatarGradient]"
        >
          {{ emoji }}
        </div>
        <div>
          <p class="text-sm font-bold text-slate-900 capitalize">{{ formatFecha(consulta.fecha) }}</p>
          <p class="text-lg font-extrabold text-indigo-600 tabular-nums">{{ consulta.hora ?? '—' }}</p>
        </div>
      </div>

      <!-- Info clínica -->
      <div class="flex-1 min-w-0 border-l border-slate-100 pl-5">
        <div class="flex items-center gap-2 flex-wrap">
          <h3 class="text-base font-bold text-slate-900">{{ mascotaNombre }}</h3>
          <span :class="estadoBadge">{{ consulta.estado ?? 'Finalizada' }}</span>
        </div>
        <p class="text-xs text-slate-500 mt-0.5">{{ propietario }} · {{ vetNombre }}</p>
        <p class="text-sm font-medium text-slate-700 mt-2 truncate">{{ motivo }}</p>
        <p class="text-xs text-slate-500 mt-1 line-clamp-1">
          <span class="font-semibold text-slate-600">Dx:</span> {{ consulta.diagnostico }}
        </p>
      </div>

      <!-- Costo + acciones -->
      <div class="shrink-0 text-right border-l border-slate-100 pl-5 w-36 lg:w-40">
        <p class="text-lg font-extrabold text-slate-900 tabular-nums">${{ (consulta.costo ?? 0).toLocaleString('es-CO') }}</p>
        <p class="text-[10px] text-slate-400 uppercase font-semibold">Costo</p>
        <div class="flex items-center justify-end gap-1 mt-3">
          <button type="button" title="Ver historial" class="cons-action" @click="$emit('view', consulta)">
            <DocumentTextIcon class="w-4 h-4" />
          </button>
          <button type="button" title="Editar" class="cons-action" @click="$emit('edit', consulta)">
            <PencilSquareIcon class="w-4 h-4" />
          </button>
          <button type="button" title="PDF" class="cons-action" @click="$emit('pdf', consulta)">
            <ArrowDownTrayIcon class="w-4 h-4" />
          </button>
          <button type="button" title="Eliminar" class="cons-action cons-action--danger" @click="$emit('delete', consulta)">
            <TrashIcon class="w-4 h-4" />
          </button>
        </div>
      </div>
    </div>

    <!-- Móvil vertical -->
    <div class="md:hidden space-y-3">
      <div class="flex items-start gap-3">
        <div :class="['w-12 h-12 rounded-xl bg-gradient-to-br flex items-center justify-center text-xl shrink-0', avatarGradient]">
          {{ emoji }}
        </div>
        <div class="flex-1 min-w-0">
          <div class="flex items-center gap-2 flex-wrap">
            <p class="font-bold text-slate-900">{{ mascotaNombre }}</p>
            <span :class="estadoBadge">{{ consulta.estado ?? 'Finalizada' }}</span>
          </div>
          <p class="text-xs text-slate-500">{{ formatFecha(consulta.fecha) }} · {{ consulta.hora }}</p>
          <p class="text-sm text-slate-600 mt-1">{{ propietario }}</p>
        </div>
        <p class="text-sm font-bold text-slate-900 shrink-0">${{ (consulta.costo ?? 0).toLocaleString('es-CO') }}</p>
      </div>
      <p class="text-sm text-slate-700"><span class="font-semibold">Motivo:</span> {{ motivo }}</p>
      <p class="text-xs text-slate-500 line-clamp-2">{{ consulta.diagnostico }}</p>
      <div class="flex gap-1 pt-2 border-t border-slate-100">
        <button type="button" class="cons-action flex-1" @click="$emit('view', consulta)"><DocumentTextIcon class="w-4 h-4 mx-auto" /></button>
        <button type="button" class="cons-action flex-1" @click="$emit('edit', consulta)"><PencilSquareIcon class="w-4 h-4 mx-auto" /></button>
        <button type="button" class="cons-action flex-1" @click="$emit('pdf', consulta)"><ArrowDownTrayIcon class="w-4 h-4 mx-auto" /></button>
        <button type="button" class="cons-action cons-action--danger flex-1" @click="$emit('delete', consulta)"><TrashIcon class="w-4 h-4 mx-auto" /></button>
      </div>
    </div>
  </article>
</template>

<script setup>
import { computed } from 'vue'
import {
  DocumentTextIcon, PencilSquareIcon, ArrowDownTrayIcon, TrashIcon,
} from '@heroicons/vue/24/outline'

const props = defineProps({
  consulta: { type: Object, required: true },
  mascotaNombre: { type: String, default: '—' },
  propietario: { type: String, default: '—' },
  vetNombre: { type: String, default: '—' },
  motivo: { type: String, default: '—' },
  emoji: { type: String, default: '🐾' },
  avatarGradient: { type: String, default: 'from-slate-400 to-slate-600' },
})

defineEmits(['view', 'edit', 'pdf', 'delete'])

const estadoStyles = {
  Finalizada: 'inline-flex text-[10px] font-bold uppercase px-2 py-0.5 rounded-full bg-emerald-50 text-emerald-700 ring-1 ring-emerald-200',
  Pendiente: 'inline-flex text-[10px] font-bold uppercase px-2 py-0.5 rounded-full bg-amber-50 text-amber-700 ring-1 ring-amber-200',
  Seguimiento: 'inline-flex text-[10px] font-bold uppercase px-2 py-0.5 rounded-full bg-blue-50 text-blue-700 ring-1 ring-blue-200',
  Cancelada: 'inline-flex text-[10px] font-bold uppercase px-2 py-0.5 rounded-full bg-rose-50 text-rose-700 ring-1 ring-rose-200',
}

const estadoBadge = computed(() => estadoStyles[props.consulta.estado] ?? estadoStyles.Finalizada)

function formatFecha(f) {
  return new Date(f + 'T12:00:00').toLocaleDateString('es-CO', { day: 'numeric', month: 'short', year: 'numeric' })
}
</script>

<style scoped>
.cons-action {
  @apply p-2 rounded-xl text-slate-500 hover:text-indigo-600 hover:bg-indigo-50 transition-colors cursor-pointer;
}
.cons-action--danger {
  @apply hover:text-rose-600 hover:bg-rose-50;
}
</style>
