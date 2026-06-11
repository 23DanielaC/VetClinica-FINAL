<template>
  <div class="w-full space-y-7">
    <!-- Top bar -->
    <DashboardTopBar
      :name="auth.user?.name"
      :role="auth.user?.role"
      :initial="auth.user?.name?.[0] ?? 'A'"
    />

    <!-- Hero header -->
    <div class="relative overflow-hidden bg-white rounded-2xl border border-slate-100 shadow-[0_1px_2px_rgba(0,0,0,0.04),0_8px_32px_rgba(0,0,0,0.06)] p-6 lg:p-8">
      <div class="absolute top-0 right-0 w-72 h-72 bg-gradient-to-bl from-primary/10 via-indigo-50 to-transparent rounded-full blur-3xl pointer-events-none" />
      <div class="absolute bottom-0 left-0 w-48 h-48 bg-gradient-to-tr from-violet-100/60 to-transparent rounded-full blur-2xl pointer-events-none" />

      <div class="relative flex flex-col xl:flex-row xl:items-center xl:justify-between gap-6">
        <div class="flex-1">
          <div class="flex flex-wrap items-center gap-2 mb-3">
            <span class="inline-flex items-center gap-1.5 text-xs font-semibold text-primary bg-primary/10 px-3 py-1 rounded-full">
              <span class="w-1.5 h-1.5 rounded-full bg-primary animate-pulse" />
              Sistema activo
            </span>
            <span class="text-xs text-slate-400 capitalize">{{ fechaActual }}</span>
          </div>
          <h1 class="text-2xl sm:text-3xl lg:text-4xl font-extrabold text-slate-900 tracking-tight">
            Bienvenido Sr {{ nombreSaludo }}
          </h1>
        </div>

        <div class="flex flex-wrap gap-2.5 shrink-0">
          <router-link to="/mascotas" class="dash-action bg-gradient-to-r from-violet-600 to-purple-500 text-white shadow-lg shadow-violet-500/25">
            <HeartIcon class="w-4 h-4" /> Nueva Mascota
          </router-link>
          <router-link to="/clientes" class="dash-action bg-gradient-to-r from-blue-600 to-indigo-500 text-white shadow-lg shadow-blue-500/25">
            <UsersIcon class="w-4 h-4" /> Nuevo Cliente
          </router-link>
          <router-link to="/consultas" class="dash-action bg-white text-slate-700 border border-slate-200 hover:border-primary/30 hover:bg-slate-50">
            <ClipboardDocumentListIcon class="w-4 h-4 text-primary" /> Nueva Consulta
          </router-link>
        </div>
      </div>
    </div>

    <!-- Mini widgets -->
    <div class="grid grid-cols-1 sm:grid-cols-2 xl:grid-cols-4 gap-5">
      <DashboardMiniWidget
        v-for="w in miniWidgets"
        :key="w.label"
        :label="w.label"
        :value="w.value"
        :hint="w.hint"
        :badge="w.badge"
        :badge-class="w.badgeClass"
        :icon-name="w.iconName"
        :gradient="w.gradient"
      />
    </div>

    <!-- KPIs -->
    <div class="grid grid-cols-1 sm:grid-cols-2 xl:grid-cols-4 gap-5">
      <DashboardKpiCard
        v-for="kpi in kpis"
        :key="kpi.label"
        :label="kpi.label"
        :value="kpi.value"
        :growth="kpi.growth"
        :icon-name="kpi.icon"
        :gradient="kpi.color"
      />
    </div>

    <!-- Gráficos fila 2 -->
    <div class="grid grid-cols-1 xl:grid-cols-2 gap-6">
      <DashboardChartCard
        title="Mascotas por Especie"
        subtitle="Composición del registro animal"
        :stats="[
          { label: 'Total', value: mascotasPorEspecie.total },
          { label: 'Líder', value: mascotasPorEspecie.top, color: 'text-violet-600' },
          { label: 'Especies', value: mascotasPorEspecie.labels.length, color: 'text-blue-600' },
        ]"
      >
        <apexchart type="donut" height="390" :options="donutOptions" :series="mascotasPorEspecie.series" />
      </DashboardChartCard>

      <DashboardChartCard
        title="Consultas por Mes"
        subtitle="Tendencia últimos 6 meses"
        :stats="[
          { label: 'Total', value: consultasPorMes.total },
          { label: 'Promedio', value: consultasPorMes.promedio + '/mes', color: 'text-emerald-600' },
          { label: 'Pico', value: consultasPorMes.pico, color: 'text-amber-600' },
        ]"
      >
        <apexchart type="area" height="390" :options="lineOptions" :series="lineSeries" />
      </DashboardChartCard>
    </div>

    <!-- Gráficos fila 3 -->
    <div class="grid grid-cols-1 xl:grid-cols-12 gap-6">
      <DashboardChartCard
        class="xl:col-span-8"
        title="Facturación Mensual"
        subtitle="Ingresos en pesos colombianos (COP)"
        :stats="[
          { label: 'Total 6M', value: '$' + (facturacionMensual.total / 1000000).toFixed(1) + 'M' },
          { label: 'Mejor mes', value: facturacionMensual.mejor, color: 'text-blue-600' },
          { label: 'Máximo', value: '$' + facturacionMensual.mejorValor.toLocaleString('es-CO'), color: 'text-emerald-600' },
        ]"
      >
        <apexchart type="bar" height="390" :options="barOptions" :series="facturacionMensual.series" />
      </DashboardChartCard>

      <DashboardChartCard class="xl:col-span-4" title="Vacunas Aplicadas" subtitle="Meta mensual por tipo">
        <div class="space-y-5">
          <div v-for="v in vacunasAplicadas" :key="v.id" class="group">
            <div class="flex items-center justify-between mb-2">
              <span class="text-sm font-semibold text-slate-700 group-hover:text-primary transition-colors">{{ v.nombre }}</span>
              <span class="text-xs font-bold text-slate-500 bg-slate-100 px-2 py-0.5 rounded-full">{{ v.aplicadas }}/{{ v.meta }}</span>
            </div>
            <div class="h-3 bg-slate-100 rounded-full overflow-hidden">
              <div
                class="h-full rounded-full bg-gradient-to-r from-indigo-600 via-primary to-sky-400 transition-all duration-700 ease-out"
                :style="{ width: `${v.porcentaje}%` }"
              />
            </div>
            <p class="text-xs text-slate-400 mt-1.5 font-medium">{{ v.porcentaje }}% completado</p>
          </div>
        </div>
      </DashboardChartCard>
    </div>

    <!-- Actividad + Citas -->
    <div class="grid grid-cols-1 xl:grid-cols-2 gap-6">
      <!-- Timeline -->
      <DashboardChartCard title="Actividad Reciente" subtitle="Timeline de eventos del sistema">
        <div class="relative">
          <div class="absolute left-[22px] top-3 bottom-3 w-0.5 bg-gradient-to-b from-primary/40 via-slate-200 to-transparent" />
          <div v-for="ev in actividadReciente" :key="ev.id" class="relative flex gap-4 pb-5 last:pb-0 group">
            <div :class="['relative z-10 w-11 h-11 rounded-xl flex items-center justify-center shrink-0 shadow-sm ring-4 ring-white group-hover:scale-105 transition-transform', ev.iconBg]">
              <ClipboardDocumentListIcon v-if="ev.tipo === 'consulta'" class="w-5 h-5" />
              <BanknotesIcon v-else-if="ev.tipo === 'pago'" class="w-5 h-5" />
              <CalendarDaysIcon v-else class="w-5 h-5" />
            </div>
            <div class="flex-1 min-w-0 bg-slate-50/80 hover:bg-white border border-transparent hover:border-slate-100 rounded-xl px-4 py-3 transition-all duration-200 hover:shadow-sm">
              <div class="flex items-start justify-between gap-2">
                <p class="text-sm font-bold text-slate-800">{{ ev.titulo }}</p>
                <span class="text-[11px] font-semibold text-primary bg-primary/10 px-2 py-0.5 rounded-full whitespace-nowrap">{{ ev.relativo }}</span>
              </div>
              <p class="text-xs text-slate-500 mt-1 leading-relaxed">{{ ev.descripcion }}</p>
              <p class="text-[11px] text-slate-400 mt-2">{{ ev.fecha }} · {{ ev.hora }}</p>
            </div>
          </div>
          <p v-if="!actividadReciente.length" class="text-sm text-slate-400 text-center py-10">Sin actividad reciente</p>
        </div>
      </DashboardChartCard>

      <!-- Agenda -->
      <DashboardChartCard title="Próximas Citas" subtitle="Agenda profesional veterinaria">
        <template #action>
          <router-link to="/turnos" class="text-xs font-semibold text-primary hover:text-primary-dark transition-colors">Ver todas →</router-link>
        </template>
        <div class="space-y-3">
          <div
            v-for="cita in proximasCitas"
            :key="cita.id"
            class="flex items-center gap-4 p-4 rounded-2xl bg-slate-50/60 border border-slate-100/80
                   hover:bg-white hover:border-primary/20 hover:shadow-[0_4px_20px_rgba(0,0,0,0.06)]
                   transition-all duration-200 group"
          >
            <div
              :class="['w-14 h-14 rounded-2xl bg-gradient-to-br flex items-center justify-center text-2xl shrink-0 shadow-md ring-2 ring-white group-hover:scale-105 transition-transform', cita.avatarGradient]"
            >
              {{ cita.emoji }}
            </div>
            <div class="flex-1 min-w-0">
              <div class="flex items-center gap-2 flex-wrap">
                <p class="text-sm font-bold text-slate-900">{{ cita.mascotaNombre }}</p>
                <span :class="estadoClass(cita.estado)">{{ cita.estado }}</span>
              </div>
              <p class="text-xs text-slate-500 mt-0.5">{{ cita.propietario }}</p>
              <p class="text-xs font-semibold text-primary mt-1 truncate">{{ cita.consulta }}</p>
            </div>
            <div class="text-right shrink-0 pl-2 border-l border-slate-200/80">
              <p class="text-lg font-extrabold text-slate-900 tabular-nums">{{ cita.hora }}</p>
              <p class="text-[11px] font-medium text-slate-400 mt-0.5">{{ formatFecha(cita.fecha) }}</p>
            </div>
          </div>
          <p v-if="!proximasCitas.length" class="text-sm text-slate-400 text-center py-10">No hay citas programadas</p>
        </div>
      </DashboardChartCard>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useAuthStore } from '@/stores/auth'
