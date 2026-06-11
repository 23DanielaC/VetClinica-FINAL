<template>
  <div class="relative z-10 space-y-6">
    <!-- Encabezado -->
    <div class="flex flex-col lg:flex-row lg:items-end lg:justify-between gap-4">
      <div>
        <h1 class="text-2xl sm:text-3xl font-extrabold text-slate-900 tracking-tight">Consultas</h1>
        <p class="text-slate-500 mt-1.5 text-sm sm:text-base">Historial clínico veterinario</p>
      </div>
      <button type="button" @click="openModal()" class="cons-btn-primary shrink-0">
        <PlusIcon class="w-4 h-4" /> Nueva Consulta
      </button>
    </div>

    <!-- Filtros -->
    <div class="bg-white rounded-2xl border border-slate-100 shadow-[0_1px_2px_rgba(0,0,0,0.04),0_4px_16px_rgba(0,0,0,0.04)] p-4 lg:p-5">
      <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-5 gap-3">
        <div class="lg:col-span-2 relative">
          <MagnifyingGlassIcon class="w-5 h-5 absolute left-3.5 top-1/2 -translate-y-1/2 text-slate-400" />
          <input v-model="filters.search" type="search" placeholder="Buscar diagnóstico, mascota, propietario..."
            class="w-full pl-11 pr-4 py-2.5 text-sm bg-slate-50 border border-slate-200 rounded-xl focus:outline-none focus:ring-2 focus:ring-teal-500/25 focus:border-teal-400 transition" />
        </div>
        <input v-model="filters.fecha" type="date" class="cons-select" />
        <select v-model="filters.veterinarioId" class="cons-select">
          <option value="">Todos los veterinarios</option>
          <option v-for="v in store.veterinarios" :key="v.id" :value="v.id">{{ v.nombre }}</option>
        </select>
        <select v-model="filters.mascotaId" class="cons-select">
          <option value="">Todas las mascotas</option>
          <option v-for="m in store.mascotas" :key="m.id" :value="m.id">{{ m.nombre }}</option>
        </select>
        <select v-model="filters.tipo" class="cons-select sm:col-span-2 lg:col-span-1">
          <option value="">Tipo de consulta</option>
          <option v-for="t in tiposConsulta" :key="t" :value="t">{{ t }}</option>
        </select>
      </div>
      <p class="text-xs text-slate-400 mt-3">{{ filtered.length }} de {{ store.consultas.length }} consultas</p>
    </div>

    <!-- KPIs -->
    <div class="grid grid-cols-2 lg:grid-cols-4 gap-4">
      <div v-for="kpi in kpis" :key="kpi.label" class="cons-kpi">
        <div :class="['w-11 h-11 rounded-xl flex items-center justify-center bg-gradient-to-br shadow-md', kpi.gradient]">
          <component :is="kpi.icon" class="w-5 h-5 text-white" />
        </div>
        <p class="text-2xl font-extrabold text-slate-900 mt-3 tabular-nums">{{ kpi.value }}</p>
        <p class="text-sm font-semibold text-slate-700">{{ kpi.label }}</p>
        <span :class="['inline-flex text-xs font-bold mt-1', kpi.changeClass]">{{ kpi.change }}</span>
      </div>
    </div>

    <!-- Layout principal -->
    <div class="grid grid-cols-1 xl:grid-cols-12 gap-6">
      <!-- Historial -->
      <div class="xl:col-span-9 space-y-3">
        <h2 class="text-lg font-bold text-slate-900 mb-1">Historial clínico</h2>
        <ConsultaHistorialCard
          v-for="c in filtered"
          :key="c.id"
          :consulta="c"
          :mascota-nombre="ctx(c).mascotaNombre"
          :propietario="ctx(c).propietario"
          :vet-nombre="ctx(c).vetNombre"
          :motivo="ctx(c).motivo"
          :emoji="ctx(c).emoji"
          :avatar-gradient="ctx(c).avatarGradient"
          @view="verConsulta"
          @edit="openModal"
          @pdf="descargarPdf"
          @delete="confirmarEliminar"
        />
        <div v-if="!filtered.length" class="bg-white rounded-2xl border border-slate-100 py-16 text-center">
          <ClipboardDocumentListIcon class="w-12 h-12 text-slate-300 mx-auto mb-3" />
          <p class="text-slate-500">No se encontraron consultas</p>
        </div>
      </div>

      <!-- Panel lateral -->
      <div class="xl:col-span-3">
        <div class="bg-white rounded-2xl border border-slate-100 shadow-[0_1px_2px_rgba(0,0,0,0.04),0_4px_20px_rgba(0,0,0,0.05)] p-5 sticky top-4 space-y-5">
          <h3 class="text-base font-bold text-slate-900">Estadísticas</h3>

          <div class="p-4 rounded-xl bg-gradient-to-br from-teal-50 to-emerald-50 border border-teal-100">
            <p class="text-xs font-semibold text-teal-700 uppercase">Ingresos del mes</p>
            <p class="text-2xl font-extrabold text-slate-900 mt-1">${{ stats.ingresosMes.toLocaleString('es-CO') }}</p>
          </div>

          <div>
            <p class="text-xs font-semibold text-slate-400 uppercase mb-2">Veterinario destacado</p>
            <p class="text-sm font-bold text-slate-800">{{ stats.topVet?.nombre ?? '—' }}</p>
            <p class="text-xs text-slate-400">{{ stats.topVet?.count ?? 0 }} consultas</p>
          </div>

          <div>
            <p class="text-xs font-semibold text-slate-400 uppercase mb-2">Mascota con más visitas</p>
            <p class="text-sm font-bold text-slate-800">{{ stats.topMascota?.nombre ?? '—' }}</p>
            <p class="text-xs text-slate-400">{{ stats.topMascota?.count ?? 0 }} visitas</p>
          </div>

          <div class="pt-3 border-t border-slate-100">
            <p class="text-xs font-semibold text-slate-400 uppercase">Consultas esta semana</p>
            <p class="text-3xl font-extrabold text-slate-900 mt-1">{{ stats.semana }}</p>
          </div>
        </div>
      </div>
    </div>

    <!-- Modal CRUD -->
    <CrudModal v-model="showModal" :title="editing ? 'Editar Consulta' : 'Nueva Consulta'" @confirm="save">
      <div class="space-y-4">
        <div><label class="form-label">Turno relacionado</label>
          <select v-model="form.turnoId" class="form-input">
            <option v-for="t in store.turnos" :key="t.id" :value="t.id">
              {{ t.fecha }} {{ t.hora }} – {{ clienteNombre(t.clienteId) }}
            </option>
          </select>
        </div>
        <div class="grid grid-cols-2 gap-3">
          <div><label class="form-label">Fecha</label><input v-model="form.fecha" type="date" class="form-input" /></div>
          <div><label class="form-label">Hora</label><input v-model="form.hora" type="time" class="form-input" /></div>
        </div>
        <div class="grid grid-cols-2 gap-3">
          <div><label class="form-label">Estado</label>
            <select v-model="form.estado" class="form-input">
              <option>Finalizada</option><option>Pendiente</option><option>Seguimiento</option><option>Cancelada</option>
            </select>
          </div>
          <div><label class="form-label">Costo (COP)</label><input v-model.number="form.costo" type="number" class="form-input" /></div>
        </div>
        <div><label class="form-label">Diagnóstico</label><textarea v-model="form.diagnostico" class="form-input" rows="3" /></div>
        <div><label class="form-label">Tratamiento</label><textarea v-model="form.tratamiento" class="form-input" rows="3" /></div>
        <div><label class="form-label">Observaciones</label><textarea v-model="form.observaciones" class="form-input" rows="2" /></div>
      </div>
    </CrudModal>

    <!-- Modal ver -->
    <CrudModal v-model="showView" title="Historial clínico completo" hide-footer>
      <template v-if="viewing">
        <div class="flex items-center gap-4 mb-5 pb-4 border-b border-slate-100">
          <div :class="['w-14 h-14 rounded-2xl bg-gradient-to-br flex items-center justify-center text-2xl', ctx(viewing).avatarGradient]">
            {{ ctx(viewing).emoji }}
          </div>
          <div>
            <h3 class="text-lg font-bold text-slate-900">{{ ctx(viewing).mascotaNombre }}</h3>
            <p class="text-sm text-slate-500">{{ ctx(viewing).propietario }} · {{ ctx(viewing).vetNombre }}</p>
            <p class="text-xs text-slate-400 mt-0.5">{{ viewing.fecha }} {{ viewing.hora }} · {{ viewing.estado }}</p>
          </div>
        </div>
        <dl class="space-y-4 text-sm">
          <div><dt class="text-xs font-bold text-slate-400 uppercase">Motivo</dt><dd class="mt-1 text-slate-800">{{ ctx(viewing).motivo }}</dd></div>
          <div><dt class="text-xs font-bold text-slate-400 uppercase">Diagnóstico</dt><dd class="mt-1 text-slate-800">{{ viewing.diagnostico }}</dd></div>
          <div><dt class="text-xs font-bold text-slate-400 uppercase">Tratamiento</dt><dd class="mt-1 text-slate-800">{{ viewing.tratamiento }}</dd></div>
          <div><dt class="text-xs font-bold text-slate-400 uppercase">Observaciones</dt><dd class="mt-1 text-slate-800">{{ viewing.observaciones || '—' }}</dd></div>
          <div><dt class="text-xs font-bold text-slate-400 uppercase">Costo</dt><dd class="mt-1 text-xl font-bold text-slate-900">${{ (viewing.costo ?? 0).toLocaleString('es-CO') }}</dd></div>
        </dl>
      </template>
      <template #footer>
        <button type="button" @click="descargarPdf(viewing)" class="btn-secondary flex-1 justify-center">Descargar PDF</button>
        <button type="button" @click="editarDesdeVista" class="btn-primary flex-1 justify-center">Editar</button>
      </template>
    </CrudModal>
  </div>
