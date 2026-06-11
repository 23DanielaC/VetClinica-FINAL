<template>
  <div class="relative z-10 space-y-6">
    <!-- Encabezado -->
    <div>
      <h1 class="text-2xl sm:text-3xl font-extrabold text-slate-900 tracking-tight">Pagos</h1>
      <p class="text-slate-500 mt-1.5 text-sm sm:text-base">Historial de pagos recibidos</p>
    </div>

    <!-- KPIs -->
    <div class="grid grid-cols-2 lg:grid-cols-4 gap-4">
      <div v-for="kpi in kpis" :key="kpi.label" class="pag-kpi">
        <div>
          <p class="text-sm font-medium text-slate-500">{{ kpi.label }}</p>
          <p :class="['text-2xl sm:text-3xl font-extrabold mt-1 tabular-nums', kpi.color]">{{ kpi.value }}</p>
        </div>
        <div :class="['w-11 h-11 rounded-xl flex items-center justify-center shrink-0', kpi.iconBg]">
          <component :is="kpi.icon" :class="['w-5 h-5', kpi.iconColor]" />
        </div>
      </div>
    </div>

    <!-- Card principal -->
    <div class="bg-white rounded-2xl border border-slate-100 shadow-[0_1px_2px_rgba(0,0,0,0.04),0_4px_20px_rgba(0,0,0,0.05)] overflow-hidden">
      <!-- Toolbar -->
      <div class="flex flex-col lg:flex-row lg:items-center lg:justify-between gap-4 p-5 lg:p-6 border-b border-slate-100">
        <div>
          <h2 class="text-lg font-bold text-slate-900">Historial de Pagos</h2>
          <p class="text-sm text-slate-500 mt-0.5">{{ store.pagos.length }} transacciones</p>
        </div>
        <div class="flex flex-col sm:flex-row gap-3 sm:items-center">
          <div class="relative">
            <MagnifyingGlassIcon class="w-5 h-5 absolute left-3.5 top-1/2 -translate-y-1/2 text-slate-400" />
            <input
              v-model="search"
              type="search"
              placeholder="Buscar por factura, cliente, referencia..."
              class="w-full sm:w-72 pl-11 pr-4 py-2.5 text-sm bg-slate-50 border border-slate-200 rounded-xl focus:outline-none focus:ring-2 focus:ring-blue-500/25 focus:border-blue-400 transition"
            />
          </div>
          <button type="button" @click="openModal()" class="pag-btn-primary whitespace-nowrap">
            <PlusIcon class="w-4 h-4" /> Registrar Pago
          </button>
        </div>
      </div>

      <!-- Tabla desktop / tablet -->
      <div class="hidden md:block overflow-x-auto">
        <table class="w-full min-w-[980px]">
          <thead>
            <tr class="bg-slate-50/80 border-b border-slate-100">
              <th
                v-for="(col, i) in columns"
                :key="col"
                :class="['pag-th', i === columns.length - 1 && 'pag-th--center']"
              >
                {{ col }}
              </th>
            </tr>
          </thead>
          <tbody class="divide-y divide-slate-50">
            <tr
              v-for="pago in filtered"
              :key="pago.id"
              class="hover:bg-emerald-50/20 transition-colors duration-150"
            >
              <td class="pag-td">
                <span class="inline-flex text-xs font-bold text-slate-600 bg-slate-100 px-2.5 py-1 rounded-lg ring-1 ring-slate-200">
                  {{ facturaNumero(pago.facturaId) }}
                </span>
              </td>
              <td class="pag-td font-semibold text-slate-900">{{ clienteNombre(pago.facturaId) }}</td>
              <td class="pag-td">
                <span class="text-base font-semibold text-emerald-700 tabular-nums">{{ formatMoney(pago.monto) }}</span>
              </td>
              <td class="pag-td">
                <span class="inline-flex items-center gap-2 text-slate-600">
                  <component :is="metodoIcon(pago.metodo)" :class="['w-4 h-4 shrink-0', metodoIconColor(pago.metodo)]" />
                  {{ pago.metodo }}
                </span>
              </td>
              <td class="pag-td text-slate-600 whitespace-nowrap">{{ formatFecha(pago.fecha) }}</td>
              <td class="pag-td text-slate-500 font-mono text-xs">{{ pago.referencia || '—' }}</td>
              <td class="pag-td">
                <span :class="estadoBadge(pago.estado)">{{ pago.estado ?? 'Completado' }}</span>
              </td>
              <td class="pag-td pag-td--center">
                <div class="flex items-center justify-center gap-0.5">
                  <button type="button" title="Ver" class="pag-action pag-action--blue" @click="verPago(pago)">
                    <EyeIcon class="w-4 h-4" />
                  </button>
                  <button type="button" title="Eliminar" class="pag-action pag-action--red" @click="confirmarEliminar(pago)">
                    <TrashIcon class="w-4 h-4" />
                  </button>
                  <button type="button" title="Generar Comprobante PDF" class="pag-action pag-action--slate" @click="generarComprobante(pago)">
                    <DocumentArrowDownIcon class="w-4 h-4" />
                  </button>
                </div>
              </td>
            </tr>
            <tr v-if="!filtered.length">
              <td colspan="8" class="text-center py-16 text-slate-400 text-sm">No se encontraron pagos</td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- Mobile cards -->
      <div class="md:hidden p-4 space-y-3">
        <div
          v-for="pago in filtered"
          :key="pago.id"
          class="border border-slate-100 rounded-xl p-4 hover:shadow-md transition-shadow"
        >
          <div class="flex items-start justify-between gap-2">
            <div>
              <span class="inline-flex text-xs font-bold text-slate-600 bg-slate-100 px-2 py-0.5 rounded-lg">{{ facturaNumero(pago.facturaId) }}</span>
              <p class="font-semibold text-slate-900 mt-1.5">{{ clienteNombre(pago.facturaId) }}</p>
            </div>
            <span :class="estadoBadge(pago.estado)">{{ pago.estado ?? 'Completado' }}</span>
          </div>
          <p class="text-lg font-semibold text-emerald-700 mt-2 tabular-nums">{{ formatMoney(pago.monto) }}</p>
          <div class="flex items-center gap-2 text-sm text-slate-600 mt-1">
            <component :is="metodoIcon(pago.metodo)" :class="['w-4 h-4', metodoIconColor(pago.metodo)]" />
            {{ pago.metodo }} · {{ formatFecha(pago.fecha) }}
          </div>
          <p class="text-xs text-slate-400 font-mono mt-1">{{ pago.referencia }}</p>
          <div class="flex gap-1 mt-3 pt-3 border-t border-slate-100">
            <button type="button" class="pag-action pag-action--blue flex-1" @click="verPago(pago)"><EyeIcon class="w-4 h-4 mx-auto" /></button>
            <button type="button" class="pag-action pag-action--red flex-1" @click="confirmarEliminar(pago)"><TrashIcon class="w-4 h-4 mx-auto" /></button>
            <button type="button" class="pag-action pag-action--slate flex-1" @click="generarComprobante(pago)"><DocumentArrowDownIcon class="w-4 h-4 mx-auto" /></button>
          </div>
        </div>
      </div>

      <p class="px-5 lg:px-6 py-3 text-xs text-slate-400 border-t border-slate-50 md:hidden">
        {{ filtered.length }} de {{ store.pagos.length }} registros
      </p>
    </div>

    <!-- Modal CRUD -->
    <CrudModal v-model="showModal" title="Registrar Pago" @confirm="save">
      <div class="space-y-4">
        <div>
          <label class="form-label">Factura</label>
          <select v-model="form.facturaId" class="form-input" @change="onFacturaChange">
            <option v-for="f in store.facturas" :key="f.id" :value="f.id">
              {{ f.numero ?? `FAC-${f.id}` }} — {{ clienteNombreByFactura(f) }} — {{ formatMoney(f.total) }}
            </option>
          </select>
        </div>
        <div class="grid grid-cols-2 gap-3">
          <div><label class="form-label">Monto ($)</label><input v-model.number="form.monto" type="number" class="form-input" /></div>
          <div><label class="form-label">Fecha</label><input v-model="form.fecha" type="date" class="form-input" /></div>
        </div>
        <div class="grid grid-cols-2 gap-3">
          <div>
            <label class="form-label">Método de pago</label>
            <select v-model="form.metodo" class="form-input">
              <option>Tarjeta Crédito</option>
              <option>Tarjeta Débito</option>
              <option>Transferencia</option>
              <option>Efectivo</option>
            </select>
          </div>
          <div>
            <label class="form-label">Estado</label>
            <select v-model="form.estado" class="form-input">
              <option>Completado</option>
              <option>Pendiente</option>
              <option>Rechazado</option>
            </select>
          </div>
        </div>
        <div><label class="form-label">Referencia bancaria</label><input v-model="form.referencia" class="form-input" placeholder="TC-4521-XXXX" /></div>
      </div>
    </CrudModal>

    <!-- Modal ver -->
    <CrudModal v-model="showView" title="Detalle del Pago" hide-footer>
      <template v-if="viewing">
        <div class="flex items-center justify-between mb-4 pb-4 border-b border-slate-100">
          <div>
            <p class="text-xl font-bold text-emerald-700">{{ formatMoney(viewing.monto) }}</p>
            <p class="text-sm text-slate-500 mt-0.5">{{ viewing.numero ?? numeroPago(viewing) }}</p>
          </div>
          <span :class="estadoBadge(viewing.estado)">{{ viewing.estado ?? 'Completado' }}</span>
        </div>
        <dl class="grid grid-cols-2 gap-3 text-sm">
          <div><dt class="text-slate-400 text-xs">Factura</dt><dd class="font-medium">{{ facturaNumero(viewing.facturaId) }}</dd></div>
          <div><dt class="text-slate-400 text-xs">Cliente</dt><dd class="font-medium">{{ clienteNombre(viewing.facturaId) }}</dd></div>
          <div><dt class="text-slate-400 text-xs">Fecha</dt><dd class="font-medium">{{ formatFecha(viewing.fecha) }}</dd></div>
          <div><dt class="text-slate-400 text-xs">Método</dt><dd class="font-medium inline-flex items-center gap-1.5">
            <component :is="metodoIcon(viewing.metodo)" class="w-4 h-4" /> {{ viewing.metodo }}
          </dd></div>
          <div class="col-span-2"><dt class="text-slate-400 text-xs">Referencia</dt><dd class="font-mono text-sm">{{ viewing.referencia || '—' }}</dd></div>
        </dl>
        <div v-if="facturaById(viewing.facturaId)?.items?.length" class="mt-4 rounded-xl border border-slate-100 overflow-hidden">
          <div class="bg-slate-50 px-3 py-2 text-xs font-bold text-slate-500 uppercase">Detalle factura</div>
          <div v-for="(item, i) in facturaById(viewing.facturaId).items" :key="i" class="px-3 py-2 text-sm border-t border-slate-50 flex justify-between">
            <span>{{ item.tipo }}: {{ item.descripcion }}</span>
            <span class="font-medium tabular-nums">{{ formatMoney(item.subtotal) }}</span>
          </div>
        </div>
      </template>
      <template #footer>
        <button type="button" @click="generarComprobante(viewing)" class="btn-secondary flex-1 justify-center">
          <DocumentArrowDownIcon class="w-4 h-4" /> Comprobante PDF
        </button>
        <button type="button" @click="showView = false" class="btn-primary flex-1 justify-center">Cerrar</button>
      </template>
    </CrudModal>
  </div>
