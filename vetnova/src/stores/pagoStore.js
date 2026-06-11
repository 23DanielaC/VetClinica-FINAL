import { defineStore } from 'pinia'
import { ref } from 'vue'
import { pagoService } from '@/services/pagoService'

export const usePagoStore = defineStore('pagos', () => {
  const pagos = ref([])
  const cargando = ref(false)
  const error = ref(null)

  async function crear(payload) {
    cargando.value = true
    error.value = null
    try {
      const { data } = await pagoService.crear(payload)
      const nuevo = data.data ?? data
      pagos.value.push(nuevo)
      return nuevo
    } catch (e) {
      error.value = e.response?.data?.message ?? 'Error al registrar pago'
      throw e
    } finally {
      cargando.value = false
    }
  }

  return { pagos, cargando, error, crear }
})
