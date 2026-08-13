<script setup lang="ts">
import { ref, reactive, onMounted } from "vue";
import { ElMessageBox } from "element-plus";
import { message } from "@/utils/message";
import { userApi } from "@/api/user";
import type { User } from "@/api/types";

defineOptions({ name: "SystemUser" });

const loading = ref(false);
const dataList = ref<User[]>([]);
const total = ref(0);
const selectedIds = ref<number[]>([]);
const query = reactive<{
  page: number;
  size: number;
  sort: string;
  description?: string;
  status?: number;
}>({
  page: 1,
  size: 10,
  sort: "createTime,desc"
});

const genderMap: Record<number, string> = { 0: "未知", 1: "男", 2: "女" };

async function loadData() {
  loading.value = true;
  try {
    const res = await userApi.page({
      page: query.page,
      size: query.size,
      sort: query.sort,
      description: query.description || undefined,
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
  query.description = undefined;
  query.status = undefined;
  handleSearch();
}

function handlePageChange() {
  loadData();
}

function handleSelectionChange(rows: User[]) {
  selectedIds.value = rows.map(row => row.id as number);
}

/** 导出 */
async function handleExport() {
  const blob = await userApi.exportExcel({
    description: query.description || undefined,
    status: query.status
  });
  const url = URL.createObjectURL(blob);
  const a = document.createElement("a");
  a.href = url;
  a.download = "用户数据.xlsx";
  a.click();
  URL.revokeObjectURL(url);
}

/** 批量删除 */
async function handleBatchDelete() {
  if (selectedIds.value.length === 0) {
    message("请先选择要删除的用户", { type: "warning" });
    return;
  }
  await ElMessageBox.confirm(`确定删除选中的 ${selectedIds.value.length} 个用户吗？`, "删除确认", {
    type: "warning"
  });
  await userApi.batchDelete(selectedIds.value);
  message("删除成功", { type: "success" });
  loadData();
}

/** 新增 / 编辑 */
const dialogVisible = ref(false);
const dialogTitle = ref("新增用户");
const isEdit = ref(false);
const formRef = ref();
const form = reactive<{
  id?: number;
  username: string;
  password?: string;
  nickname?: string;
  gender?: number;
  email?: string;
  phone?: string;
  description?: string;
  status: number;
}>({
  username: "",
  password: "",
  nickname: "",
  gender: 0,
  email: "",
  phone: "",
  description: "",
  status: 1
});

const rules = {
  username: [{ required: true, message: "请输入用户名", trigger: "blur" }],
  password: [{ required: true, message: "请输入密码", trigger: "blur" }]
};

function openCreate() {
  isEdit.value = false;
  dialogTitle.value = "新增用户";
  Object.assign(form, {
    id: undefined,
    username: "",
    password: "",
    nickname: "",
    gender: 0,
    email: "",
    phone: "",
    description: "",
    status: 1
  });
  dialogVisible.value = true;
}

function openEdit(row: User) {
  isEdit.value = true;
  dialogTitle.value = "编辑用户";
  Object.assign(form, {
    id: row.id,
    username: row.username,
    password: undefined,
    nickname: row.nickname,
    gender: Number(row.gender ?? 0),
    email: row.email,
    phone: row.phone,
    description: row.description,
    status: row.status as number
  });
  dialogVisible.value = true;
}

async function handleSubmit() {
  await formRef.value.validate();
  if (isEdit.value) {
    await userApi.update(form.id as number, {
      username: form.username,
      nickname: form.nickname,
      gender: form.gender,
      email: form.email,
      phone: form.phone,
      description: form.description,
      status: form.status
    });
  } else {
    await userApi.create({
      username: form.username,
      password: form.password,
      nickname: form.nickname,
      gender: form.gender,
      email: form.email,
      phone: form.phone,
      description: form.description,
      status: form.status
    });
  }
  message(isEdit.value ? "修改成功" : "新增成功", { type: "success" });
  dialogVisible.value = false;
  loadData();
}

/** 删除 */
async function handleDelete(row: User) {
  await ElMessageBox.confirm(`确定删除用户「${row.username}」吗？`, "删除确认", {
    type: "warning"
  });
  await userApi.batchDelete([row.id as number]);
  message("删除成功", { type: "success" });
  loadData();
}

onMounted(loadData);
</script>

<template>
  <div class="p-4">
    <el-card shadow="never">
      <!-- 搜索 -->
      <el-form :inline="true" class="search-form mb-4">
        <el-form-item label="关键词">
          <el-input
            v-model="query.description"
            placeholder="描述关键词"
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

      <!-- 工具栏 -->
      <div class="mb-4 flex items-center gap-2">
        <el-button type="primary" @click="openCreate">新增</el-button>
        <el-button type="danger" plain :disabled="selectedIds.length === 0" @click="handleBatchDelete">
          批量删除
        </el-button>
        <el-button type="success" plain @click="handleExport">导出</el-button>
      </div>

      <!-- 表格 -->
      <el-table
        v-loading="loading"
        :data="dataList"
        row-key="id"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="50" />
        <el-table-column prop="username" label="用户名" min-width="110" show-overflow-tooltip />
        <el-table-column prop="nickname" label="昵称" min-width="110" show-overflow-tooltip />
        <el-table-column label="性别" width="70">
          <template #default="{ row }">{{ genderMap[row.gender] ?? "未知" }}</template>
        </el-table-column>
        <el-table-column prop="email" label="邮箱" min-width="170" show-overflow-tooltip />
        <el-table-column prop="phone" label="手机号" min-width="120" />
        <el-table-column label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? "启用" : "禁用" }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="170" />
        <el-table-column label="操作" width="140" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="openEdit(row)">编辑</el-button>
            <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
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

    <!-- 新增 / 编辑 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="560px" destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item v-if="!isEdit" label="密码" prop="password">
          <el-input v-model="form.password" type="password" show-password placeholder="请输入密码" />
        </el-form-item>
        <el-form-item label="昵称">
          <el-input v-model="form.nickname" placeholder="请输入昵称" />
        </el-form-item>
        <el-form-item label="性别">
          <el-select v-model="form.gender" style="width: 100%">
            <el-option label="未知" :value="0" />
            <el-option label="男" :value="1" />
            <el-option label="女" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="form.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="form.phone" placeholder="请输入手机号" />
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
