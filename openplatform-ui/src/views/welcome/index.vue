<script setup lang="ts">
import { ref, onMounted, onBeforeUnmount, computed } from "vue";
import echarts from "@/plugins/echarts";
import { statisticsApi } from "@/api/statistics";
import type { StatisticsRow } from "@/api/types";

defineOptions({ name: "Welcome" });

const loading = ref(false);

/** 汇总卡片数据（由 byApi 数据聚合） */
const summary = ref({
  total: 0,
  success: 0,
  fail: 0,
  totalCost: 0,
  count: 0
});
const avgCost = computed(() => {
  if (!summary.value.count) return 0;
  return (summary.value.totalCost / summary.value.count).toFixed(1);
});

/** Top API 列表 */
const topApis = ref<StatisticsRow[]>([]);

const trendChartRef = ref<HTMLElement>();
let trendChart: echarts.ECharts | null = null;

const cards = computed(() => [
  {
    label: "总调用次数",
    value: summary.value.total,
    color: "#409eff",
    icon: "i-ep:data-line"
  },
  {
    label: "成功调用",
    value: summary.value.success,
    color: "#67c23a",
    icon: "i-ep:circle-check"
  },
  {
    label: "失败调用",
    value: summary.value.fail,
    color: "#f56c6c",
    icon: "i-ep:warning"
  },
  {
    label: "平均耗时 (ms)",
    value: avgCost.value,
    color: "#e6a23c",
    icon: "i-ep:timer"
  }
]);

function renderTrend(el: HTMLElement, data: StatisticsRow[]) {
  const chart = echarts.init(el);
  const dates = data.map(row => String(row.date ?? "-"));
  const total = data.map(row => Number(row.total ?? 0));
  const success = data.map(row => Number(row.success ?? 0));
  const fail = data.map(row => Number(row.fail ?? 0));

  chart.setOption({
    title: { text: "近 7 日调用趋势", left: "center", textStyle: { fontSize: 14 } },
    tooltip: { trigger: "axis" },
    legend: { data: ["总调用", "成功", "失败"], bottom: 0 },
    grid: { left: 50, right: 20, top: 40, bottom: 40 },
    xAxis: { type: "category", data: dates, boundaryGap: false },
    yAxis: { type: "value" },
    series: [
      { name: "总调用", type: "line", smooth: true, areaStyle: { opacity: 0.15 }, data: total, itemStyle: { color: "#409eff" } },
      { name: "成功", type: "line", smooth: true, data: success, itemStyle: { color: "#67c23a" } },
      { name: "失败", type: "line", smooth: true, data: fail, itemStyle: { color: "#f56c6c" } }
    ]
  });
  return chart;
}

async function loadData() {
  loading.value = true;
  try {
    const [apiStats, trendData] = await Promise.all([
      statisticsApi.byApi(),
      statisticsApi.trend()
    ]);

    let total = 0;
    let success = 0;
    let fail = 0;
    let totalCost = 0;
    let count = 0;
    apiStats.forEach(row => {
      total += Number(row.total ?? 0);
      success += Number(row.success ?? 0);
      fail += Number(row.fail ?? 0);
      totalCost += Number(row.avgCostTime ?? 0) * Number(row.total ?? 0);
      count += Number(row.total ?? 0);
    });
    summary.value = { total, success, fail, totalCost, count };

    // Top5：按总调用排序取前 5
    topApis.value = [...apiStats]
      .sort((a, b) => Number(b.total ?? 0) - Number(a.total ?? 0))
      .slice(0, 5);

    if (trendChartRef.value) {
      trendChart?.dispose();
      trendChart = renderTrend(trendChartRef.value, trendData);
    }
  } finally {
    loading.value = false;
  }
}

function handleResize() {
  trendChart?.resize();
}

onMounted(() => {
  loadData();
  window.addEventListener("resize", handleResize);
});

onBeforeUnmount(() => {
  window.removeEventListener("resize", handleResize);
  trendChart?.dispose();
});
</script>

<template>
  <div v-loading="loading" class="p-4">
    <!-- 统计卡片 -->
    <el-row :gutter="16">
      <el-col v-for="card in cards" :key="card.label" :xs="12" :md="6">
        <el-card shadow="never" class="mb-4">
          <div class="flex items-center justify-between">
            <div>
              <div class="text-sm text-gray-500">{{ card.label }}</div>
              <div class="mt-1 text-2xl font-bold" :style="{ color: card.color }">
                {{ card.value }}
              </div>
            </div>
            <el-icon :size="36" :color="card.color">
              <component :is="card.icon" />
            </el-icon>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="16">
      <!-- 趋势图 -->
      <el-col :xs="24" :md="16">
        <el-card shadow="never" class="mb-4">
          <div ref="trendChartRef" class="h-80 w-full" />
        </el-card>
      </el-col>

      <!-- Top5 -->
      <el-col :xs="24" :md="8">
        <el-card shadow="never" class="mb-4">
          <template #header>
            <span class="text-sm font-medium">调用量 Top5 API</span>
          </template>
          <div v-if="topApis.length === 0" class="py-8 text-center text-sm text-gray-400">
            暂无调用数据
          </div>
          <el-table v-else :data="topApis" size="small">
            <el-table-column label="API ID" prop="apiId" width="70" />
            <el-table-column label="总调用" prop="total" width="80" />
            <el-table-column label="成功率" width="90">
              <template #default="{ row }">
                <span
                  :class="Number(row.total) > 0 && Number(row.fail) > 0 ? 'text-red-500' : 'text-green-500'"
                >
                  {{
                    Number(row.total) > 0
                      ? ((Number(row.success) / Number(row.total)) * 100).toFixed(1) + "%"
                      : "-"
                  }}
                </span>
              </template>
            </el-table-column>
            <el-table-column label="平均耗时">
              <template #default="{ row }">{{ row.avgCostTime ?? "-" }} ms</template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>
