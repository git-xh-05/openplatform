<script setup lang="ts">
import { ref, reactive, onMounted } from "vue";
import { logApi } from "@/api/log";
import type { ApiLog } from "@/api/types";

defineOptions({ name: "OpenPlatformLog" });

const loading = ref(false);
const dataList = ref<ApiLog[]>([]);
const total = ref(0);
const query = reactive<{
  page: number;
  size: number;
  sort: string;
  statusCode?: number;
  clientIp?: string;
}>({
  page: 1,
  size: 10,
  sort: "createTime,desc"
});

async function loadData() {
  loading.value = true;
  try {
    const res = await logApi.page({
      page: query.page,
      size: query.size,
      sort: query.sort,
      statusCode: query.statusCode,
      clientIp: query.clientIp || undefined
    });
    dataList.value = res.list;
    total.value = res.total;
  } finally {
    loading.value = false;
  }
}

function handleSearch() {
  query.page = 1;
  loadData();
}

function handleReset() {
  query.statusCode = undefined;
  query.clientIp = undefined;
  handleSearch();
}

function handlePageChange() {
  loadData();
}

function statusTag(code?: number): "success" | "danger" | "warning" {
  if (code === undefined) return "warning";
  return code < 400 ? "success" : "danger";
}

/** 日志详情 */
const detailVisible = ref(false);
const detailLoading = ref(false);
const detail = ref<ApiLog | null>(null);

async function handleView(row: ApiLog) {
  detailLoading.value = true;
  detailVisible.value = true;
  try {
    detail.value = await logApi.get(row.id);
  } finally {
    detailLoading.value = false;
  }
}

onMounted(loadData);
</script>

<template>
  <div class="p-4">
    <el-card shadow="never">
      <el-form :inline="true" class="search-form mb-4">
        <el-form-item label="状态码">
          <el-input-number
            v-model="query.statusCode"
            :min="100"
            :max="599"
            placeholder="如 200"
            controls-position="right"
            style="width: 130px"
          />
        </el-form-item>
        <el-form-item label="客户端IP">
          <el-input
            v-model="query.clientIp"
            placeholder="请输入客户端IP"
            clearable
            style="width: 180px"
            @keyup.enter="handleSearch"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table v-loading="loading" :data="dataList" row-key="id">
        <el-table-column prop="id" label="ID" width="70" />
        <el-table-column prop="appId" label="应用ID" width="90" />
        <el-table-column prop="apiId" label="API ID" width="90" />
        <el-table-column label="状态码" width="90">
          <template #default="{ row }">
            <el-tag :type="statusTag(row.statusCode)">{{ row.statusCode ?? "-" }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="耗时" width="110">
          <template #default="{ row }">{{ row.costTime ?? "-" }} ms</template>
        </el-table-column>
        <el-table-column prop="clientIp" label="客户端IP" width="140" />
        <el-table-column prop="errorMessage" label="错误信息" min-width="160" show-overflow-tooltip />
        <el-table-column prop="createTime" label="调用时间" width="170" />
        <el-table-column label="操作" width="90" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleView(row)">详情</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="mt-4 flex justify-end">
        <el-pagination
          v-model:current-page="query.page"
          v-model:page-size="query.size"
          :page-sizes="[10, 20, 50, 100]"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @change="handlePageChange"
        />
      </div>
    </el-card>

    <!-- 详情 -->
    <el-dialog v-model="detailVisible" title="日志详情" width="640px" destroy-on-close>
      <div v-loading="detailLoading">
        <el-descriptions v-if="detail" :column="2" border>
          <el-descriptions-item label="ID">{{ detail.id }}</el-descriptions-item>
          <el-descriptions-item label="应用ID">{{ detail.appId ?? "-" }}</el-descriptions-item>
          <el-descriptions-item label="API ID">{{ detail.apiId ?? "-" }}</el-descriptions-item>
          <el-descriptions-item label="状态码">{{ detail.statusCode ?? "-" }}</el-descriptions-item>
          <el-descriptions-item label="耗时">{{ detail.costTime ?? "-" }} ms</el-descriptions-item>
          <el-descriptions-item label="客户端IP">{{ detail.clientIp ?? "-" }}</el-descriptions-item>
          <el-descriptions-item label="调用时间" :span="2">
            {{ detail.createTime ?? "-" }}
          </el-descriptions-item>
          <el-descriptions-item label="请求参数" :span="2">
            <pre class="max-h-40 overflow-auto whitespace-pre-wrap break-all text-xs">{{ detail.requestParams ?? "-" }}</pre>
          </el-descriptions-item>
          <el-descriptions-item label="响应体" :span="2">
            <pre class="max-h-40 overflow-auto whitespace-pre-wrap break-all text-xs">{{ detail.responseBody ?? "-" }}</pre>
          </el-descriptions-item>
          <el-descriptions-item label="错误信息" :span="2">
            <pre class="max-h-24 overflow-auto whitespace-pre-wrap break-all text-xs text-red-500">{{ detail.errorMessage ?? "-" }}</pre>
          </el-descriptions-item>
        </el-descriptions>
      </div>
      <template #footer>
        <el-button type="primary" @click="detailVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>
