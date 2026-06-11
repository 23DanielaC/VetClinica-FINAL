<template>
  <div class="relative z-10 space-y-6">
    <!-- Encabezado -->
    <div>
      <h1 class="text-2xl sm:text-3xl font-extrabold text-slate-900 tracking-tight">Medicamentos</h1>
      <p class="text-slate-500 mt-1.5 text-sm sm:text-base">Inventario de medicamentos</p>
    </div>

    <!-- KPIs -->
    <div class="grid grid-cols-2 lg:grid-cols-4 gap-4">
      <div v-for="kpi in kpis" :key="kpi.label" class="med-kpi">
        <p class="text-sm font-medium text-slate-500">{{ kpi.label }}</p>
        <p :class="['text-2xl sm:text-3xl font-extrabold mt-2 tabular-nums', kpi.color]">{{ kpi.value }}</p>
      </div>
    </div>

    <!-- Card principal -->
    <div class="bg-white rounded-2xl border border-slate-100 shadow-[0_1px_2px_rgba(0,0,0,0.04),0_4px_20px_rgba(0,0,0,0.05)] overflow-hidden">
      <!-- Toolbar -->
      <div class="flex flex-col lg:flex-row lg:items-center lg:justify-between gap-4 p-5 lg:p-6 border-b border-slate-100">
        <div>
          <h2 class="text-lg font-bold text-slate-900">Inventario de Medicamentos</h2>
          <p class="text-sm text-slate-500 mt-0.5">{{ store.medicamentos.length }} medicamentos registrados</p>
        </div>
        <div class="flex flex-col sm:flex-row gap-3 sm:items-center">
          <div class="relative">
            <MagnifyingGlassIcon class="w-5 h-5 absolute left-3.5 top-1/2 -translate-y-1/2 text-slate-400" />
            <input
              v-model="search"
              type="search"
              placeholder="Buscar medicamento..."
              class="w-full sm:w-64 pl-11 pr-4 py-2.5 text-sm bg-slate-50 border border-slate-200 rounded-xl focus:outline-none focus:ring-2 focus:ring-blue-500/25 focus:border-blue-400 transition"
            />
          </div>
          <button type="button" @click="openModal()" class="med-btn-primary whitespace-nowrap">
            <PlusIcon class="w-4 h-4" /> Nuevo Medicamento
          </button>
        </div>
      </div>

      <!-- Tabla desktop -->
      <div class="hidden md:block overflow-x-auto">
        <table class="w-full">
          <thead>
            <tr class="bg-slate-50/80 border-b border-slate-100">
              <th v-for="col in columns" :key="col" class="med-th">{{ col }}</th>
            </tr>
          </thead>
          <tbody class="divide-y divide-slate-50">
            <tr
              v-for="med in filtered"
              :key="med.id"
              class="hover:bg-blue-50/30 transition-colors duration-150"
            >
              <td class="med-td font-semibold text-slate-900">{{ med.nombre }}</td>
              <td class="med-td text-slate-600">{{ med.presentacion }}</td>
              <td class="med-td text-slate-600">{{ med.dosis }}</td>
              <td class="med-td">
                <span class="inline-flex items-center gap-2 font-semibold tabular-nums">
                  <span :class="['w-2 h-2 rounded-full shrink-0', stockDot(med.stock)]" />
                  {{ med.stock }}
                </span>
              </td>
              <td class="med-td font-medium text-slate-800 tabular-nums">{{ formatPrecio(med.precio) }}</td>
              <td class="med-td text-slate-600 whitespace-nowrap">{{ formatVencimiento(med.vencimiento) }}</td>
              <td class="med-td text-slate-600">{{ med.laboratorio ?? '—' }}</td>
              <td class="med-td">
                <span :class="estadoBadge(med)">{{ estadoMed(med) }}</span>
              </td>
              <td class="med-td">
                <div class="flex items-center justify-end gap-1">
                  <button type="button" title="Ver" class="med-action med-action--blue" @click="verMed(med)">
                    <EyeIcon class="w-4 h-4" />
                  </button>
                  <button type="button" title="Editar" class="med-action med-action--green" @click="openModal(med)">
                    <PencilSquareIcon class="w-4 h-4" />
                  </button>
                  <button type="button" title="Eliminar" class="med-action med-action--red" @click="confirmarEliminar(med)">
                    <TrashIcon class="w-4 h-4" />
                  </button>
                </div>
              </td>
            </tr>
            <tr v-if="!filtered.length">
              <td colspan="9" class="text-center py-16 text-slate-400 text-sm">No se encontraron medicamentos</td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- Mobile cards -->
      <div class="md:hidden p-4 space-y-3">
        <div
          v-for="med in filtered"
          :key="med.id"
          class="border border-slate-100 rounded-xl p-4 hover:shadow-md transition-shadow"
        >
          <div class="flex items-start justify-between gap-2">
            <div>
              <p class="font-bold text-slate-900">{{ med.nombre }}</p>
              <p class="text-xs text-slate-500">{{ med.presentacion }} · {{ med.dosis }}</p>
            </div>
            <span :class="estadoBadge(med)">{{ estadoMed(med) }}</span>
          </div>
          <div class="grid grid-cols-2 gap-2 mt-3 text-sm">
            <div>
              <span class="inline-flex items-center gap-1.5 font-semibold">
                <span :class="['w-2 h-2 rounded-full', stockDot(med.stock)]" /> Stock: {{ med.stock }}
              </span>
            </div>
            <div class="text-right font-medium">{{ formatPrecio(med.precio) }}</div>
            <div class="text-slate-500 text-xs">{{ formatVencimiento(med.vencimiento) }}</div>
            <div class="text-slate-500 text-xs text-right">{{ med.laboratorio }}</div>
          </div>
          <div class="flex gap-1 mt-3 pt-3 border-t border-slate-100">
            <button type="button" class="med-action med-action--blue flex-1" @click="verMed(med)"><EyeIcon class="w-4 h-4 mx-auto" /></button>
            <button type="button" class="med-action med-action--green flex-1" @click="openModal(med)"><PencilSquareIcon class="w-4 h-4 mx-auto" /></button>
            <button type="button" class="med-action med-action--red flex-1" @click="confirmarEliminar(med)"><TrashIcon class="w-4 h-4 mx-auto" /></button>
          </div>
        </div>
      </div>
    </div>

    <!-- Modal CRUD -->
    <CrudModal v-model="showModal" :title="editing ? 'Editar Medicamento' : 'Nuevo Medicamento'" @confirm="save">
      <div class="space-y-4">
        <div><label class="form-label">Nombre</label><input v-model="form.nombre" class="form-input" /></div>
        <div class="grid grid-cols-2 gap-3">
          <div><label class="form-label">Presentación</label>
            <select v-model="form.presentacion" class="form-input">
              <option>Tabletas</option><option>Inyectable</option><option>Líquido</option><option>Gotas</option><option>Oral</option><option>Crema</option>
            </select>
          </div>
          <div><label class="form-label">Concentración</label><input v-model="form.dosis" class="form-input" placeholder="500mg" /></div>
        </div>
        <div class="grid grid-cols-2 gap-3">
          <div><label class="form-label">Stock</label><input v-model.number="form.stock" type="number" class="form-input" /></div>
          <div><label class="form-label">Precio ($)</label><input v-model.number="form.precio" type="number" class="form-input" /></div>
        </div>
        <div class="grid grid-cols-2 gap-3">
          <div><label class="form-label">Vencimiento</label><input v-model="form.vencimiento" type="date" class="form-input" /></div>
          <div><label class="form-label">Laboratorio</label><input v-model="form.laboratorio" class="form-input" /></div>
        </div>
        <div><label class="form-label">Proveedor</label><input v-model="form.proveedor" class="form-input" /></div>
      </div>
    </CrudModal>

    <!-- Modal ver -->
    <CrudModal v-model="showView" title="Detalle del Medicamento" hide-footer>
      <template v-if="viewing">
        <dl class="space-y-3 text-sm">
          <div class="grid grid-cols-2 gap-3">
            <div><dt class="text-slate-400 text-xs">Nombre</dt><dd class="font-bold text-slate-900">{{ viewing.nombre }}</dd></div>
            <div><dt class="text-slate-400 text-xs">Estado</dt><dd><span :class="estadoBadge(viewing)">{{ estadoMed(viewing) }}</span></dd></div>
            <div><dt class="text-slate-400 text-xs">Presentación</dt><dd class="font-medium">{{ viewing.presentacion }}</dd></div>
            <div><dt class="text-slate-400 text-xs">Concentración</dt><dd class="font-medium">{{ viewing.dosis }}</dd></div>
            <div><dt class="text-slate-400 text-xs">Stock</dt><dd class="font-medium inline-flex items-center gap-1.5"><span :class="['w-2 h-2 rounded-full', stockDot(viewing.stock)]" />{{ viewing.stock }}</dd></div>
            <div><dt class="text-slate-400 text-xs">Precio</dt><dd class="font-medium">{{ formatPrecio(viewing.precio) }}</dd></div>
            <div><dt class="text-slate-400 text-xs">Vencimiento</dt><dd class="font-medium">{{ formatVencimiento(viewing.vencimiento) }}</dd></div>
            <div><dt class="text-slate-400 text-xs">Laboratorio</dt><dd class="font-medium">{{ viewing.laboratorio }}</dd></div>
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
import { ref, computed } from 'vue'
import { useDataStore } from '@/stores/data'
import CrudModal from '@/components/ui/CrudModal.vue'
import {
  PlusIcon, MagnifyingGlassIcon, EyeIcon, PencilSquareIcon, TrashIcon,
} from '@heroicons/vue/24/outline'

