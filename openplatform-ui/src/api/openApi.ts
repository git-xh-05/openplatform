import { createCrudApi } from "./crud";
import type { OpenApi } from "./types";

/** 开放 API 管理 */
export const openApiApi = createCrudApi<
  OpenApi,
  { name?: string; description?: string; status?: number }
>("/open-platform/api");