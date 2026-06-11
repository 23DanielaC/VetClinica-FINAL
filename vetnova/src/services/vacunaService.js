import api from './api'

export const vacunaService = {
  /** GET /vacunas/proximas — vacunas por vencer */
  proximas: () => api.get('/vacunas/proximas'),

  /** POST /vacunas — registrar vacuna aplicada */
  crear: (payload) => api.post('/vacunas', payload),
}