import { useDashboard } from '@/composables/useDashboard'
import DashboardTopBar from '@/components/dashboard/DashboardTopBar.vue'
import DashboardKpiCard from '@/components/dashboard/DashboardKpiCard.vue'
import DashboardMiniWidget from '@/components/dashboard/DashboardMiniWidget.vue'
import DashboardChartCard from '@/components/dashboard/DashboardChartCard.vue'
import {
  UsersIcon, HeartIcon, CalendarDaysIcon, ClipboardDocumentListIcon, BanknotesIcon,
} from '@heroicons/vue/24/outline'

const auth = useAuthStore()

const nombreSaludo = computed(() => {
  const raw = auth.user?.name ?? 'Admin'
  return raw.replace(/^(Dr\.?|Dra\.?)\s*/i, '').trim() || 'Admin'
})

const {
  fechaActual,
  miniWidgets,
  kpis,
  mascotasPorEspecie,
  consultasPorMes,
  facturacionMensual,
  vacunasAplicadas,
  actividadReciente,
  proximasCitas,
} = useDashboard()

const vibrantColors = ['#6366F1', '#8B5CF6', '#F97316', '#22C55E', '#EC4899', '#06B6D4', '#3B82F6']

const chartAnim = {
  enabled: true,
  easing: 'easeinout',
  speed: 900,
  animateGradually: { enabled: true, delay: 120 },
  dynamicAnimation: { enabled: true, speed: 400 },
}

