import axios from 'axios'
import { ElMessage } from 'element-plus'

const request = axios.create({
  baseURL: '',
  timeout: 30000,
})

request.interceptors.request.use((config) => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = token
  }
  return config
})

let isRedirecting = false

request.interceptors.response.use(
  (response) => {
    const data = response.data
    if (data.code !== 0) {
      ElMessage.error(data.msg || '请求失败')
      return Promise.reject(new Error(data.msg))
    }
    return data.data
  },
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token')
      if (!isRedirecting) {
        isRedirecting = true
        ElMessage.error('登录已过期，请重新登录')
        import('@/router').then(({ default: router }) => {
          if (router.currentRoute.value.path !== '/login') {
            router.push('/login')
          }
        })
      }
      return Promise.reject(error)
    }
    ElMessage.error(error.response?.data?.msg || error.message || '网络错误')
    return Promise.reject(error)
  },
)

export default request