</template>

<script setup>
import { ref, computed, reactive, markRaw } from 'vue'
import { useDataStore } from '@/stores/data'
import CrudModal from '@/components/ui/CrudModal.vue'
import ConsultaHistorialCard from '@/components/consultas/ConsultaHistorialCard.vue'
import {
  PlusIcon, MagnifyingGlassIcon, ClipboardDocumentListIcon,
  ClipboardDocumentCheckIcon, CalendarDaysIcon, BanknotesIcon, ChartBarIcon,
} from '@heroicons/vue/24/outline'

const store = useDataStore()
const showModal = ref(false)
const showView = ref(false)
const editing = ref(null)
const viewing = ref(null)
const filters = reactive({ search: '', fecha: '', veterinarioId: '', mascotaId: '', tipo: '' })

const petEmojis = { Perro: '🐶', Gato: '🐱', Ave: '🐦', Conejo: '🐰', Otro: '🐾' }
const petGradients = {
  Perro: 'from-amber-400 to-orange-500', Gato: 'from-violet-400 to-purple-600',
  Ave: 'from-sky-400 to-blue-500', Conejo: 'from-rose-300 to-pink-500', Otro: 'from-slate-400 to-slate-600',
}

const blank = () => ({
  turnoId: store.turnos[0]?.id ?? '',
  fecha: new Date().toISOString().slice(0, 10),
  hora: '10:00',
  diagnostico: '',
  tratamiento: '',
  observaciones: '',
  estado: 'Finalizada',
  costo: 50000,
})
const form = ref(blank())

