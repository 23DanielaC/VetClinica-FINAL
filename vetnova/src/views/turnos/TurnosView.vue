<template>
  <div class="relative z-10 space-y-6">
    <!-- Encabezado -->
    <div class="flex flex-col lg:flex-row lg:items-end lg:justify-between gap-4">
      <div>
        <h1 class="text-2xl sm:text-3xl font-extrabold text-slate-900 tracking-tight">Turnos</h1>
        <p class="text-slate-500 mt-1.5 text-sm sm:text-base">Gestión de citas y turnos</p>
      </div>
      <button type="button" @click="openModal()" class="turno-btn-primary shrink-0">
        <PlusIcon class="w-4 h-4" /> Nuevo Turno
      </button>
    </div>

    <!-- Filtros -->
    <div class="bg-white rounded-2xl border border-slate-100 shadow-[0_1px_2px_rgba(0,0,0,0.04),0_4px_16px_rgba(0,0,0,0.04)] p-4 lg:p-5">
      <div class="grid grid-cols-1 sm:grid-cols-3 gap-3">
        <div class="relative sm:col-span-2">
          <MagnifyingGlassIcon class="w-5 h-5 absolute left-3.5 top-1/2 -translate-y-1/2 text-slate-400" />
          <input
            v-model="search"
            type="search"
            placeholder="Buscar por mascota, propietario, motivo..."
            class="w-full pl-11 pr-4 py-2.5 text-sm bg-slate-50 border border-slate-200 rounded-xl focus:outline-none focus:ring-2 focus:ring-blue-500/25 focus:border-blue-400 transition"
          />
        </div>
        <input
          v-model="fechaFiltro"
          type="date"
          class="turno-select"
        />
      </div>
    </div>

    <!-- KPIs -->
    <div class="grid grid-cols-2 lg:grid-cols-4 gap-4">
      <div v-for="kpi in kpis" :key="kpi.label" class="turno-kpi">
        <div :class="['w-11 h-11 rounded-xl flex items-center justify-center bg-gradient-to-br shadow-md', kpi.gradient]">
          <component :is="kpi.icon" class="w-5 h-5 text-white" />
        </div>
        <div class="mt-3">
          <p class="text-2xl font-extrabold text-slate-900 tabular-nums">{{ kpi.value }}</p>
          <p class="text-sm font-semibold text-slate-700 mt-0.5">{{ kpi.label }}</p>
          <p class="text-xs text-slate-400">{{ kpi.desc }}</p>
        </div>
      </div>
    </div>

    <!-- Agenda -->
    <div>
      <div class="flex items-center justify-between mb-4">
        <h2 class="text-lg font-bold text-slate-900 capitalize">{{ tituloAgenda }}</h2>
        <span class="text-xs font-semibold text-slate-500 bg-white px-3 py-1 rounded-full border border-slate-200">
          {{ filtered.length }} turnos
        </span>
      </div>

      <div v-if="filtered.length" class="space-y-3">
        <TurnoAgendaCard
          v-for="t in filtered"
          :key="t.id"
          :turno="t"
          :mascota-nombre="mascotaNombre(t.mascotaId)"
          :cliente-nombre="clienteNombre(t.clienteId)"
          :cliente-telefono="clienteTelefono(t.clienteId)"
          :vet-nombre="vetNombre(t.veterinarioId)"
          :emoji="petEmoji(t.mascotaId)"
          :avatar-gradient="petGradient(t.mascotaId)"
          @view="verTurno"
          @edit="openModal"
          @reschedule="reagendar"
          @cancel="cancelarTurno"
        />
      </div>

      <div v-else class="bg-white rounded-2xl border border-slate-100 py-16 text-center">
        <CalendarDaysIcon class="w-12 h-12 text-slate-300 mx-auto mb-3" />
        <p class="text-slate-500 font-medium">No hay turnos para esta fecha</p>
        <button type="button" @click="openModal()" class="text-sm text-blue-600 font-semibold mt-2 hover:underline">Agendar turno</button>
      </div>
    </div>

    <!-- Modal crear/editar -->
    <CrudModal v-model="showModal" :title="editing ? 'Editar Turno' : 'Nuevo Turno'" @confirm="save">
      <div class="space-y-4">
        <div><label class="form-label">Cliente</label>
          <select v-model="form.clienteId" class="form-input" @change="onClienteChange">
            <option v-for="c in store.clientes" :key="c.id" :value="c.id">{{ c.nombre }}</option>
          </select>
        </div>
        <div><label class="form-label">Mascota</label>
          <select v-model="form.mascotaId" class="form-input">
            <option v-for="m in mascotasCliente" :key="m.id" :value="m.id">{{ m.nombre }}</option>
          </select>
        </div>
        <div><label class="form-label">Veterinario</label>
          <select v-model="form.veterinarioId" class="form-input">
            <option v-for="v in store.veterinarios" :key="v.id" :value="v.id">{{ v.nombre }}</option>
          </select>
        </div>
        <div class="grid grid-cols-2 gap-3">
          <div><label class="form-label">Fecha</label><input v-model="form.fecha" type="date" class="form-input" /></div>
          <div><label class="form-label">Hora</label><input v-model="form.hora" type="time" class="form-input" /></div>
        </div>
        <div class="grid grid-cols-2 gap-3">
          <div><label class="form-label">Duración (min)</label><input v-model.number="form.duracion" type="number" min="15" step="15" class="form-input" /></div>
          <div><label class="form-label">Estado</label>
            <select v-model="form.estado" class="form-input">
              <option>Pendiente</option><option>Confirmado</option><option>En curso</option><option>Completado</option><option>Cancelado</option>
            </select>
          </div>
        </div>
        <div><label class="form-label">Motivo / Tipo de consulta</label><input v-model="form.motivo" class="form-input" placeholder="Control anual, vacunación..." /></div>
      </div>
    </CrudModal>

    <!-- Modal ver -->
    <CrudModal v-model="showView" title="Detalle del Turno" hide-footer>
      <template v-if="viewing">
        <div :class="['rounded-xl p-4 mb-4 border', estadoCardClass(viewing.estado)]">
          <div class="flex items-center gap-3">
            <div :class="['w-12 h-12 rounded-xl bg-gradient-to-br flex items-center justify-center text-xl', petGradient(viewing.mascotaId)]">
              {{ petEmoji(viewing.mascotaId) }}
            </div>
            <div>
              <p class="font-bold text-slate-900">{{ mascotaNombre(viewing.mascotaId) }}</p>
              <p class="text-sm text-slate-600">{{ viewing.motivo }}</p>
              <span class="text-xs font-bold uppercase mt-1 inline-block">{{ viewing.estado }}</span>
            </div>
          </div>
        </div>
        <dl class="grid grid-cols-2 gap-3 text-sm">
          <div><dt class="text-slate-400 text-xs">Fecha</dt><dd class="font-medium">{{ viewing.fecha }}</dd></div>
          <div><dt class="text-slate-400 text-xs">Hora</dt><dd class="font-medium">{{ viewing.hora }}</dd></div>
          <div><dt class="text-slate-400 text-xs">Duración</dt><dd class="font-medium">{{ viewing.duracion ?? 30 }} min</dd></div>
          <div><dt class="text-slate-400 text-xs">Veterinario</dt><dd class="font-medium">{{ vetNombre(viewing.veterinarioId) }}</dd></div>
          <div class="col-span-2"><dt class="text-slate-400 text-xs">Propietario</dt><dd class="font-medium">{{ clienteNombre(viewing.clienteId) }} · {{ clienteTelefono(viewing.clienteId) }}</dd></div>
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
import TurnoAgendaCard from '@/components/turnos/TurnoAgendaCard.vue'
import {
  PlusIcon, MagnifyingGlassIcon, CalendarDaysIcon,
  ClockIcon, CheckCircleIcon, PauseCircleIcon, CheckBadgeIcon,
} from '@heroicons/vue/24/outline'