const store = useDataStore()
const search = ref('')
const showModal = ref(false)
const showView = ref(false)
const editing = ref(null)
const viewing = ref(null)

const columns = ['Medicamento', 'Presentación', 'Concentración', 'Stock', 'Precio', 'Vencimiento', 'Laboratorio', 'Estado', 'Acciones']

const blank = () => ({
  nombre: '', presentacion: 'Tabletas', dosis: '', stock: 0, precio: 0,
  laboratorio: '', vencimiento: '', proveedor: '',
})
const form = ref(blank())

const filtered = computed(() => {
  if (!search.value) return store.medicamentos
  const q = search.value.toLowerCase()
  return store.medicamentos.filter(m =>
    ['nombre', 'presentacion', 'dosis', 'laboratorio', 'proveedor'].some(k => String(m[k] ?? '').toLowerCase().includes(q))
  )
})

const kpis = computed(() => {
  const list = store.medicamentos
  const criticos = list.filter(m => m.stock < 10).length
  const porVencer = list.filter(m => isPorVencer(m.vencimiento)).length
  const valor = list.reduce((s, m) => s + (m.stock * (m.precio ?? 0)), 0)
  return [
    { label: 'Total Medicamentos', value: list.length, color: 'text-slate-900' },
    { label: 'Críticos', value: criticos, color: 'text-rose-600' },
    { label: 'Por Vencer', value: porVencer, color: 'text-amber-600' },
    { label: 'Valor Inventario', value: `$${valor.toLocaleString('es-CO')}`, color: 'text-blue-600' },
  ]
})

