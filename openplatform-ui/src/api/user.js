import request from '@/utils/request'

export function page(query) {
  return request.get('/system/user', { params: query })
}

export function get(id) {
  return request.get(`/system/user/${id}`)
}

export function create(data) {
  return request.post('/system/user', data)
}

export function update(id, data) {
  return request.put(`/system/user/${id}`, data)
}

export function remove(id) {
  return request.delete(`/system/user/${id}`)
}

export function batchDelete(ids) {
  return request.delete('/system/user', { data: { ids } })
}