<template>
  <div class="relative z-10 space-y-6">
    <!-- Encabezado -->
    <div>
      <h1 class="text-2xl sm:text-3xl font-extrabold text-slate-900 tracking-tight">Vacunas</h1>
      <p class="text-slate-500 mt-1.5 text-sm sm:text-base">Control de vacunación</p>
    </div>

    <!-- KPIs con gradiente -->
    <div class="grid grid-cols-2 lg:grid-cols-4 gap-4">
      <div
        v-for="kpi in kpis"
        :key="kpi.label"
        :class="['vac-kpi bg-gradient-to-br shadow-lg', kpi.gradient]"
      >
        <div>
          <p class="text-sm font-medium text-white/80">{{ kpi.label }}</p>
          <p class="text-2xl sm:text-3xl font-extrabold text-white mt-2 tabular-nums">{{ kpi.value }}</p>
        </div>
        <div class="w-11 h-11 sm:w-12 sm:h-12 rounded-xl bg-white/20 flex items-center justify-center shrink-0">
          <component :is="kpi.icon" class="w-5 h-5 sm:w-6 sm:h-6 text-white" />
        </div>
      </div>
    </div>

    <!-- Card principal -->
    <div class="bg-white rounded-2xl border border-slate-100 shadow-[0_1px_2px_rgba(0,0,0,0.04),0_4px_20px_rgba(0,0,0,0.05)] overflow-hidden">
      <!-- Toolbar -->
      <div class="flex flex-col lg:flex-row lg:items-center lg:justify-between gap-4 p-5 lg:p-6 border-b border-slate-100">
        <div>
          <h2 class="text-lg font-bold text-slate-900">Registro de Vacunas</h2>
          <p class="text-sm text-slate-500 mt-0.5">{{ store.vacunas.length }} registros de vacunación</p>
        </div>
        <div class="flex flex-col sm:flex-row gap-3 sm:items-center">
          <div class="relative">
            <MagnifyingGlassIcon class="w-5 h-5 absolute left-3.5 top-1/2 -translate-y-1/2 text-slate-400" />
            <input
              v-model="search"
              type="search"
              placeholder="Buscar vacuna..."
              class="w-full sm:w-64 pl-11 pr-4 py-2.5 text-sm bg-slate-50 border border-slate-200 rounded-xl focus:outline-none focus:ring-2 focus:ring-blue-500/25 focus:border-blue-400 transition"
            />
          </div>
          <button type="button" @click="openModal()" class="vac-btn-primary whitespace-nowrap">
            <PlusIcon class="w-4 h-4" /> Nueva Vacuna
          </button>
        </div>
      </div>

      <!-- Tabla desktop / tablet -->
      <div class="hidden md:block overflow-x-auto">
        <table class="w-full min-w-[900px]">
          <thead>
            <tr class="bg-slate-50/80 border-b border-slate-100">
              <th v-for="col in columns" :key="col" class="vac-th">{{ col }}</th>
            </tr>
          </thead>
          <tbody class="divide-y divide-slate-50">
            <tr
              v-for="vac in filtered"
              :key="vac.id"
              class="hover:bg-blue-50/30 transition-colors duration-150"
            >
              <td class="vac-td">
                <div class="flex items-center gap-3 min-w-[140px]">
                  <div :class="['w-10 h-10 rounded-full bg-gradient-to-br flex items-center justify-center text-lg shadow-sm shrink-0', petGradient(vac.mascotaId)]">
                    {{ petEmoji(vac.mascotaId) }}
                  </div>
                  <span class="font-semibold text-slate-900">{{ mascotaNombre(vac.mascotaId) }}</span>
                </div>
              </td>
              <td class="vac-td font-medium text-slate-800">{{ vac.tipoVacuna }}</td>
              <td class="vac-td text-slate-600">{{ vac.laboratorio }}</td>
              <td class="vac-td text-slate-600 whitespace-nowrap">{{ formatFecha(vac.fechaAplicacion) }}</td>
              <td class="vac-td text-slate-600 whitespace-nowrap">{{ formatFecha(vac.proximaDosis) }}</td>
              <td class="vac-td text-slate-700">{{ vetNombre(vac.veterinarioId) }}</td>
              <td class="vac-td">
                <span :class="estadoBadge(vac.estado)">{{ vac.estado }}</span>
              </td>
              <td class="vac-td">
                <div class="flex items-center justify-end gap-1">
                  <button type="button" title="Ver" class="vac-action vac-action--blue" @click="verVac(vac)">
                    <EyeIcon class="w-4 h-4" />
                  </button>
                  <button type="button" title="Editar" class="vac-action vac-action--green" @click="openModal(vac)">
                    <PencilSquareIcon class="w-4 h-4" />
                  </button>
                  <button type="button" title="Eliminar" class="vac-action vac-action--red" @click="confirmarEliminar(vac)">
                    <TrashIcon class="w-4 h-4" />
                  </button>
                </div>
              </td>
            </tr>
            <tr v-if="!filtered.length">
              <td colspan="8" class="text-center py-16 text-slate-400 text-sm">No se encontraron registros</td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- Mobile cards -->
      <div class="md:hidden p-4 space-y-3">
        <div
          v-for="vac in filtered"
          :key="vac.id"
          class="border border-slate-100 rounded-xl p-4 hover:shadow-md transition-shadow"
        >
          <div class="flex items-start gap-3">
            <div :class="['w-11 h-11 rounded-full bg-gradient-to-br flex items-center justify-center text-lg shrink-0', petGradient(vac.mascotaId)]">
              {{ petEmoji(vac.mascotaId) }}
            </div>
            <div class="flex-1 min-w-0">
              <div class="flex items-start justify-between gap-2">
                <div>
                  <p class="font-bold text-slate-900">{{ mascotaNombre(vac.mascotaId) }}</p>
                  <p class="text-sm text-slate-600 mt-0.5">{{ vac.tipoVacuna }}</p>
                </div>
                <span :class="estadoBadge(vac.estado)">{{ vac.estado }}</span>
              </div>
              <div class="grid grid-cols-2 gap-2 mt-3 text-sm text-slate-600">
                <div><span class="text-xs text-slate-400 block">Laboratorio</span>{{ vac.laboratorio }}</div>
                <div class="text-right"><span class="text-xs text-slate-400 block">Veterinario</span>{{ vetNombre(vac.veterinarioId) }}</div>
                <div><span class="text-xs text-slate-400 block">Aplicación</span>{{ formatFecha(vac.fechaAplicacion) }}</div>
                <div class="text-right"><span class="text-xs text-slate-400 block">Próxima dosis</span>{{ formatFecha(vac.proximaDosis) }}</div>
              </div>
              <div class="flex gap-1 mt-3 pt-3 border-t border-slate-100">
                <button type="button" class="vac-action vac-action--blue flex-1" @click="verVac(vac)"><EyeIcon class="w-4 h-4 mx-auto" /></button>
                <button type="button" class="vac-action vac-action--green flex-1" @click="openModal(vac)"><PencilSquareIcon class="w-4 h-4 mx-auto" /></button>
                <button type="button" class="vac-action vac-action--red flex-1" @click="confirmarEliminar(vac)"><TrashIcon class="w-4 h-4 mx-auto" /></button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Modal CRUD -->
    <CrudModal v-model="showModal" :title="editing ? 'Editar Vacuna' : 'Nueva Vacuna'" @confirm="save">
      <div class="space-y-4">
        <div>
          <label class="form-label">Mascota</label>
          <select v-model="form.mascotaId" class="form-input">
            <option v-for="m in store.mascotas" :key="m.id" :value="m.id">{{ m.nombre }} ({{ m.especie }})</option>
          </select>
        </div>
        <div class="grid grid-cols-2 gap-3">
          <div>
            <label class="form-label">Tipo de vacuna</label>
            <input v-model="form.tipoVacuna" class="form-input" placeholder="Rabia" />
          </div>
          <div>
            <label class="form-label">Laboratorio</label>
            <input v-model="form.laboratorio" class="form-input" placeholder="Pfizer" />
          </div>
        </div>
        <div class="grid grid-cols-2 gap-3">
          <div>
            <label class="form-label">Fecha aplicación</label>
            <input v-model="form.fechaAplicacion" type="date" class="form-input" />
          </div>
          <div>
            <label class="form-label">Próxima dosis</label>
            <input v-model="form.proximaDosis" type="date" class="form-input" />
          </div>
        </div>
        <div class="grid grid-cols-2 gap-3">
          <div>
            <label class="form-label">Veterinario</label>
            <select v-model="form.veterinarioId" class="form-input">
              <option v-for="v in store.veterinarios" :key="v.id" :value="v.id">{{ v.nombre }}</option>
            </select>
          </div>
          <div>
            <label class="form-label">Estado</label>
            <select v-model="form.estado" class="form-input">
              <option>Aplicada</option>
              <option>Pendiente</option>
              <option>Programada</option>
            </select>
          </div>
        </div>
      </div>
    </CrudModal>

    <!-- Modal ver -->
    <CrudModal v-model="showView" title="Detalle de Vacunación" hide-footer>
      <template v-if="viewing">
        <div class="flex items-center gap-4 mb-5 pb-4 border-b border-slate-100">
          <div :class="['w-14 h-14 rounded-full bg-gradient-to-br flex items-center justify-center text-2xl', petGradient(viewing.mascotaId)]">
            {{ petEmoji(viewing.mascotaId) }}
          </div>
          <div>
            <p class="font-bold text-slate-900 text-lg">{{ mascotaNombre(viewing.mascotaId) }}</p>
            <p class="text-sm text-slate-500">{{ viewing.tipoVacuna }}</p>
          </div>
          <span :class="[estadoBadge(viewing.estado), 'ml-auto']">{{ viewing.estado }}</span>
        </div>
        <dl class="grid grid-cols-2 gap-3 text-sm">
          <div><dt class="text-slate-400 text-xs">Laboratorio</dt><dd class="font-medium">{{ viewing.laboratorio }}</dd></div>
          <div><dt class="text-slate-400 text-xs">Veterinario</dt><dd class="font-medium">{{ vetNombre(viewing.veterinarioId) }}</dd></div>
          <div><dt class="text-slate-400 text-xs">Fecha aplicación</dt><dd class="font-medium">{{ formatFecha(viewing.fechaAplicacion) }}</dd></div>
          <div><dt class="text-slate-400 text-xs">Próxima dosis</dt><dd class="font-medium">{{ formatFecha(viewing.proximaDosis) }}</dd></div>
        </dl>
      </template>
      <template #footer>
        <button type="button" @click="editarDesdeVista" class="btn-primary flex-1 justify-center">Editar</button>
        <button type="button" @click="showView = false" class="btn-secondary flex-1 justify-center">Cerrar</button>
      </template>
    </CrudModal>
  </div>
