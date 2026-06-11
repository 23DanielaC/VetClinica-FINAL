<template>
  <article
    class="pet-premium group"
    :class="theme.card"
  >
    <!-- Header gradiente -->
    <div :class="['pet-premium__header bg-gradient-to-br transition-all duration-500 group-hover:brightness-105', theme.header]" />

    <!-- Avatar superpuesto -->
    <div class="pet-premium__avatar-wrap">
      <div class="pet-premium__avatar group-hover:scale-110 transition-transform duration-300">
        <span class="text-4xl select-none">{{ emoji }}</span>
      </div>
    </div>

    <!-- Cuerpo -->
    <div class="pet-premium__body">
      <!-- Nombre y especie -->
      <div class="text-center pt-2">
        <h3 class="text-xl font-extrabold text-slate-900 tracking-tight">{{ pet.nombre }}</h3>
        <p class="text-sm text-slate-500 mt-0.5">{{ pet.especie }} · {{ pet.raza }}</p>
      </div>

      <!-- Badges -->
      <div class="flex flex-wrap justify-center gap-1.5 mt-3">
        <span :class="['pet-badge', theme.badgeSpecie]">{{ emoji }} {{ pet.especie }}</span>
        <span v-if="ctx.vacunado" class="pet-badge pet-badge--green">💉 Vacunado</span>
        <span class="pet-badge pet-badge--rose">❤️ Activo</span>
      </div>

      <!-- Datos en grid -->
      <div class="grid grid-cols-2 gap-2 mt-4">
        <div class="pet-mini-stat">
          <span class="pet-mini-stat__label">Edad</span>
          <span class="pet-mini-stat__value">{{ pet.edad }} {{ pet.edad === 1 ? 'año' : 'años' }}</span>
        </div>
        <div class="pet-mini-stat">
          <span class="pet-mini-stat__label">Peso</span>
          <span class="pet-mini-stat__value">{{ pet.peso }} kg</span>
        </div>
        <div class="pet-mini-stat">
          <span class="pet-mini-stat__label">Sexo</span>
          <span class="pet-mini-stat__value">{{ pet.sexo ?? '—' }}</span>
        </div>
        <div class="pet-mini-stat">
          <span class="pet-mini-stat__label">Propietario</span>
          <span class="pet-mini-stat__value truncate">{{ ctx.propietario }}</span>
        </div>
      </div>

      <!-- Info médica -->
      <div class="mt-4 p-3 rounded-xl bg-slate-50/80 border border-slate-100 space-y-2">
        <div class="flex justify-between text-xs">
          <span class="text-slate-400">Última consulta</span>
          <span class="font-semibold text-slate-700">{{ ctx.ultimaConsulta }}</span>
        </div>
        <div class="flex justify-between text-xs">
          <span class="text-slate-400">Próxima vacuna</span>
          <span class="font-semibold text-slate-700">{{ ctx.proximaVacuna }}</span>
        </div>
        <div class="flex justify-between text-xs">
          <span class="text-slate-400">Estado</span>
          <span :class="['font-bold', ctx.estadoSaludClass]">{{ ctx.estadoSalud }}</span>
        </div>
      </div>

      <!-- Métricas -->
      <div class="grid grid-cols-3 gap-2 mt-3">
        <div class="pet-metric">
          <ClipboardDocumentCheckIcon class="w-3.5 h-3.5 text-blue-500" />
          <span class="pet-metric__num">{{ ctx.consultas }}</span>
          <span class="pet-metric__lbl">Consultas</span>
        </div>
        <div class="pet-metric">
          <ShieldCheckIcon class="w-3.5 h-3.5 text-emerald-500" />
          <span class="pet-metric__num">{{ ctx.vacunas }}</span>
          <span class="pet-metric__lbl">Vacunas</span>
        </div>
        <div class="pet-metric">
          <BeakerIcon class="w-3.5 h-3.5 text-violet-500" />
          <span class="pet-metric__num">{{ ctx.tratamientos }}</span>
          <span class="pet-metric__lbl">Tratamientos</span>
        </div>
      </div>

      <!-- Pie: propietario -->
      <div class="mt-4 pt-3 border-t border-slate-100 text-center">
        <p class="text-xs text-slate-400">Propietario</p>
        <p class="text-sm font-semibold text-slate-800">{{ ctx.propietario }}</p>
        <p class="text-xs text-slate-500 mt-0.5">{{ ctx.telefono }}</p>
      </div>

      <!-- Acciones -->
      <div class="flex items-center justify-center gap-2 mt-4 pt-3 border-t border-slate-100">
        <button type="button" title="Ver detalle" class="pet-action pet-action--blue" @click.stop="$emit('view', pet)">
          <EyeIcon class="w-4 h-4" />
        </button>
        <button type="button" title="Editar" class="pet-action pet-action--green" @click.stop="$emit('edit', pet)">
          <PencilSquareIcon class="w-4 h-4" />
        </button>
        <button type="button" title="Historial médico" class="pet-action pet-action--violet" @click.stop="$emit('history', pet)">
          <ClipboardDocumentListIcon class="w-4 h-4" />
        </button>
        <button type="button" title="Eliminar" class="pet-action pet-action--red" @click.stop="$emit('delete', pet)">
          <TrashIcon class="w-4 h-4" />
        </button>
      </div>
    </div>
  </article>
