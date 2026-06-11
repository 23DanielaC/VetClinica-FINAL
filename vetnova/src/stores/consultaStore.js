import { defineStore } from 'pinia'
import { ref } from 'vue'
import { consultaService } from '@/services/consultaService'

export const useConsultaStore = defineStore('consultas', () => {
  const consultas = ref([])
  const cargando = ref(false)
  const error = ref(null)

  async function crear(payload) {
    cargando.value = true
    error.value = null
    try {
      const { data } = await consultaService.crear(payload)
      const nueva = data.data ?? data
      consultas.value.push(nueva)
      return nueva
    } catch (e) {
      error.value = e.response?.data?.message ?? 'Error al registrar consulta'
      throw e
    } finally {
      cargando.value = false
    }
  }

  async function agregarMedicamentos(consultaId, medicamentos) {
    const { data } = await consultaService.agregarMedicamentos(consultaId, medicamentos)
    return data.data ?? data
  }

  return { consultas, cargando, error, crear, agregarMedicamentos }
})
