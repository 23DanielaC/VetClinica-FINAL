import api from './api'

export const consultaService = {
  /** POST /consultas — registrar consulta */
  crear: (payload) => api.post('/consultas', payload),

  /** POST /consultas/{id}/medicamentos — asociar medicamentos */
  agregarMedicamentos: (consultaId, medicamentos) =>
    api.post(`/consultas/${consultaId}/medicamentos`, { medicamentos }),
}