const donutOptions = computed(() => ({
  chart: { fontFamily: 'Inter, sans-serif', toolbar: { show: false }, animations: chartAnim },
  labels: mascotasPorEspecie.value.labels,
  colors: vibrantColors,
  legend: { position: 'bottom', fontSize: '13px', fontWeight: 500, markers: { width: 10, height: 10, radius: 4 } },
  dataLabels: { enabled: true, formatter: (val) => `${Math.round(val)}%`, style: { fontWeight: 700, fontSize: '12px' } },
  plotOptions: {
    pie: {
      donut: {
        size: '72%',
        labels: {
          show: true,
          name: { fontSize: '13px', color: '#64748B' },
          value: { fontSize: '22px', fontWeight: 800, color: '#0F172A' },
          total: { show: true, label: 'Mascotas', fontSize: '13px', fontWeight: 600, color: '#64748B' },
        },
      },
    },
  },
  stroke: { width: 3, colors: ['#fff'] },
  tooltip: { theme: 'light', y: { formatter: (v) => `${Math.round(v)}%` } },
}))

const lineOptions = computed(() => ({
  chart: { fontFamily: 'Inter, sans-serif', toolbar: { show: false }, animations: chartAnim },
  colors: ['#6366F1'],
  stroke: { curve: 'smooth', width: 3.5 },
  fill: {
    type: 'gradient',
    gradient: { shadeIntensity: 1, opacityFrom: 0.45, opacityTo: 0.02, stops: [0, 95, 100] },
  },
  xaxis: {
    categories: consultasPorMes.value.labels,
    axisBorder: { show: false },
    axisTicks: { show: false },
    labels: { style: { colors: '#94A3B8', fontSize: '12px', fontWeight: 500 } },
  },
  yaxis: {
    labels: { style: { colors: '#94A3B8', fontSize: '12px' } },
    min: 0,
  },
  grid: { borderColor: '#F1F5F9', strokeDashArray: 4, padding: { top: 4 } },
  dataLabels: { enabled: false },
  markers: { size: 5, strokeWidth: 3, strokeColors: '#fff', hover: { size: 8 } },
  tooltip: { theme: 'light' },
}))

