import { createCrudApi } from "./crud";
import type { Role } from "./types";

/** 角色管理 */
export const roleApi = createCrudApi<
  Role,
  { name?: string; description?: string; status?: number }
>("/system/role");