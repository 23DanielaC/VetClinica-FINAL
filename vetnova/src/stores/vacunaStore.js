import { defineStore } from 'pinia'
import { ref } from 'vue'
import { vacunaService } from '@/services/vacunaService'

export const useVacunaStore = defineStore('vacunas', () => {
  const vacunas = ref([])
  const proximas = ref([])
  const cargando = ref(false)
  const error = ref(null)

  async function cargarProximas() {
    cargando.value = true
    error.value = null
    try {
      const { data } = await vacunaService.proximas()
      proximas.value = data.data ?? data
    } catch (e) {
      error.value = e.response?.data?.message ?? 'Error al cargar vacunas próximas'
    } finally {
      cargando.value = false
    }
  }

  async function crear(payload) {
    const { data } = await vacunaService.crear(payload)
    const nueva = data.data ?? data
    vacunas.value.push(nueva)
    return nueva
  }

  return { vacunas, proximas, cargando, error, cargarProximas, crear }
})
