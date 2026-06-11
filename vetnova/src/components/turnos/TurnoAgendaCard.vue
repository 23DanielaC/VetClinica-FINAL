<template>
  <article
    :class="[
      'group rounded-2xl border p-4 lg:p-5 transition-all duration-300',
      'hover:shadow-lg hover:-translate-y-0.5',
      theme.card,
    ]"
  >
    <!-- Desktop / tablet horizontal -->
    <div class="hidden sm:flex items-center gap-4 lg:gap-6">
      <!-- Hora -->
      <div class="shrink-0 w-20 lg:w-24 text-center border-r border-black/5 pr-4">
        <p class="text-2xl lg:text-3xl font-extrabold text-slate-900 tabular-nums leading-none">{{ turno.hora }}</p>
        <p class="text-xs text-slate-500 mt-1.5 font-medium">{{ turno.duracion ?? '30' }} min</p>
      </div>

      <!-- Mascota + consulta -->
      <div class="flex items-center gap-4 flex-1 min-w-0">
        <div
          :class="['w-14 h-14 rounded-2xl bg-gradient-to-br flex items-center justify-center text-2xl shadow-md ring-2 ring-white shrink-0', avatarGradient]"
        >
          {{ emoji }}
        </div>
        <div class="min-w-0 flex-1">
          <div class="flex items-center gap-2 flex-wrap">
            <h3 class="text-base font-bold text-slate-900 truncate">{{ mascotaNombre }}</h3>
            <span :class="theme.badge">{{ turno.estado }}</span>
          </div>
          <p class="text-sm font-medium text-slate-600 mt-0.5 truncate">{{ turno.motivo }}</p>
          <p class="text-xs text-slate-400 mt-1 flex items-center gap-1 truncate">
            <UserIcon class="w-3.5 h-3.5 shrink-0" /> {{ vetNombre }}
          </p>
        </div>
      </div>

      <!-- Propietario -->
      <div class="hidden md:block shrink-0 w-40 lg:w-44 border-l border-black/5 pl-4">
        <p class="text-sm font-bold text-slate-800 truncate">{{ clienteNombre }}</p>
        <p class="text-xs text-slate-500 mt-1 flex items-center gap-1 truncate">
          <PhoneIcon class="w-3.5 h-3.5 shrink-0" /> {{ clienteTelefono }}
        </p>
      </div>

      <!-- Acciones -->
      <div class="flex items-center gap-1 shrink-0">
        <button type="button" title="Ver" class="turno-action" @click="$emit('view', turno)">
          <EyeIcon class="w-4 h-4" />
        </button>
        <button type="button" title="Editar" class="turno-action" @click="$emit('edit', turno)">
          <PencilSquareIcon class="w-4 h-4" />
        </button>
        <button type="button" title="Reagendar" class="turno-action" @click="$emit('reschedule', turno)">
          <CalendarDaysIcon class="w-4 h-4" />
        </button>
        <button type="button" title="Cancelar" class="turno-action turno-action--danger" @click="$emit('cancel', turno)">
          <XCircleIcon class="w-4 h-4" />
        </button>
      </div>
    </div>

    <!-- Móvil vertical -->
    <div class="sm:hidden space-y-3">
      <div class="flex items-start justify-between gap-3">
        <div>
          <p class="text-2xl font-extrabold text-slate-900 tabular-nums">{{ turno.hora }}</p>
          <p class="text-xs text-slate-500">{{ turno.duracion ?? '30' }} min · {{ formatFecha(turno.fecha) }}</p>
        </div>
        <span :class="theme.badge">{{ turno.estado }}</span>
      </div>
      <div class="flex items-center gap-3">
        <div :class="['w-12 h-12 rounded-xl bg-gradient-to-br flex items-center justify-center text-xl shrink-0', avatarGradient]">
          {{ emoji }}
        </div>
        <div class="min-w-0">
          <p class="font-bold text-slate-900">{{ mascotaNombre }}</p>
          <p class="text-sm text-slate-600">{{ turno.motivo }}</p>
          <p class="text-xs text-slate-400">{{ vetNombre }}</p>
        </div>
      </div>
      <div class="text-sm">
        <p class="font-semibold text-slate-800">{{ clienteNombre }}</p>
        <p class="text-xs text-slate-500">{{ clienteTelefono }}</p>
      </div>
      <div class="flex gap-1 pt-2 border-t border-black/5">
        <button type="button" class="turno-action flex-1" @click="$emit('view', turno)"><EyeIcon class="w-4 h-4" /></button>
        <button type="button" class="turno-action flex-1" @click="$emit('edit', turno)"><PencilSquareIcon class="w-4 h-4" /></button>
        <button type="button" class="turno-action flex-1" @click="$emit('reschedule', turno)"><CalendarDaysIcon class="w-4 h-4" /></button>
        <button type="button" class="turno-action turno-action--danger flex-1" @click="$emit('cancel', turno)"><XCircleIcon class="w-4 h-4" /></button>
      </div>
    </div>
  </article>
