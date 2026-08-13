<script setup lang="ts">
import { ref, reactive, onMounted } from "vue";
import { ElMessageBox } from "element-plus";
import { message } from "@/utils/message";
import { appApi, getAppSecret, resetAppSecret } from "@/api/app";
import type { OpenApp, OpenAppSecret, Status } from "@/api/types";

defineOptions({ name: "OpenPlatformApp" });

const loading = ref(false);
const dataList = ref<OpenApp[]>([]);
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

async function loadData() {
  loading.value = true;
  try {
    const res = await appApi.page({
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

function handleSelectionChange(rows: OpenApp[]) {
  selectedIds.value = rows.map(row => row.id as number);
}

async function handleBatchDelete() {
  if (selectedIds.value.length === 0) {
    message("请先选择要删除的应用", { type: "warning" });
    return;
  }
  await ElMessageBox.confirm(`确定删除选中的 ${selectedIds.value.length} 个应用吗？`, "删除确认", {
    type: "warning"
  });
  await appApi.batchDelete(selectedIds.value);
  message("删除成功", { type: "success" });
  loadData();
}

/** 查看密钥 */
const secretDialogVisible = ref(false);
const secretLoading = ref(false);
const secretInfo = ref<OpenAppSecret | null>(null);
const secretAppName = ref("");

async function handleViewSecret(row: OpenApp) {
  secretLoading.value = true;
  secretDialogVisible.value = true;
  secretAppName.value = row.name;
  try {
    secretInfo.value = await getAppSecret(row.id as number);
  } finally {
    secretLoading.value = false;
  }
}

async function copyText(text: string) {
  await navigator.clipboard.writeText(text);
  message("已复制到剪贴板", { type: "success" });
}

/** 重置密钥 */
async function handleResetSecret(row: OpenApp) {
  await ElMessageBox.confirm(`确定重置应用「${row.name}」的密钥吗？重置后原密钥将失效。`, "重置确认", {
    type: "warning"
  });
  await resetAppSecret(row.id as number);
  message("密钥已重置", { type: "success" });
}

const dialogVisible = ref(false);
const dialogTitle = ref("新增应用");
const isEdit = ref(false);
const formRef = ref();
const form = reactive<{
  id?: number;
  name: string;
  expireTime?: string;
  ipBlacklist?: string;
  rateLimit?: number;
  description?: string;
  status: number;
}>({
  name: "",
  expireTime: undefined,
  ipBlacklist: "",
  rateLimit: 100,
  description: "",
  status: 1
});

const rules = {
  name: [{ required: true, message: "请输入应用名称", trigger: "blur" }]
};

function openCreate() {
  isEdit.value = false;
  dialogTitle.value = "新增应用";
  Object.assign(form, {
    id: undefined,
    name: "",
    expireTime: undefined,
    ipBlacklist: "",
    rateLimit: 100,
    description: "",
    status: 1
  });
  dialogVisible.value = true;
}

function openEdit(row: OpenApp) {
  isEdit.value = true;
  dialogTitle.value = "编辑应用";
  Object.assign(form, {
    id: row.id,
    name: row.name,
    expireTime: row.expireTime,
    ipBlacklist: row.ipBlacklist,
    rateLimit: row.rateLimit,
    description: row.description,
    status: row.status as number
  });
  dialogVisible.value = true;
}

async function handleSubmit() {
  await formRef.value.validate();
  const payload: Omit<OpenApp, "accessKey"> = {
    name: form.name,
    expireTime: form.expireTime,
    ipBlacklist: form.ipBlacklist,
    rateLimit: form.rateLimit,
    description: form.description,
    status: form.status as Status
  };
  if (isEdit.value) {
    await appApi.update(form.id as number, payload);
  } else {
    await appApi.create(payload);
  }
  message(isEdit.value ? "修改成功" : "新增成功", { type: "success" });
  dialogVisible.value = false;
  loadData();
}

async function handleDelete(row: OpenApp) {
  await ElMessageBox.confirm(`确定删除应用「${row.name}」吗？`, "删除确认", {
    type: "warning"
  });
  await appApi.batchDelete([row.id as number]);
  message("删除成功", { type: "success" });
  loadData();
}

onMounted(loadData);
</script>

<template>
  <div class="p-4">
    <el-card shadow="never">
      <el-form :inline="true" class="search-form mb-4">
        <el-form-item label="应用名称">
          <el-input
            v-model="query.name"
            placeholder="请输入应用名称"
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
      </div>

      <el-table
        v-loading="loading"
        :data="dataList"
        row-key="id"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="50" />
        <el-table-column prop="name" label="应用名称" min-width="130" show-overflow-tooltip />
        <el-table-column label="AccessKey" min-width="180" show-overflow-tooltip>
          <template #default="{ row }">
            <span class="font-mono text-xs">{{ row.accessKey }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="expireTime" label="失效时间" width="170" />
        <el-table-column label="速率限制" width="100">
          <template #default="{ row }">{{ row.rateLimit }} 次/秒</template>
        </el-table-column>
        <el-table-column label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? "启用" : "禁用" }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="描述" min-width="120" show-overflow-tooltip />
        <el-table-column prop="createTime" label="创建时间" width="170" />
        <el-table-column label="操作" width="230" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleViewSecret(row)">查看密钥</el-button>
            <el-button type="warning" link @click="handleResetSecret(row)">重置密钥</el-button>
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

    <!-- 查看密钥 -->
    <el-dialog v-model="secretDialogVisible" title="查看密钥" width="520px" destroy-on-close>
      <div v-loading="secretLoading">
        <el-alert
          type="warning"
          :closable="false"
          show-icon
          class="mb-4"
          title="请妥善保管密钥，SecretKey 仅在查看时显示，请勿泄露。"
        />
        <el-descriptions :column="1" border>
          <el-descriptions-item label="应用名称">{{ secretAppName }}</el-descriptions-item>
          <el-descriptions-item label="AccessKey">
            <div class="flex items-center gap-2">
              <span class="font-mono text-xs break-all">{{ secretInfo?.accessKey }}</span>
              <el-button
                v-if="secretInfo?.accessKey"
                type="primary"
                link
                @click="copyText(secretInfo.accessKey)"
              >
                复制
              </el-button>
            </div>
          </el-descriptions-item>
          <el-descriptions-item label="SecretKey">
            <div class="flex items-center gap-2">
              <span class="font-mono text-xs break-all">{{ secretInfo?.secretKey }}</span>
              <el-button
                v-if="secretInfo?.secretKey"
                type="primary"
                link
                @click="copyText(secretInfo.secretKey)"
              >
                复制
              </el-button>
            </div>
          </el-descriptions-item>
        </el-descriptions>
      </div>
      <template #footer>
        <el-button type="primary" @click="secretDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>

    <!-- 新增 / 编辑 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="520px" destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="90px">
        <el-form-item label="应用名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入应用名称" />
        </el-form-item>
        <el-form-item label="失效时间">
          <el-date-picker
            v-model="form.expireTime"
            type="datetime"
            placeholder="选择失效时间"
            value-format="YYYY-MM-DD HH:mm:ss"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="IP 黑名单">
          <el-input v-model="form.ipBlacklist" placeholder="多个 IP 用逗号分隔" />
        </el-form-item>
        <el-form-item label="速率限制">
          <el-input-number v-model="form.rateLimit" :min="0" :max="100000" />
          <span class="ml-2 text-xs text-gray-400">次/秒</span>
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