</template>

<script setup>
import { ref, computed, markRaw } from 'vue'
import { useDataStore } from '@/stores/data'
import CrudModal from '@/components/ui/CrudModal.vue'
import { generateComprobantePago } from '@/utils/generateComprobantePago'
import {
  PlusIcon, MagnifyingGlassIcon, EyeIcon, TrashIcon,
  DocumentArrowDownIcon, BanknotesIcon, CalendarDaysIcon, CreditCardIcon,
  BuildingLibraryIcon, ChartBarIcon,
} from '@heroicons/vue/24/outline'

const store = useDataStore()
const search = ref('')
const showModal = ref(false)
const showView = ref(false)
const viewing = ref(null)

const columns = ['Factura', 'Cliente', 'Monto', 'Método', 'Fecha', 'Referencia', 'Estado', 'Acciones']

const blank = () => ({
  facturaId: store.facturas[0]?.id ?? '',
  monto: store.facturas[0]?.total ?? 0,
  metodo: 'Efectivo',
  fecha: new Date().toISOString().slice(0, 10),
  referencia: '',
  estado: 'Completado',
})
const form = ref(blank())

const kpis = computed(() => {
  const completados = store.pagos.filter(p => (p.estado ?? 'Completado') === 'Completado')
  const totalCobrado = completados.reduce((s, p) => s + (p.monto ?? 0), 0)
  const now = new Date()
  const mes = now.getMonth()
  const anio = now.getFullYear()
  const esteMes = completados
    .filter(p => {
      const d = new Date(p.fecha + 'T12:00:00')
      return d.getMonth() === mes && d.getFullYear() === anio
    })
    .reduce((s, p) => s + (p.monto ?? 0), 0)
  const promedio = completados.length ? Math.round(totalCobrado / completados.length) : 0

  return [
    { label: 'Total Cobrado', value: formatMoney(totalCobrado), color: 'text-emerald-600', icon: markRaw(BanknotesIcon), iconBg: 'bg-emerald-50', iconColor: 'text-emerald-600' },
    { label: 'Este Mes', value: formatMoney(esteMes), color: 'text-blue-600', icon: markRaw(CalendarDaysIcon), iconBg: 'bg-blue-50', iconColor: 'text-blue-600' },
    { label: 'Total Transacciones', value: store.pagos.length, color: 'text-slate-800', icon: markRaw(CreditCardIcon), iconBg: 'bg-slate-100', iconColor: 'text-slate-600' },
    { label: 'Promedio', value: formatMoney(promedio), color: 'text-indigo-600', icon: markRaw(ChartBarIcon), iconBg: 'bg-indigo-50', iconColor: 'text-indigo-600' },
  ]
})

