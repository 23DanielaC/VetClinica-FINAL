import api from './api'

export const turnoService = {
  /** GET /turnos — listado de turnos */
  listar: () => api.get('/turnos'),

  /** POST /turnos — agendar turno */
  crear: (payload) => api.post('/turnos', payload),

  /** PATCH /turnos/{id}/estado — actualizar estado del turno */
  actualizarEstado: (turnoId, estado) =>
    api.patch(`/turnos/${turnoId}/estado`, { estado }),
}
