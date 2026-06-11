import { jsPDF } from 'jspdf'

export const CLINICA = {
  nombre: 'VetNova Clínica Veterinaria',
  direccion: 'Calle 100 #15-20, Bogotá, Colombia',
  telefono: '+57 (601) 555-0100',
  nit: '900.123.456-7',
}

function fmtMoney(n) {
  return '$' + Number(n ?? 0).toLocaleString('es-CO')
}

function fmtFecha(f) {
  if (!f) return '—'
  return new Date(f + 'T12:00:00').toLocaleDateString('es-CO', {
    day: 'numeric', month: 'long', year: 'numeric',
  })
}

function numeroFactura(factura) {
  return factura.numero ?? `FAC-${String(factura.id).padStart(4, '0')}`
}

function drawHeader(doc) {
  doc.setFillColor(37, 99, 235)
  doc.rect(0, 0, 210, 42, 'F')

  doc.setFillColor(255, 255, 255)
  doc.circle(22, 21, 11, 'F')
  doc.setTextColor(37, 99, 235)
  doc.setFont('helvetica', 'bold')
  doc.setFontSize(13)
  doc.text('VN', 17.5, 24)

  doc.setTextColor(255, 255, 255)
  doc.setFontSize(16)
  doc.text(CLINICA.nombre, 40, 16)
  doc.setFont('helvetica', 'normal')
  doc.setFontSize(9)
  doc.text(CLINICA.direccion, 40, 23)
  doc.text(`Tel: ${CLINICA.telefono}`, 40, 29)
  doc.text(`NIT: ${CLINICA.nit}`, 40, 35)
}

function drawFooter(doc, y) {
  const footerY = Math.max(y + 12, 275)
  doc.setDrawColor(226, 232, 240)
  doc.line(14, footerY - 4, 196, footerY - 4)
  doc.setTextColor(100, 116, 139)
  doc.setFontSize(9)
  doc.setFont('helvetica', 'italic')
  doc.text('Gracias por confiar en nuestra veterinaria.', 105, footerY, { align: 'center' })
}

