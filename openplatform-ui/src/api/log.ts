import { createCrudApi } from "./crud";
import type { ApiLog } from "./types";

/** 调用日志（只读：分页 + 详情） */
export const logApi = {
  /** 分页查询 */
  page: createCrudApi<ApiLog, { statusCode?: number; clientIp?: string }>(
    "/open-platform/log"
  ).page,
  /** 查询详情 */
  get: createCrudApi<ApiLog>("/open-platform/log").get
};