const tiposConsulta = computed(() => {
  const set = new Set()
  store.turnos.forEach(t => { if (t.motivo) set.add(t.motivo) })
  return [...set]
})

function turnoDe(consulta) { return store.turnos.find(t => t.id === consulta.turnoId) }

function ctx(consulta) {
  const t = turnoDe(consulta)
  const mascota = t ? store.mascotas.find(m => m.id === t.mascotaId) : null
  const cliente = t ? store.clientes.find(c => c.id === t.clienteId) : null
  const vet = t ? store.veterinarios.find(v => v.id === t.veterinarioId) : null
  const esp = mascota?.especie ?? 'Otro'
  return {
    mascotaNombre: mascota?.nombre ?? '—',
    propietario: cliente?.nombre ?? '—',
    vetNombre: vet?.nombre ?? '—',
    motivo: t?.motivo ?? 'Consulta general',
    emoji: petEmojis[esp] ?? '🐾',
    avatarGradient: petGradients[esp] ?? petGradients.Otro,
    veterinarioId: t?.veterinarioId,
    mascotaId: t?.mascotaId,
  }
}

function clienteNombre(id) { return store.clientes.find(c => c.id === id)?.nombre ?? id }

const filtered = computed(() => {
  let list = [...store.consultas]
  if (filters.search) {
    const q = filters.search.toLowerCase()
    list = list.filter(c => {
      const x = ctx(c)
      return [c.diagnostico, c.tratamiento, x.mascotaNombre, x.propietario, x.vetNombre, x.motivo]
        .some(v => String(v ?? '').toLowerCase().includes(q))
    })
  }
  if (filters.fecha) list = list.filter(c => c.fecha === filters.fecha)
  if (filters.veterinarioId) list = list.filter(c => ctx(c).veterinarioId === filters.veterinarioId)
  if (filters.mascotaId) list = list.filter(c => ctx(c).mascotaId === filters.mascotaId)
  if (filters.tipo) list = list.filter(c => ctx(c).motivo === filters.tipo)
  return list.sort((a, b) => `${b.fecha}${b.hora}`.localeCompare(`${a.fecha}${a.hora}`))
})

