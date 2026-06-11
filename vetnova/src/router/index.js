import { createRouter, createWebHashHistory } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const routes = [
  { path: '/login', name: 'Login', component: () => import('@/views/auth/LoginView.vue'), meta: { guest: true } },
  {
    path: '/',
    component: () => import('@/components/layout/AppLayout.vue'),
    meta: { requiresAuth: true },
    children: [
      { path: '', redirect: '/dashboard' },
      { path: 'dashboard', name: 'Dashboard', component: () => import('@/views/DashboardView.vue') },
      { path: 'clientes', name: 'Clientes', component: () => import('@/views/clientes/ClientesView.vue') },
      { path: 'mascotas', name: 'Mascotas', component: () => import('@/views/mascotas/MascotasView.vue') },
      { path: 'veterinarios', name: 'Veterinarios', component: () => import('@/views/veterinarios/VeterinariosView.vue') },
      { path: 'turnos', name: 'Turnos', component: () => import('@/views/turnos/TurnosView.vue') },
      { path: 'consultas', name: 'Consultas', component: () => import('@/views/consultas/ConsultasView.vue') },
      { path: 'medicamentos', name: 'Medicamentos', component: () => import('@/views/medicamentos/MedicamentosView.vue') },
      { path: 'vacunas', name: 'Vacunas', component: () => import('@/views/vacunas/VacunasView.vue') },
      { path: 'facturas', name: 'Facturas', component: () => import('@/views/facturas/FacturasView.vue') },
      { path: 'pagos', name: 'Pagos', component: () => import('@/views/pagos/PagosView.vue') },
    ]
  },
  { path: '/:pathMatch(.*)*', redirect: '/' }
]

const router = createRouter({ history: createWebHashHistory(), routes })

router.beforeEach((to) => {
  const auth = useAuthStore()
  if (to.meta.requiresAuth && !auth.isAuthenticated) return '/login'
  if (to.meta.guest && auth.isAuthenticated) return '/dashboard'
})

export default router
