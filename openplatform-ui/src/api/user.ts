import { createCrudApi } from "./crud";
import type { User } from "./types";

/** 用户管理 */
export const userApi = createCrudApi<User, { description?: string; status?: number }>("/system/user");