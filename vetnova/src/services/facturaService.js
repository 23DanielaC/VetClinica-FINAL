import api from './api'

export const facturaService = {
  /** POST /facturas — emitir factura */
  crear: (payload) => api.post('/facturas', payload),
}
