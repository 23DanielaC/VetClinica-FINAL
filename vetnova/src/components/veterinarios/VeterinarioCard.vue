<template>
  <article
    class="group bg-white rounded-2xl border border-slate-100 p-5
           shadow-[0_1px_2px_rgba(0,0,0,0.04),0_4px_16px_rgba(0,0,0,0.05)]
           hover:shadow-[0_8px_28px_rgba(0,0,0,0.1)] hover:-translate-y-1
           transition-all duration-300 flex flex-col h-full"
  >
    <!-- Avatar + estado -->
    <div class="flex items-start justify-between gap-3 mb-4">
      <div class="relative">
        <div
          :class="['w-14 h-14 rounded-2xl bg-gradient-to-br flex items-center justify-center text-white font-bold text-lg shadow-md ring-2 ring-white', theme.gradient]"
        >
          {{ iniciales }}
        </div>
        <span
          :class="['absolute -bottom-0.5 -right-0.5 w-3.5 h-3.5 rounded-full ring-2 ring-white', activo ? 'bg-emerald-500' : 'bg-slate-400']"
          :title="activo ? 'Activo' : 'Inactivo'"
        />
      </div>
      <span :class="['text-[10px] font-bold uppercase tracking-wide px-2.5 py-1 rounded-full', theme.badge]">
        {{ vet.especialidad }}
      </span>
    </div>

    <!-- Nombre -->
    <h3 class="text-base font-bold text-slate-900 leading-snug group-hover:text-indigo-700 transition-colors">
      {{ vet.nombre }}
    </h3>
    <p class="text-xs text-slate-400 mt-0.5 font-medium">{{ vet.matricula }}</p>

    <!-- Contacto -->
    <div class="mt-4 space-y-2 text-sm">
      <div class="flex items-center gap-2 text-slate-600 min-w-0">
        <PhoneIcon class="w-4 h-4 text-slate-400 shrink-0" />
        <span class="truncate">{{ vet.telefono }}</span>
      </div>
      <div class="flex items-center gap-2 text-slate-600 min-w-0">
        <EnvelopeIcon class="w-4 h-4 text-slate-400 shrink-0" />
        <span class="truncate">{{ vet.email }}</span>
      </div>
    </div>

    <!-- Stats -->
    <div class="grid grid-cols-2 gap-2 mt-4">
      <div class="bg-slate-50 rounded-xl px-3 py-2 border border-slate-100">
        <p class="text-[10px] font-semibold text-slate-400 uppercase">Turnos</p>
        <p class="text-lg font-extrabold text-slate-800 tabular-nums">{{ turnos }}</p>
      </div>
      <div class="bg-slate-50 rounded-xl px-3 py-2 border border-slate-100">
        <p class="text-[10px] font-semibold text-slate-400 uppercase">Consultas</p>
        <p class="text-lg font-extrabold text-slate-800 tabular-nums">{{ consultas }}</p>
      </div>
    </div>

    <!-- Estado badge -->
    <div class="mt-3">
      <span :class="estadoClass">{{ vet.estado ?? 'Activo' }}</span>
    </div>

    <!-- Acciones -->
    <div class="flex items-center gap-1 mt-4 pt-4 border-t border-slate-100 mt-auto">
      <button type="button" title="Ver" class="vet-action" @click="$emit('view', vet)">
        <EyeIcon class="w-4 h-4" />
      </button>
      <button type="button" title="Editar" class="vet-action" @click="$emit('edit', vet)">
        <PencilSquareIcon class="w-4 h-4" />
      </button>
      <button type="button" title="Eliminar" class="vet-action vet-action--danger" @click="$emit('delete', vet)">
        <TrashIcon class="w-4 h-4" />
      </button>
    </div>
  </article>
</template>

<script setup>
import { computed } from 'vue'
import { PhoneIcon, EnvelopeIcon, EyeIcon, PencilSquareIcon, TrashIcon } from '@heroicons/vue/24/outline'

const props = defineProps({
  vet: { type: Object, required: true },
  iniciales: { type: String, default: '?' },
  turnos: { type: Number, default: 0 },
  consultas: { type: Number, default: 0 },
})

defineEmits(['view', 'edit', 'delete'])

const themes = {
  'Cirugía':          { gradient: 'from-rose-500 to-red-600',     badge: 'bg-rose-50 text-rose-700 ring-1 ring-rose-200' },
  'Dermatología':     { gradient: 'from-violet-500 to-purple-600', badge: 'bg-violet-50 text-violet-700 ring-1 ring-violet-200' },
  'Medicina General': { gradient: 'from-blue-500 to-indigo-600',   badge: 'bg-blue-50 text-blue-700 ring-1 ring-blue-200' },
  'Cardiología':      { gradient: 'from-pink-500 to-rose-600',     badge: 'bg-pink-50 text-pink-700 ring-1 ring-pink-200' },
  'Oncología':        { gradient: 'from-amber-500 to-orange-600',  badge: 'bg-amber-50 text-amber-700 ring-1 ring-amber-200' },
  'Oftalmología':     { gradient: 'from-cyan-500 to-teal-600',     badge: 'bg-cyan-50 text-cyan-700 ring-1 ring-cyan-200' },
}

const theme = computed(() => themes[props.vet.especialidad] ?? {
  gradient: 'from-slate-500 to-slate-600',
  badge: 'bg-slate-100 text-slate-700 ring-1 ring-slate-200',
})

const activo = computed(() => (props.vet.estado ?? 'Activo') === 'Activo')

const estadoClass = computed(() => {
  const e = props.vet.estado ?? 'Activo'
  const map = {
    Activo: 'inline-flex text-[10px] font-bold uppercase px-2 py-0.5 rounded-full bg-emerald-50 text-emerald-700 ring-1 ring-emerald-200',
    Inactivo: 'inline-flex text-[10px] font-bold uppercase px-2 py-0.5 rounded-full bg-slate-100 text-slate-600 ring-1 ring-slate-200',
  }
  return map[e] ?? map.Activo
})
</script>

<style scoped>
.vet-action {
  @apply flex-1 inline-flex items-center justify-center p-2 rounded-xl text-slate-500
         hover:text-indigo-600 hover:bg-indigo-50 transition-colors cursor-pointer;
}
.vet-action--danger {
  @apply hover:text-rose-600 hover:bg-rose-50;
}
</style>
