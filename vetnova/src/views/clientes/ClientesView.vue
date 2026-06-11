<template>
  <div class="relative z-10 space-y-6">
    <!-- Encabezado -->
    <div class="flex flex-col lg:flex-row lg:items-start lg:justify-between gap-4">
      <div>
        <div class="flex items-center gap-3 flex-wrap">
          <h1 class="text-2xl sm:text-3xl font-extrabold text-slate-900 tracking-tight">Clientes</h1>
          <span class="inline-flex items-center text-xs font-bold text-indigo-700 bg-indigo-50 px-3 py-1 rounded-full ring-1 ring-indigo-100">
            {{ store.clientes.length }} registrados
          </span>
        </div>
        <p class="text-slate-500 mt-1.5 text-sm sm:text-base">Gestión de clientes registrados</p>
      </div>
      <div class="flex flex-wrap gap-2.5 shrink-0">
        <button type="button" @click="exportarCsv" class="cli-btn-secondary">
          <ArrowDownTrayIcon class="w-4 h-4" /> Exportar
        </button>
        <button type="button" @click="openModal()" class="cli-btn-primary">
          <PlusIcon class="w-4 h-4" /> Nuevo Cliente
        </button>
      </div>
    </div>

    <!-- KPIs -->
    <div class="grid grid-cols-1 sm:grid-cols-2 xl:grid-cols-4 gap-4">
      <div v-for="kpi in kpis" :key="kpi.label" class="cli-kpi group">
        <div :class="['absolute inset-0 rounded-2xl bg-gradient-to-br opacity-[0.08] group-hover:opacity-[0.14] transition-opacity pointer-events-none', kpi.gradient]" />
        <div class="relative flex items-start justify-between gap-3">
          <div>
            <p class="text-sm font-medium text-slate-500">{{ kpi.label }}</p>
            <p class="text-3xl font-extrabold text-slate-900 mt-1 tabular-nums">{{ kpi.value }}</p>
            <span :class="['inline-flex items-center gap-1 text-xs font-bold mt-3 px-2 py-0.5 rounded-full', kpi.badgeClass]">
              <ArrowTrendingUpIcon class="w-3 h-3" /> {{ kpi.growth }}
            </span>
          </div>
          <div :class="['w-12 h-12 rounded-2xl flex items-center justify-center bg-gradient-to-br shadow-lg group-hover:scale-110 transition-transform', kpi.gradient]">
            <component :is="kpi.icon" class="w-6 h-6 text-white" />
          </div>
        </div>
      </div>
    </div>

    <!-- Layout principal -->
    <div class="grid grid-cols-1 xl:grid-cols-12 gap-6">
      <div class="xl:col-span-9 space-y-5">
        <!-- Filtros -->
        <div class="bg-white rounded-2xl border border-slate-100 shadow-[0_1px_2px_rgba(0,0,0,0.04),0_4px_16px_rgba(0,0,0,0.04)] p-4 lg:p-5">
          <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-3">
            <div class="lg:col-span-2 relative">
              <MagnifyingGlassIcon class="w-5 h-5 absolute left-3.5 top-1/2 -translate-y-1/2 text-slate-400" />
              <input v-model="filters.search" type="search" placeholder="Buscar por nombre, email o teléfono..."
                class="w-full pl-11 pr-4 py-2.5 text-sm bg-slate-50 border border-slate-200 rounded-xl focus:outline-none focus:ring-2 focus:ring-indigo-500/25 focus:border-indigo-400 transition" />
            </div>
            <select v-model="filters.estado" class="cli-select">
              <option value="">Todos los estados</option>
              <option value="Activo">Activo</option>
              <option value="Inactivo">Inactivo</option>
              <option value="Pendiente">Pendiente</option>
            </select>
            <select v-model="filters.mes" class="cli-select">
              <option value="">Todas las fechas</option>
              <option v-for="m in mesesDisponibles" :key="m.value" :value="m.value">{{ m.label }}</option>
            </select>
            <select v-model="filters.mascotas" class="cli-select md:col-span-2 lg:col-span-1">
              <option value="">Cualquier cantidad</option>
              <option value="0">Sin mascotas</option>
              <option value="1">1 mascota</option>
              <option value="2+">2 o más mascotas</option>
            </select>
            <button type="button" @click="limpiarFiltros" class="cli-btn-secondary md:col-span-2 lg:col-span-1 justify-center">
              <XMarkIcon class="w-4 h-4" /> Limpiar filtros
            </button>
          </div>
          <p class="text-xs text-slate-400 mt-3">{{ filtered.length }} de {{ store.clientes.length }} clientes</p>
        </div>

        <!-- Tabla desktop -->
        <div class="hidden md:block bg-white rounded-2xl border border-slate-100 shadow-[0_1px_2px_rgba(0,0,0,0.04),0_4px_20px_rgba(0,0,0,0.05)]">
          <div class="overflow-x-auto">
            <table class="w-full">
              <thead>
                <tr class="border-b border-slate-100 bg-slate-50/80">
                  <th class="text-left text-xs font-bold text-slate-500 uppercase tracking-wider px-6 py-4">Cliente</th>
                  <th class="text-left text-xs font-bold text-slate-500 uppercase tracking-wider px-4 py-4">Estado</th>
                  <th class="text-left text-xs font-bold text-slate-500 uppercase tracking-wider px-4 py-4">Mascotas</th>
                  <th class="text-left text-xs font-bold text-slate-500 uppercase tracking-wider px-4 py-4">Dirección</th>
                  <th class="text-left text-xs font-bold text-slate-500 uppercase tracking-wider px-4 py-4">Registro</th>
                  <th class="text-right text-xs font-bold text-slate-500 uppercase tracking-wider px-6 py-4">Acciones</th>
                </tr>
              </thead>
              <tbody class="divide-y divide-slate-50">
                <tr
                  v-for="cliente in filtered"
                  :key="cliente.id"
                  class="group hover:bg-indigo-50/30 transition-colors duration-150"
                >
                  <td class="px-6 py-5">
                    <div class="flex items-center gap-4 min-w-[240px]">
                      <div class="w-12 h-12 rounded-full bg-gradient-to-br from-indigo-600 to-violet-500 flex items-center justify-center text-white font-bold text-sm shadow-md ring-2 ring-white shrink-0">
                        {{ iniciales(cliente.nombre) }}
                      </div>
                      <div class="min-w-0">
                        <p class="text-sm font-bold text-slate-900 truncate">{{ cliente.nombre }}</p>
                        <p class="text-xs text-slate-500 truncate mt-0.5">{{ cliente.email }}</p>
                        <p class="text-xs text-slate-400 truncate">{{ cliente.telefono }}</p>
                      </div>
                    </div>
                  </td>
                  <td class="px-4 py-5">
                    <span :class="estadoBadge(cliente.estado ?? 'Activo')">{{ cliente.estado ?? 'Activo' }}</span>
                  </td>
                  <td class="px-4 py-5">
                    <span class="inline-flex items-center gap-1.5 text-sm font-semibold text-slate-700">
                      <HeartIcon class="w-4 h-4 text-pink-400" />
                      {{ countMascotas(cliente.id) }}
                    </span>
                  </td>
                  <td class="px-4 py-5 text-sm text-slate-600 max-w-[180px] truncate">{{ cliente.direccion }}</td>
                  <td class="px-4 py-5 text-sm text-slate-500 whitespace-nowrap">{{ formatFecha(cliente.fechaRegistro) }}</td>
                  <td class="px-6 py-5 text-right">
                    <div class="flex items-center justify-end gap-1">
                      <button type="button" title="Ver" class="cli-action" @click="verCliente(cliente)">
                        <EyeIcon class="w-4 h-4" />
                      </button>
                      <button type="button" title="Editar" class="cli-action" @click="openModal(cliente)">
                        <PencilSquareIcon class="w-4 h-4" />
                      </button>
                      <button type="button" title="Historial" class="cli-action hidden sm:inline-flex" @click="verHistorial(cliente)">
                        <ClipboardDocumentListIcon class="w-4 h-4" />
                      </button>
                      <button type="button" title="Eliminar" class="cli-action cli-action--danger" @click="confirmarEliminar(cliente)">
                        <TrashIcon class="w-4 h-4" />
                      </button>
                    </div>
                  </td>
                </tr>
                <tr v-if="!filtered.length">
                  <td colspan="6" class="text-center py-16 text-slate-400 text-sm">No se encontraron clientes</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>

        <!-- Tarjetas mobile -->
        <div class="md:hidden space-y-3">
          <div
            v-for="cliente in filtered"
            :key="cliente.id"
            class="bg-white rounded-2xl border border-slate-100 shadow-sm p-4 hover:shadow-md transition-shadow"
          >
            <div class="flex items-start gap-3">
              <div class="w-12 h-12 rounded-full bg-gradient-to-br from-indigo-600 to-violet-500 flex items-center justify-center text-white font-bold text-sm shrink-0">
                {{ iniciales(cliente.nombre) }}
              </div>
              <div class="flex-1 min-w-0">
                <div class="flex items-start justify-between gap-2">
                  <p class="text-sm font-bold text-slate-900">{{ cliente.nombre }}</p>
                </div>
                <p class="text-xs text-slate-500 mt-0.5">{{ cliente.email }}</p>
                <p class="text-xs text-slate-400">{{ cliente.telefono }}</p>
                <div class="flex flex-wrap items-center gap-2 mt-3">
                  <span :class="estadoBadge(cliente.estado ?? 'Activo')">{{ cliente.estado ?? 'Activo' }}</span>
                  <span class="text-xs text-slate-500">{{ countMascotas(cliente.id) }} mascotas</span>
                </div>
                <div class="mt-3 pt-3 border-t border-slate-100 flex justify-end gap-1">
                  <button type="button" title="Ver" class="cli-action" @click="verCliente(cliente)">
                    <EyeIcon class="w-4 h-4" />
                  </button>
                  <button type="button" title="Editar" class="cli-action" @click="openModal(cliente)">
                    <PencilSquareIcon class="w-4 h-4" />
                  </button>
                  <button type="button" title="Eliminar" class="cli-action cli-action--danger" @click="confirmarEliminar(cliente)">
                    <TrashIcon class="w-4 h-4" />
                  </button>
                </div>
              </div>
            </div>
          </div>
          <p v-if="!filtered.length" class="text-center py-12 text-slate-400 text-sm">No se encontraron clientes</p>
        </div>
      </div>

      <!-- Panel estadísticas -->
      <div class="xl:col-span-3 space-y-4">
        <div class="bg-white rounded-2xl border border-slate-100 shadow-[0_1px_2px_rgba(0,0,0,0.04),0_4px_20px_rgba(0,0,0,0.05)] p-5 sticky top-4">
          <h3 class="text-base font-bold text-slate-900 mb-4">Estadísticas</h3>
          <div class="space-y-4">
            <div class="p-4 rounded-xl bg-gradient-to-br from-indigo-50 to-violet-50 border border-indigo-100/80">
              <p class="text-xs font-semibold text-indigo-600 uppercase tracking-wide">Nuevos este mes</p>
              <p class="text-2xl font-extrabold text-slate-900 mt-1">{{ stats.nuevosMes }}</p>
            </div>
            <div>
              <p class="text-xs font-semibold text-slate-400 uppercase tracking-wide mb-2">Clientes más activos</p>
              <div class="space-y-2">
                <div v-for="c in stats.masActivos" :key="c.id" class="flex items-center gap-3 p-2 rounded-xl hover:bg-slate-50 transition-colors">
                  <div class="w-8 h-8 rounded-full bg-gradient-to-br from-indigo-500 to-violet-500 flex items-center justify-center text-white text-xs font-bold">
                    {{ iniciales(c.nombre) }}
                  </div>
                  <div class="flex-1 min-w-0">
                    <p class="text-sm font-semibold text-slate-800 truncate">{{ c.nombre }}</p>
                    <p class="text-xs text-slate-400">{{ c.mascotas }} mascotas</p>
                  </div>
                </div>
              </div>
            </div>
            <div class="pt-3 border-t border-slate-100">
              <p class="text-xs font-semibold text-slate-400 uppercase tracking-wide">Último registrado</p>
              <p class="text-sm font-bold text-slate-800 mt-1">{{ stats.ultimo?.nombre ?? '—' }}</p>
              <p class="text-xs text-slate-400">{{ stats.ultimo ? formatFecha(stats.ultimo.fechaRegistro) : '' }}</p>
            </div>
            <div class="pt-3 border-t border-slate-100">
              <p class="text-xs font-semibold text-slate-400 uppercase tracking-wide">Promedio mascotas</p>
              <p class="text-2xl font-extrabold text-slate-900 mt-1">{{ stats.promedioMascotas }}</p>
              <p class="text-xs text-slate-400">por cliente</p>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Modal CRUD -->
    <CrudModal v-model="showModal" :title="editing ? 'Editar Cliente' : 'Nuevo Cliente'" @confirm="save">
      <div class="space-y-4">
        <div><label class="form-label">Nombre completo</label><input v-model="form.nombre" class="form-input" /></div>
        <div class="grid grid-cols-2 gap-3">
          <div><label class="form-label">Teléfono</label><input v-model="form.telefono" class="form-input" /></div>
          <div><label class="form-label">Email</label><input v-model="form.email" type="email" class="form-input" /></div>
        </div>
        <div><label class="form-label">Dirección</label><input v-model="form.direccion" class="form-input" /></div>
        <div class="grid grid-cols-2 gap-3">
          <div><label class="form-label">Estado</label>
            <select v-model="form.estado" class="form-input">
              <option>Activo</option><option>Inactivo</option><option>Pendiente</option>
            </select>
          </div>
          <div><label class="form-label">Fecha de Registro</label><input v-model="form.fechaRegistro" type="date" class="form-input" /></div>
        </div>
      </div>
    </CrudModal>

    <!-- Modal ver cliente -->
    <CrudModal v-model="showView" title="Detalle del Cliente" hide-footer>
      <template v-if="viewing">
        <div class="flex items-center gap-4 mb-5">
          <div class="w-14 h-14 rounded-full bg-gradient-to-br from-indigo-600 to-violet-500 flex items-center justify-center text-white font-bold text-lg">
            {{ iniciales(viewing.nombre) }}
          </div>
          <div>
            <h3 class="text-lg font-bold text-slate-900">{{ viewing.nombre }}</h3>
            <span :class="estadoBadge(viewing.estado ?? 'Activo')">{{ viewing.estado ?? 'Activo' }}</span>
          </div>
        </div>
        <dl class="space-y-3 text-sm">
          <div><dt class="text-slate-400 text-xs">Email</dt><dd class="font-medium text-slate-800">{{ viewing.email }}</dd></div>
          <div><dt class="text-slate-400 text-xs">Teléfono</dt><dd class="font-medium text-slate-800">{{ viewing.telefono }}</dd></div>
          <div><dt class="text-slate-400 text-xs">Dirección</dt><dd class="font-medium text-slate-800">{{ viewing.direccion }}</dd></div>
          <div><dt class="text-slate-400 text-xs">Mascotas</dt><dd class="font-medium text-slate-800">{{ countMascotas(viewing.id) }}</dd></div>
        </dl>
      </template>
      <template #footer>
        <button type="button" @click="editarDesdeVista" class="btn-primary flex-1 justify-center">Editar</button>
        <button type="button" @click="showView = false" class="btn-secondary flex-1 justify-center">Cerrar</button>
      </template>
    </CrudModal>

    <!-- Modal historial -->
    <CrudModal v-model="showHistorial" :title="historialCliente ? `Historial — ${historialCliente.nombre}` : 'Historial'" hide-footer cancel-label="Cerrar">
      <template v-if="historialCliente">
        <div class="space-y-4">
          <div>
            <p class="text-xs font-bold text-slate-400 uppercase mb-2">Mascotas</p>
            <div v-if="mascotasDe(historialCliente.id).length" class="space-y-2">
              <div v-for="m in mascotasDe(historialCliente.id)" :key="m.id" class="flex items-center gap-2 p-2 bg-slate-50 rounded-xl text-sm">
                <span>{{ m.nombre }}</span>
                <span class="text-slate-400">· {{ m.especie }}</span>
              </div>
            </div>
            <p v-else class="text-sm text-slate-400">Sin mascotas registradas</p>
          </div>
          <div>
            <p class="text-xs font-bold text-slate-400 uppercase mb-2">Turnos</p>
            <div v-if="turnosDe(historialCliente.id).length" class="space-y-2">
              <div v-for="t in turnosDe(historialCliente.id)" :key="t.id" class="p-2 bg-slate-50 rounded-xl text-sm">
                <p class="font-medium">{{ t.motivo }}</p>
                <p class="text-xs text-slate-400">{{ t.fecha }} · {{ t.hora }} · {{ t.estado }}</p>
              </div>
            </div>
            <p v-else class="text-sm text-slate-400">Sin turnos registrados</p>
          </div>
        </div>
      </template>
      <template #footer>
        <button type="button" @click="showHistorial = false" class="btn-secondary w-full justify-center">Cerrar</button>
      </template>
    </CrudModal>
  </div>
