import { http } from "@/utils/http";
import type { StatisticsRow } from "./types";

/** 调用统计 */
export const statisticsApi = {
  /** API 维度统计 */
  byApi: () => {
    return http.request<StatisticsRow[]>("get", "/open-platform/statistics/api");
  },
  /** 应用维度统计 */
  byApp: () => {
    return http.request<StatisticsRow[]>("get", "/open-platform/statistics/app");
  },
  /** 调用趋势统计（startDate/endDate 可选，格式 yyyy-MM-dd） */
  trend: (startDate?: string, endDate?: string) => {
    return http.request<StatisticsRow[]>("get", "/open-platform/statistics/trend", {
      params: { startDate, endDate }
    });
  }
};