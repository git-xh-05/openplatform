<template>
  <el-container class="layout-container">
    <el-aside width="220px" class="sidebar-wrap">
      <Sidebar />
    </el-aside>
    <el-container>
      <el-header class="header-wrap">
        <Navbar />
      </el-header>
      <el-main class="main-wrap">
        <div class="page-header">
          <el-breadcrumb>
            <el-breadcrumb-item :to="{ path: '/dashboard' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item v-if="route.meta?.title">{{ route.meta.title }}</el-breadcrumb-item>
          </el-breadcrumb>
          <h3 class="page-title">{{ route.meta?.title }}</h3>
        </div>
        <div class="page-content">
          <router-view />
        </div>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { useRoute } from 'vue-router'
import Sidebar from './sidebar.vue'
import Navbar from './navbar.vue'

const route = useRoute()
</script>

<style scoped>
.layout-container {
  height: 100vh;
}
.sidebar-wrap {
  background: #304156;
  overflow-y: auto;
  overflow-x: hidden;
  user-select: none;
}
.header-wrap {
  background: #fff;
  border-bottom: 1px solid #e6e6e6;
  display: flex;
  align-items: center;
  justify-content: flex-end;
  padding: 0 24px;
  height: 60px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.04);
}
.main-wrap {
  background: #f0f2f5;
  padding: 0;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
}
.page-header {
  padding: 16px 24px 0;
  flex-shrink: 0;
}
.page-header .page-title {
  margin: 8px 0 0;
  font-size: 20px;
  font-weight: 600;
  color: #1d2129;
}
.page-content {
  padding: 16px 24px 24px;
  flex: 1;
}
</style>