</template>

<script setup>
import { ref, computed, reactive, markRaw } from 'vue'
import { useDataStore } from '@/stores/data'
import CrudModal from '@/components/ui/CrudModal.vue'
import {
  PlusIcon, MagnifyingGlassIcon, ArrowDownTrayIcon, XMarkIcon,
  UsersIcon, UserGroupIcon, ClockIcon, HeartIcon, ArrowTrendingUpIcon,
  EyeIcon, PencilSquareIcon, TrashIcon, ClipboardDocumentListIcon,
} from '@heroicons/vue/24/outline'

const store = useDataStore()
const showModal = ref(false)
const showView = ref(false)
const showHistorial = ref(false)
const editing = ref(null)
const viewing = ref(null)
const historialCliente = ref(null)

const filters = reactive({ search: '', estado: '', mes: '', mascotas: '' })

const blank = () => ({
  nombre: '', telefono: '', email: '', direccion: '',
  fechaRegistro: new Date().toISOString().slice(0, 10), estado: 'Activo',
})
const form = ref(blank())

const mesesDisponibles = computed(() => {
  const set = new Map()
  store.clientes.forEach(c => {
    const d = new Date(c.fechaRegistro + 'T12:00:00')
    const val = `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}`
    const label = d.toLocaleDateString('es-CO', { month: 'long', year: 'numeric' })
    set.set(val, label)
  })
  return [...set.entries()].map(([value, label]) => ({ value, label }))
})

