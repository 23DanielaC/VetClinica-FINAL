import { defineStore } from 'pinia'
import { ref } from 'vue'
import { clienteService } from '@/services/clienteService'

export const useClienteStore = defineStore('clientes', () => {
  const clientes = ref([])
  const cargando = ref(false)
  const error = ref(null)

  async function cargar() {
    cargando.value = true
    error.value = null
    try {
      const { data } = await clienteService.listar()
      clientes.value = data.data ?? data
    } catch (e) {
      error.value = e.response?.data?.message ?? 'Error al cargar clientes'
    } finally {
      cargando.value = false
    }
  }

  async function crear(payload) {
    const { data } = await clienteService.crear(payload)
    const nuevo = data.data ?? data
    clientes.value.push(nuevo)
    return nuevo
  }

  async function obtenerMascotas(clienteId) {
    const { data } = await clienteService.mascotas(clienteId)
    return data.data ?? data
  }

  return { clientes, cargando, error, cargar, crear, obtenerMascotas }
})
