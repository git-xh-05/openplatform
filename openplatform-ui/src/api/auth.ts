import { http } from "@/utils/http";
import type { UserInfo, RouteResp } from "./types";

/** 登录返回结果 */
export interface LoginResult {
  token: string;
  tenantId?: number;
}

/** 登录 */
export const loginApi = (data: { username: string; password: string }) => {
  return http.request<LoginResult>("post", "/auth/login", { data });
};

/** 登出 */
export const logoutApi = () => {
  return http.request<void>("post", "/auth/logout");
};

/** 获取当前登录用户信息 */
export const getUserInfoApi = () => {
  return http.request<UserInfo>("get", "/auth/user/info");
};

/** 获取动态路由（后端路由树） */
export const getRoutesApi = () => {
  return http.request<RouteResp[]>("get", "/auth/user/route");
};