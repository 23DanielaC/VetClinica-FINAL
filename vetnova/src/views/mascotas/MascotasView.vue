<template>
  <div class="relative z-10 space-y-6">
    <!-- Encabezado -->
    <div class="flex flex-col sm:flex-row sm:items-start sm:justify-between gap-4">
      <div>
        <h1 class="text-2xl sm:text-3xl font-extrabold text-slate-900 tracking-tight">Mascotas</h1>
        <p class="text-slate-500 mt-1.5 text-sm sm:text-base">Registro y seguimiento de pacientes</p>
      </div>
      <button type="button" @click="openModal()" class="mas-btn-primary shrink-0">
        <PlusIcon class="w-4 h-4" /> Nueva Mascota
      </button>
    </div>

    <!-- Buscador -->
    <div class="bg-white rounded-2xl border border-slate-100 shadow-[0_1px_2px_rgba(0,0,0,0.04),0_4px_16px_rgba(0,0,0,0.05)] p-4">
      <div class="relative max-w-md">
        <MagnifyingGlassIcon class="w-5 h-5 absolute left-3.5 top-1/2 -translate-y-1/2 text-slate-400" />
        <input
          v-model="search"
          type="search"
          placeholder="Buscar por nombre, especie o raza..."
          class="w-full pl-11 pr-4 py-2.5 text-sm bg-slate-50 border border-slate-200 rounded-xl focus:outline-none focus:ring-2 focus:ring-blue-500/25 focus:border-blue-400 transition"
        />
      </div>
      <p class="text-xs text-slate-400 mt-3">{{ filtered.length }} de {{ store.mascotas.length }} mascotas</p>
    </div>

    <!-- Grid de tarjetas premium -->
    <div v-if="filtered.length" class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6 lg:gap-8">
      <MascotaCard
        v-for="pet in filtered"
        :key="pet.id"
        :pet="pet"
        :ctx="petContext(pet)"
        @view="verDetalle"
        @edit="openModal"
        @history="verHistorial"
        @delete="removePet"
      />
    </div>

    <div v-else class="bg-white rounded-3xl border border-slate-100 shadow-sm text-center py-20 text-slate-400">
      <span class="text-5xl block mb-3">🐾</span>
      <p class="text-sm font-medium">No se encontraron mascotas</p>
    </div>

    <!-- Modal CRUD -->
    <CrudModal v-model="showModal" :title="editing ? 'Editar Mascota' : 'Nueva Mascota'" @confirm="save">
      <div class="space-y-4">
        <div><label class="form-label">Nombre</label><input v-model="form.nombre" class="form-input" /></div>
        <div class="grid grid-cols-2 gap-3">
          <div>
            <label class="form-label">Especie</label>
            <select v-model="form.especie" class="form-input">
              <option>Perro</option><option>Gato</option><option>Ave</option><option>Conejo</option><option>Otro</option>
            </select>
          </div>
          <div><label class="form-label">Raza</label><input v-model="form.raza" class="form-input" /></div>
        </div>
        <div class="grid grid-cols-2 gap-3">
          <div><label class="form-label">Edad (años)</label><input v-model.number="form.edad" type="number" class="form-input" /></div>
          <div><label class="form-label">Peso (kg)</label><input v-model.number="form.peso" type="number" step="0.1" class="form-input" /></div>
        </div>
        <div class="grid grid-cols-2 gap-3">
          <div>
            <label class="form-label">Sexo</label>
            <select v-model="form.sexo" class="form-input">
              <option>Macho</option><option>Hembra</option>
            </select>
          </div>
          <div>
            <label class="form-label">Estado de salud</label>
            <select v-model="form.estadoSalud" class="form-input">
              <option>Saludable</option><option>En seguimiento</option><option>En tratamiento</option>
            </select>
          </div>
        </div>
        <div><label class="form-label">Fecha de nacimiento</label><input v-model="form.fechaNacimiento" type="date" class="form-input" /></div>
        <div>
          <label class="form-label">Propietario</label>
          <select v-model="form.clienteId" class="form-input">
            <option v-for="c in store.clientes" :key="c.id" :value="c.id">{{ c.nombre }}</option>
          </select>
        </div>
      </div>
    </CrudModal>

    <!-- Modal detalle -->
    <CrudModal v-model="showView" title="Detalle de Mascota" hide-footer>
      <template v-if="viewing">
        <div class="text-center mb-4">
          <div :class="['w-20 h-20 mx-auto rounded-full flex items-center justify-center text-4xl bg-gradient-to-br shadow-lg', avatarGradient(viewing.especie)]">
            {{ emojiFor(viewing.especie) }}
          </div>
          <h3 class="text-xl font-bold text-slate-900 mt-3">{{ viewing.nombre }}</h3>
          <p class="text-sm text-slate-500">{{ viewing.especie }} · {{ viewing.raza }}</p>
        </div>
        <dl class="grid grid-cols-2 gap-3 text-sm">
          <div v-for="item in detalleItems(viewing)" :key="item.label">
            <dt class="text-slate-400 text-xs">{{ item.label }}</dt>
            <dd class="font-medium">{{ item.value }}</dd>
          </div>
        </dl>
      </template>
      <template #footer>
        <button type="button" @click="verHistorial(viewing)" class="btn-secondary flex-1 justify-center">Historial</button>
        <button type="button" @click="editarDesdeVista" class="btn-primary flex-1 justify-center">Editar</button>
      </template>
    </CrudModal>

    <!-- Modal historial -->
    <CrudModal v-model="showHistory" :title="`Historial — ${historyPet?.nombre ?? ''}`" hide-footer>
      <template v-if="historyPet">
        <div v-if="historialItems(historyPet).length" class="space-y-3 max-h-80 overflow-y-auto">
          <div
            v-for="(item, i) in historialItems(historyPet)"
            :key="i"
            class="p-3 rounded-xl border border-slate-100 bg-slate-50/50"
          >
            <div class="flex justify-between items-start gap-2">
              <span class="text-xs font-bold text-blue-600 uppercase">{{ item.tipo }}</span>
              <span class="text-xs text-slate-400">{{ item.fecha }}</span>
            </div>
            <p class="text-sm font-medium text-slate-800 mt-1">{{ item.titulo }}</p>
            <p v-if="item.detalle" class="text-xs text-slate-500 mt-0.5">{{ item.detalle }}</p>
          </div>
        </div>
        <p v-else class="text-center text-slate-400 py-8 text-sm">Sin registros médicos</p>
      </template>
      <template #footer>
        <button type="button" @click="showHistory = false" class="btn-secondary w-full justify-center">Cerrar</button>
      </template>
    </CrudModal>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useDataStore } from '@/stores/data'
