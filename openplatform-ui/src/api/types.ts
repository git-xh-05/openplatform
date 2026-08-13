/** 通用基础字段（对应后端 BaseResp/BaseDetailResp） */
export interface BaseResp {
  /** ID */
  id: number;
  /** 创建人（昵称） */
  createUserString?: string;
  /** 创建时间 */
  createTime?: string;
  /** 修改人（昵称） */
  updateUserString?: string;
  /** 修改时间 */
  updateTime?: string;
  /** 是否禁用修改 */
  disabled?: boolean;
}

/** 分页响应 */
export interface PageResult<T> {
  list: T[];
  total: number;
}

/** 分页查询参数 */
export interface PageQuery {
  /** 页码，默认 1 */
  page?: number;
  /** 每页条数，默认 10 */
  size?: number;
  /** 排序字段（如 createTime,desc） */
  sort?: string;
}

/** 启用/禁用状态（1：启用；2：禁用） */
export type Status = 1 | 2;

/** 用户信息（/auth/user/info 返回） */
export interface UserInfo {
  id: number;
  username: string;
  nickname: string;
  gender?: string;
  email?: string;
  phone?: string;
  avatar?: string;
  description?: string;
  permissions: string[];
  roles: string[];
  pwdResetTime?: string;
}

/** 后端动态路由节点（/auth/user/route 返回） */
export interface RouteResp {
  id: number;
  parentId: number;
  /** 菜单标题 */
  title: string;
  /** 菜单类型（1：目录；2：菜单；3：按钮） */
  type: number;
  /** 路由地址 */
  path: string;
  /** 路由名称 */
  name: string;
  /** 组件路径 */
  component?: string;
  /** 重定向地址 */
  redirect?: string;
  /** 图标 */
  icon?: string;
  /** 是否外链 */
  isExternal?: boolean;
  /** 是否缓存 */
  isCache?: boolean;
  /** 是否隐藏 */
  isHidden?: boolean;
  /** 权限标识 */
  permission?: string;
  /** 排序 */
  sort?: number;
  /** 子菜单 */
  children?: RouteResp[];
}

/** 用户（/system/user） */
export interface User {
  id?: number;
  username: string;
  nickname?: string;
  password?: string;
  gender?: string;
  email?: string;
  phone?: string;
  avatar?: string;
  description?: string;
  status?: Status;
  isSystem?: boolean;
  pwdResetTime?: string;
  createTime?: string;
}

/** 角色（/system/role） */
export interface Role {
  id?: number;
  name: string;
  code: string;
  description?: string;
  sort?: number;
  isSystem?: boolean;
  status?: Status;
  createTime?: string;
}

/** 菜单（/system/menu） */
export interface Menu {
  id?: number;
  title: string;
  parentId?: number;
  /** 菜单类型（1：目录；2：菜单；3：按钮） */
  type: number;
  path?: string;
  name?: string;
  component?: string;
  redirect?: string;
  icon?: string;
  isExternal?: boolean;
  isCache?: boolean;
  isHidden?: boolean;
  permission?: string;
  sort?: number;
  status?: Status;
  children?: Menu[];
}

/** 开放 API（/open-platform/api） */
export interface OpenApi {
  id?: number;
  /** 名称 */
  name: string;
  /** 路径（对外暴露的路径，如 /api/user/list） */
  path: string;
  /** 请求方法（GET/POST/PUT/DELETE） */
  method: string;
  /** 后端服务地址 */
  serviceUrl: string;
  /** 状态 */
  status?: Status;
  /** 描述 */
  description?: string;
  createTime?: string;
}

/** 应用（/open-platform/app） */
export interface OpenApp {
  id?: number;
  name: string;
  /** Access Key */
  accessKey?: string;
  /** 失效时间 */
  expireTime?: string;
  /** IP 黑名单 */
  ipBlacklist?: string;
  /** 速率限制（每秒请求数） */
  rateLimit?: number;
  status?: Status;
  description?: string;
  createTime?: string;
}

/** 应用密钥 */
export interface OpenAppSecret {
  accessKey: string;
  secretKey: string;
}

/** 订阅状态（0：待审核；1：已通过；2：已拒绝） */
export type SubscribeStatus = 0 | 1 | 2;

/** 订阅（/open-platform/subscribe） */
export interface Subscribe {
  id?: number;
  /** 应用ID */
  appId: number;
  /** API ID */
  apiId: number;
  status?: SubscribeStatus;
  /** 配额限制 */
  quotaLimit?: number;
  /** 审批人 */
  approveUser?: number;
  /** 审批时间 */
  approveTime?: string;
  createTime?: string;
}

/** 调用日志（/open-platform/log） */
export interface ApiLog {
  id: number;
  appId?: number;
  apiId?: number;
  /** 状态码 */
  statusCode?: number;
  /** 耗时（毫秒） */
  costTime?: number;
  /** 客户端 IP */
  clientIp?: string;
  /** 请求参数 */
  requestParams?: string;
  /** 响应体 */
  responseBody?: string;
  /** 错误信息 */
  errorMessage?: string;
  /** 调用时间 */
  createTime?: string;
}

/** 统计数据（statistics 接口返回的 Map 行） */
export interface StatisticsRow {
  [key: string]: string | number | null;
}