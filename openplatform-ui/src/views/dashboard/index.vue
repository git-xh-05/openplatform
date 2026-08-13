<template>
  <div class="dashboard">
    <el-row :gutter="16">
      <el-col :span="6" v-for="item in stats" :key="item.title">
        <el-card shadow="never" class="stat-card" :body-style="{ padding: '20px' }">
          <div class="stat-item">
            <div class="stat-icon" :style="{ background: item.bg }">
              <el-icon :size="24" :color="item.color"><component :is="item.icon" /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ item.value }}</div>
              <div class="stat-title">{{ item.title }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Document, Monitor, TrendCharts, DataAnalysis } from '@element-plus/icons-vue'
import { page as getApps } from '@/api/app'
import { page as getApis } from '@/api/openapi'
import { statByApi } from '@/api/statistics'

const stats = ref([
  { title: '注册应用', value: '-', icon: Monitor, color: '#409eff', bg: 'rgba(64,158,255,0.1)' },
  { title: '注册API', value: '-', icon: Document, color: '#67c23a', bg: 'rgba(103,194,58,0.1)' },
  { title: '今日调用', value: '-', icon: TrendCharts, color: '#e6a23c', bg: 'rgba(230,162,60,0.1)' },
  { title: '总调用次数', value: '-', icon: DataAnalysis, color: '#f56c6c', bg: 'rgba(245,108,108,0.1)' },
])

async function loadData() {
  try {
    const [appRes, apiRes, statRes] = await Promise.allSettled([
      getApps({ page: 1, size: 1 }),
      getApis({ page: 1, size: 1 }),
      statByApi(),
    ])

    const appCount = appRes.status === 'fulfilled' ? appRes.value.total ?? '-' : '-'
    const apiCount = apiRes.status === 'fulfilled' ? apiRes.value.total ?? '-' : '-'

    let totalCallCount = '-'
    let todayCallCount = '-'
    if (statRes.status === 'fulfilled' && Array.isArray(statRes.value)) {
      totalCallCount = statRes.value.reduce((s, d) => s + (d.total || d.count || d.callCount || 0), 0)
    }

    stats.value = [
      { title: '注册应用', value: appCount, icon: Monitor, color: '#409eff', bg: 'rgba(64,158,255,0.1)' },
      { title: '注册API', value: apiCount, icon: Document, color: '#67c23a', bg: 'rgba(103,194,58,0.1)' },
      { title: '今日调用', value: todayCallCount, icon: TrendCharts, color: '#e6a23c', bg: 'rgba(230,162,60,0.1)' },
      { title: '总调用次数', value: totalCallCount, icon: DataAnalysis, color: '#f56c6c', bg: 'rgba(245,108,108,0.1)' },
    ]
  } catch {}
}

onMounted(loadData)
</script>

<style scoped>
.dashboard {
  margin: 0;
}
.stat-card {
  border-radius: 6px;
  border: 1px solid #e8e8e8;
}
.stat-item {
  display: flex;
  align-items: center;
  gap: 16px;
}
.stat-icon {
  width: 48px;
  height: 48px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}
.stat-info {
  flex: 1;
}
.stat-value {
  font-size: 26px;
  font-weight: 700;
  color: #1d2129;
  line-height: 1.2;
}
.stat-title {
  font-size: 13px;
  color: #86909c;
  margin-top: 4px;
}
</style>