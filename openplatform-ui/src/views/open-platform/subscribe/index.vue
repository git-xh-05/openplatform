<script setup lang="ts">
import { ref, reactive, onMounted } from "vue";
import { ElMessageBox } from "element-plus";
import { message } from "@/utils/message";
import { subscribeApi, approveSubscribe, rejectSubscribe } from "@/api/subscribe";
import type { Subscribe } from "@/api/types";

defineOptions({ name: "OpenPlatformSubscribe" });

const loading = ref(false);
const dataList = ref<Subscribe[]>([]);
const total = ref(0);
const selectedIds = ref<number[]>([]);
const query = reactive<{
  page: number;
  size: number;
  sort: string;
  appId?: number;
  status?: number;
}>({
  page: 1,
  size: 10,
  sort: "createTime,desc"
});

const statusMap: Record<number, { label: string; tag: "warning" | "success" | "danger" }> = {
  0: { label: "待审核", tag: "warning" },
  1: { label: "已通过", tag: "success" },
  2: { label: "已拒绝", tag: "danger" }
};

async function loadData() {
  loading.value = true;
  try {
    const res = await subscribeApi.page({
      page: query.page,
      size: query.size,
      sort: query.sort,
      appId: query.appId,
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
  query.appId = undefined;
  query.status = undefined;
  handleSearch();
}

function handlePageChange() {
  loadData();
}

function handleSelectionChange(rows: Subscribe[]) {
  selectedIds.value = rows.map(row => row.id as number);
}

async function handleBatchDelete() {
  if (selectedIds.value.length === 0) {
    message("请先选择要删除的订阅", { type: "warning" });
    return;
  }
  await ElMessageBox.confirm(`确定删除选中的 ${selectedIds.value.length} 条订阅吗？`, "删除确认", {
    type: "warning"
  });
  await subscribeApi.batchDelete(selectedIds.value);
  message("删除成功", { type: "success" });
  loadData();
}

/** 审核通过 */
async function handleApprove(row: Subscribe) {
  await ElMessageBox.confirm(`确定通过订阅 #${row.id} 的审核吗？`, "审核确认", {
    type: "warning"
  });
  await approveSubscribe(row.id as number);
  message("审核已通过", { type: "success" });
  loadData();
}

/** 审核拒绝 */
async function handleReject(row: Subscribe) {
  await ElMessageBox.confirm(`确定拒绝订阅 #${row.id} 吗？`, "审核确认", {
    type: "warning"
  });
  await rejectSubscribe(row.id as number);
  message("已拒绝该订阅", { type: "success" });
  loadData();
}

const dialogVisible = ref(false);
const dialogTitle = ref("新增订阅");
const isEdit = ref(false);
const formRef = ref();
const form = reactive<{
  id?: number;
  appId?: number;
  apiId?: number;
  quotaLimit?: number;
  status: number;
}>({
  appId: undefined,
  apiId: undefined,
  quotaLimit: undefined,
  status: 0
});

const rules = {
  appId: [{ required: true, message: "请输入应用ID", trigger: "blur" }],
  apiId: [{ required: true, message: "请输入API ID", trigger: "blur" }]
};

function openCreate() {
  isEdit.value = false;
  dialogTitle.value = "新增订阅";
  Object.assign(form, {
    id: undefined,
    appId: undefined,
    apiId: undefined,
    quotaLimit: undefined,
    status: 0
  });
  dialogVisible.value = true;
}

function openEdit(row: Subscribe) {
  isEdit.value = true;
  dialogTitle.value = "编辑订阅";
  Object.assign(form, {
    id: row.id,
    appId: row.appId,
    apiId: row.apiId,
    quotaLimit: row.quotaLimit,
    status: row.status ?? 0
  });
  dialogVisible.value = true;
}

async function handleSubmit() {
  await formRef.value.validate();
  const payload = {
    appId: form.appId,
    apiId: form.apiId,
    quotaLimit: form.quotaLimit,
    status: form.status
  };
  if (isEdit.value) {
    await subscribeApi.update(form.id as number, payload);
  } else {
    await subscribeApi.create(payload);
  }
  message(isEdit.value ? "修改成功" : "新增成功", { type: "success" });
  dialogVisible.value = false;
  loadData();
}

async function handleDelete(row: Subscribe) {
  await ElMessageBox.confirm(`确定删除订阅 #${row.id} 吗？`, "删除确认", {
    type: "warning"
  });
  await subscribeApi.batchDelete([row.id as number]);
  message("删除成功", { type: "success" });
  loadData();
}

onMounted(loadData);
</script>

<template>
  <div class="p-4">
    <el-card shadow="never">
      <el-form :inline="true" class="search-form mb-4">
        <el-form-item label="应用ID">
          <el-input-number v-model="query.appId" :min="0" placeholder="应用ID" controls-position="right" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="query.status" placeholder="全部" clearable style="width: 130px">
            <el-option label="待审核" :value="0" />
            <el-option label="已通过" :value="1" />
            <el-option label="已拒绝" :value="2" />
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
      </div>

      <el-table
        v-loading="loading"
        :data="dataList"
        row-key="id"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="50" />
        <el-table-column prop="id" label="ID" width="70" />
        <el-table-column prop="appId" label="应用ID" width="100" />
        <el-table-column prop="apiId" label="API ID" width="100" />
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="statusMap[row.status]?.tag || 'info'">
              {{ statusMap[row.status]?.label ?? "未知" }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="quotaLimit" label="配额限制" width="110" />
        <el-table-column prop="approveTime" label="审批时间" width="170" />
        <el-table-column prop="createTime" label="创建时间" width="170" />
        <el-table-column label="操作" width="210" fixed="right">
          <template #default="{ row }">
            <template v-if="row.status === 0">
              <el-button type="success" link @click="handleApprove(row)">通过</el-button>
              <el-button type="danger" link @click="handleReject(row)">拒绝</el-button>
            </template>
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

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="480px" destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="90px">
        <el-form-item label="应用ID" prop="appId">
          <el-input-number v-model="form.appId" :min="0" style="width: 100%" />
        </el-form-item>
        <el-form-item label="API ID" prop="apiId">
          <el-input-number v-model="form.apiId" :min="0" style="width: 100%" />
        </el-form-item>
        <el-form-item label="配额限制">
          <el-input-number v-model="form.quotaLimit" :min="0" :max="10000000" style="width: 100%" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio :value="0">待审核</el-radio>
            <el-radio :value="1">已通过</el-radio>
            <el-radio :value="2">已拒绝</el-radio>
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
