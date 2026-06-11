<template>
  <div class="relative z-10 space-y-6">
    <!-- Encabezado -->
    <div>
      <h1 class="text-2xl sm:text-3xl font-extrabold text-slate-900 tracking-tight">Facturas</h1>
      <p class="text-slate-500 mt-1.5 text-sm sm:text-base">Gestión de facturación</p>
    </div>

    <!-- KPIs -->
    <div class="grid grid-cols-2 lg:grid-cols-4 gap-4">
      <div v-for="kpi in kpis" :key="kpi.label" class="fac-kpi">
        <div>
          <p class="text-sm font-medium text-slate-500">{{ kpi.label }}</p>
          <p :class="['text-2xl sm:text-3xl font-extrabold mt-1 tabular-nums', kpi.color]">{{ kpi.value }}</p>
        </div>
        <div :class="['w-11 h-11 rounded-xl flex items-center justify-center shrink-0', kpi.iconBg]">
          <component :is="kpi.icon" :class="['w-5 h-5', kpi.iconColor]" />
        </div>
      </div>
    </div>

    <!-- Barra de acciones -->
    <div class="bg-white rounded-2xl border border-slate-100 shadow-[0_1px_2px_rgba(0,0,0,0.04),0_4px_16px_rgba(0,0,0,0.05)] p-4 lg:p-5">
      <div class="flex flex-col lg:flex-row lg:items-center gap-3">
        <div class="relative flex-1 min-w-0">
          <MagnifyingGlassIcon class="w-5 h-5 absolute left-3.5 top-1/2 -translate-y-1/2 text-slate-400" />
          <input
            v-model="search"
            type="search"
            placeholder="Buscar por factura, cliente o estado..."
            class="w-full pl-11 pr-4 py-2.5 text-sm bg-slate-50 border border-slate-200 rounded-xl focus:outline-none focus:ring-2 focus:ring-blue-500/25 focus:border-blue-400 transition"
          />
        </div>
        <select v-model="filtroEstado" class="fac-select lg:w-44">
          <option value="">Todos</option>
          <option value="Pagada">Pagadas</option>
          <option value="Pendiente">Pendientes</option>
          <option value="Vencida">Vencidas</option>
        </select>
        <div class="flex flex-col sm:flex-row gap-2.5 shrink-0">
          <button type="button" @click="exportarListado" class="fac-btn-secondary whitespace-nowrap">
            <ArrowDownTrayIcon class="w-4 h-4" /> Exportar PDF
          </button>
          <button type="button" @click="openModal()" class="fac-btn-primary whitespace-nowrap">
            <PlusIcon class="w-4 h-4" /> Nueva Factura
          </button>
        </div>
      </div>
      <p class="text-xs text-slate-400 mt-3">{{ filtered.length }} de {{ store.facturas.length }} facturas</p>
    </div>

    <!-- Tabla desktop / tablet -->
    <div class="hidden md:block bg-white rounded-2xl border border-slate-100 shadow-[0_1px_2px_rgba(0,0,0,0.04),0_4px_20px_rgba(0,0,0,0.05)] overflow-hidden">
      <div class="overflow-x-auto">
        <table class="w-full min-w-[960px]">
          <thead>
            <tr class="bg-slate-50/80 border-b border-slate-100">
              <th v-for="col in columns" :key="col" class="fac-th">{{ col }}</th>
            </tr>
          </thead>
          <tbody class="divide-y divide-slate-50">
            <tr
              v-for="fac in filtered"
              :key="fac.id"
              class="hover:bg-blue-50/30 transition-colors duration-150"
            >
              <td class="fac-td">
                <span class="font-bold text-blue-600">{{ fac.numero ?? numeroFactura(fac) }}</span>
              </td>
              <td class="fac-td">
                <div class="flex items-center gap-3 min-w-[180px]">
                  <div class="w-10 h-10 rounded-full bg-gradient-to-br from-indigo-600 to-violet-500 flex items-center justify-center text-white text-xs font-bold shadow-sm shrink-0">
                    {{ iniciales(clienteNombre(fac.clienteId)) }}
                  </div>
                  <span class="font-semibold text-slate-900">{{ clienteNombre(fac.clienteId) }}</span>
                </div>
              </td>
              <td class="fac-td text-slate-600 whitespace-nowrap">{{ formatFecha(fac.fecha) }}</td>
              <td class="fac-td text-slate-600 whitespace-nowrap">{{ formatFecha(fac.vencimiento) }}</td>
              <td class="fac-td font-semibold text-slate-900 tabular-nums">{{ formatMoney(fac.total) }}</td>
              <td class="fac-td">
                <span :class="estadoBadge(estadoEfectivo(fac))">{{ estadoEfectivo(fac) }}</span>
              </td>
              <td class="fac-td text-slate-600">{{ metodoPago(fac) }}</td>
              <td class="fac-td">
                <div class="flex items-center justify-end gap-0.5">
                  <button type="button" title="Ver factura" class="fac-action fac-action--blue" @click="verFactura(fac)">
                    <EyeIcon class="w-4 h-4" />
                  </button>
                  <button type="button" title="Editar" class="fac-action fac-action--green" @click="openModal(fac)">
                    <PencilSquareIcon class="w-4 h-4" />
                  </button>
                  <button type="button" title="Eliminar" class="fac-action fac-action--red" @click="confirmarEliminar(fac)">
                    <TrashIcon class="w-4 h-4" />
                  </button>
                  <button type="button" title="Descargar PDF" class="fac-action fac-action--slate" @click="descargarPdf(fac)">
                    <DocumentArrowDownIcon class="w-4 h-4" />
                  </button>
                </div>
              </td>
            </tr>
            <tr v-if="!filtered.length">
              <td colspan="8" class="text-center py-16 text-slate-400 text-sm">No se encontraron facturas</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- Mobile cards -->
    <div class="md:hidden space-y-3">
      <div
        v-for="fac in filtered"
        :key="fac.id"
        class="bg-white rounded-2xl border border-slate-100 shadow-[0_1px_2px_rgba(0,0,0,0.04),0_4px_16px_rgba(0,0,0,0.05)] p-4 hover:shadow-md transition-shadow"
      >
        <div class="flex items-start justify-between gap-2">
          <div class="flex items-center gap-3">
            <div class="w-10 h-10 rounded-full bg-gradient-to-br from-indigo-600 to-violet-500 flex items-center justify-center text-white text-xs font-bold shrink-0">
              {{ iniciales(clienteNombre(fac.clienteId)) }}
            </div>
            <div>
              <p class="font-bold text-blue-600 text-sm">{{ fac.numero ?? numeroFactura(fac) }}</p>
              <p class="font-semibold text-slate-900">{{ clienteNombre(fac.clienteId) }}</p>
            </div>
          </div>
          <span :class="estadoBadge(estadoEfectivo(fac))">{{ estadoEfectivo(fac) }}</span>
        </div>
        <div class="grid grid-cols-2 gap-2 mt-3 text-sm">
          <div><span class="text-xs text-slate-400 block">Fecha</span>{{ formatFecha(fac.fecha) }}</div>
          <div class="text-right"><span class="text-xs text-slate-400 block">Vence</span>{{ formatFecha(fac.vencimiento) }}</div>
          <div class="font-bold text-slate-900">{{ formatMoney(fac.total) }}</div>
          <div class="text-right text-slate-600">{{ metodoPago(fac) }}</div>
        </div>
        <div class="flex gap-1 mt-3 pt-3 border-t border-slate-100">
          <button type="button" class="fac-action fac-action--blue flex-1" @click="verFactura(fac)"><EyeIcon class="w-4 h-4 mx-auto" /></button>
          <button type="button" class="fac-action fac-action--green flex-1" @click="openModal(fac)"><PencilSquareIcon class="w-4 h-4 mx-auto" /></button>
          <button type="button" class="fac-action fac-action--red flex-1" @click="confirmarEliminar(fac)"><TrashIcon class="w-4 h-4 mx-auto" /></button>
          <button type="button" class="fac-action fac-action--slate flex-1" @click="descargarPdf(fac)"><DocumentArrowDownIcon class="w-4 h-4 mx-auto" /></button>
        </div>
      </div>
    </div>

    <!-- Modal CRUD -->
    <CrudModal v-model="showModal" :title="editing ? 'Editar Factura' : 'Nueva Factura'" @confirm="save">
      <div class="space-y-4">
        <div>
          <label class="form-label">Cliente</label>
          <select v-model="form.clienteId" class="form-input">
            <option v-for="c in store.clientes" :key="c.id" :value="c.id">{{ c.nombre }}</option>
          </select>
        </div>
        <div class="grid grid-cols-2 gap-3">
          <div><label class="form-label">Fecha</label><input v-model="form.fecha" type="date" class="form-input" /></div>
          <div><label class="form-label">Vencimiento</label><input v-model="form.vencimiento" type="date" class="form-input" /></div>
        </div>
        <div class="grid grid-cols-2 gap-3">
          <div><label class="form-label">Subtotal ($)</label><input v-model.number="form.subtotal" type="number" class="form-input" @input="calcTotal" /></div>
          <div><label class="form-label">IVA ($)</label><input v-model.number="form.iva" type="number" class="form-input" @input="calcTotal" /></div>
        </div>
        <div class="grid grid-cols-2 gap-3">
          <div><label class="form-label">Total ($)</label><input :value="form.total" class="form-input bg-slate-50" readonly /></div>
          <div>
            <label class="form-label">Estado</label>
            <select v-model="form.estado" class="form-input">
              <option>Pendiente</option><option>Pagada</option>
            </select>
          </div>
        </div>
        <div>
          <label class="form-label">Método de pago</label>
          <select v-model="form.metodoPago" class="form-input">
            <option value="—">Sin definir</option>
            <option>Efectivo</option><option>Tarjeta</option><option>Transferencia</option>
          </select>
        </div>
      </div>
    </CrudModal>

    <!-- Modal ver -->
    <CrudModal v-model="showView" title="Detalle de Factura" hide-footer>
      <template v-if="viewing">
        <div class="flex items-center justify-between mb-4 pb-4 border-b border-slate-100">
          <div>
            <p class="text-xl font-bold text-blue-600">{{ viewing.numero ?? numeroFactura(viewing) }}</p>
            <p class="text-sm text-slate-500 mt-0.5">{{ formatFecha(viewing.fecha) }}</p>
          </div>
          <span :class="estadoBadge(estadoEfectivo(viewing))">{{ estadoEfectivo(viewing) }}</span>
        </div>
        <div class="flex items-center gap-3 mb-4">
          <div class="w-12 h-12 rounded-full bg-gradient-to-br from-indigo-600 to-violet-500 flex items-center justify-center text-white font-bold">
            {{ iniciales(clienteNombre(viewing.clienteId)) }}
          </div>
          <div>
            <p class="font-bold text-slate-900">{{ clienteNombre(viewing.clienteId) }}</p>
            <p class="text-xs text-slate-500">{{ clienteEmail(viewing.clienteId) }}</p>
          </div>
        </div>
        <dl class="grid grid-cols-2 gap-3 text-sm mb-4">
          <div><dt class="text-slate-400 text-xs">Vencimiento</dt><dd class="font-medium">{{ formatFecha(viewing.vencimiento) }}</dd></div>
          <div><dt class="text-slate-400 text-xs">Método de pago</dt><dd class="font-medium">{{ metodoPago(viewing) }}</dd></div>
          <div><dt class="text-slate-400 text-xs">Subtotal</dt><dd class="font-medium">{{ formatMoney(viewing.subtotal) }}</dd></div>
          <div><dt class="text-slate-400 text-xs">IVA</dt><dd class="font-medium">{{ formatMoney(viewing.iva) }}</dd></div>
        </dl>
        <div v-if="viewing.items?.length" class="rounded-xl border border-slate-100 overflow-hidden">
          <div class="bg-slate-50 px-3 py-2 text-xs font-bold text-slate-500 uppercase">Detalle</div>
          <div v-for="(item, i) in viewing.items" :key="i" class="px-3 py-2 text-sm border-t border-slate-50 flex justify-between">
            <span>{{ item.tipo }}: {{ item.descripcion }}</span>
            <span class="font-medium tabular-nums">{{ formatMoney(item.subtotal) }}</span>
          </div>
        </div>
        <p class="text-right text-lg font-extrabold text-slate-900 mt-4">{{ formatMoney(viewing.total) }}</p>
      </template>
      <template #footer>
        <button type="button" @click="descargarPdf(viewing)" class="btn-secondary flex-1 justify-center">
          <DocumentArrowDownIcon class="w-4 h-4" /> PDF
        </button>
        <button type="button" @click="editarDesdeVista" class="btn-primary flex-1 justify-center">Editar</button>
      </template>
    </CrudModal>
  </div>
