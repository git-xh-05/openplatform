import request from '@/utils/request'

export function page(query) {
  return request.get('/open-platform/api', { params: query })
}

export function get(id) {
  return request.get(`/open-platform/api/${id}`)
}

export function create(data) {
  return request.post('/open-platform/api', data)
}

export function update(id, data) {
  return request.put(`/open-platform/api/${id}`, data)
}

export function remove(id) {
  return request.delete(`/open-platform/api/${id}`)
}

export function batchDelete(ids) {
  return request.delete('/open-platform/api', { data: { ids } })
}