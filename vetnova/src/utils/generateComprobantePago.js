import { jsPDF } from 'jspdf'
import autoTable from 'jspdf-autotable'
import { CLINICA } from './generateFacturaPdf.js'

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
  return factura?.numero ?? `FAC-${String(factura?.id ?? '0').padStart(4, '0')}`
}

function numeroPago(pago) {
  return pago.numero ?? `PAG-${String(pago.id).padStart(4, '0')}`
}

function drawHeader(doc) {
  doc.setFillColor(16, 185, 129)
  doc.rect(0, 0, 210, 42, 'F')

  doc.setFillColor(255, 255, 255)
  doc.circle(22, 21, 11, 'F')
  doc.setTextColor(16, 185, 129)
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
  doc.text('Gracias por su pago.', 105, footerY, { align: 'center' })
}

export function generateComprobantePago(pago, factura, cliente) {
  const doc = new jsPDF()
  const facNum = numeroFactura(factura)
  const items = factura?.items?.length
    ? factura.items.map(i => [i.tipo ? `${i.tipo}: ${i.descripcion}` : i.descripcion, fmtMoney(i.subtotal)])
    : [['Servicio veterinario', fmtMoney(factura?.subtotal ?? pago.monto)]]

  drawHeader(doc)

  let y = 52
  doc.setTextColor(30, 41, 59)
  doc.setFont('helvetica', 'bold')
  doc.setFontSize(14)
  doc.text('COMPROBANTE DE PAGO', 14, y)
  y += 10

  doc.setFont('helvetica', 'normal')
  doc.setFontSize(10)
  doc.text(`Número de pago: ${numeroPago(pago)}`, 14, y)
  doc.text(`Factura: ${facNum}`, 110, y)
  y += 6
  doc.text(`Fecha: ${fmtFecha(pago.fecha)}`, 14, y)
  doc.text(`Estado: ${pago.estado ?? 'Completado'}`, 110, y)
  y += 6
  doc.text(`Método: ${pago.metodo}`, 14, y)
  doc.text(`Referencia: ${pago.referencia ?? '—'}`, 110, y)
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

  autoTable(doc, {
    startY: y,
    head: [['Servicio', 'Valor']],
    body: items,
    margin: { left: 14, right: 14 },
    headStyles: { fillColor: [241, 245, 249], textColor: [71, 85, 105], fontStyle: 'bold', fontSize: 9 },
    bodyStyles: { fontSize: 9, textColor: [51, 65, 85] },
    alternateRowStyles: { fillColor: [248, 250, 252] },
    theme: 'plain',
  })

  y = doc.lastAutoTable.finalY + 10
  doc.setDrawColor(226, 232, 240)
  doc.line(120, y, 196, y)
  y += 7
  doc.setFont('helvetica', 'normal')
  doc.setFontSize(10)
  doc.text('Subtotal:', 130, y)
  doc.text(fmtMoney(factura?.subtotal ?? pago.monto), 168, y)
  y += 6
  doc.text('IVA:', 130, y)
  doc.text(fmtMoney(factura?.iva ?? 0), 168, y)
  y += 7
  doc.setFont('helvetica', 'bold')
  doc.setFontSize(11)
  doc.setTextColor(16, 185, 129)
  doc.text('TOTAL PAGADO:', 130, y)
  doc.text(fmtMoney(pago.monto), 168, y)

  drawFooter(doc, y)
  doc.save(`Comprobante-Pago-${facNum}.pdf`)
}