</template>

<script setup>
import { ref, computed, markRaw } from 'vue'
import { useDataStore } from '@/stores/data'
import CrudModal from '@/components/ui/CrudModal.vue'
import { generateFacturaPdf, generateFacturasListPdf } from '@/utils/generateFacturaPdf'
import {
  PlusIcon, MagnifyingGlassIcon, EyeIcon, PencilSquareIcon, TrashIcon,
  ArrowDownTrayIcon, DocumentArrowDownIcon, DocumentTextIcon,
  CheckCircleIcon, ClockIcon, ExclamationCircleIcon,
} from '@heroicons/vue/24/outline'

const store = useDataStore()
const search = ref('')
const filtroEstado = ref('')
const showModal = ref(false)
const showView = ref(false)
const editing = ref(null)
const viewing = ref(null)

const columns = ['Factura', 'Cliente', 'Fecha', 'Vence', 'Total', 'Estado', 'Método de Pago', 'Acciones']

const blank = () => ({
  clienteId: store.clientes[0]?.id ?? '',
  fecha: new Date().toISOString().slice(0, 10),
  vencimiento: new Date(Date.now() + 30 * 86400000).toISOString().slice(0, 10),
  subtotal: 0, iva: 0, total: 0,
  estado: 'Pendiente', metodoPago: '—', items: [],
})
const form = ref(blank())

