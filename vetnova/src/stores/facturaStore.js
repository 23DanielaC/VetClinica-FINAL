import { defineStore } from 'pinia'
import { ref } from 'vue'
import { facturaService } from '@/services/facturaService'

export const useFacturaStore = defineStore('facturas', () => {
  const facturas = ref([])
  const cargando = ref(false)
  const error = ref(null)

  async function crear(payload) {
    cargando.value = true
    error.value = null
    try {
      const { data } = await facturaService.crear(payload)
      const nueva = data.data ?? data
      facturas.value.push(nueva)
      return nueva
    } catch (e) {
      error.value = e.response?.data?.message ?? 'Error al emitir factura'
      throw e
    } finally {
      cargando.value = false
    }
  }

  return { facturas, cargando, error, crear }
})