import CrudModal from '@/components/ui/CrudModal.vue'
import MascotaCard from '@/components/mascotas/MascotaCard.vue'
import { MagnifyingGlassIcon, PlusIcon } from '@heroicons/vue/24/outline'

const store = useDataStore()
const showModal = ref(false)
const showView = ref(false)
const showHistory = ref(false)
const editing = ref(null)
const viewing = ref(null)
const historyPet = ref(null)
const search = ref('')

const emojis = { Perro: '🐶', Gato: '🐱', Ave: '🐦', Conejo: '🐰', Otro: '🐾' }
const avatarGradients = {
  Perro: 'from-orange-400 to-amber-400',
  Gato: 'from-violet-500 to-blue-500',
  Ave: 'from-emerald-400 to-teal-400',
  Conejo: 'from-pink-400 to-violet-400',
  Otro: 'from-blue-500 to-indigo-500',
}

const blank = () => ({
  nombre: '', especie: 'Perro', raza: '', edad: 1, peso: 0, sexo: 'Macho',
  estadoSalud: 'Saludable', fechaNacimiento: '', clienteId: store.clientes[0]?.id ?? '',
})
const form = ref(blank())

const filtered = computed(() => {
  if (!search.value) return store.mascotas
  const q = search.value.toLowerCase()
  return store.mascotas.filter(p =>
    ['nombre', 'especie', 'raza'].some(k => String(p[k] ?? '').toLowerCase().includes(q))
    || clienteNombre(p.clienteId).toLowerCase().includes(q)
  )
})

function clienteNombre(id) {
  return store.clientes.find(c => c.id === id)?.nombre ?? '—'
}

function clienteTelefono(id) {
  return store.clientes.find(c => c.id === id)?.telefono ?? '—'
}

function formatFecha(f) {
  if (!f) return '—'
  return new Date(f + 'T12:00:00').toLocaleDateString('es-CO', { day: 'numeric', month: 'short', year: 'numeric' })
}

function emojiFor(especie) {
  return emojis[especie] ?? emojis.Otro
}

function avatarGradient(especie) {
  return avatarGradients[especie] ?? avatarGradients.Otro
}

