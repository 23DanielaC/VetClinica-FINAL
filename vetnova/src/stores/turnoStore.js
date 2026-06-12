import { defineStore } from 'pinia'
import { ref } from 'vue'
import { turnoService } from '@/services/turnoService'

export const useTurnoStore = defineStore('turnos', () => {
  const turnos = ref([])
  const cargando = ref(false)
  const error = ref(null)

  async function cargar() {
    cargando.value = true
    error.value = null
    try {
      const { data } = await turnoService.listar()
      turnos.value = data.data ?? data
    } catch (e) {
      error.value = e.response?.data?.message ?? 'Error al cargar turnos'
    } finally {
      cargando.value = false
    }
  }

  async function crear(payload) {
    const { data } = await turnoService.crear(payload)
    const nuevo = data.data ?? data
    turnos.value.push(nuevo)
    return nuevo
  }

  async function actualizarEstado(turnoId, estado) {
    const { data } = await turnoService.actualizarEstado(turnoId, estado)
    const actualizado = data.data ?? data
    const idx = turnos.value.findIndex((t) => t.id === turnoId)
    if (idx !== -1) turnos.value[idx] = { ...turnos.value[idx], ...actualizado }
    return actualizado
  }

  return { turnos, cargando, error, cargar, crear, actualizarEstado }
})