</template>

<script setup>
import { ref, computed, markRaw } from 'vue'
import { useDataStore } from '@/stores/data'
import CrudModal from '@/components/ui/CrudModal.vue'
import {
  PlusIcon, MagnifyingGlassIcon, EyeIcon, PencilSquareIcon, TrashIcon,
  CheckBadgeIcon, ExclamationTriangleIcon, CalendarDaysIcon, BeakerIcon,
} from '@heroicons/vue/24/outline'

const store = useDataStore()
const search = ref('')
const showModal = ref(false)
const showView = ref(false)
const editing = ref(null)
const viewing = ref(null)

const columns = ['Mascota', 'Tipo Vacuna', 'Laboratorio', 'Fecha Aplicación', 'Próxima Dosis', 'Veterinario', 'Estado', 'Acciones']

const petEmojis = { Perro: '🐶', Gato: '🐱', Ave: '🐦', Conejo: '🐰', Otro: '🐾' }
const petGradients = {
  Perro: 'from-amber-400 to-orange-500',
  Gato: 'from-violet-400 to-purple-600',
  Ave: 'from-sky-400 to-blue-500',
  Conejo: 'from-rose-300 to-pink-500',
  Otro: 'from-slate-400 to-slate-600',
}

const blank = () => ({
  mascotaId: store.mascotas[0]?.id ?? '',
  tipoVacuna: '',
  laboratorio: '',
  fechaAplicacion: '',
  proximaDosis: '',
  veterinarioId: store.veterinarios[0]?.id ?? '',
  estado: 'Pendiente',
})
const form = ref(blank())