export function generateFacturaPdf(factura, cliente, pagos = []) {
  const doc = new jsPDF()
  const pago = pagos.find(p => p.facturaId === factura.id)
  const metodo = factura.metodoPago && factura.metodoPago !== '—'
    ? factura.metodoPago
    : (pago?.metodo ?? '—')
  const items = factura.items?.length ? factura.items : [
    { tipo: 'Consulta', descripcion: 'Servicio veterinario', cantidad: 1, precioUnitario: factura.subtotal, subtotal: factura.subtotal },
  ]

  drawHeader(doc)

  let y = 52
  doc.setTextColor(30, 41, 59)
  doc.setFont('helvetica', 'bold')
  doc.setFontSize(14)
  doc.text('FACTURA', 14, y)
  y += 10

  doc.setFont('helvetica', 'normal')
  doc.setFontSize(10)
  doc.text(`Número: ${numeroFactura(factura)}`, 14, y)
  doc.text(`Fecha: ${fmtFecha(factura.fecha)}`, 110, y)
  y += 6
  doc.text(`Vencimiento: ${fmtFecha(factura.vencimiento)}`, 14, y)
  doc.text(`Estado: ${factura.estado}`, 110, y)
  y += 10

  doc.setFillColor(248, 250, 252)
  doc.roundedRect(14, y, 182, 28, 2, 2, 'F')
  doc.setFont('helvetica', 'bold')
  doc.setFontSize(10)
  doc.text('CLIENTE', 18, y + 7)
  doc.setFont('helvetica', 'normal')
  doc.setFontSize(9)
  doc.text(`Nombre: ${cliente?.nombre ?? '—'}`, 18, y + 14)
  doc.text(`Documento: ${cliente?.documento ?? '—'}`, 18, y + 20)
  doc.text(`Teléfono: ${cliente?.telefono ?? '—'}`, 110, y + 14)
  doc.text(`Correo: ${cliente?.email ?? '—'}`, 110, y + 20)
  y += 36

  doc.setFont('helvetica', 'bold')
  doc.setFontSize(10)
  doc.text('DETALLE', 14, y)
  y += 4

  doc.setFillColor(241, 245, 249)
  doc.rect(14, y, 182, 8, 'F')
  doc.setFontSize(8)
  doc.text('Concepto', 16, y + 5.5)
  doc.text('Cant.', 110, y + 5.5)
  doc.text('P. Unit.', 130, y + 5.5)
  doc.text('Subtotal', 168, y + 5.5)
  y += 8

  doc.setFont('helvetica', 'normal')
  items.forEach(item => {
    y += 7
    if (y > 250) {
      doc.addPage()
      y = 20
    }
    const label = item.tipo ? `${item.tipo}: ${item.descripcion}` : item.descripcion
    doc.text(label.slice(0, 55), 16, y)
    doc.text(String(item.cantidad ?? 1), 112, y)
    doc.text(fmtMoney(item.precioUnitario), 130, y)
    doc.text(fmtMoney(item.subtotal), 168, y)
  })

  y += 12
  doc.setDrawColor(226, 232, 240)
  doc.line(120, y, 196, y)
  y += 7
  doc.setFont('helvetica', 'normal')
  doc.text('Subtotal:', 130, y)
  doc.text(fmtMoney(factura.subtotal), 168, y)
  y += 6
  doc.text('IVA:', 130, y)
  doc.text(fmtMoney(factura.iva), 168, y)
  y += 7
  doc.setFont('helvetica', 'bold')
  doc.setFontSize(11)
  doc.text('TOTAL:', 130, y)
  doc.text(fmtMoney(factura.total), 168, y)
  y += 8
  doc.setFont('helvetica', 'normal')
  doc.setFontSize(9)
  doc.text(`Método de pago: ${metodo}`, 130, y)

  drawFooter(doc, y)
  doc.save(`Factura-${numeroFactura(factura)}.pdf`)
}

export function generateFacturasListPdf(facturas, clientes, getEstado) {
  const doc = new jsPDF()
  drawHeader(doc)

  let y = 52
  doc.setTextColor(30, 41, 59)
  doc.setFont('helvetica', 'bold')
  doc.setFontSize(14)
  doc.text('REPORTE DE FACTURAS', 14, y)
  y += 8
  doc.setFont('helvetica', 'normal')
  doc.setFontSize(9)
  doc.text(`Generado: ${new Date().toLocaleDateString('es-CO')}`, 14, y)
  doc.text(`Total registros: ${facturas.length}`, 110, y)
  y += 10

  doc.setFillColor(241, 245, 249)
  doc.rect(14, y, 182, 8, 'F')
  doc.setFontSize(8)
  doc.setFont('helvetica', 'bold')
  doc.text('Factura', 16, y + 5.5)
  doc.text('Cliente', 50, y + 5.5)
  doc.text('Fecha', 110, y + 5.5)
  doc.text('Total', 140, y + 5.5)
  doc.text('Estado', 168, y + 5.5)
  y += 8

  doc.setFont('helvetica', 'normal')
  facturas.forEach(f => {
    y += 7
    if (y > 265) {
      doc.addPage()
      y = 20
    }
    const cliente = clientes.find(c => c.id === f.clienteId)
    doc.text(numeroFactura(f), 16, y)
    doc.text((cliente?.nombre ?? '—').slice(0, 28), 50, y)
    doc.text(fmtFecha(f.fecha).slice(0, 12), 110, y)
    doc.text(fmtMoney(f.total), 140, y)
    doc.text(getEstado(f), 168, y)
  })

  drawFooter(doc, y)
  doc.save(`Reporte-Facturas-${new Date().toISOString().slice(0, 10)}.pdf`)
}
