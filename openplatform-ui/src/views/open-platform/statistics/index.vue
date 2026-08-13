<script setup lang="ts">
import { ref, onMounted, onBeforeUnmount, nextTick } from "vue";
import echarts from "@/plugins/echarts";
import { statisticsApi } from "@/api/statistics";
import type { StatisticsRow } from "@/api/types";

defineOptions({ name: "OpenPlatformStatistics" });

const loading = ref(false);
const trendLoading = ref(false);
const dateRange = ref<[string, string] | null>(null);

const apiChartRef = ref<HTMLElement>();
const appChartRef = ref<HTMLElement>();
const trendChartRef = ref<HTMLElement>();

let apiChart: echarts.ECharts | null = null;
let appChart: echarts.ECharts | null = null;
let trendChart: echarts.ECharts | null = null;

function renderBarChart(
  el: HTMLElement,
  data: StatisticsRow[],
  nameKey: string,
  title: string
) {
  const chart = echarts.init(el);
  const names = data.map(row => String(row[nameKey] ?? "-"));
  const success = data.map(row => Number(row.success ?? 0));
  const fail = data.map(row => Number(row.fail ?? 0));

  chart.setOption({
    title: { text: title, left: "center", textStyle: { fontSize: 14 } },
    tooltip: { trigger: "axis" },
    legend: { data: ["成功", "失败"], bottom: 0 },
    grid: { left: 50, right: 20, top: 40, bottom: 40 },
    xAxis: {
      type: "category",
      data: names,
      axisLabel: { interval: 0, rotate: names.length > 6 ? 30 : 0 }
    },
    yAxis: { type: "value" },
    series: [
      { name: "成功", type: "bar", stack: "total", data: success, itemStyle: { color: "#67c23a" } },
      { name: "失败", type: "bar", stack: "total", data: fail, itemStyle: { color: "#f56c6c" } }
    ]
  });
  return chart;
}

function renderTrendChart(el: HTMLElement, data: StatisticsRow[]) {
  const chart = echarts.init(el);
  const dates = data.map(row => String(row.date ?? "-"));
  const total = data.map(row => Number(row.total ?? 0));
  const success = data.map(row => Number(row.success ?? 0));
  const fail = data.map(row => Number(row.fail ?? 0));

  chart.setOption({
    title: { text: "调用趋势", left: "center", textStyle: { fontSize: 14 } },
    tooltip: { trigger: "axis" },
    legend: { data: ["总调用", "成功", "失败"], bottom: 0 },
    grid: { left: 50, right: 20, top: 40, bottom: 40 },
    xAxis: { type: "category", data: dates, boundaryGap: false },
    yAxis: { type: "value" },
    series: [
      { name: "总调用", type: "line", smooth: true, data: total, itemStyle: { color: "#409eff" } },
      { name: "成功", type: "line", smooth: true, data: success, itemStyle: { color: "#67c23a" } },
      { name: "失败", type: "line", smooth: true, data: fail, itemStyle: { color: "#f56c6c" } }
    ]
  });
  return chart;
}

async function loadApiStats() {
  const res = await statisticsApi.byApi();
  if (apiChartRef.value) {
    apiChart?.dispose();
    apiChart = renderBarChart(apiChartRef.value, res, "apiId", "按 API 调用统计");
  }
}

async function loadAppStats() {
  const res = await statisticsApi.byApp();
  if (appChartRef.value) {
    appChart?.dispose();
    appChart = renderBarChart(appChartRef.value, res, "appId", "按应用调用统计");
  }
}

async function loadTrend() {
  trendLoading.value = true;
  try {
    const res = await statisticsApi.trend(
      dateRange.value?.[0],
      dateRange.value?.[1]
    );
    if (trendChartRef.value) {
      trendChart?.dispose();
      trendChart = renderTrendChart(trendChartRef.value, res);
    }
  } finally {
    trendLoading.value = false;
  }
}

function handleResize() {
  apiChart?.resize();
  appChart?.resize();
  trendChart?.resize();
}

onMounted(async () => {
  loading.value = true;
  try {
    await Promise.all([loadApiStats(), loadAppStats()]);
    await loadTrend();
  } finally {
    loading.value = false;
  }
  window.addEventListener("resize", handleResize);
  await nextTick();
});

onBeforeUnmount(() => {
  window.removeEventListener("resize", handleResize);
  apiChart?.dispose();
  appChart?.dispose();
  trendChart?.dispose();
});
</script>

<template>
  <div class="p-4">
    <el-card shadow="never" class="mb-4">
      <div class="flex flex-wrap items-center justify-between gap-2">
        <div class="text-sm font-medium">统计图表</div>
        <div class="flex items-center gap-2">
          <el-date-picker
            v-model="dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="YYYY-MM-DD"
            style="width: 260px"
          />
          <el-button type="primary" :loading="trendLoading" @click="loadTrend">查询</el-button>
        </div>
      </div>
    </el-card>

    <div v-loading="loading">
      <el-row :gutter="16">
        <el-col :xs="24" :md="12">
          <el-card shadow="never">
            <div ref="apiChartRef" class="h-80 w-full" />
          </el-card>
        </el-col>
        <el-col :xs="24" :md="12">
          <el-card shadow="never">
            <div ref="appChartRef" class="h-80 w-full" />
          </el-card>
        </el-col>
      </el-row>
      <el-row class="mt-4">
        <el-col :span="24">
          <el-card shadow="never">
            <div v-loading="trendLoading" ref="trendChartRef" class="h-80 w-full" />
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>
