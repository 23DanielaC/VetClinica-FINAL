import api from './api'

export const clienteService = {
  /** GET /clientes — listado completo */
  listar: () => api.get('/clientes'),

  /** POST /clientes — crear cliente */
  crear: (payload) => api.post('/clientes', payload),

  /** GET /clientes/{id}/mascotas — mascotas del cliente */
  mascotas: (clienteId) => api.get(`/clientes/${clienteId}/mascotas`),
}