const filtered = computed(() => {
  if (!search.value) return [...store.pagos].sort((a, b) => (b.fecha ?? '').localeCompare(a.fecha ?? ''))
  const q = search.value.toLowerCase()
  return store.pagos
    .filter(p =>
      [
        p.referencia, p.metodo, p.estado, formatMoney(p.monto),
        facturaNumero(p.facturaId), clienteNombre(p.facturaId),
      ].some(v => String(v ?? '').toLowerCase().includes(q))
    )
    .sort((a, b) => (b.fecha ?? '').localeCompare(a.fecha ?? ''))
})

function facturaById(id) {
  return store.facturas.find(f => f.id === id)
}

function facturaNumero(facturaId) {
  const f = facturaById(facturaId)
  return f?.numero ?? `FAC-${String(facturaId).padStart(4, '0')}`
}

function clienteNombre(facturaId) {
  const f = facturaById(facturaId)
  if (!f) return '—'
  return store.clientes.find(c => c.id === f.clienteId)?.nombre ?? '—'
}

function clienteNombreByFactura(f) {
  return store.clientes.find(c => c.id === f.clienteId)?.nombre ?? '—'
}

function clienteByFacturaId(facturaId) {
  const f = facturaById(facturaId)
  if (!f) return null
  return store.clientes.find(c => c.id === f.clienteId)
}

