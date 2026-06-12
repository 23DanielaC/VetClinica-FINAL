import { defineStore } from 'pinia'
import { ref } from 'vue'
import { mascotaService } from '@/services/mascotaService'

export const useMascotaStore = defineStore('mascotas', () => {
  const mascotas = ref([])
  const cargando = ref(false)
  const error = ref(null)

  async function crear(payload) {
    cargando.value = true
    error.value = null
    try {
      const { data } = await mascotaService.crear(payload)
      const nueva = data.data ?? data
      mascotas.value.push(nueva)
      return nueva
    } catch (e) {
      error.value = e.response?.data?.message ?? 'Error al registrar mascota'
      throw e
    } finally {
      cargando.value = false
    }
  }

  return { mascotas, cargando, error, crear }
})