const store = useDataStore()
const showModal = ref(false)
const showView = ref(false)
const editing = ref(null)
const viewing = ref(null)
const search = ref('')
const fechaFiltro = ref(new Date().toISOString().slice(0, 10))

const petEmojis = { Perro: '🐶', Gato: '🐱', Ave: '🐦', Conejo: '🐰', Otro: '🐾' }
const petGradients = {
  Perro: 'from-amber-400 to-orange-500', Gato: 'from-violet-400 to-purple-600',
  Ave: 'from-sky-400 to-blue-500', Conejo: 'from-rose-300 to-pink-500', Otro: 'from-slate-400 to-slate-600',
}

const blank = () => ({
  clienteId: store.clientes[0]?.id ?? '',
  mascotaId: store.mascotas.find(m => m.clienteId === store.clientes[0]?.id)?.id ?? store.mascotas[0]?.id ?? '',
  veterinarioId: store.veterinarios[0]?.id ?? '',
  fecha: fechaFiltro.value,
  hora: '09:00',
  duracion: 30,
  motivo: '',
  estado: 'Pendiente',
})
const form = ref(blank())

const mascotasCliente = computed(() =>
  store.mascotas.filter(m => m.clienteId === form.value.clienteId)
)

const tituloAgenda = computed(() => {
  const d = new Date(fechaFiltro.value + 'T12:00:00')
  return d.toLocaleDateString('es-CO', { weekday: 'long', day: 'numeric', month: 'long' })
})

