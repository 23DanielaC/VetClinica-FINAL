import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useAuthStore = defineStore('auth', () => {
  const user = ref(JSON.parse(localStorage.getItem('vetnova_user') || 'null'))
  const token = ref(localStorage.getItem('vetnova_token') || '')

  const isAuthenticated = computed(() => !!token.value)

  function login(credentials) {
    const validEmails = ['admin@vetclinica.com', 'admin@vetnova.com']
    if (validEmails.includes(credentials.email) && credentials.password === 'admin123') {
      const fakeUser = { id: 1, name: 'Admin', email: credentials.email, role: 'Administrador' }
      const fakeToken = 'fake-jwt-token-' + Date.now()
      user.value = fakeUser
      token.value = fakeToken
      localStorage.setItem('vetnova_user', JSON.stringify(fakeUser))
      localStorage.setItem('vetnova_token', fakeToken)
      return true
    }
    return false
  }

  function logout() {
    user.value = null
    token.value = ''
    localStorage.removeItem('vetnova_user')
    localStorage.removeItem('vetnova_token')
  }

  return { user, token, isAuthenticated, login, logout }
})
