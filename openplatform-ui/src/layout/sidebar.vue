<template>
  <div class="sidebar-logo">
    <el-icon :size="22" color="#409eff"><Platform /></el-icon>
    <span>开放平台</span>
  </div>
  <el-menu
    :default-active="activeMenu"
    background-color="#304156"
    text-color="#bfcbd9"
    active-text-color="#409eff"
    :router="true"
    :collapse="false"
  >
    <MenuItem :menus="menuTree" />
  </el-menu>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { useUserStore } from '@/store/user'
import { addDynamicRoutes } from '@/router'
import { Platform } from '@element-plus/icons-vue'
import MenuItem from './menu-item.vue'

const route = useRoute()
const userStore = useUserStore()
const menuTree = ref([])

const activeMenu = computed(() => route.path.replace(/^\//, ''))

async function loadMenu() {
  let routes = userStore.routes
  if (routes.length === 0) {
    routes = await userStore.getRoutes()
    addDynamicRoutes(routes)
  }
  menuTree.value = routes
}

onMounted(loadMenu)
</script>

<style scoped>
.sidebar-logo {
  height: 60px;
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 0 20px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.08);
}
.sidebar-logo span {
  color: #fff;
  font-size: 18px;
  font-weight: 600;
  letter-spacing: 1px;
}
:deep(.el-menu) {
  border-right: none;
}
:deep(.el-menu-item:hover) {
  background-color: rgba(255, 255, 255, 0.05) !important;
}
:deep(.el-menu-item.is-active) {
  background-color: rgba(64, 158, 255, 0.15) !important;
}
:deep(.el-sub-menu__title:hover) {
  background-color: rgba(255, 255, 255, 0.05) !important;
}
</style>