import { http } from "@/utils/http";
import type { PageResult, PageQuery } from "./types";

/**
 * 根据后端 ContiNew Starter CRUD 接口规范生成统一 API
 * 接口约定：
 *  - GET    {base}        分页查询（参数：page、size、sort + 查询条件）
 *  - GET    {base}/{id}   查询详情
 *  - POST   {base}        创建（返回 { id }）
 *  - PUT    {base}/{id}   修改
 *  - DELETE {base}        批量删除（body：{ ids }）
 *  - GET    {base}/export 导出
 */
export function createCrudApi<T = any, Q = Record<string, any>, R = any>(base: string) {
  return {
    /** 分页查询 */
    page: (query?: Q & PageQuery) => {
      return http.request<PageResult<T>>("get", base, { params: query });
    },
    /** 查询详情 */
    get: (id: number) => {
      return http.request<T>("get", `${base}/${id}`);
    },
    /** 创建 */
    create: (data: R) => {
      return http.request<{ id: number }>("post", base, { data });
    },
    /** 修改 */
    update: (id: number, data: R) => {
      return http.request<void>("put", `${base}/${id}`, { data });
    },
    /** 批量删除 */
    batchDelete: (ids: number[]) => {
      return http.request<void>("delete", base, { data: { ids } });
    },
    /** 导出 */
    exportExcel: (query?: Q & PageQuery) => {
      return http.request<Blob>("get", `${base}/export`, {
        params: query,
        responseType: "blob"
      });
    }
  };
}