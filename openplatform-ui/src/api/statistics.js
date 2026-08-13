import request from '@/utils/request'

export function statByApi() {
  return request.get('/open-platform/statistics/api')
}

export function statByApp() {
  return request.get('/open-platform/statistics/app')
}

export function statTrend(startDate, endDate) {
  return request.get('/open-platform/statistics/trend', { params: { startDate, endDate } })
}