const kpis = computed(() => {
  const activos = store.clientes.filter(c => (c.estado ?? 'Activo') === 'Activo').length
  const pendientes = store.clientes.filter(c => c.estado === 'Pendiente').length
  const totalMascotas = store.mascotas.length
  return [
    { label: 'Total Clientes', value: store.clientes.length, growth: '+12% mes', badgeClass: 'bg-indigo-50 text-indigo-700', gradient: 'from-indigo-600 to-violet-400', icon: markRaw(UsersIcon) },
    { label: 'Clientes Activos', value: activos, growth: '+8% mes', badgeClass: 'bg-emerald-50 text-emerald-700', gradient: 'from-emerald-500 to-teal-400', icon: markRaw(UserGroupIcon) },
    { label: 'Clientes Pendientes', value: pendientes, growth: pendientes ? 'Revisar' : 'Al día', badgeClass: 'bg-amber-50 text-amber-700', gradient: 'from-amber-500 to-orange-400', icon: markRaw(ClockIcon) },
    { label: 'Mascotas Registradas', value: totalMascotas, growth: '+5% mes', badgeClass: 'bg-pink-50 text-pink-700', gradient: 'from-pink-500 to-rose-400', icon: markRaw(HeartIcon) },
  ]
})

const stats = computed(() => {
  const now = new Date()
  const key = `${now.getFullYear()}-${String(now.getMonth() + 1).padStart(2, '0')}`
  const nuevosMes = store.clientes.filter(c => c.fechaRegistro?.startsWith(key)).length

  const conMascotas = store.clientes.map(c => ({
    ...c,
    mascotas: store.mascotas.filter(m => m.clienteId === c.id).length,
  }))
  const masActivos = [...conMascotas].sort((a, b) => b.mascotas - a.mascotas).slice(0, 3)
  const ultimo = [...store.clientes].sort((a, b) => b.fechaRegistro.localeCompare(a.fechaRegistro))[0]
  const promedio = store.clientes.length
    ? (store.mascotas.length / store.clientes.length).toFixed(1)
    : '0'

  return { nuevosMes, masActivos, ultimo, promedioMascotas: promedio }
})

