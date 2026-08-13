import request from '@/utils/request'

export function page(query) {
  return request.get('/system/menu', { params: query })
}

export function get(id) {
  return request.get(`/system/menu/${id}`)
}

export function create(data) {
  return request.post('/system/menu', data)
}

export function update(id, data) {
  return request.put(`/system/menu/${id}`, data)
}

export function remove(id) {
  return request.delete(`/system/menu/${id}`)
}

export function batchDelete(ids) {
  return request.delete('/system/menu', { data: { ids } })
}

export function listAll() {
  return request.get('/system/menu', { params: { page: 1, size: 999 } })
}