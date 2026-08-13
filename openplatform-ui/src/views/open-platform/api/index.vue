<script setup lang="ts">
import { ref, reactive, onMounted } from "vue";
import { ElMessageBox } from "element-plus";
import { message } from "@/utils/message";
import { openApiApi } from "@/api/openApi";
import type { OpenApi } from "@/api/types";

defineOptions({ name: "OpenPlatformApi" });

const loading = ref(false);
const dataList = ref<OpenApi[]>([]);
const total = ref(0);
const selectedIds = ref<number[]>([]);
const query = reactive<{
  page: number;
  size: number;
  sort: string;
  name?: string;
  status?: number;
}>({
  page: 1,
  size: 10,
  sort: "createTime,desc"
});

const methodTag: Record<string, "success" | "primary" | "warning" | "danger"> = {
  GET: "success",
  POST: "primary",
  PUT: "warning",
  DELETE: "danger"
};

async function loadData() {
  loading.value = true;
  try {
    const res = await openApiApi.page({
      page: query.page,
      size: query.size,
      sort: query.sort,
      name: query.name || undefined,
      status: query.status
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
  query.name = undefined;
  query.status = undefined;
  handleSearch();
}

function handlePageChange() {
  loadData();
}

function handleSelectionChange(rows: OpenApi[]) {
  selectedIds.value = rows.map(row => row.id as number);
}

async function handleBatchDelete() {
  if (selectedIds.value.length === 0) {
    message("请先选择要删除的 API", { type: "warning" });
    return;
  }
  await ElMessageBox.confirm(`确定删除选中的 ${selectedIds.value.length} 个 API 吗？`, "删除确认", {
    type: "warning"
  });
  await openApiApi.batchDelete(selectedIds.value);
  message("删除成功", { type: "success" });
  loadData();
}

async function handleExport() {
  const blob = await openApiApi.exportExcel({
    name: query.name || undefined,
    status: query.status
  });
  const url = URL.createObjectURL(blob);
  const a = document.createElement("a");
  a.href = url;
  a.download = "API数据.xlsx";
  a.click();
  URL.revokeObjectURL(url);
}

const dialogVisible = ref(false);
const dialogTitle = ref("新增 API");
const isEdit = ref(false);
const formRef = ref();
const form = reactive<{
  id?: number;
  name: string;
  path: string;
  method: string;
  serviceUrl: string;
  description?: string;
  status: number;
}>({
  name: "",
  path: "",
  method: "GET",
  serviceUrl: "",
  description: "",
  status: 1
});

const rules = {
  name: [{ required: true, message: "请输入名称", trigger: "blur" }],
  path: [{ required: true, message: "请输入 API 路径", trigger: "blur" }],
  serviceUrl: [{ required: true, message: "请输入后端服务地址", trigger: "blur" }]
};

function openCreate() {
  isEdit.value = false;
  dialogTitle.value = "新增 API";
  Object.assign(form, {
    id: undefined,
    name: "",
    path: "",
    method: "GET",
    serviceUrl: "",
    description: "",
    status: 1
  });
  dialogVisible.value = true;
}

function openEdit(row: OpenApi) {
  isEdit.value = true;
  dialogTitle.value = "编辑 API";
  Object.assign(form, {
    id: row.id,
    name: row.name,
    path: row.path,
    method: row.method,
    serviceUrl: row.serviceUrl,
    description: row.description,
    status: row.status as number
  });
  dialogVisible.value = true;
}

async function handleSubmit() {
  await formRef.value.validate();
  const payload = {
    name: form.name,
    path: form.path,
    method: form.method,
    serviceUrl: form.serviceUrl,
    description: form.description,
    status: form.status
  };
  if (isEdit.value) {
    await openApiApi.update(form.id as number, payload);
  } else {
    await openApiApi.create(payload);
  }
  message(isEdit.value ? "修改成功" : "新增成功", { type: "success" });
  dialogVisible.value = false;
  loadData();
}

async function handleDelete(row: OpenApi) {
  await ElMessageBox.confirm(`确定删除 API「${row.name}」吗？`, "删除确认", {
    type: "warning"
  });
  await openApiApi.batchDelete([row.id as number]);
  message("删除成功", { type: "success" });
  loadData();
}

onMounted(loadData);
</script>

<template>
  <div class="p-4">
    <el-card shadow="never">
      <el-form :inline="true" class="search-form mb-4">
        <el-form-item label="名称">
          <el-input
            v-model="query.name"
            placeholder="请输入名称"
            clearable
            style="width: 200px"
            @keyup.enter="handleSearch"
          />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="query.status" placeholder="全部" clearable style="width: 120px">
            <el-option label="启用" :value="1" />
            <el-option label="禁用" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <div class="mb-4 flex items-center gap-2">
        <el-button type="primary" @click="openCreate">新增</el-button>
        <el-button type="danger" plain :disabled="selectedIds.length === 0" @click="handleBatchDelete">
          批量删除
        </el-button>
        <el-button type="success" plain @click="handleExport">导出</el-button>
      </div>

      <el-table
        v-loading="loading"
        :data="dataList"
        row-key="id"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="50" />
        <el-table-column prop="name" label="名称" min-width="130" show-overflow-tooltip />
        <el-table-column prop="path" label="路径" min-width="150" show-overflow-tooltip />
        <el-table-column label="请求方法" width="100">
          <template #default="{ row }">
            <el-tag :type="methodTag[row.method] || 'info'">{{ row.method }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="serviceUrl" label="服务地址" min-width="200" show-overflow-tooltip />
        <el-table-column label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? "启用" : "禁用" }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="描述" min-width="140" show-overflow-tooltip />
        <el-table-column prop="createTime" label="创建时间" width="170" />
        <el-table-column label="操作" width="140" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="openEdit(row)">编辑</el-button>
            <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
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

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="560px" destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="110px">
        <el-form-item label="名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入名称" />
        </el-form-item>
        <el-form-item label="路径" prop="path">
          <el-input v-model="form.path" placeholder="如 /api/user/list" />
        </el-form-item>
        <el-form-item label="请求方法">
          <el-select v-model="form.method" style="width: 100%">
            <el-option label="GET" value="GET" />
            <el-option label="POST" value="POST" />
            <el-option label="PUT" value="PUT" />
            <el-option label="DELETE" value="DELETE" />
          </el-select>
        </el-form-item>
        <el-form-item label="服务地址" prop="serviceUrl">
          <el-input v-model="form.serviceUrl" placeholder="如 http://localhost:8000/api/user/list" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" type="textarea" :rows="2" placeholder="请输入描述" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio :value="1">启用</el-radio>
            <el-radio :value="2">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>
