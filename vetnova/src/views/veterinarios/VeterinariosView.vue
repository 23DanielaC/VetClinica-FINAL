<template>
  <div class="relative z-10 space-y-6">
    <!-- Encabezado -->
    <div class="flex flex-col lg:flex-row lg:items-start lg:justify-between gap-4">
      <div>
        <div class="flex items-center gap-3 flex-wrap">
          <h1 class="text-2xl sm:text-3xl font-extrabold text-slate-900 tracking-tight">Veterinarios</h1>
          <span class="text-xs font-bold text-indigo-700 bg-indigo-50 px-3 py-1 rounded-full ring-1 ring-indigo-100">
            {{ store.veterinarios.length }} profesionales
          </span>
        </div>
        <p class="text-slate-500 mt-1.5 text-sm sm:text-base">Directorio del equipo médico veterinario</p>
      </div>
      <button type="button" @click="openModal()" class="vet-btn-primary shrink-0">
        <PlusIcon class="w-4 h-4" /> Nuevo Registro
      </button>
    </div>

    <!-- Filtros -->
    <div class="bg-white rounded-2xl border border-slate-100 shadow-[0_1px_2px_rgba(0,0,0,0.04),0_4px_16px_rgba(0,0,0,0.04)] p-4 lg:p-5">
      <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-3">
        <div class="sm:col-span-2 relative">
          <MagnifyingGlassIcon class="w-5 h-5 absolute left-3.5 top-1/2 -translate-y-1/2 text-slate-400" />
          <input
            v-model="filters.search"
            type="search"
            placeholder="Buscar por nombre, especialidad o matrícula..."
            class="w-full pl-11 pr-4 py-2.5 text-sm bg-slate-50 border border-slate-200 rounded-xl focus:outline-none focus:ring-2 focus:ring-indigo-500/25 focus:border-indigo-400 transition"
          />
        </div>
        <select v-model="filters.especialidad" class="vet-select">
          <option value="">Todas las especialidades</option>
          <option v-for="e in especialidades" :key="e" :value="e">{{ e }}</option>
        </select>
        <select v-model="filters.estado" class="vet-select">
          <option value="">Todos los estados</option>
          <option value="Activo">Activo</option>
          <option value="Inactivo">Inactivo</option>
        </select>
      </div>
      <div class="flex items-center justify-between mt-3">
        <p class="text-xs text-slate-400">{{ filtered.length }} de {{ store.veterinarios.length }} veterinarios</p>
        <button v-if="hasFilters" type="button" @click="limpiarFiltros" class="text-xs font-semibold text-indigo-600 hover:text-indigo-800">
          Limpiar filtros
        </button>
      </div>
    </div>

    <!-- Grid de tarjetas -->
    <div v-if="filtered.length" class="grid grid-cols-1 sm:grid-cols-2 xl:grid-cols-4 gap-5">
      <VeterinarioCard
        v-for="vet in filtered"
        :key="vet.id"
        :vet="vet"
        :iniciales="iniciales(vet.nombre)"
        :turnos="countTurnos(vet.id)"
        :consultas="countConsultas(vet.id)"
        @view="verVet"
        @edit="openModal"
        @delete="confirmarEliminar"
      />
    </div>

    <div v-else class="bg-white rounded-2xl border border-slate-100 py-20 text-center">
      <UserGroupIcon class="w-12 h-12 text-slate-300 mx-auto mb-3" />
      <p class="text-slate-500 font-medium">No se encontraron veterinarios</p>
      <button type="button" @click="limpiarFiltros" class="text-sm text-indigo-600 font-semibold mt-2 hover:underline">Limpiar filtros</button>
    </div>

    <!-- Modal crear/editar -->
    <CrudModal v-model="showModal" :title="editing ? 'Editar Veterinario' : 'Nuevo Veterinario'" @confirm="save">
      <div class="space-y-4">
        <div><label class="form-label">Nombre completo</label><input v-model="form.nombre" class="form-input" placeholder="Dr. Juan Pérez" /></div>
        <div class="grid grid-cols-2 gap-3">
          <div><label class="form-label">Especialidad</label>
            <select v-model="form.especialidad" class="form-input">
              <option v-for="e in especialidades" :key="e" :value="e">{{ e }}</option>
            </select>
          </div>
          <div><label class="form-label">Estado</label>
            <select v-model="form.estado" class="form-input">
              <option>Activo</option><option>Inactivo</option>
            </select>
          </div>
        </div>
        <div class="grid grid-cols-2 gap-3">
          <div><label class="form-label">Teléfono</label><input v-model="form.telefono" class="form-input" /></div>
          <div><label class="form-label">Matrícula</label><input v-model="form.matricula" class="form-input" /></div>
        </div>
        <div><label class="form-label">Email</label><input v-model="form.email" type="email" class="form-input" /></div>
      </div>
    </CrudModal>

    <!-- Modal ver -->
    <CrudModal v-model="showView" title="Perfil del Veterinario" hide-footer>
      <template v-if="viewing">
        <div class="flex items-center gap-4 mb-5">
          <div class="w-16 h-16 rounded-2xl bg-gradient-to-br from-indigo-600 to-violet-500 flex items-center justify-center text-white font-bold text-xl shadow-lg">
            {{ iniciales(viewing.nombre) }}
          </div>
          <div>
            <h3 class="text-lg font-bold text-slate-900">{{ viewing.nombre }}</h3>
            <p class="text-sm text-indigo-600 font-semibold">{{ viewing.especialidad }}</p>
            <p class="text-xs text-slate-400 mt-0.5">{{ viewing.matricula }}</p>
          </div>
        </div>
        <dl class="space-y-3 text-sm">
          <div><dt class="text-slate-400 text-xs">Teléfono</dt><dd class="font-medium text-slate-800">{{ viewing.telefono }}</dd></div>
          <div><dt class="text-slate-400 text-xs">Email</dt><dd class="font-medium text-slate-800">{{ viewing.email }}</dd></div>
          <div><dt class="text-slate-400 text-xs">Estado</dt><dd class="font-medium text-slate-800">{{ viewing.estado ?? 'Activo' }}</dd></div>
          <div class="grid grid-cols-2 gap-3 pt-2">
            <div class="bg-slate-50 rounded-xl p-3 text-center">
              <p class="text-xs text-slate-400">Turnos</p>
              <p class="text-xl font-bold text-slate-900">{{ countTurnos(viewing.id) }}</p>
            </div>
            <div class="bg-slate-50 rounded-xl p-3 text-center">
              <p class="text-xs text-slate-400">Consultas</p>
              <p class="text-xl font-bold text-slate-900">{{ countConsultas(viewing.id) }}</p>
            </div>
          </div>
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
import { ref, computed, reactive } from 'vue'
import { useDataStore } from '@/stores/data'
import CrudModal from '@/components/ui/CrudModal.vue'
import VeterinarioCard from '@/components/veterinarios/VeterinarioCard.vue'
import { PlusIcon, MagnifyingGlassIcon, UserGroupIcon } from '@heroicons/vue/24/outline'