const lineSeries = computed(() => [{ name: 'Consultas', data: consultasPorMes.value.series }])

const barOptions = computed(() => ({
  chart: { fontFamily: 'Inter, sans-serif', toolbar: { show: false }, animations: chartAnim },
  colors: ['#3B82F6'],
  plotOptions: {
    bar: {
      borderRadius: 10,
      columnWidth: '52%',
      distributed: false,
      dataLabels: { position: 'top' },
    },
  },
  fill: {
    type: 'gradient',
    gradient: { shade: 'light', type: 'vertical', shadeIntensity: 0.3, opacityFrom: 1, opacityTo: 0.85, stops: [0, 100] },
  },
  dataLabels: { enabled: false },
  xaxis: {
    categories: facturacionMensual.value.labels,
    axisBorder: { show: false },
    axisTicks: { show: false },
    labels: { style: { colors: '#94A3B8', fontSize: '12px', fontWeight: 500 } },
  },
  yaxis: {
    labels: {
      style: { colors: '#94A3B8', fontSize: '12px' },
      formatter: (v) => `$${(v / 1000).toFixed(0)}k`,
    },
  },
  grid: { borderColor: '#F1F5F9', strokeDashArray: 4 },
  tooltip: { theme: 'light', y: { formatter: (v) => `$${v.toLocaleString('es-CO')}` } },
}))

function estadoClass(estado) {
  const map = {
    Confirmado: 'inline-flex text-[10px] font-bold uppercase tracking-wide px-2 py-0.5 rounded-full bg-emerald-100 text-emerald-700 ring-1 ring-emerald-200',
    Pendiente: 'inline-flex text-[10px] font-bold uppercase tracking-wide px-2 py-0.5 rounded-full bg-amber-100 text-amber-700 ring-1 ring-amber-200',
    'En curso': 'inline-flex text-[10px] font-bold uppercase tracking-wide px-2 py-0.5 rounded-full bg-violet-100 text-violet-700 ring-1 ring-violet-200',
    Completado: 'inline-flex text-[10px] font-bold uppercase tracking-wide px-2 py-0.5 rounded-full bg-blue-100 text-blue-700 ring-1 ring-blue-200',
    Cancelado: 'inline-flex text-[10px] font-bold uppercase tracking-wide px-2 py-0.5 rounded-full bg-rose-100 text-rose-700 ring-1 ring-rose-200',
  }
  return map[estado] ?? 'inline-flex text-[10px] font-bold px-2 py-0.5 rounded-full bg-slate-100 text-slate-600'
}

function formatFecha(fecha) {
  return new Date(fecha + 'T12:00:00').toLocaleDateString('es-CO', { day: 'numeric', month: 'short' })
}
</script>

<style scoped>
.dash-action {
  @apply inline-flex items-center gap-2 px-4 py-2.5 rounded-xl text-sm font-semibold transition-all duration-200 hover:-translate-y-0.5 hover:shadow-lg;
}

:deep(.apexcharts-canvas) {
  margin: 0 auto;
}
</style>
