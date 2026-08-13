import { createCrudApi } from "./crud";
import { http } from "@/utils/http";
import type { OpenApp, OpenAppSecret } from "./types";

/** 应用管理 */
export const appApi = createCrudApi<
  OpenApp,
  { name?: string; description?: string; status?: number },
  Omit<OpenApp, "accessKey">
>("/open-platform/app");

/** 获取应用密钥 */
export const getAppSecret = (id: number) => {
  return http.request<OpenAppSecret>("get", `/open-platform/app/${id}/secret`);
};

/** 重置应用密钥 */
export const resetAppSecret = (id: number) => {
  return http.request<void>("patch", `/open-platform/app/${id}/secret`);
};