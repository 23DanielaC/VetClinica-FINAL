import api from './api'

export const catalogoService = {
  /** GET /catalogos/medicamentos */
  medicamentos: () => api.get('/catalogos/medicamentos'),

  /** GET /catalogos/veterinarios */
  veterinarios: () => api.get('/catalogos/veterinarios'),

  /** GET /catalogos/tipos-vacuna */
  tiposVacuna: () => api.get('/catalogos/tipos-vacuna'),

  /** GET /catalogos/estados-turno */
  estadosTurno: () => api.get('/catalogos/estados-turno'),

  /** GET /catalogos/metodos-pago */
  metodosPago: () => api.get('/catalogos/metodos-pago'),

  /** GET /catalogos/tipos-pago */
  tiposPago: () => api.get('/catalogos/tipos-pago'),
}
