<script setup lang="ts">
import { ref, reactive, onMounted } from "vue";
import { ElMessageBox } from "element-plus";
import { message } from "@/utils/message";
import { roleApi } from "@/api/role";
import type { Role } from "@/api/types";

defineOptions({ name: "SystemRole" });

const loading = ref(false);
const dataList = ref<Role[]>([]);
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
    const res = await roleApi.page({
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

function handleSelectionChange(rows: Role[]) {
  selectedIds.value = rows.map(row => row.id as number);
}

async function handleBatchDelete() {
  if (selectedIds.value.length === 0) {
    message("请先选择要删除的角色", { type: "warning" });
    return;
  }
  await ElMessageBox.confirm(`确定删除选中的 ${selectedIds.value.length} 个角色吗？`, "删除确认", {
    type: "warning"
  });
  await roleApi.batchDelete(selectedIds.value);
  message("删除成功", { type: "success" });
  loadData();
}

const dialogVisible = ref(false);
const dialogTitle = ref("新增角色");
const isEdit = ref(false);
const formRef = ref();
const form = reactive<{
  id?: number;
  name: string;
  code: string;
  description?: string;
  sort?: number;
  status: number;
}>({
  name: "",
  code: "",
  description: "",
  sort: 0,
  status: 1
});

const rules = {
  name: [{ required: true, message: "请输入角色名称", trigger: "blur" }],
  code: [{ required: true, message: "请输入角色编码", trigger: "blur" }]
};

function openCreate() {
  isEdit.value = false;
  dialogTitle.value = "新增角色";
  Object.assign(form, { id: undefined, name: "", code: "", description: "", sort: 0, status: 1 });
  dialogVisible.value = true;
}

function openEdit(row: Role) {
  isEdit.value = true;
  dialogTitle.value = "编辑角色";
  Object.assign(form, {
    id: row.id,
    name: row.name,
    code: row.code,
    description: row.description,
    sort: row.sort,
    status: row.status as number
  });
  dialogVisible.value = true;
}

async function handleSubmit() {
  await formRef.value.validate();
  if (isEdit.value) {
    await roleApi.update(form.id as number, {
      name: form.name,
      code: form.code,
      description: form.description,
      sort: form.sort,
      status: form.status
    });
  } else {
    await roleApi.create({
      name: form.name,
      code: form.code,
      description: form.description,
      sort: form.sort,
      status: form.status
    });
  }
  message(isEdit.value ? "修改成功" : "新增成功", { type: "success" });
  dialogVisible.value = false;
  loadData();
}

async function handleDelete(row: Role) {
  if (row.isSystem) {
    message("内置角色不可删除", { type: "warning" });
    return;
  }
  await ElMessageBox.confirm(`确定删除角色「${row.name}」吗？`, "删除确认", {
    type: "warning"
  });
  await roleApi.batchDelete([row.id as number]);
  message("删除成功", { type: "success" });
  loadData();
}

onMounted(loadData);
</script>

<template>
  <div class="p-4">
    <el-card shadow="never">
      <el-form :inline="true" class="search-form mb-4">
        <el-form-item label="角色名称">
          <el-input
            v-model="query.name"
            placeholder="请输入角色名称"
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
        <el-table-column prop="name" label="角色名称" min-width="120" show-overflow-tooltip />
        <el-table-column prop="code" label="角色编码" min-width="120" show-overflow-tooltip />
        <el-table-column prop="description" label="描述" min-width="160" show-overflow-tooltip />
        <el-table-column prop="sort" label="排序" width="80" />
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
            <el-button type="danger" link :disabled="row.isSystem" @click="handleDelete(row)">
              删除
            </el-button>
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
        <el-form-item label="角色名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入角色名称" />
        </el-form-item>
        <el-form-item label="角色编码" prop="code">
          <el-input v-model="form.code" placeholder="如 admin" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" type="textarea" :rows="2" placeholder="请输入描述" />
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="form.sort" :min="0" :max="9999" />
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