const hoy = new Date().toISOString().slice(0, 10)
const mesKey = hoy.slice(0, 7)

const kpis = computed(() => {
  const total = store.consultas.length
  const hoyCount = store.consultas.filter(c => c.fecha === hoy).length
  const mesCount = store.consultas.filter(c => c.fecha?.startsWith(mesKey)).length
  const ingresos = store.consultas.reduce((s, c) => s + (c.costo ?? 0), 0)
  return [
    { label: 'Total Consultas', value: total, change: '+8% mes', changeClass: 'text-emerald-600', icon: markRaw(ClipboardDocumentCheckIcon), gradient: 'from-teal-500 to-emerald-400' },
    { label: 'Consultas Hoy', value: hoyCount, change: 'En vivo', changeClass: 'text-blue-600', icon: markRaw(CalendarDaysIcon), gradient: 'from-blue-500 to-indigo-500' },
    { label: 'Este Mes', value: mesCount, change: '+12% vs ant.', changeClass: 'text-violet-600', icon: markRaw(ChartBarIcon), gradient: 'from-violet-500 to-purple-500' },
    { label: 'Ingresos', value: `$${(ingresos / 1000).toFixed(0)}k`, change: '+15% mes', changeClass: 'text-amber-600', icon: markRaw(BanknotesIcon), gradient: 'from-amber-500 to-orange-400' },
  ]
})

const stats = computed(() => {
  const now = new Date()
  const weekAgo = new Date(now)
  weekAgo.setDate(weekAgo.getDate() - 7)
  const semana = store.consultas.filter(c => new Date(c.fecha) >= weekAgo).length
  const ingresosMes = store.consultas.filter(c => c.fecha?.startsWith(mesKey)).reduce((s, c) => s + (c.costo ?? 0), 0)

  const vetCounts = {}
  const mascotaCounts = {}
  store.consultas.forEach(c => {
    const x = ctx(c)
    if (x.vetNombre !== '—') vetCounts[x.vetNombre] = (vetCounts[x.vetNombre] || 0) + 1
    if (x.mascotaNombre !== '—') mascotaCounts[x.mascotaNombre] = (mascotaCounts[x.mascotaNombre] || 0) + 1
  })
  const topVetName = Object.entries(vetCounts).sort((a, b) => b[1] - a[1])[0]
  const topMascotaName = Object.entries(mascotaCounts).sort((a, b) => b[1] - a[1])[0]

  return {
    ingresosMes,
    semana,
    topVet: topVetName ? { nombre: topVetName[0], count: topVetName[1] } : null,
    topMascota: topMascotaName ? { nombre: topMascotaName[0], count: topMascotaName[1] } : null,
  }
})