</template>

<script setup>
import { computed } from 'vue'
import {
  EyeIcon, PencilSquareIcon, TrashIcon, ClipboardDocumentListIcon,
  ClipboardDocumentCheckIcon, ShieldCheckIcon, BeakerIcon,
} from '@heroicons/vue/24/outline'

const props = defineProps({
  pet: { type: Object, required: true },
  ctx: { type: Object, required: true },
})

defineEmits(['view', 'edit', 'history', 'delete'])

const themes = {
  Perro: {
    card: 'pet-premium--perro',
    header: 'from-orange-400 via-amber-400 to-yellow-300',
    badgeSpecie: 'pet-badge--amber',
  },
  Gato: {
    card: 'pet-premium--gato',
    header: 'from-violet-500 via-purple-500 to-blue-500',
    badgeSpecie: 'pet-badge--violet',
  },
  Ave: {
    card: 'pet-premium--ave',
    header: 'from-emerald-400 via-green-400 to-teal-400',
    badgeSpecie: 'pet-badge--emerald',
  },
  Conejo: {
    card: 'pet-premium--conejo',
    header: 'from-pink-400 via-rose-400 to-violet-400',
    badgeSpecie: 'pet-badge--pink',
  },
  Otro: {
    card: 'pet-premium--otro',
    header: 'from-blue-500 to-indigo-500',
    badgeSpecie: 'pet-badge--blue',
  },
}

const emojis = { Perro: '🐶', Gato: '🐱', Ave: '🐦', Conejo: '🐰', Otro: '🐾' }

const theme = computed(() => themes[props.pet.especie] ?? themes.Otro)
const emoji = computed(() => emojis[props.pet.especie] ?? emojis.Otro)
</script>

<style scoped>
.pet-premium {
  @apply relative bg-white rounded-3xl overflow-hidden
         border border-slate-100/80
         shadow-[0_2px_8px_rgba(0,0,0,0.04),0_8px_24px_rgba(0,0,0,0.06)]
         transition-all duration-300 ease-out;
}

.pet-premium:hover {
  @apply -translate-y-2
         shadow-[0_8px_30px_rgba(0,0,0,0.08),0_20px_50px_rgba(0,0,0,0.1)];
}

.pet-premium__header {
  @apply h-24 w-full;
}

.pet-premium__avatar-wrap {
  @apply absolute left-1/2 -translate-x-1/2 top-12 z-10;
}

.pet-premium__avatar {
  @apply w-[90px] h-[90px] rounded-full bg-white
         border-4 border-white
         shadow-[0_4px_20px_rgba(0,0,0,0.12)]
         flex items-center justify-center;
}

.pet-premium__body {
  @apply px-5 pb-5 pt-14;
}

.pet-badge {
  @apply inline-flex items-center gap-1 text-[10px] font-bold uppercase tracking-wide
         px-2.5 py-1 rounded-full;
}
.pet-badge--amber { @apply bg-amber-50 text-amber-700 ring-1 ring-amber-200; }
.pet-badge--violet { @apply bg-violet-50 text-violet-700 ring-1 ring-violet-200; }
.pet-badge--emerald { @apply bg-emerald-50 text-emerald-700 ring-1 ring-emerald-200; }
.pet-badge--pink { @apply bg-pink-50 text-pink-700 ring-1 ring-pink-200; }
.pet-badge--blue { @apply bg-blue-50 text-blue-700 ring-1 ring-blue-200; }
.pet-badge--green { @apply bg-emerald-50 text-emerald-700 ring-1 ring-emerald-200; }
.pet-badge--rose { @apply bg-rose-50 text-rose-600 ring-1 ring-rose-200; }

.pet-mini-stat {
  @apply bg-slate-50 rounded-xl px-3 py-2 border border-slate-100;
}
.pet-mini-stat__label {
  @apply block text-[10px] font-semibold text-slate-400 uppercase tracking-wide;
}
.pet-mini-stat__value {
  @apply block text-sm font-bold text-slate-800 mt-0.5 truncate;
}

.pet-metric {
  @apply flex flex-col items-center gap-0.5 py-2 rounded-xl bg-white border border-slate-100
         transition-colors duration-200 group-hover:border-slate-200;
}
.pet-metric__num {
  @apply text-base font-extrabold text-slate-900 tabular-nums;
}
.pet-metric__lbl {
  @apply text-[9px] font-semibold text-slate-400 uppercase;
}

.pet-action {
  @apply w-9 h-9 rounded-full flex items-center justify-center
         transition-all duration-200 hover:scale-110;
}
.pet-action--blue { @apply text-blue-500 bg-blue-50 hover:bg-blue-100 hover:text-blue-700; }
.pet-action--green { @apply text-emerald-500 bg-emerald-50 hover:bg-emerald-100 hover:text-emerald-700; }
.pet-action--violet { @apply text-violet-500 bg-violet-50 hover:bg-violet-100 hover:text-violet-700; }
.pet-action--red { @apply text-rose-500 bg-rose-50 hover:bg-rose-100 hover:text-rose-700; }
</style>
