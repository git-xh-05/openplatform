<template>
  <template v-for="menu in menus" :key="menu.id">
    <el-sub-menu v-if="menu.children && menu.children.length > 0" :index="menu.path">
      <template #title>
        <el-icon v-if="menu.icon"><component :is="menu.icon" /></el-icon>
        <span>{{ menu.title }}</span>
      </template>
      <MenuItem :menus="menu.children" />
    </el-sub-menu>
    <el-menu-item v-else-if="menu.type === 2" :index="menu.path">
      <el-icon v-if="menu.icon"><component :is="menu.icon" /></el-icon>
      <template #title>{{ menu.title }}</template>
    </el-menu-item>
  </template>
</template>

<script setup>
defineProps({
  menus: { type: Array, default: () => [] },
})
</script>