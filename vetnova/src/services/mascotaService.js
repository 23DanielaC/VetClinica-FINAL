import api from './api'

export const mascotaService = {
  /** POST /mascotas — registrar mascota */
  crear: (payload) => api.post('/mascotas', payload),
}
