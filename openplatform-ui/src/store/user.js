import { defineStore } from 'pinia'
import { login, getUserInfo, getRoutes, logout } from '@/api/auth'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: localStorage.getItem('token') || '',
    userInfo: null,
    routes: [],
    permissions: [],
  }),
  getters: {
    isLoggedIn: () => !!localStorage.getItem('token'),
  },
  actions: {
    async login(credentials) {
      const res = await login(credentials)
      this.token = res.token
      localStorage.setItem('token', res.token)
    },
    async getUserInfo() {
      const res = await getUserInfo()
      this.userInfo = res
      this.permissions = res.permissions || []
      return res
    },
    async getRoutes() {
      const res = await getRoutes()
      this.routes = res
      return res
    },
    async logout() {
      try {
        await logout()
      } catch {
      }
      this.token = ''
      this.userInfo = null
      this.routes = []
      this.permissions = []
      localStorage.removeItem('token')
    },
    hasPermission(perm) {
      return this.permissions.includes(perm)
    },
  },
})