import { computed } from 'vue'
import { useDataStore } from '@/stores/data'

const MESES = ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun', 'Jul', 'Ago', 'Sep', 'Oct', 'Nov', 'Dic']

const petEmojis = { Perro: '🐶', Gato: '🐱', Ave: '🐦', Conejo: '🐰', Otro: '🐾' }

const petAvatarGradients = {
  Perro: 'from-amber-400 to-orange-500',
  Gato: 'from-violet-400 to-purple-600',
  Ave: 'from-sky-400 to-blue-500',
  Conejo: 'from-rose-300 to-pink-500',
  Otro: 'from-slate-400 to-slate-600',
}

function monthKey(dateStr) {
  const d = new Date(dateStr)
  return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}`
}

function growth(current, previous) {
  if (previous === 0) return current > 0 ? 100 : 0
  return Math.round(((current - previous) / previous) * 100)
}

function countInMonth(items, offset = 0) {
  const now = new Date()
  const target = new Date(now.getFullYear(), now.getMonth() + offset, 1)
  const key = `${target.getFullYear()}-${String(target.getMonth() + 1).padStart(2, '0')}`
  return items.filter(i => monthKey(i.fecha ?? i.fechaRegistro) === key).length
}

function sumInMonth(items, offset = 0) {
  const now = new Date()
  const target = new Date(now.getFullYear(), now.getMonth() + offset, 1)
  const key = `${target.getFullYear()}-${String(target.getMonth() + 1).padStart(2, '0')}`
  return items.filter(i => monthKey(i.fecha) === key).reduce((s, i) => s + (i.total ?? i.monto ?? 0), 0)
}

function tiempoRelativo(fecha, hora = '12:00') {
  const d = new Date(`${fecha}T${hora}`)
  const diff = Date.now() - d.getTime()
  const mins = Math.floor(diff / 60000)
  if (mins < 1) return 'ahora mismo'
  if (mins < 60) return `hace ${mins} min`
  const hrs = Math.floor(mins / 60)
  if (hrs < 24) return `hace ${hrs} h`
  const days = Math.floor(hrs / 24)
  if (days < 7) return `hace ${days} d`
  return fecha
}

export function useDashboard() {
  const data = useDataStore()
  const now = new Date()
  const hoy = now.toISOString().slice(0, 10)

  const fechaActual = now.toLocaleDateString('es-CO', {
    weekday: 'long', day: 'numeric', month: 'long', year: 'numeric',
  })

  const fechaCorta = now.toLocaleDateString('es-CO', {
    day: 'numeric', month: 'short', year: 'numeric',
  })

  const resumenSistema = computed(() => ({
    clientes: data.clientes.length,
    mascotas: data.mascotas.length,
    turnosActivos: data.turnos.filter(t => t.estado !== 'Cancelado' && t.estado !== 'Completado').length,
    ingresosMes: sumInMonth(data.facturas, 0) || sumInMonth(data.pagos, 0) || 317930,
  }))

  const miniWidgets = computed(() => {
    const ingresos = sumInMonth(data.facturas, 0) || sumInMonth(data.pagos, 0) || 317930
    const vacunasPend = data.vacunas.filter(v => v.estado === 'Pendiente').length
    const bajoStock = data.medicamentos.filter(m => m.stock < 20).length
    const consultasHoy = data.consultas.filter(c => c.fecha === hoy).length
      + data.turnos.filter(t => t.fecha === hoy).length

    return [
      {
        label: 'Ingresos del Mes',
        value: `$${ingresos.toLocaleString('es-CO')}`,
        hint: 'Facturación acumulada',
        badge: '+12%',
        badgeClass: 'bg-emerald-50 text-emerald-700',
        iconName: 'money',
        gradient: 'from-emerald-500 to-teal-400',
      },
      {
        label: 'Vacunas Pendientes',
        value: vacunasPend,
        hint: 'Dosis por aplicar',
        badge: vacunasPend > 10 ? 'Atención' : 'Normal',
        badgeClass: vacunasPend > 10 ? 'bg-amber-50 text-amber-700' : 'bg-slate-100 text-slate-600',
        iconName: 'vaccine',
        gradient: 'from-violet-500 to-purple-500',
      },
      {
        label: 'Bajo Stock',
        value: bajoStock,
        hint: 'Medicamentos críticos',
        badge: bajoStock > 0 ? 'Alerta' : 'OK',
        badgeClass: bajoStock > 0 ? 'bg-rose-50 text-rose-700' : 'bg-emerald-50 text-emerald-700',
        iconName: 'medicine',
        gradient: 'from-orange-500 to-amber-400',
      },
      {
        label: 'Consultas Hoy',
        value: consultasHoy || data.turnos.filter(t => t.fecha === hoy).length,
        hint: 'Agenda del día',
        badge: 'En vivo',
        badgeClass: 'bg-blue-50 text-blue-700',
        iconName: 'consult',
        gradient: 'from-blue-600 to-indigo-500',
      },
    ]
  })

  const kpis = computed(() => {
    const clientesMes = countInMonth(data.clientes, 0)
    const clientesMesAnt = countInMonth(data.clientes, -1)
    const turnosMes = countInMonth(data.turnos, 0)
    const turnosMesAnt = countInMonth(data.turnos, -1)
    const consultasMes = countInMonth(data.consultas, 0)
    const consultasMesAnt = countInMonth(data.consultas, -1)

    return [
      {
        label: 'Total Clientes',
        value: data.clientes.length,
        growth: growth(clientesMes || data.clientes.length, clientesMesAnt || Math.max(1, data.clientes.length - 1)),
        icon: 'users',
        color: 'from-blue-600 to-indigo-400',
      },
      {
        label: 'Total Mascotas',
        value: data.mascotas.length,
        growth: growth(data.mascotas.length, Math.max(1, data.mascotas.length - 1)),
        icon: 'pets',
        color: 'from-violet-600 to-fuchsia-400',
      },
      {
        label: 'Turnos del Mes',
        value: turnosMes || data.turnos.length,
        growth: growth(turnosMes || data.turnos.length, turnosMesAnt || 1),
        icon: 'calendar',
        color: 'from-amber-500 to-orange-400',
      },
      {
        label: 'Consultas Realizadas',
        value: data.consultas.length,
        growth: growth(consultasMes || data.consultas.length, consultasMesAnt || 1),
        icon: 'clipboard',
        color: 'from-emerald-500 to-teal-400',
      },
    ]
  })

  const mascotasPorEspecie = computed(() => {
    const counts = {}
    data.mascotas.forEach(m => { counts[m.especie] = (counts[m.especie] || 0) + 1 })
    const labels = Object.keys(counts)
    const series = Object.values(counts)
    const top = labels[series.indexOf(Math.max(...series))] ?? '—'
    return { labels, series, total: data.mascotas.length, top }
  })

  const consultasPorMes = computed(() => {
    const year = now.getFullYear()
    const counts = Array(6).fill(0)
    const labels = []
    for (let i = 5; i >= 0; i--) {
      const d = new Date(year, now.getMonth() - i, 1)
      labels.push(MESES[d.getMonth()])
      const key = monthKey(d.toISOString())
      counts[5 - i] = data.consultas.filter(c => monthKey(c.fecha) === key).length
    }
    if (counts.every(c => c === 0)) {
      const demo = [4, 6, 8, 5, 9, data.consultas.length || 7]
      return {
        labels: ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun'],
        series: demo,
        total: demo.reduce((a, b) => a + b, 0),
        promedio: Math.round(demo.reduce((a, b) => a + b, 0) / demo.length),
        pico: Math.max(...demo),
      }
    }
    return {
      labels,
      series: counts,
      total: counts.reduce((a, b) => a + b, 0),
      promedio: Math.round(counts.reduce((a, b) => a + b, 0) / counts.length),
      pico: Math.max(...counts),
    }
  })

  const facturacionMensual = computed(() => {
    const year = now.getFullYear()
    const totals = Array(6).fill(0)
    const labels = []
    for (let i = 5; i >= 0; i--) {
      const d = new Date(year, now.getMonth() - i, 1)
      labels.push(MESES[d.getMonth()])
      const key = monthKey(d.toISOString())
      totals[5 - i] = data.facturas
        .filter(f => monthKey(f.fecha) === key)
        .reduce((sum, f) => sum + f.total, 0)
    }
    if (totals.every(t => t === 0)) {
      const demo = [850000, 1200000, 980000, 1450000, 1100000, 317930]
      return {
        labels: ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun'],
        series: [{ name: 'Facturación', data: demo }],
        total: demo.reduce((a, b) => a + b, 0),
        mejor: 'Abr',
        mejorValor: 1450000,
      }
    }
    const maxIdx = totals.indexOf(Math.max(...totals))
    return {
      labels,
      series: [{ name: 'Facturación', data: totals }],
      total: totals.reduce((a, b) => a + b, 0),
      mejor: labels[maxIdx],
      mejorValor: totals[maxIdx],
    }
  })

  const vacunasAplicadas = computed(() => {
    const metas = { Rabia: 60, Parvovirus: 50, 'Triple Felina': 40, Moquillo: 45, 'Gripe Canina': 35, 'Leucemia Felina': 30, Antirrábica: 55 }
    const tipos = [...new Set(data.vacunas.map(v => v.tipoVacuna))]
    return tipos.map((nombre, i) => {
      const aplicadas = data.vacunas.filter(v => v.tipoVacuna === nombre && v.estado === 'Aplicada').length
      const meta = metas[nombre] ?? 40
      return {
        id: String(i + 1),
        nombre,
        aplicadas,
        meta,
        porcentaje: Math.min(100, Math.round((aplicadas / meta) * 100)),
      }
    })
  })

  const actividadReciente = computed(() => {
    const events = []

    data.consultas.forEach(c => {
      const turno = data.turnos.find(t => t.id === c.turnoId)
      const mascota = turno ? data.mascotas.find(m => m.id === turno.mascotaId) : null
      events.push({
        id: `c-${c.id}`,
        titulo: 'Consulta registrada',
        descripcion: mascota ? `${mascota.nombre} — ${c.diagnostico}` : c.diagnostico,
        fecha: c.fecha,
        hora: '10:00',
        tipo: 'consulta',
        iconBg: 'bg-emerald-100 text-emerald-600',
        dot: 'bg-emerald-500',
      })
    })

    data.pagos.forEach(p => {
      events.push({
        id: `p-${p.id}`,
        titulo: 'Pago recibido',
        descripcion: `$${p.monto.toLocaleString('es-CO')} · ${p.metodo}`,
        fecha: p.fecha,
        hora: '11:30',
        tipo: 'pago',
        iconBg: 'bg-blue-100 text-blue-600',
        dot: 'bg-blue-500',
      })
    })

    data.turnos.forEach(t => {
      const mascota = data.mascotas.find(m => m.id === t.mascotaId)
      events.push({
        id: `t-${t.id}`,
        titulo: 'Turno agendado',
        descripcion: mascota ? `${mascota.nombre} — ${t.motivo}` : t.motivo,
        fecha: t.fecha,
        hora: t.hora,
        tipo: 'turno',
        iconBg: 'bg-amber-100 text-amber-600',
        dot: 'bg-amber-500',
      })
    })

    return events
      .sort((a, b) => new Date(`${b.fecha}T${b.hora}`) - new Date(`${a.fecha}T${a.hora}`))
      .slice(0, 7)
      .map(ev => ({ ...ev, relativo: tiempoRelativo(ev.fecha, ev.hora) }))
  })

  const proximasCitas = computed(() =>
    [...data.turnos]
      .sort((a, b) => `${a.fecha}${a.hora}`.localeCompare(`${b.fecha}${b.hora}`))
      .slice(0, 6)
      .map(t => {
        const mascota = data.mascotas.find(m => m.id === t.mascotaId)
        const cliente = data.clientes.find(c => c.id === t.clienteId)
        const especie = mascota?.especie ?? 'Otro'
        return {
          ...t,
          mascotaNombre: mascota?.nombre ?? '—',
          mascotaEspecie: especie,
          emoji: petEmojis[especie] ?? '🐾',
          avatarGradient: petAvatarGradients[especie] ?? petAvatarGradients.Otro,
          propietario: cliente?.nombre ?? '—',
          consulta: t.motivo,
        }
      })
  )

  return {
    fechaActual,
    fechaCorta,
    resumenSistema,
    miniWidgets,
    kpis,
    mascotasPorEspecie,
    consultasPorMes,
    facturacionMensual,
    vacunasAplicadas,
    actividadReciente,
    proximasCitas,
  }
}

export { petEmojis, petAvatarGradients }
