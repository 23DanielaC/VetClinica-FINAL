<template>
  <div class="login-page relative min-h-screen overflow-hidden">
    <!-- Fondo gradiente -->
    <div class="login-page__gradient absolute inset-0" />

    <!-- Círculos decorativos -->
    <div class="login-blob login-blob--1" />
    <div class="login-blob login-blob--2" />
    <div class="login-blob login-blob--3" />
    <div class="login-blob login-blob--4" />

    <div class="relative z-10 min-h-screen grid grid-cols-1 lg:grid-cols-2">
      <!-- Lado izquierdo -->
      <div class="login-left flex flex-col justify-center px-6 sm:px-10 lg:px-14 xl:px-20 py-10 lg:py-12">
        <!-- Logo + branding (compacto en móvil) -->
        <div class="flex items-center gap-4 mb-6 lg:mb-10">
          <div class="login-logo shrink-0">
            <svg class="w-10 h-10 text-white" fill="currentColor" viewBox="0 0 24 24" aria-hidden="true">
              <ellipse cx="12" cy="17" rx="4.5" ry="3.8"/>
              <circle cx="6.5" cy="11" r="2.8"/>
              <circle cx="10" cy="7.5" r="2.8"/>
              <circle cx="14" cy="7.5" r="2.8"/>
              <circle cx="17.5" cy="11" r="2.8"/>
            </svg>
          </div>
          <div>
            <h1 class="text-2xl sm:text-3xl lg:text-4xl xl:text-5xl font-extrabold text-white tracking-tight drop-shadow-sm">
              VetClinica
            </h1>
            <p class="text-blue-100/90 text-sm lg:hidden mt-0.5">Sistema de gestión veterinaria</p>
          </div>
        </div>

        <p class="hidden lg:block text-blue-50/90 text-lg leading-relaxed max-w-lg mb-10">
          Sistema de gestión veterinaria profesional para el cuidado integral de tus mascotas.
        </p>

        <!-- Tarjetas informativas -->
        <div class="space-y-3 lg:space-y-4 max-w-lg">
          <div
            v-for="feat in features"
            :key="feat.title"
            class="login-feature group"
          >
            <div :class="['login-feature__icon bg-gradient-to-br', feat.gradient]">
              <component :is="feat.icon" class="w-5 h-5 text-white" />
            </div>
            <div>
              <h3 class="text-white font-bold text-sm sm:text-base">{{ feat.title }}</h3>
              <p class="text-blue-100/80 text-xs sm:text-sm mt-0.5">{{ feat.desc }}</p>
            </div>
          </div>
        </div>
      </div>

      <!-- Lado derecho -->
      <div class="login-right flex flex-col items-center justify-center px-4 sm:px-8 py-10 lg:py-12">
        <div class="w-full max-w-md">
          <!-- Card login -->
          <div class="login-card">
            <div class="mb-6">
              <h2 class="text-2xl font-extrabold text-slate-900">Bienvenido</h2>
              <p class="text-slate-500 text-sm mt-1">Inicia sesión para continuar</p>
            </div>

            <div v-if="error" class="mb-4 p-3 bg-rose-50 border border-rose-200 rounded-xl text-rose-600 text-sm flex items-center gap-2">
              <ExclamationCircleIcon class="w-4 h-4 shrink-0" />
              {{ error }}
            </div>

            <form class="space-y-4" @submit.prevent="handleLogin">
              <div>
                <label class="login-label">Correo Electrónico</label>
                <input
                  v-model="form.email"
                  type="email"
                  class="login-input"
                  placeholder="correo@ejemplo.com"
                  autocomplete="username"
                />
              </div>
              <div>
                <label class="login-label">Contraseña</label>
                <input
                  v-model="form.password"
                  type="text"
                  class="login-input"
                  placeholder="Tu contraseña"
                  autocomplete="off"
                />
              </div>

              <div class="flex items-center justify-between gap-3 pt-1">
                <label class="flex items-center gap-2 cursor-pointer select-none">
                  <input v-model="remember" type="checkbox" class="login-checkbox" />
                  <span class="text-sm text-slate-600">Recordarme</span>
                </label>
                <a href="#" class="text-sm font-medium text-blue-600 hover:text-blue-700 transition-colors" @click.prevent>
                  ¿Olvidaste tu contraseña?
                </a>
              </div>

              <button type="submit" :disabled="loading" class="login-btn">
                <svg v-if="loading" class="w-4 h-4 animate-spin" fill="none" viewBox="0 0 24 24">
                  <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"/>
                  <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8v8H4z"/>
                </svg>
                {{ loading ? 'Ingresando...' : 'Iniciar Sesión' }}
              </button>
            </form>
          </div>

          <p class="text-center text-xs text-white/70 mt-6 drop-shadow-sm">
            © 2024 VetClinica. Todos los derechos reservados.
          </p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import {
  UsersIcon, ChartBarIcon, BanknotesIcon, ExclamationCircleIcon,
} from '@heroicons/vue/24/outline'
import { markRaw } from 'vue'