</template>

<script setup>
import { computed } from 'vue'
import {
  EyeIcon, PencilSquareIcon, CalendarDaysIcon, XCircleIcon,
  UserIcon, PhoneIcon,
} from '@heroicons/vue/24/outline'

const props = defineProps({
  turno: { type: Object, required: true },
  mascotaNombre: { type: String, default: '—' },
  clienteNombre: { type: String, default: '—' },
  clienteTelefono: { type: String, default: '—' },
  vetNombre: { type: String, default: '—' },
  emoji: { type: String, default: '🐾' },
  avatarGradient: { type: String, default: 'from-slate-400 to-slate-600' },
})

defineEmits(['view', 'edit', 'reschedule', 'cancel'])

const estadoThemes = {
  Pendiente: {
    card: 'bg-amber-50/80 border-amber-200 shadow-sm',
    badge: 'inline-flex text-[10px] font-bold uppercase px-2 py-0.5 rounded-full bg-amber-100 text-amber-800 ring-1 ring-amber-200',
  },
  Confirmado: {
    card: 'bg-emerald-50/80 border-emerald-200 shadow-sm',
    badge: 'inline-flex text-[10px] font-bold uppercase px-2 py-0.5 rounded-full bg-emerald-100 text-emerald-800 ring-1 ring-emerald-200',
  },
  'En curso': {
    card: 'bg-blue-50/80 border-blue-200 shadow-sm',
    badge: 'inline-flex text-[10px] font-bold uppercase px-2 py-0.5 rounded-full bg-blue-100 text-blue-800 ring-1 ring-blue-200',
  },
  'En Espera': {
    card: 'bg-blue-50/80 border-blue-200 shadow-sm',
    badge: 'inline-flex text-[10px] font-bold uppercase px-2 py-0.5 rounded-full bg-blue-100 text-blue-800 ring-1 ring-blue-200',
  },
  Completado: {
    card: 'bg-slate-50/90 border-slate-200 shadow-sm',
    badge: 'inline-flex text-[10px] font-bold uppercase px-2 py-0.5 rounded-full bg-slate-200 text-slate-700 ring-1 ring-slate-300',
  },
  Cancelado: {
    card: 'bg-rose-50/60 border-rose-200 shadow-sm opacity-75',
    badge: 'inline-flex text-[10px] font-bold uppercase px-2 py-0.5 rounded-full bg-rose-100 text-rose-700 ring-1 ring-rose-200',
  },
}

const theme = computed(() => estadoThemes[props.turno.estado] ?? estadoThemes.Pendiente)

function formatFecha(f) {
  return new Date(f + 'T12:00:00').toLocaleDateString('es-CO', { day: 'numeric', month: 'short' })
}
</script>

<style scoped>
.turno-action {
  @apply p-2 rounded-xl text-slate-500 hover:text-indigo-600 hover:bg-white/80 transition-colors cursor-pointer;
}
.turno-action--danger {
  @apply hover:text-rose-600 hover:bg-rose-50/80;
}
</style>