const filtered = computed(() => {
  let list = [...store.clientes]
  if (filters.search) {
    const q = filters.search.toLowerCase()
    list = list.filter(c => ['nombre', 'email', 'telefono'].some(k => String(c[k] ?? '').toLowerCase().includes(q)))
  }
  if (filters.estado) list = list.filter(c => (c.estado ?? 'Activo') === filters.estado)
  if (filters.mes) list = list.filter(c => c.fechaRegistro?.startsWith(filters.mes))
  if (filters.mascotas === '0') list = list.filter(c => countMascotas(c.id) === 0)
  if (filters.mascotas === '1') list = list.filter(c => countMascotas(c.id) === 1)
  if (filters.mascotas === '2+') list = list.filter(c => countMascotas(c.id) >= 2)
  return list
})

function countMascotas(clienteId) { return store.mascotas.filter(m => m.clienteId === clienteId).length }
function mascotasDe(id) { return store.mascotas.filter(m => m.clienteId === id) }
function turnosDe(id) { return store.turnos.filter(t => t.clienteId === id) }
function iniciales(nombre) {
  return (nombre ?? '').split(' ').filter(Boolean).slice(0, 2).map(w => w[0]?.toUpperCase()).join('') || '?'
}
function formatFecha(f) {
  return new Date(f + 'T12:00:00').toLocaleDateString('es-CO', { day: 'numeric', month: 'short', year: 'numeric' })
}
function estadoBadge(estado) {
  const map = {
    Activo: 'inline-flex text-[11px] font-bold uppercase tracking-wide px-2.5 py-1 rounded-full bg-emerald-50 text-emerald-700 ring-1 ring-emerald-200',
    Inactivo: 'inline-flex text-[11px] font-bold uppercase tracking-wide px-2.5 py-1 rounded-full bg-rose-50 text-rose-600 ring-1 ring-rose-200',
    Pendiente: 'inline-flex text-[11px] font-bold uppercase tracking-wide px-2.5 py-1 rounded-full bg-amber-50 text-amber-700 ring-1 ring-amber-200',
  }
  return map[estado] ?? map.Activo
}
function limpiarFiltros() { Object.assign(filters, { search: '', estado: '', mes: '', mascotas: '' }) }
function openModal(row = null) {
  editing.value = row
  form.value = row ? { ...row, estado: row.estado ?? 'Activo' } : blank()
  showModal.value = true
}

