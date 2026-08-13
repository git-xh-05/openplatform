import request from '@/utils/request'

export function page(query) {
  return request.get('/open-platform/app', { params: query })
}

export function get(id) {
  return request.get(`/open-platform/app/${id}`)
}

export function create(data) {
  return request.post('/open-platform/app', data)
}

export function update(id, data) {
  return request.put(`/open-platform/app/${id}`, data)
}

export function remove(id) {
  return request.delete(`/open-platform/app/${id}`)
}

export function batchDelete(ids) {
  return request.delete('/open-platform/app', { data: { ids } })
}

export function getSecret(id) {
  return request.get(`/open-platform/app/${id}/secret`)
}

export function resetSecret(id) {
  return request.patch(`/open-platform/app/${id}/secret`)
}