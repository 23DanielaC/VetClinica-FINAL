import api from './api'

export const pagoService = {
  /** POST /pagos — registrar pago */
  crear: (payload) => api.post('/pagos', payload),
}