function numeroPago(p) {
  return p.numero ?? `PAG-${String(p.id).padStart(4, '0')}`
}

function metodoIcon(metodo) {
  const map = {
    'Tarjeta Crédito': markRaw(CreditCardIcon),
    'Tarjeta Débito': markRaw(CreditCardIcon),
    Tarjeta: markRaw(CreditCardIcon),
    Transferencia: markRaw(BuildingLibraryIcon),
    Efectivo: markRaw(BanknotesIcon),
  }
  return map[metodo] ?? markRaw(CreditCardIcon)
}

function metodoIconColor(metodo) {
  const map = {
    'Tarjeta Crédito': 'text-blue-500',
    'Tarjeta Débito': 'text-indigo-500',
    Tarjeta: 'text-blue-500',
    Transferencia: 'text-violet-500',
    Efectivo: 'text-emerald-500',
  }
  return map[metodo] ?? 'text-slate-500'
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
    Completado: 'inline-flex text-[10px] font-bold uppercase px-2.5 py-1 rounded-full bg-emerald-50 text-emerald-700 ring-1 ring-emerald-200',
    Pendiente: 'inline-flex text-[10px] font-bold uppercase px-2.5 py-1 rounded-full bg-amber-50 text-amber-700 ring-1 ring-amber-200',
    Rechazado: 'inline-flex text-[10px] font-bold uppercase px-2.5 py-1 rounded-full bg-rose-50 text-rose-700 ring-1 ring-rose-200',
  }
  return map[estado] ?? map.Completado
}

