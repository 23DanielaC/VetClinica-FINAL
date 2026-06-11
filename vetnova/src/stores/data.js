import { defineStore } from 'pinia'
import { ref } from 'vue'

const makeId = () => Date.now() + Math.random().toString(36).slice(2)

export const useDataStore = defineStore('data', () => {
  // ── Clientes ──────────────────────────────────────────────
  const clientes = ref([
    { id: '1', nombre: 'María García', documento: '1023456789', telefono: '555-0101', email: 'maria@email.com', direccion: 'Calle 10 #5-20', fechaRegistro: '2024-01-15', estado: 'Activo' },
    { id: '2', nombre: 'Carlos López', documento: '801234567', telefono: '555-0202', email: 'carlos@email.com', direccion: 'Carrera 7 #80-40', fechaRegistro: '2024-02-20', estado: 'Activo' },
    { id: '3', nombre: 'Ana Martínez', documento: '529876543', telefono: '555-0303', email: 'ana@email.com', direccion: 'Av. 68 #12-30', fechaRegistro: '2024-03-05', estado: 'Pendiente' },
  ])

  // ── Mascotas ──────────────────────────────────────────────
  const mascotas = ref([
    { id: '1', nombre: 'Max', especie: 'Perro', raza: 'Labrador', edad: 3, peso: 28.5, sexo: 'Macho', clienteId: '1', fechaNacimiento: '2021-04-10', estadoSalud: 'Saludable' },
    { id: '2', nombre: 'Luna', especie: 'Gato', raza: 'Siamés', edad: 2, peso: 4.2, sexo: 'Hembra', clienteId: '1', fechaNacimiento: '2022-06-15', estadoSalud: 'Saludable' },
    { id: '3', nombre: 'Rocky', especie: 'Perro', raza: 'Bulldog Francés', edad: 5, peso: 22.0, sexo: 'Macho', clienteId: '2', fechaNacimiento: '2019-11-20', estadoSalud: 'En seguimiento' },
  ])

  // ── Veterinarios ──────────────────────────────────────────
  const veterinarios = ref([
    { id: '1', nombre: 'Dr. Andrés Pérez', especialidad: 'Cirugía', telefono: '555-1001', email: 'andres@vetnova.com', matricula: 'VET-001', estado: 'Activo' },
    { id: '2', nombre: 'Dra. Laura Sánchez', especialidad: 'Dermatología', telefono: '555-1002', email: 'laura@vetnova.com', matricula: 'VET-002', estado: 'Activo' },
    { id: '3', nombre: 'Dr. Roberto Torres', especialidad: 'Medicina General', telefono: '555-1003', email: 'roberto@vetnova.com', matricula: 'VET-003', estado: 'Inactivo' },
  ])

  // ── Turnos ────────────────────────────────────────────────
  const turnos = ref([
    { id: '1', clienteId: '1', mascotaId: '1', veterinarioId: '1', fecha: '2026-06-10', hora: '09:00', duracion: 45, motivo: 'Control anual', estado: 'Confirmado' },
    { id: '2', clienteId: '2', mascotaId: '3', veterinarioId: '3', fecha: '2026-06-10', hora: '10:30', duracion: 30, motivo: 'Vacunación', estado: 'Pendiente' },
    { id: '3', clienteId: '3', mascotaId: '2', veterinarioId: '2', fecha: '2026-06-11', hora: '14:00', duracion: 30, motivo: 'Revisión piel', estado: 'Confirmado' },
  ])

  // ── Consultas ─────────────────────────────────────────────
  const consultas = ref([
    { id: '1', turnoId: '1', diagnostico: 'Animal sano, en buen estado', tratamiento: 'Ninguno', observaciones: 'Control en 6 meses', fecha: '2026-06-10', hora: '09:45', estado: 'Finalizada', costo: 85000 },
    { id: '2', turnoId: '2', diagnostico: 'Otitis leve', tratamiento: 'Gotas óticas x 7 días', observaciones: 'Revisión en 1 semana', fecha: '2026-06-08', hora: '11:00', estado: 'Seguimiento', costo: 62000 },
    { id: '3', turnoId: '3', diagnostico: 'Dermatitis alérgica leve', tratamiento: 'Shampoo medicado + antihistamínico', observaciones: 'Evitar polvo', fecha: '2026-06-05', hora: '14:30', estado: 'Finalizada', costo: 95000 },
  ])

  // ── Medicamentos ──────────────────────────────────────────
  const medicamentos = ref([
    { id: '1', nombre: 'Amoxicilina', presentacion: 'Tabletas', dosis: '500mg', stock: 120, precio: 15000, laboratorio: 'Bayer', vencimiento: '2026-07-31', proveedor: 'FarmVet' },
    { id: '2', nombre: 'Ivermectina', presentacion: 'Inyectable', dosis: '10%', stock: 35, precio: 28000, laboratorio: 'Merial', vencimiento: '2026-08-14', proveedor: 'VetPharma' },
    { id: '3', nombre: 'Prednisolona', presentacion: 'Tabletas', dosis: '5mg', stock: 8, precio: 9800, laboratorio: 'Pfizer', vencimiento: '2027-03-15', proveedor: 'FarmVet' },
    { id: '4', nombre: 'Dipirona', presentacion: 'Gotas', dosis: '20mg', stock: 15, precio: 42000, laboratorio: 'Roche', vencimiento: '2026-06-30', proveedor: 'VetPharma' },
    { id: '5', nombre: 'Metronidazol', presentacion: 'Líquido', dosis: '250mg', stock: 52, precio: 18500, laboratorio: 'Virbac', vencimiento: '2027-01-20', proveedor: 'FarmVet' },
  ])

  // ── Vacunas (registros de vacunación) ─────────────────────
  const vacunas = ref([
    { id: '1', mascotaId: '1', tipoVacuna: 'Rabia', laboratorio: 'Pfizer', fechaAplicacion: '2026-06-05', proximaDosis: '2027-06-05', veterinarioId: '1', estado: 'Aplicada' },
    { id: '2', mascotaId: '2', tipoVacuna: 'Triple Felina', laboratorio: 'Bayer', fechaAplicacion: '2026-06-08', proximaDosis: '2027-06-08', veterinarioId: '2', estado: 'Aplicada' },
    { id: '3', mascotaId: '3', tipoVacuna: 'Moquillo', laboratorio: 'Merial', fechaAplicacion: '2024-05-14', proximaDosis: '2026-06-20', veterinarioId: '3', estado: 'Programada' },
    { id: '4', mascotaId: '1', tipoVacuna: 'Parvovirus', laboratorio: 'Virbac', fechaAplicacion: '', proximaDosis: '2026-06-15', veterinarioId: '1', estado: 'Pendiente' },
    { id: '5', mascotaId: '3', tipoVacuna: 'Gripe Canina', laboratorio: 'Boehringer', fechaAplicacion: '2024-06-04', proximaDosis: '2025-06-04', veterinarioId: '2', estado: 'Aplicada' },
    { id: '6', mascotaId: '2', tipoVacuna: 'Leucemia Felina', laboratorio: 'Merial', fechaAplicacion: '', proximaDosis: '2026-06-25', veterinarioId: '2', estado: 'Programada' },
    { id: '7', mascotaId: '1', tipoVacuna: 'Antirrábica', laboratorio: 'Zoetis', fechaAplicacion: '', proximaDosis: '2026-06-12', veterinarioId: '3', estado: 'Pendiente' },
  ])

  // ── Facturas ──────────────────────────────────────────────
  const facturas = ref([
    {
      id: '1', numero: 'FAC-0001', clienteId: '1', fecha: '2026-05-10', vencimiento: '2026-06-10',
      subtotal: 85000, iva: 16150, total: 101150, estado: 'Pagada', metodoPago: 'Tarjeta',
      items: [
        { tipo: 'Consulta', descripcion: 'Control anual — Max', cantidad: 1, precioUnitario: 60000, subtotal: 60000 },
        { tipo: 'Vacuna', descripcion: 'Rabia', cantidad: 1, precioUnitario: 25000, subtotal: 25000 },
      ],
    },
    {
      id: '2', numero: 'FAC-0002', clienteId: '2', fecha: '2026-05-15', vencimiento: '2026-05-30',
      subtotal: 62000, iva: 11780, total: 73780, estado: 'Pendiente', metodoPago: '—',
      items: [
        { tipo: 'Consulta', descripcion: 'Otitis leve — Rocky', cantidad: 1, precioUnitario: 45000, subtotal: 45000 },
        { tipo: 'Medicamento', descripcion: 'Gotas óticas', cantidad: 1, precioUnitario: 17000, subtotal: 17000 },
      ],
    },
    {
      id: '3', numero: 'FAC-0003', clienteId: '3', fecha: '2026-05-20', vencimiento: '2026-06-20',
      subtotal: 120000, iva: 22800, total: 142800, estado: 'Pagada', metodoPago: 'Efectivo',
      items: [
        { tipo: 'Consulta', descripcion: 'Dermatitis alérgica — Luna', cantidad: 1, precioUnitario: 75000, subtotal: 75000 },
        { tipo: 'Medicamento', descripcion: 'Shampoo medicado + antihistamínico', cantidad: 1, precioUnitario: 45000, subtotal: 45000 },
      ],
    },
    {
      id: '4', numero: 'FAC-0004', clienteId: '1', fecha: '2026-06-01', vencimiento: '2026-07-01',
      subtotal: 45000, iva: 8550, total: 53550, estado: 'Pendiente', metodoPago: '—',
      items: [
        { tipo: 'Vacuna', descripcion: 'Parvovirus — Max', cantidad: 1, precioUnitario: 35000, subtotal: 35000 },
        { tipo: 'Medicamento', descripcion: 'Amoxicilina 500mg', cantidad: 1, precioUnitario: 10000, subtotal: 10000 },
      ],
    },
    {
      id: '5', numero: 'FAC-0005', clienteId: '2', fecha: '2026-04-01', vencimiento: '2026-04-15',
      subtotal: 98000, iva: 18620, total: 116620, estado: 'Pendiente', metodoPago: '—',
      items: [
        { tipo: 'Consulta', descripcion: 'Cirugía menor — Rocky', cantidad: 1, precioUnitario: 98000, subtotal: 98000 },
      ],
    },
  ])

  // ── Pagos ─────────────────────────────────────────────────
  const pagos = ref([
    { id: '1', numero: 'PAG-0001', facturaId: '1', monto: 101150, metodo: 'Tarjeta Crédito', fecha: '2026-06-09', referencia: 'TC-4521-8842', estado: 'Completado' },
    { id: '2', numero: 'PAG-0002', facturaId: '3', monto: 142800, metodo: 'Efectivo', fecha: '2026-06-08', referencia: 'EF-2026-0608', estado: 'Completado' },
    { id: '3', numero: 'PAG-0003', facturaId: '4', monto: 53550, metodo: 'Transferencia', fecha: '2026-06-07', referencia: 'TRF-2026-06-1587', estado: 'Pendiente' },
    { id: '4', numero: 'PAG-0004', facturaId: '2', monto: 73780, metodo: 'Tarjeta Débito', fecha: '2026-05-28', referencia: 'TD-8834-2210', estado: 'Rechazado' },
    { id: '5', numero: 'PAG-0005', facturaId: '5', monto: 54500, metodo: 'Tarjeta Crédito', fecha: '2026-05-20', referencia: 'TC-1190-5532', estado: 'Completado' },
    { id: '6', numero: 'PAG-0006', facturaId: '3', monto: 28000, metodo: 'Transferencia', fecha: '2026-06-01', referencia: 'TRF-2026-06-0042', estado: 'Completado' },
  ])

  // ── Helpers CRUD genérico ─────────────────────────────────
  const listOf = (collection) => collection?.value ?? collection

  function addItem(collection, item) {
    listOf(collection).push({ ...item, id: makeId() })
  }
  function updateItem(collection, id, patch) {
    const list = listOf(collection)
    const i = list.findIndex(x => x.id === id)
    if (i !== -1) list[i] = { ...list[i], ...patch }
  }
  function deleteItem(collection, id) {
    const list = listOf(collection)
    const i = list.findIndex(x => x.id === id)
    if (i !== -1) list.splice(i, 1)
  }

  return {
    clientes, mascotas, veterinarios, turnos, consultas,
    medicamentos, vacunas, facturas, pagos,
    addItem, updateItem, deleteItem
  }
})