function verCliente(c) {
  viewing.value = { ...c }
  showView.value = true
}

function editarDesdeVista() {
  const c = viewing.value
  showView.value = false
  if (c) openModal(c)
}

function verHistorial(c) {
  historialCliente.value = { ...c }
  showHistorial.value = true
}

function save() {
  if (!form.value.nombre?.trim()) { alert('El nombre es obligatorio'); return }
  if (editing.value) store.updateItem(store.clientes, editing.value.id, { ...form.value })
  else store.addItem(store.clientes, { ...form.value })
  showModal.value = false
  editing.value = null
  form.value = blank()
}

function confirmarEliminar(row) {
  if (confirm(`¿Eliminar a ${row.nombre}? Esta acción no se puede deshacer.`)) {
    store.deleteItem(store.clientes, row.id)
    if (viewing.value?.id === row.id) { viewing.value = null; showView.value = false }
    if (historialCliente.value?.id === row.id) { historialCliente.value = null; showHistorial.value = false }
  }
}

function exportarCsv() {
  const headers = ['Nombre', 'Teléfono', 'Email', 'Dirección', 'Estado', 'Registro', 'Mascotas']
  const rows = filtered.value.map(c => [
    c.nombre, c.telefono, c.email, c.direccion, c.estado ?? 'Activo', c.fechaRegistro, countMascotas(c.id),
  ])
  const csv = [headers, ...rows].map(r => r.map(v => `"${v}"`).join(',')).join('\n')
  const blob = new Blob([csv], { type: 'text/csv;charset=utf-8;' })
  const a = document.createElement('a')
  a.href = URL.createObjectURL(blob)
  a.download = 'clientes-vetclinica.csv'
  a.click()
}
</script>