const kpis = computed(() => {
  const list = store.facturas
  const pagadas = list.filter(f => f.estado === 'Pagada').length
  const pendientes = list.filter(f => estadoEfectivo(f) === 'Pendiente').length
  const vencidas = list.filter(f => estadoEfectivo(f) === 'Vencida').length
  return [
    { label: 'Total Facturas', value: list.length, color: 'text-blue-600', icon: markRaw(DocumentTextIcon), iconBg: 'bg-blue-50', iconColor: 'text-blue-600' },
    { label: 'Facturas Pagadas', value: pagadas, color: 'text-emerald-600', icon: markRaw(CheckCircleIcon), iconBg: 'bg-emerald-50', iconColor: 'text-emerald-600' },
    { label: 'Facturas Pendientes', value: pendientes, color: 'text-amber-600', icon: markRaw(ClockIcon), iconBg: 'bg-amber-50', iconColor: 'text-amber-600' },
    { label: 'Facturas Vencidas', value: vencidas, color: 'text-rose-600', icon: markRaw(ExclamationCircleIcon), iconBg: 'bg-rose-50', iconColor: 'text-rose-600' },
  ]
})

const filtered = computed(() => {
  let list = store.facturas
  if (filtroEstado.value) {
    list = list.filter(f => estadoEfectivo(f) === filtroEstado.value)
  }
  if (search.value) {
    const q = search.value.toLowerCase()
    list = list.filter(f =>
      [
        f.numero, f.estado, estadoEfectivo(f), metodoPago(f),
        clienteNombre(f.clienteId), formatMoney(f.total),
      ].some(v => String(v ?? '').toLowerCase().includes(q))
    )
  }
  return [...list].sort((a, b) => (b.fecha ?? '').localeCompare(a.fecha ?? ''))
})

