import request from '@/utils/request'

export function page(query) {
  return request.get('/open-platform/log', { params: query })
}

export function get(id) {
  return request.get(`/open-platform/log/${id}`)
}