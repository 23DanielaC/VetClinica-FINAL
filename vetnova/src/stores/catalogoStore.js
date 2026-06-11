import { defineStore } from 'pinia'
import { ref } from 'vue'
import { catalogoService } from '@/services/catalogoService'

export const useCatalogoStore = defineStore('catalogos', () => {
  const medicamentos = ref([])
  const veterinarios = ref([])
  const tiposVacuna = ref([])
  const estadosTurno = ref([])
  const metodosPago = ref([])
  const tiposPago = ref([])
  const cargando = ref(false)
  const error = ref(null)

  async function cargarTodos() {
    cargando.value = true
    error.value = null
    try {
      const [med, vet, tv, et, mp, tp] = await Promise.all([
        catalogoService.medicamentos(),
        catalogoService.veterinarios(),
        catalogoService.tiposVacuna(),
        catalogoService.estadosTurno(),
        catalogoService.metodosPago(),
        catalogoService.tiposPago(),
      ])
      medicamentos.value = med.data.data ?? med.data
      veterinarios.value = vet.data.data ?? vet.data
      tiposVacuna.value = tv.data.data ?? tv.data
      estadosTurno.value = et.data.data ?? et.data
      metodosPago.value = mp.data.data ?? mp.data
      tiposPago.value = tp.data.data ?? tp.data
    } catch (e) {
      error.value = e.response?.data?.message ?? 'Error al cargar catálogos'
    } finally {
      cargando.value = false
    }
  }

  async function cargarMedicamentos() {
    const { data } = await catalogoService.medicamentos()
    medicamentos.value = data.data ?? data
    return medicamentos.value
  }

  async function cargarVeterinarios() {
    const { data } = await catalogoService.veterinarios()
    veterinarios.value = data.data ?? data
    return veterinarios.value
  }

  return {
    medicamentos, veterinarios, tiposVacuna,
    estadosTurno, metodosPago, tiposPago,
    cargando, error,
    cargarTodos, cargarMedicamentos, cargarVeterinarios,
  }
})