function openModal(row = null) {
  editing.value = row
  form.value = row ? { ...row, hora: row.hora ?? '10:00', costo: row.costo ?? 0, estado: row.estado ?? 'Finalizada' } : blank()
  showModal.value = true
}

function verConsulta(c) { viewing.value = { ...c }; showView.value = true }

function editarDesdeVista() {
  const c = viewing.value
  showView.value = false
  if (c) openModal(c)
}

function save() {
  if (!form.value.diagnostico?.trim()) { alert('El diagnóstico es obligatorio'); return }
  if (editing.value) store.updateItem(store.consultas, editing.value.id, { ...form.value })
  else store.addItem(store.consultas, { ...form.value })
  showModal.value = false
  editing.value = null
  form.value = blank()
}

function confirmarEliminar(row) {
  if (confirm('¿Eliminar esta consulta del historial?')) {
    store.deleteItem(store.consultas, row.id)
    if (viewing.value?.id === row.id) { viewing.value = null; showView.value = false }
  }
}

function descargarPdf(consulta) {
  if (!consulta) return
  const x = ctx(consulta)
  const html = `<!DOCTYPE html><html><head><meta charset="utf-8"><title>Consulta ${x.mascotaNombre}</title>
<style>body{font-family:sans-serif;padding:40px;max-width:700px;margin:0 auto}h1{color:#0f766e}hr{margin:20px 0;border:none;border-top:1px solid #e2e8f0}p{margin:8px 0}</style></head><body>
<h1>VetClinica — Informe Clínico</h1><hr>
<p><strong>Mascota:</strong> ${x.mascotaNombre}</p>
<p><strong>Propietario:</strong> ${x.propietario}</p>
<p><strong>Veterinario:</strong> ${x.vetNombre}</p>
<p><strong>Fecha:</strong> ${consulta.fecha} ${consulta.hora ?? ''}</p>
<p><strong>Estado:</strong> ${consulta.estado ?? 'Finalizada'}</p>
<p><strong>Motivo:</strong> ${x.motivo}</p>
<p><strong>Diagnóstico:</strong> ${consulta.diagnostico}</p>
<p><strong>Tratamiento:</strong> ${consulta.tratamiento}</p>
<p><strong>Observaciones:</strong> ${consulta.observaciones ?? '—'}</p>
<p><strong>Costo:</strong> $${(consulta.costo ?? 0).toLocaleString('es-CO')}</p>
</body></html>`
  const blob = new Blob([html], { type: 'text/html' })
  const a = document.createElement('a')
  a.href = URL.createObjectURL(blob)
  a.download = `consulta-${x.mascotaNombre.replace(/\s/g, '-')}.html`
  a.click()
}
</script>

<style scoped>
.cons-btn-primary {
  @apply inline-flex items-center gap-2 px-4 py-2.5 rounded-xl text-sm font-semibold text-white
         bg-gradient-to-r from-teal-600 to-emerald-600 shadow-lg shadow-teal-500/25
         hover:shadow-xl hover:-translate-y-0.5 transition-all duration-200;
}
.cons-select {
  @apply w-full px-3 py-2.5 text-sm bg-slate-50 border border-slate-200 rounded-xl
         focus:outline-none focus:ring-2 focus:ring-teal-500/25 focus:border-teal-400 transition;
}
.cons-kpi {
  @apply bg-white rounded-2xl border border-slate-100 p-5
         shadow-[0_1px_2px_rgba(0,0,0,0.04),0_4px_16px_rgba(0,0,0,0.05)]
         hover:shadow-[0_8px_24px_rgba(0,0,0,0.08)] transition-shadow duration-300;
}
</style>