const router = useRouter()
const auth = useAuthStore()

const form = ref({ email: '', password: '' })
const remember = ref(true)
const error = ref('')
const loading = ref(false)

const features = [
  {
    title: 'Gestión Completa',
    desc: 'Controla clientes, mascotas y turnos.',
    icon: markRaw(UsersIcon),
    gradient: 'from-blue-500 to-indigo-600',
  },
  {
    title: 'Dashboard Avanzado',
    desc: 'Métricas y estadísticas en tiempo real.',
    icon: markRaw(ChartBarIcon),
    gradient: 'from-emerald-500 to-teal-500',
  },
  {
    title: 'Facturación Integrada',
    desc: 'Control de ingresos y pagos.',
    icon: markRaw(BanknotesIcon),
    gradient: 'from-amber-400 to-orange-500',
  },
]

onMounted(() => {
  const saved = localStorage.getItem('vetclinica_remember')
  if (saved) {
    try {
      const { email, password } = JSON.parse(saved)
      form.value.email = email
      form.value.password = password
      remember.value = true
    } catch { /* ignore */ }
  }
})

async function handleLogin() {
  error.value = ''
  loading.value = true
  await new Promise(r => setTimeout(r, 500))
  const ok = auth.login(form.value)
  loading.value = false
  if (ok) {
    if (remember.value) {
      localStorage.setItem('vetclinica_remember', JSON.stringify({
        email: form.value.email,
        password: form.value.password,
      }))
    } else {
      localStorage.removeItem('vetclinica_remember')
    }
    router.push('/dashboard')
  } else {
    error.value = 'Correo o contraseña incorrectos'
  }
}
</script>

<style scoped>
.login-page__gradient {
  background: linear-gradient(
    135deg,
    #1e40af 0%,
    #3b82f6 28%,
    #60a5fa 45%,
    #6ee7b7 72%,
    #fde047 100%
  );
}

.login-blob {
  @apply absolute rounded-full pointer-events-none;
  background: rgba(255, 255, 255, 0.08);
}
.login-blob--1 { width: 320px; height: 320px; top: -80px; left: -80px; }
.login-blob--2 { width: 240px; height: 240px; top: 40%; left: 8%; }
.login-blob--3 { width: 180px; height: 180px; bottom: 10%; right: 45%; }
.login-blob--4 { width: 400px; height: 400px; bottom: -120px; right: -100px; }

.login-logo {
  @apply w-16 h-16 rounded-2xl flex items-center justify-center
         bg-white/20 backdrop-blur-md border border-white/30 shadow-lg;
}

.login-feature {
  @apply flex items-start gap-4 p-4 rounded-2xl
         bg-white/10 backdrop-blur-md border border-white/20
         transition-all duration-300
         hover:bg-white/15 hover:border-white/30 hover:-translate-y-0.5 hover:shadow-lg;
}
.login-feature__icon {
  @apply w-11 h-11 rounded-xl flex items-center justify-center shrink-0 shadow-md
         transition-transform duration-300 group-hover:scale-110;
}

.login-card {
  @apply p-6 sm:p-8 rounded-[20px]
         bg-white/85 backdrop-blur-xl
         border border-white/60
         shadow-[0_8px_32px_rgba(0,0,0,0.12),0_2px_8px_rgba(0,0,0,0.06)];
}

.login-label {
  @apply block text-sm font-semibold text-slate-700 mb-1.5;
}

.login-input {
  @apply w-full px-4 py-2.5 text-sm text-slate-900
         bg-white/80 border border-slate-200 rounded-xl
         placeholder:text-slate-400
         focus:outline-none focus:ring-2 focus:ring-blue-500/30 focus:border-blue-400
         transition-all duration-200;
}

.login-checkbox {
  @apply w-4 h-4 rounded border-slate-300 text-blue-600 focus:ring-blue-500/30;
}

.login-btn {
  @apply w-full flex items-center justify-center gap-2
         px-4 py-3 mt-2 rounded-xl text-sm font-bold text-white
         bg-gradient-to-r from-blue-600 to-indigo-600
         shadow-lg shadow-blue-500/30
         hover:shadow-xl hover:shadow-blue-500/40 hover:-translate-y-0.5
         active:translate-y-0
         disabled:opacity-60 disabled:pointer-events-none
         transition-all duration-200;
}
</style>