const filtered = computed(() => {
  if (!search.value) return store.vacunas
  const q = search.value.toLowerCase()
  return store.vacunas.filter(v =>
    [
      v.tipoVacuna, v.laboratorio, v.estado,
      mascotaNombre(v.mascotaId), vetNombre(v.veterinarioId),
    ].some(val => String(val ?? '').toLowerCase().includes(q))
  )
})

const kpis = computed(() => {
  const now = new Date()
  const mes = now.getMonth()
  const anio = now.getFullYear()
  const aplicadasMes = store.vacunas.filter(v => {
    if (v.estado !== 'Aplicada' || !v.fechaAplicacion) return false
    const d = new Date(v.fechaAplicacion + 'T12:00:00')
    return d.getMonth() === mes && d.getFullYear() === anio
  }).length
  const pendientes = store.vacunas.filter(v => v.estado === 'Pendiente').length
  const programadas = store.vacunas.filter(v => v.estado === 'Programada').length
  const totalTipos = new Set(store.vacunas.map(v => v.tipoVacuna)).size

  return [
    { label: 'Aplicadas Este Mes', value: aplicadasMes, icon: markRaw(CheckBadgeIcon), gradient: 'from-blue-600 to-indigo-600 shadow-blue-500/25' },
    { label: 'Pendientes', value: pendientes, icon: markRaw(ExclamationTriangleIcon), gradient: 'from-orange-500 to-amber-500 shadow-orange-500/25' },
    { label: 'Programadas', value: programadas, icon: markRaw(CalendarDaysIcon), gradient: 'from-emerald-500 to-teal-500 shadow-emerald-500/25' },
    { label: 'Total Tipos', value: totalTipos, icon: markRaw(BeakerIcon), gradient: 'from-violet-600 to-purple-600 shadow-violet-500/25' },
  ]
})

