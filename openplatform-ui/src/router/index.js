import { createRouter, createWebHashHistory } from 'vue-router'
import { useUserStore } from '@/store/user'
import Layout from '@/layout/index.vue'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/index.vue'),
    meta: { title: '登录' },
  },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/index.vue'),
        meta: { title: '首页' },
      },
    ],
  },
]

const router = createRouter({
  history: createWebHashHistory(),
  routes,
})

const componentMap = {
  'system/user/index': () => import('@/views/system/user/index.vue'),
  'system/role/index': () => import('@/views/system/role/index.vue'),
  'system/menu/index': () => import('@/views/system/menu/index.vue'),
  'open-platform/api/index': () => import('@/views/open/api/index.vue'),
  'open-platform/app/index': () => import('@/views/open/app/index.vue'),
  'open-platform/subscribe/index': () => import('@/views/open/subscribe/index.vue'),
  'open-platform/log/index': () => import('@/views/open/log/index.vue'),
  'open-platform/statistics/index': () => import('@/views/open/statistics/index.vue'),
}

export function addDynamicRoutes(serverRoutes) {
  const layoutRoute = router.getRoutes().find((r) => r.path === '/')
  if (!layoutRoute) return

  const existingNames = new Set(router.getRoutes().map((r) => r.name))

  function walk(menuRoutes, parentPath) {
    for (const menu of menuRoutes) {
      const fullPath = menu.path.startsWith('/') ? menu.path : parentPath ? `${parentPath}/${menu.path}` : menu.path
      if (menu.type === 1) {
        walk(menu.children || [], fullPath)
      } else if (menu.type === 2) {
        if (existingNames.has(menu.name)) continue
        existingNames.add(menu.name)
        const component = componentMap[menu.component]
        if (component) {
          router.addRoute('/', {
            path: fullPath,
            name: menu.name,
            component,
            meta: { title: menu.title, menuId: menu.id },
          })
        }
        menu.path = fullPath
        if (menu.children) walk(menu.children, fullPath)
      }
    }
  }
  walk(serverRoutes, '')
}

router.beforeEach(async (to, from, next) => {
  const userStore = useUserStore()
  if (to.path !== '/login' && !userStore.isLoggedIn) {
    return next('/login')
  }
  if (to.path === '/login' && userStore.isLoggedIn) {
    return next('/dashboard')
  }
  if (to.path !== '/login' && userStore.isLoggedIn) {
    if (!userStore.userInfo) {
      try {
        await userStore.getUserInfo()
      } catch {
        return next('/login')
      }
    }
    if (userStore.routes.length === 0) {
      try {
        const serverRoutes = await userStore.getRoutes()
        addDynamicRoutes(serverRoutes)
      } catch {
        return next('/login')
      }
    }
  }
  next()
})

export default router