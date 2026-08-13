import Cookies from "js-cookie";
import { useUserStoreHook } from "@/store/modules/user";
import { storageLocal, isString, isIncludeAllChildren } from "@pureadmin/utils";

export interface DataInfo<T> {
  /** token */
  accessToken: string;
  /** `accessToken`的过期时间（时间戳） */
  expires: T;
  /** 用于调用刷新accessToken的接口时所需的token */
  refreshToken: string;
  /** 头像 */
  avatar?: string;
  /** 用户名 */
  username?: string;
  /** 昵称 */
  nickname?: string;
  /** 当前登录用户的角色 */
  roles?: Array<string>;
  /** 当前登录用户的按钮级别权限 */
  permissions?: Array<string>;
}

export const userKey = "user-info";
export const TokenKey = "authorized-token";
/**
 * 通过`multiple-tabs`是否在`cookie`中，判断用户是否已经登录系统，
 * 从而支持多标签页打开已经登录的系统后无需再登录。
 * 浏览器完全关闭后`multiple-tabs`将自动从`cookie`中销毁，
 * 再次打开浏览器需要重新登录系统
 */
export const multipleTabsKey = "multiple-tabs";

/** 获取`token` */
export function getToken(): DataInfo<number> {
  // 此处与`TokenKey`相同，此写法解决初始化时`Cookies`中不存在`TokenKey`报错
  return Cookies.get(TokenKey)
    ? JSON.parse(Cookies.get(TokenKey))
    : storageLocal().getItem(userKey);
}

/**
 * @description 设置`token`以及一些必要信息
 * 后端返回 `{ token }`（Sa-Token 的 token 值，默认有效期为 24 小时，对应后端
 * `sa-token.timeout = 86400`），前端直接存放到 cookie 中并在请求头
 * `Authorization` 中携带（无需 `Bearer` 前缀，后端 Sa-Token 直接读取该请求头）
 */
export function setToken(data: { token: string; tenantId?: number }) {
  const { token } = data;
  // Sa-Token 默认 timeout 为 86400 秒（24小时），与后端保持一致
  const expires = new Date().getTime() + 24 * 60 * 60 * 1000;
  const cookieString = JSON.stringify({
    accessToken: token,
    expires,
    refreshToken: ""
  });

  Cookies.set(TokenKey, cookieString, {
    expires: 1
  });

  Cookies.set(multipleTabsKey, "true", {});
}

/** 存储当前登录用户信息（头像、用户名、昵称、角色、按钮权限） */
export function setUserInfo(info: {
  username?: string;
  nickname?: string;
  avatar?: string;
  roles?: Array<string>;
  permissions?: Array<string>;
}) {
  const { username = "", nickname = "", avatar = "", roles = [], permissions = [] } = info;
  useUserStoreHook().SET_AVATAR(avatar);
  useUserStoreHook().SET_USERNAME(username);
  useUserStoreHook().SET_NICKNAME(nickname);
  useUserStoreHook().SET_ROLES(roles);
  useUserStoreHook().SET_PERMS(permissions);
  storageLocal().setItem(userKey, {
    refreshToken: "",
    expires: new Date().getTime() + 24 * 60 * 60 * 1000,
    avatar,
    username,
    nickname,
    roles,
    permissions
  });
}

/** 删除`token`以及key值为`user-info`的localStorage信息 */
export function removeToken() {
  Cookies.remove(TokenKey);
  Cookies.remove(multipleTabsKey);
  storageLocal().removeItem(userKey);
}

/**
 * 格式化 token：本项目后端（Sa-Token）直接通过请求头 `Authorization` 读取
 * token 值本身，无需 `Bearer` 前缀，因此原样返回
 */
export const formatToken = (token: string): string => {
  return token;
};

/** 是否有按钮级别的权限（根据登录接口返回的`permissions`字段进行判断）*/
export const hasPerms = (value: string | Array<string>): boolean => {
  if (!value) return false;
  const allPerms = "*:*:*";
  const { permissions } = useUserStoreHook();
  if (!permissions) return false;
  if (permissions.length === 1 && permissions[0] === allPerms) return true;
  const isAuths = isString(value)
    ? permissions.includes(value)
    : isIncludeAllChildren(value, permissions);
  return isAuths ? true : false;
};