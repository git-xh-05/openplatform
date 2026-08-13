import request from '@/utils/request'

export function page(query) {
  return request.get('/system/role', { params: query })
}

export function get(id) {
  return request.get(`/system/role/${id}`)
}

export function create(data) {
  return request.post('/system/role', data)
}

export function update(id, data) {
  return request.put(`/system/role/${id}`, data)
}

export function remove(id) {
  return request.delete(`/system/role/${id}`)
}

export function batchDelete(ids) {
  return request.delete('/system/role', { data: { ids } })
}