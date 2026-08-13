
<template>
  <div class="navbar">
    <div class="navbar-left">
      <el-icon :size="18" color="#909399"><Monitor /></el-icon>
      <span class="env-tag">管理后台</span>
    </div>
    <div class="navbar-right">
      <el-dropdown trigger="click" @command="handleCommand">
        <span class="user-info">
          <el-avatar :size="32" icon="UserFilled" />
          <span class="user-name">{{ userStore.userInfo?.nickname || userStore.userInfo?.username || '用户' }}</span>
          <el-icon><ArrowDown /></el-icon>
        </span>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item command="profile">个人中心</el-dropdown-item>
            <el-dropdown-item divided command="logout">退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'
import { ArrowDown } from '@element-plus/icons-vue'

const router = useRouter()
const userStore = useUserStore()

async function handleCommand(command) {
  if (command === 'logout') {
    await userStore.logout()
    router.push('/login')
  }
}
</script>

<style scoped>
.navbar {
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.navbar-left {
  display: flex;
  align-items: center;
  gap: 8px;
}
.env-tag {
  font-size: 13px;
  color: #909399;
}
.navbar-right {
  display: flex;
  align-items: center;
}
.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 4px 8px;
  border-radius: 4px;
  transition: background 0.2s;
}
.user-info:hover {
  background: #f5f7fa;
}
.user-name {
  font-size: 14px;
  color: #333;
}
</style>