function isPorVencer(dateStr) {
  if (!dateStr) return false
  const d = new Date(dateStr + 'T12:00:00')
  const now = new Date()
  const diff = (d - now) / 86400000
  return diff >= 0 && diff <= 90
}

function stockDot(stock) {
  if (stock < 10) return 'bg-rose-500'
  if (stock < 20) return 'bg-amber-400'
  return 'bg-emerald-500'
}

function estadoMed(med) {
  if (med.stock < 10) return 'Crítico'
  if (isPorVencer(med.vencimiento)) return 'Por Vencer'
  return 'Disponible'
}

function estadoBadge(med) {
  const e = estadoMed(med)
  const map = {
    Disponible: 'inline-flex text-[10px] font-bold uppercase px-2.5 py-1 rounded-full bg-emerald-50 text-emerald-700 ring-1 ring-emerald-200',
    Crítico: 'inline-flex text-[10px] font-bold uppercase px-2.5 py-1 rounded-full bg-rose-50 text-rose-700 ring-1 ring-rose-200',
    'Por Vencer': 'inline-flex text-[10px] font-bold uppercase px-2.5 py-1 rounded-full bg-amber-50 text-amber-700 ring-1 ring-amber-200',
  }
  return map[e] ?? map.Disponible
}

function formatPrecio(n) {
  return '$' + Number(n ?? 0).toLocaleString('es-CO', { minimumFractionDigits: 0 })
}

function formatVencimiento(f) {
  if (!f) return '—'
  return new Date(f + 'T12:00:00').toLocaleDateString('es-CO', { day: 'numeric', month: 'short', year: 'numeric' })
}

function openModal(row = null) {
  editing.value = row
  form.value = row ? { ...row } : blank()
  showModal.value = true
}

function verMed(m) { viewing.value = { ...m }; showView.value = true }

function editarDesdeVista() {
  const m = viewing.value
  showView.value = false
  if (m) openModal(m)
}

function save() {
  if (!form.value.nombre?.trim()) { alert('El nombre es obligatorio'); return }
  if (editing.value) store.updateItem(store.medicamentos, editing.value.id, { ...form.value })
  else store.addItem(store.medicamentos, { ...form.value })
  showModal.value = false
  editing.value = null
  form.value = blank()
}

function confirmarEliminar(row) {
  if (confirm(`¿Eliminar ${row.nombre}?`)) {
    store.deleteItem(store.medicamentos, row.id)
    if (viewing.value?.id === row.id) { viewing.value = null; showView.value = false }
  }
}
</script>

<style scoped>
.med-kpi {
  @apply bg-white rounded-2xl border border-slate-100 p-5 min-h-[100px]
         shadow-[0_1px_2px_rgba(0,0,0,0.04),0_4px_16px_rgba(0,0,0,0.05)];
}
.med-btn-primary {
  @apply inline-flex items-center justify-center gap-2 px-4 py-2.5 rounded-xl text-sm font-semibold text-white
         bg-gradient-to-r from-blue-600 to-indigo-600 shadow-lg shadow-blue-500/25
         hover:shadow-xl hover:-translate-y-0.5 transition-all duration-200;
}
.med-th {
  @apply px-4 py-3.5 text-left text-xs font-bold text-slate-500 uppercase tracking-wider whitespace-nowrap;
}
.med-td {
  @apply px-4 py-4 text-sm;
}
.med-action {
  @apply p-2 rounded-lg transition-colors cursor-pointer;
}
.med-action--blue {
  @apply text-blue-500 hover:bg-blue-50 hover:text-blue-700;
}
.med-action--green {
  @apply text-emerald-500 hover:bg-emerald-50 hover:text-emerald-700;
}
.med-action--red {
  @apply text-rose-500 hover:bg-rose-50 hover:text-rose-700;
}
</style>
