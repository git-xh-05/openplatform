import { createCrudApi } from "./crud";
import { http } from "@/utils/http";
import type { Subscribe } from "./types";

/** 订阅管理 */
export const subscribeApi = createCrudApi<Subscribe, { appId?: number; apiId?: number; status?: number }>(
  "/open-platform/subscribe"
);

/** 审核通过 */
export const approveSubscribe = (id: number) => {
  return http.request<void>("patch", `/open-platform/subscribe/${id}/approve`);
};

/** 审核拒绝 */
export const rejectSubscribe = (id: number) => {
  return http.request<void>("patch", `/open-platform/subscribe/${id}/reject`);
};