function mascotaNombre(id) {
  return store.mascotas.find(m => m.id === id)?.nombre ?? '—'
}

function vetNombre(id) {
  return store.veterinarios.find(v => v.id === id)?.nombre ?? '—'
}

function petEmoji(mascotaId) {
  const esp = store.mascotas.find(m => m.id === mascotaId)?.especie ?? 'Otro'
  return petEmojis[esp] ?? '🐾'
}

function petGradient(mascotaId) {
  const esp = store.mascotas.find(m => m.id === mascotaId)?.especie ?? 'Otro'
  return petGradients[esp] ?? petGradients.Otro
}

function estadoBadge(estado) {
  const map = {
    Aplicada: 'inline-flex text-[10px] font-bold uppercase px-2.5 py-1 rounded-full bg-emerald-50 text-emerald-700 ring-1 ring-emerald-200',
    Pendiente: 'inline-flex text-[10px] font-bold uppercase px-2.5 py-1 rounded-full bg-amber-50 text-amber-700 ring-1 ring-amber-200',
    Programada: 'inline-flex text-[10px] font-bold uppercase px-2.5 py-1 rounded-full bg-blue-50 text-blue-700 ring-1 ring-blue-200',
  }
  return map[estado] ?? map.Pendiente
}

function formatFecha(f) {
  if (!f) return '—'
  return new Date(f + 'T12:00:00').toLocaleDateString('es-CO', { day: 'numeric', month: 'short', year: 'numeric' })
}

function openModal(row = null) {
  editing.value = row
  form.value = row ? { ...row } : blank()
  showModal.value = true
}

function verVac(v) {
  viewing.value = { ...v }
  showView.value = true
}

function editarDesdeVista() {
  const v = viewing.value
  showView.value = false
  if (v) openModal(v)
}

function save() {
  if (!form.value.tipoVacuna?.trim()) { alert('El tipo de vacuna es obligatorio'); return }
  if (editing.value) store.updateItem(store.vacunas, editing.value.id, { ...form.value })
  else store.addItem(store.vacunas, { ...form.value })
  showModal.value = false
  editing.value = null
  form.value = blank()
}

function confirmarEliminar(row) {
  if (confirm(`¿Eliminar registro de ${row.tipoVacuna} para ${mascotaNombre(row.mascotaId)}?`)) {
    store.deleteItem(store.vacunas, row.id)
    if (viewing.value?.id === row.id) { viewing.value = null; showView.value = false }
  }
}
</script>

<style scoped>
.vac-kpi {
  @apply rounded-2xl p-5 min-h-[108px] flex items-start justify-between gap-3;
}
.vac-btn-primary {
  @apply inline-flex items-center justify-center gap-2 px-4 py-2.5 rounded-xl text-sm font-semibold text-white
         bg-gradient-to-r from-blue-600 to-indigo-600 shadow-lg shadow-blue-500/25
         hover:shadow-xl hover:-translate-y-0.5 transition-all duration-200;
}
.vac-th {
  @apply px-4 py-3.5 text-left text-xs font-bold text-slate-500 uppercase tracking-wider whitespace-nowrap;
}
.vac-td {
  @apply px-4 py-4 text-sm;
}
.vac-action {
  @apply p-2 rounded-lg transition-colors cursor-pointer;
}
.vac-action--blue {
  @apply text-blue-500 hover:bg-blue-50 hover:text-blue-700;
}
.vac-action--green {
  @apply text-emerald-500 hover:bg-emerald-50 hover:text-emerald-700;
}
.vac-action--red {
  @apply text-rose-500 hover:bg-rose-50 hover:text-rose-700;
}
</style>
