import request from '@/utils/request'

export function getPublicKey() {
  return request.get('/auth/public-key')
}

export function login(data) {
  return request.post('/auth/login', data)
}

export function logout() {
  return request.post('/auth/logout')
}

export function getUserInfo() {
  return request.get('/auth/user/info')
}

export function getRoutes() {
  return request.get('/auth/user/route')
}