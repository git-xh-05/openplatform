import { http } from "@/utils/http";
import type { RouteResp } from "./types";

/**
 * 后端路由图标名称 -> vue-pure-admin（Element Plus 图标）映射
 * 后端菜单表中 icon 字段存的是业务图标名，这里映射为 `ep:` 前缀的图标
 */
const iconMap: Record<string, string> = {
  settings: "ep:setting",
  user: "ep:user",
  role: "ep:avatar",
  menu: "ep:menu",
  api: "ep:connection",
  common: "ep:grid",
  app: "ep:monitor",
  subscribe: "ep:document-add",
  log: "ep:document",
  statistics: "ep:data-analysis",
  chart: "ep:trend-charts",
  dashboard: "ep:odometer",
  home: "ep:home-filled",
  list: "ep:list",
  lock: "ep:lock",
  key: "ep:key"
};

/** 后端路由（RouteResp）转换为 vue-pure-admin 动态路由格式 */
function transformBackendRoute(route: RouteResp): any {
  const isDir = route.type === 1;
  return {
    path: route.path,
    name: route.name || route.title,
    // 目录不绑定组件（作为分组容器）
    component: isDir ? undefined : route.component,
    redirect: route.redirect,
    meta: {
      title: route.title,
      icon: route.icon ? iconMap[route.icon] || `ep:${route.icon}` : undefined,
      // 隐藏菜单
      showLink: route.isHidden !== true,
      // 是否缓存页面
      keepAlive: route.isCache === true,
      // 排序
      rank: route.sort,
      // 按钮级别权限（当前路由页面的权限码）
      auths: route.permission ? [route.permission] : undefined,
      // 标记为后端返回路由
      backstage: true
    },
    children: route.children?.length
      ? route.children.map(child => transformBackendRoute(child))
      : undefined
  };
}

/** 获取后端动态路由（已转换为模板所需格式） */
export const getAsyncRoutes = async () => {
  const routes = await http.request<RouteResp[]>("get", "/auth/user/route");
  return { data: (routes || []).map(route => transformBackendRoute(route)) };
};