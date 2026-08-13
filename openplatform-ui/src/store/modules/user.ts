import { ref } from "vue";
import { defineStore } from "pinia";
import { storageLocal } from "@pureadmin/utils";
import { loginApi, logoutApi, getUserInfoApi } from "@/api/auth";
import { setToken, removeToken, userKey } from "@/utils/auth";
import { resetRouter } from "@/router";

export const useUserStore = defineStore("pure-user", () => {
  const username = ref("");
  const nickname = ref("");
  const avatar = ref("");
  const roles = ref<Array<string>>([]);
  const permissions = ref<Array<string>>([]);

  /** 登录 */
  async function loginByUsername(data: { username: string; password: string }) {
    const res = await loginApi(data);
    setToken(res);
    return res;
  }

  /** 获取当前登录用户信息（基本信息 + 按钮权限 + 角色） */
  async function getInfo() {
    const userInfo = await getUserInfoApi();
    username.value = userInfo.username;
    nickname.value = userInfo.nickname;
    avatar.value = userInfo.avatar || "";
    roles.value = userInfo.roles || [];
    permissions.value = userInfo.permissions || [];
    // 同步持久化到 localStorage，供路由守卫判断登录态与菜单过滤使用
    storageLocal().setItem(userKey, {
      refreshToken: "",
      expires: new Date().getTime() + 24 * 60 * 60 * 1000,
      avatar: avatar.value,
      username: username.value,
      nickname: nickname.value,
      roles: roles.value,
      permissions: permissions.value
    });
    return userInfo;
  }

  /** 登出 */
  async function logOut() {
    // 通知后端销毁会话（失败不影响前端登出）
    try {
      await logoutApi();
    } catch {
      // ignore
    }
    removeToken();
    resetRouter();
    // 刷新页面以彻底清理路由与状态
    location.reload();
  }

  function SET_USERNAME(value: string) {
    username.value = value;
  }

  function SET_NICKNAME(value: string) {
    nickname.value = value;
  }

  function SET_AVATAR(value: string) {
    avatar.value = value;
  }

  function SET_ROLES(value: Array<string>) {
    roles.value = value;
  }

  function SET_PERMS(value: Array<string>) {
    permissions.value = value;
  }

  return {
    username,
    nickname,
    avatar,
    roles,
    permissions,
    loginByUsername,
    getInfo,
    logOut,
    SET_USERNAME,
    SET_NICKNAME,
    SET_AVATAR,
    SET_ROLES,
    SET_PERMS
  };
});

export function useUserStoreHook() {
  return useUserStore();
}