<style scoped>
.cli-kpi {
  @apply relative bg-white rounded-2xl border border-slate-100 p-5 overflow-hidden
         shadow-[0_1px_2px_rgba(0,0,0,0.04),0_4px_16px_rgba(0,0,0,0.05)]
         hover:shadow-[0_8px_28px_rgba(0,0,0,0.1)] hover:-translate-y-0.5 transition-all duration-300;
}
.cli-btn-primary {
  @apply inline-flex items-center gap-2 px-4 py-2.5 rounded-xl text-sm font-semibold text-white
         bg-gradient-to-r from-indigo-600 to-violet-600 shadow-lg shadow-indigo-500/25
         hover:shadow-xl hover:-translate-y-0.5 transition-all duration-200;
}
.cli-btn-secondary {
  @apply inline-flex items-center gap-2 px-4 py-2.5 rounded-xl text-sm font-semibold
         text-slate-700 bg-white border border-slate-200 hover:bg-slate-50 hover:border-slate-300 transition-all;
}
.cli-select {
  @apply w-full px-3 py-2.5 text-sm bg-slate-50 border border-slate-200 rounded-xl
         focus:outline-none focus:ring-2 focus:ring-indigo-500/25 focus:border-indigo-400 transition;
}
.cli-action {
  @apply p-2 rounded-xl text-slate-500 hover:text-indigo-600 hover:bg-indigo-50 transition-colors cursor-pointer;
}
.cli-action--danger {
  @apply hover:text-rose-600 hover:bg-rose-50;
}
</style>