function petContext(pet) {
  const turnos = store.turnos.filter(t => t.mascotaId === pet.id)
  const consultas = store.consultas.filter(c => {
    const t = store.turnos.find(tr => tr.id === c.turnoId)
    return t?.mascotaId === pet.id
  })
  const vacunasPet = store.vacunas.filter(v => v.mascotaId === pet.id)
  const vacunasAplicadas = vacunasPet.filter(v => v.estado === 'Aplicada')

  const ultimaConsulta = consultas.length
    ? formatFecha([...consultas].sort((a, b) => b.fecha.localeCompare(a.fecha))[0].fecha)
    : '—'

  const proximas = vacunasPet
    .filter(v => v.proximaDosis && v.estado !== 'Aplicada')
    .map(v => v.proximaDosis)
    .sort()
  const proximaVacuna = proximas.length ? formatFecha(proximas[0])
    : vacunasPet.length ? formatFecha(vacunasPet.sort((a, b) => (b.fechaAplicacion ?? '').localeCompare(a.fechaAplicacion ?? ''))[0]?.proximaDosis)
    : '—'

  const tratamientos = consultas.filter(c => c.tratamiento && c.tratamiento !== 'Ninguno').length
  const estadoSalud = pet.estadoSalud ?? 'Saludable'
  const estadoSaludClass = {
    Saludable: 'text-emerald-600',
    'En seguimiento': 'text-amber-600',
    'En tratamiento': 'text-rose-600',
  }[estadoSalud] ?? 'text-emerald-600'

  return {
    propietario: clienteNombre(pet.clienteId),
    telefono: clienteTelefono(pet.clienteId),
    ultimaConsulta,
    proximaVacuna,
    estadoSalud,
    estadoSaludClass,
    consultas: consultas.length || turnos.length,
    vacunas: vacunasAplicadas.length,
    tratamientos,
    vacunado: vacunasAplicadas.length > 0,
  }
}

function detalleItems(pet) {
  const ctx = petContext(pet)
  return [
    { label: 'Edad', value: `${pet.edad} años` },
    { label: 'Peso', value: `${pet.peso} kg` },
    { label: 'Sexo', value: pet.sexo ?? '—' },
    { label: 'Estado', value: ctx.estadoSalud },
    { label: 'Propietario', value: ctx.propietario },
    { label: 'Teléfono', value: ctx.telefono },
    { label: 'Última consulta', value: ctx.ultimaConsulta },
    { label: 'Próxima vacuna', value: ctx.proximaVacuna },
  ]
}

function historialItems(pet) {
  const items = []
  store.consultas.forEach(c => {
    const t = store.turnos.find(tr => tr.id === c.turnoId)
    if (t?.mascotaId !== pet.id) return
    items.push({
      tipo: 'Consulta',
      fecha: formatFecha(c.fecha),
      titulo: c.diagnostico,
      detalle: c.tratamiento !== 'Ninguno' ? c.tratamiento : null,
      sortKey: c.fecha,
    })
  })
  store.vacunas.filter(v => v.mascotaId === pet.id).forEach(v => {
    items.push({
      tipo: 'Vacuna',
      fecha: formatFecha(v.fechaAplicacion || v.proximaDosis),
      titulo: v.tipoVacuna,
      detalle: `${v.estado} · ${v.laboratorio}`,
      sortKey: v.fechaAplicacion || v.proximaDosis,
    })
  })
  return items.sort((a, b) => (b.sortKey ?? '').localeCompare(a.sortKey ?? ''))
}

function openModal(row = null) {
  editing.value = row
  form.value = row ? { ...row } : blank()
  showModal.value = true
}

function verDetalle(pet) {
  viewing.value = { ...pet }
  showView.value = true
}

function verHistorial(pet) {
  historyPet.value = pet
  showHistory.value = true
  showView.value = false
}

function editarDesdeVista() {
  const p = viewing.value
  showView.value = false
  if (p) openModal(p)
}

function save() {
  if (!form.value.nombre?.trim()) { alert('El nombre es obligatorio'); return }
  if (editing.value) store.updateItem(store.mascotas, editing.value.id, { ...form.value })
  else store.addItem(store.mascotas, { ...form.value })
  showModal.value = false
  editing.value = null
  form.value = blank()
}

function removePet(row) {
  if (confirm(`¿Eliminar a ${row.nombre}?`)) {
    store.deleteItem(store.mascotas, row.id)
    if (viewing.value?.id === row.id) { viewing.value = null; showView.value = false }
  }
}
</script>

<style scoped>
.mas-btn-primary {
  @apply inline-flex items-center justify-center gap-2 px-4 py-2.5 rounded-xl text-sm font-semibold text-white
         bg-gradient-to-r from-blue-600 to-indigo-600 shadow-lg shadow-blue-500/25
         hover:shadow-xl hover:-translate-y-0.5 transition-all duration-200;
}
</style>