function nextNumero() {
  const nums = store.pagos.map(p => {
    const m = (p.numero ?? '').match(/PAG-(\d+)/)
    return m ? parseInt(m[1], 10) : 0
  })
  return `PAG-${String(Math.max(0, ...nums) + 1).padStart(4, '0')}`
}

function onFacturaChange() {
  const f = facturaById(form.value.facturaId)
  if (f) form.value.monto = f.total
}

function openModal() {
  form.value = blank()
  showModal.value = true
}

function verPago(p) {
  viewing.value = { ...p }
  showView.value = true
}

function save() {
  if (!form.value.facturaId) { alert('Seleccione una factura'); return }
  if (!form.value.monto || form.value.monto <= 0) { alert('El monto debe ser mayor a cero'); return }
  const payload = { ...form.value, numero: nextNumero() }
  store.addItem(store.pagos, payload)
  showModal.value = false
  form.value = blank()
}

function confirmarEliminar(row) {
  if (confirm(`¿Eliminar pago ${row.numero ?? numeroPago(row)}?`)) {
    store.deleteItem(store.pagos, row.id)
    if (viewing.value?.id === row.id) { viewing.value = null; showView.value = false }
  }
}

function generarComprobante(pago) {
  if (!pago) return
  const factura = facturaById(pago.facturaId)
  const cliente = clienteByFacturaId(pago.facturaId)
  generateComprobantePago(pago, factura, cliente)
}
</script>

<style scoped>
.pag-kpi {
  @apply bg-white rounded-2xl border border-slate-100 p-4 min-h-[90px]
         flex items-center justify-between gap-3
         shadow-[0_1px_2px_rgba(0,0,0,0.04),0_4px_16px_rgba(0,0,0,0.05)];
}
.pag-btn-primary {
  @apply inline-flex items-center justify-center gap-2 px-4 py-2.5 rounded-xl text-sm font-semibold text-white
         bg-gradient-to-r from-blue-600 to-indigo-600 shadow-lg shadow-blue-500/25
         hover:shadow-xl hover:-translate-y-0.5 transition-all duration-200;
}
.pag-th {
  @apply px-4 py-3.5 text-left text-xs font-bold text-slate-500 uppercase tracking-wider whitespace-nowrap;
}
.pag-th--center {
  @apply text-center;
}
.pag-td {
  @apply px-4 py-4 text-sm;
}
.pag-td--center {
  @apply text-center;
}
.pag-action {
  @apply p-2 rounded-lg transition-all duration-150 cursor-pointer hover:scale-105;
}
.pag-action--blue {
  @apply text-blue-500 hover:bg-blue-50 hover:text-blue-700;
}
.pag-action--red {
  @apply text-rose-500 hover:bg-rose-50 hover:text-rose-700;
}
.pag-action--slate {
  @apply text-slate-500 hover:bg-slate-100 hover:text-slate-700;
}
</style>