function estadoEfectivo(f) {
  if (f.estado === 'Pagada') return 'Pagada'
  if (f.vencimiento && new Date(f.vencimiento + 'T23:59:59') < new Date()) return 'Vencida'
  return 'Pendiente'
}

function numeroFactura(f) {
  return f.numero ?? `FAC-${String(f.id).padStart(4, '0')}`
}

function clienteNombre(id) {
  return store.clientes.find(c => c.id === id)?.nombre ?? '—'
}

function clienteEmail(id) {
  return store.clientes.find(c => c.id === id)?.email ?? ''
}

function clienteById(id) {
  return store.clientes.find(c => c.id === id)
}

function metodoPago(f) {
  if (f.metodoPago && f.metodoPago !== '—') return f.metodoPago
  const pago = store.pagos.find(p => p.facturaId === f.id)
  return pago?.metodo ?? '—'
}

function iniciales(nombre) {
  return (nombre ?? '').split(' ').filter(Boolean).slice(0, 2).map(w => w[0]?.toUpperCase()).join('') || '?'
}

function formatFecha(f) {
  if (!f) return '—'
  return new Date(f + 'T12:00:00').toLocaleDateString('es-CO', { day: 'numeric', month: 'short', year: 'numeric' })
}

function formatMoney(n) {
  return '$' + Number(n ?? 0).toLocaleString('es-CO')
}

