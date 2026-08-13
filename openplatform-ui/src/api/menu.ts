import { createCrudApi } from "./crud";
import type { Menu } from "./types";

/** 菜单管理 */
export const menuApi = createCrudApi<Menu, { title?: string; status?: number }>("/system/menu");