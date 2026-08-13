import request from '@/utils/request'

export function page(query) {
  return request.get('/open-platform/subscribe', { params: query })
}

export function get(id) {
  return request.get(`/open-platform/subscribe/${id}`)
}

export function create(data) {
  return request.post('/open-platform/subscribe', data)
}

export function update(id, data) {
  return request.put(`/open-platform/subscribe/${id}`, data)
}

export function remove(id) {
  return request.delete(`/open-platform/subscribe/${id}`)
}

export function batchDelete(ids) {
  return request.delete('/open-platform/subscribe', { data: { ids } })
}

export function approve(id) {
  return request.patch(`/open-platform/subscribe/${id}/approve`)
}

export function reject(id) {
  return request.patch(`/open-platform/subscribe/${id}/reject`)
}