const filtered = computed(() => {
  let list = store.turnos.filter(t => t.fecha === fechaFiltro.value)
  if (search.value) {
    const q = search.value.toLowerCase()
    list = list.filter(t =>
      [t.motivo, t.estado, clienteNombre(t.clienteId), mascotaNombre(t.mascotaId), vetNombre(t.veterinarioId)]
        .some(v => String(v).toLowerCase().includes(q))
    )
  }
  return [...list].sort((a, b) => a.hora.localeCompare(b.hora))
})

const kpis = computed(() => {
  const delDia = store.turnos.filter(t => t.fecha === fechaFiltro.value)
  return [
    { label: 'Pendientes', value: delDia.filter(t => t.estado === 'Pendiente').length, desc: 'Por confirmar', icon: markRaw(ClockIcon), gradient: 'from-amber-500 to-orange-400' },
    { label: 'Confirmados', value: delDia.filter(t => t.estado === 'Confirmado').length, desc: 'Listos para atender', icon: markRaw(CheckCircleIcon), gradient: 'from-emerald-500 to-teal-400' },
    { label: 'En Espera', value: delDia.filter(t => t.estado === 'En curso').length, desc: 'En consultorio', icon: markRaw(PauseCircleIcon), gradient: 'from-blue-500 to-indigo-500' },
    { label: 'Completados', value: delDia.filter(t => t.estado === 'Completado').length, desc: 'Finalizados hoy', icon: markRaw(CheckBadgeIcon), gradient: 'from-slate-500 to-slate-600' },
  ]
})

function clienteNombre(id) { return store.clientes.find(c => c.id === id)?.nombre ?? '—' }
function clienteTelefono(id) { return store.clientes.find(c => c.id === id)?.telefono ?? '—' }
function mascotaNombre(id) { return store.mascotas.find(m => m.id === id)?.nombre ?? '—' }
function vetNombre(id) { return store.veterinarios.find(v => v.id === id)?.nombre ?? '—' }
function petEmoji(mascotaId) {
  const esp = store.mascotas.find(m => m.id === mascotaId)?.especie ?? 'Otro'
  return petEmojis[esp] ?? '🐾'
}
function petGradient(mascotaId) {
  const esp = store.mascotas.find(m => m.id === mascotaId)?.especie ?? 'Otro'
  return petGradients[esp] ?? petGradients.Otro
}
function estadoCardClass(estado) {
  const m = {
    Pendiente: 'bg-amber-50 border-amber-200',
    Confirmado: 'bg-emerald-50 border-emerald-200',
    'En curso': 'bg-blue-50 border-blue-200',
    Completado: 'bg-slate-50 border-slate-200',
    Cancelado: 'bg-rose-50 border-rose-200',
  }
  return m[estado] ?? m.Pendiente
}

function onClienteChange() {
  const first = store.mascotas.find(m => m.clienteId === form.value.clienteId)
  if (first) form.value.mascotaId = first.id
}

function openModal(row = null) {
  editing.value = row
  form.value = row ? { ...row, duracion: row.duracion ?? 30 } : blank()
  showModal.value = true
}

function verTurno(t) { viewing.value = { ...t }; showView.value = true }

function editarDesdeVista() {
  const t = viewing.value
  showView.value = false
  if (t) openModal(t)
}

function reagendar(t) {
  openModal(t)
}

function cancelarTurno(t) {
  if (confirm(`¿Cancelar el turno de ${mascotaNombre(t.mascotaId)} a las ${t.hora}?`)) {
    store.updateItem(store.turnos, t.id, { ...t, estado: 'Cancelado' })
  }
}

function save() {
  if (!form.value.motivo?.trim()) { alert('El motivo es obligatorio'); return }
  if (editing.value) store.updateItem(store.turnos, editing.value.id, { ...form.value })
  else store.addItem(store.turnos, { ...form.value })
  showModal.value = false
  editing.value = null
  form.value = blank()
}
</script>

<style scoped>
.turno-btn-primary {
  @apply inline-flex items-center gap-2 px-4 py-2.5 rounded-xl text-sm font-semibold text-white
         bg-gradient-to-r from-blue-600 to-indigo-600 shadow-lg shadow-blue-500/25
         hover:shadow-xl hover:-translate-y-0.5 transition-all duration-200;
}
.turno-select {
  @apply w-full px-3 py-2.5 text-sm bg-slate-50 border border-slate-200 rounded-xl
         focus:outline-none focus:ring-2 focus:ring-blue-500/25 focus:border-blue-400 transition;
}
.turno-kpi {
  @apply bg-white rounded-2xl border border-slate-100 p-5
         shadow-[0_1px_2px_rgba(0,0,0,0.04),0_4px_16px_rgba(0,0,0,0.05)]
         hover:shadow-[0_8px_24px_rgba(0,0,0,0.08)] transition-shadow duration-300;
}
</style>
