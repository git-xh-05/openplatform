<template>
  <div class="page">
    <el-row :gutter="16">
      <el-col :span="12">
        <el-card shadow="never">
          <template #header><span class="card-title">按API统计</span></template>
          <v-chart :option="apiChartOption" style="height: 350px" autoresize />
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="never">
          <template #header><span class="card-title">按应用统计</span></template>
          <v-chart :option="appChartOption" style="height: 350px" autoresize />
        </el-card>
      </el-col>
    </el-row>
    <el-row :gutter="16" style="margin-top: 16px">
      <el-col :span="24">
        <el-card shadow="never">
          <template #header><span class="card-title">调用趋势</span></template>
          <v-chart :option="trendChartOption" style="height: 350px" autoresize />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import VChart from 'vue-echarts'
import 'echarts'
import { statByApi, statByApp, statTrend } from '@/api/statistics'

const apiData = ref([])
const appData = ref([])
const trendData = ref([])

const apiChartOption = computed(() => ({
  tooltip: { trigger: 'axis' },
  grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
  xAxis: { type: 'category', data: apiData.value.map((d) => d.name || d.apiName || '未知'), axisLabel: { rotate: 30 } },
  yAxis: { type: 'value' },
  series: [
    {
      name: '调用次数', type: 'bar',
      data: apiData.value.map((d) => d.count || d.callCount || 0),
      itemStyle: { borderRadius: [4, 4, 0, 0], color: '#409eff' },
    },
  ],
}))

const appChartOption = computed(() => ({
  tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },
  series: [
    {
      type: 'pie',
      radius: ['40%', '65%'],
      avoidLabelOverlap: true,
      label: { show: true, formatter: '{b}\n{d}%' },
      emphasis: { label: { show: true, fontSize: 14, fontWeight: 'bold' } },
      data: appData.value.map((d) => ({
        name: d.name || d.appName || '未知',
        value: d.count || d.callCount || 0,
      })),
    },
  ],
}))

const trendChartOption = computed(() => ({
  tooltip: { trigger: 'axis' },
  grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
  xAxis: { type: 'category', data: trendData.value.map((d) => d.date || d.time || '未知'), boundaryGap: false },
  yAxis: { type: 'value' },
  series: [
    {
      name: '调用次数', type: 'line', smooth: true,
      data: trendData.value.map((d) => d.count || d.callCount || 0),
      areaStyle: { color: 'rgba(64,158,255,0.15)' },
      lineStyle: { color: '#409eff', width: 2 },
      itemStyle: { color: '#409eff' },
    },
  ],
}))

function getDefaultDateRange() {
  const end = new Date()
  const start = new Date()
  start.setDate(start.getDate() - 7)
  const fmt = (d) => d.getFullYear() + '-' + String(d.getMonth() + 1).padStart(2, '0') + '-' + String(d.getDate()).padStart(2, '0')
  return { startDate: fmt(start), endDate: fmt(end) }
}

async function loadData() {
  try { apiData.value = await statByApi() } catch {}
  try { appData.value = await statByApp() } catch {}
  try {
    const { startDate, endDate } = getDefaultDateRange()
    trendData.value = await statTrend(startDate, endDate)
  } catch {}
}

onMounted(loadData)
</script>

<style scoped>
.card-title {
  font-size: 15px;
  font-weight: 600;
  color: #1d2129;
}
</style>