const store = useDataStore()
const showModal = ref(false)
const showView = ref(false)
const editing = ref(null)
const viewing = ref(null)

const filters = reactive({ search: '', especialidad: '', estado: '' })

const especialidades = [
  'Medicina General', 'Cirugía', 'Dermatología', 'Cardiología', 'Oncología', 'Oftalmología',
]

const blank = () => ({
  nombre: '', especialidad: 'Medicina General', telefono: '', email: '', matricula: '', estado: 'Activo',
})
const form = ref(blank())

const hasFilters = computed(() => filters.search || filters.especialidad || filters.estado)

const filtered = computed(() => {
  let list = [...store.veterinarios]
  if (filters.search) {
    const q = filters.search.toLowerCase()
    list = list.filter(v => ['nombre', 'especialidad', 'matricula', 'email'].some(k => String(v[k] ?? '').toLowerCase().includes(q)))
  }
  if (filters.especialidad) list = list.filter(v => v.especialidad === filters.especialidad)
  if (filters.estado) list = list.filter(v => (v.estado ?? 'Activo') === filters.estado)
  return list
})

function iniciales(nombre) {
  return (nombre ?? '').replace(/^(Dr\.|Dra\.)\s*/i, '').split(' ').filter(Boolean).slice(0, 2).map(w => w[0]?.toUpperCase()).join('') || '?'
}

function countTurnos(vetId) { return store.turnos.filter(t => t.veterinarioId === vetId).length }
function countConsultas(vetId) {
  const turnoIds = store.turnos.filter(t => t.veterinarioId === vetId).map(t => t.id)
  return store.consultas.filter(c => turnoIds.includes(c.turnoId)).length
}

function limpiarFiltros() { Object.assign(filters, { search: '', especialidad: '', estado: '' }) }

function openModal(row = null) {
  editing.value = row
  form.value = row ? { ...row, estado: row.estado ?? 'Activo' } : blank()
  showModal.value = true
}

function verVet(v) { viewing.value = { ...v }; showView.value = true }

function editarDesdeVista() {
  const v = viewing.value
  showView.value = false
  if (v) openModal(v)
}

function save() {
  if (!form.value.nombre?.trim()) { alert('El nombre es obligatorio'); return }
  if (editing.value) store.updateItem(store.veterinarios, editing.value.id, { ...form.value })
  else store.addItem(store.veterinarios, { ...form.value })
  showModal.value = false
  editing.value = null
  form.value = blank()
}

function confirmarEliminar(row) {
  if (confirm(`¿Eliminar a ${row.nombre}?`)) {
    store.deleteItem(store.veterinarios, row.id)
    if (viewing.value?.id === row.id) { viewing.value = null; showView.value = false }
  }
}
</script>

<style scoped>
.vet-btn-primary {
  @apply inline-flex items-center gap-2 px-4 py-2.5 rounded-xl text-sm font-semibold text-white
         bg-gradient-to-r from-indigo-600 to-violet-600 shadow-lg shadow-indigo-500/25
         hover:shadow-xl hover:-translate-y-0.5 transition-all duration-200;
}
.vet-select {
  @apply w-full px-3 py-2.5 text-sm bg-slate-50 border border-slate-200 rounded-xl
         focus:outline-none focus:ring-2 focus:ring-indigo-500/25 focus:border-indigo-400 transition;
}
</style>