function estadoBadge(estado) {
  const map = {
    Pagada: 'inline-flex text-[10px] font-bold uppercase px-2.5 py-1 rounded-full bg-emerald-50 text-emerald-700 ring-1 ring-emerald-200',
    Pendiente: 'inline-flex text-[10px] font-bold uppercase px-2.5 py-1 rounded-full bg-amber-50 text-amber-700 ring-1 ring-amber-200',
    Vencida: 'inline-flex text-[10px] font-bold uppercase px-2.5 py-1 rounded-full bg-rose-50 text-rose-700 ring-1 ring-rose-200',
  }
  return map[estado] ?? map.Pendiente
}

function calcTotal() {
  form.value.total = (form.value.subtotal || 0) + (form.value.iva || 0)
}

function nextNumero() {
  const nums = store.facturas.map(f => {
    const m = (f.numero ?? '').match(/FAC-(\d+)/)
    return m ? parseInt(m[1], 10) : 0
  })
  const next = Math.max(0, ...nums) + 1
  return `FAC-${String(next).padStart(4, '0')}`
}

function openModal(row = null) {
  editing.value = row
  form.value = row ? { ...row } : blank()
  showModal.value = true
}

function verFactura(f) {
  viewing.value = { ...f }
  showView.value = true
}

function editarDesdeVista() {
  const f = viewing.value
  showView.value = false
  if (f) openModal(f)
}

function save() {
  if (!form.value.clienteId) { alert('Seleccione un cliente'); return }
  calcTotal()
  const payload = { ...form.value }
  if (editing.value) {
    store.updateItem(store.facturas, editing.value.id, payload)
  } else {
    payload.numero = nextNumero()
    if (!payload.vencimiento) {
      payload.vencimiento = new Date(Date.now() + 30 * 86400000).toISOString().slice(0, 10)
    }
    store.addItem(store.facturas, payload)
  }
  showModal.value = false
  editing.value = null
  form.value = blank()
}

function confirmarEliminar(row) {
  if (confirm(`¿Eliminar factura ${row.numero ?? numeroFactura(row)}?`)) {
    store.deleteItem(store.facturas, row.id)
    if (viewing.value?.id === row.id) { viewing.value = null; showView.value = false }
  }
}

function descargarPdf(factura) {
  if (!factura) return
  generateFacturaPdf(factura, clienteById(factura.clienteId), store.pagos)
}

function exportarListado() {
  generateFacturasListPdf(filtered.value, store.clientes, estadoEfectivo)
}
</script>

<style scoped>
.fac-kpi {
  @apply bg-white rounded-2xl border border-slate-100 p-4 min-h-[90px]
         flex items-center justify-between gap-3
         shadow-[0_1px_2px_rgba(0,0,0,0.04),0_4px_16px_rgba(0,0,0,0.05)];
}
.fac-btn-primary {
  @apply inline-flex items-center justify-center gap-2 px-4 py-2.5 rounded-xl text-sm font-semibold text-white
         bg-gradient-to-r from-blue-600 to-indigo-600 shadow-lg shadow-blue-500/25
         hover:shadow-xl hover:-translate-y-0.5 transition-all duration-200;
}
.fac-btn-secondary {
  @apply inline-flex items-center justify-center gap-2 px-4 py-2.5 rounded-xl text-sm font-semibold
         text-slate-700 bg-white border border-slate-200 shadow-sm
         hover:bg-slate-50 hover:border-slate-300 transition-all duration-200;
}
.fac-select {
  @apply px-3 py-2.5 text-sm bg-slate-50 border border-slate-200 rounded-xl
         focus:outline-none focus:ring-2 focus:ring-blue-500/25 focus:border-blue-400 transition;
}
.fac-th {
  @apply px-4 py-3.5 text-left text-xs font-bold text-slate-500 uppercase tracking-wider whitespace-nowrap;
}
.fac-td {
  @apply px-4 py-4 text-sm;
}
.fac-action {
  @apply p-2 rounded-lg transition-colors cursor-pointer;
}
.fac-action--blue {
  @apply text-blue-500 hover:bg-blue-50 hover:text-blue-700;
}
.fac-action--green {
  @apply text-emerald-500 hover:bg-emerald-50 hover:text-emerald-700;
}
.fac-action--red {
  @apply text-rose-500 hover:bg-rose-50 hover:text-rose-700;
}
.fac-action--slate {
  @apply text-slate-500 hover:bg-slate-100 hover:text-slate-700;
}
</style>
