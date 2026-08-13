<script setup lang="ts">
import { ref, reactive, onMounted } from "vue";
import { ElMessageBox } from "element-plus";
import { message } from "@/utils/message";
import { menuApi } from "@/api/menu";
import { handleTree } from "@/utils/tree";
import type { Menu } from "@/api/types";

defineOptions({ name: "SystemMenu" });

const loading = ref(false);
const treeData = ref<Menu[]>([]);
const query = reactive<{
  title?: string;
  status?: number;
}>({});

const typeMap: Record<number, { label: string; tag: "warning" | "primary" | "info" }> = {
  1: { label: "目录", tag: "warning" },
  2: { label: "菜单", tag: "primary" },
  3: { label: "按钮", tag: "info" }
};

async function loadData() {
  loading.value = true;
  try {
    const res = await menuApi.page({
      page: 1,
      size: 999,
      sort: "sort,asc",
      title: query.title || undefined,
      status: query.status
    });
    treeData.value = handleTree(res.list as unknown as Record<string, any>[], "id", "parentId") as unknown as Menu[];
  } finally {
    loading.value = false;
  }
}

function handleSearch() {
  loadData();
}

function handleReset() {
  query.title = undefined;
  query.status = undefined;
  loadData();
}

const dialogVisible = ref(false);
const dialogTitle = ref("新增菜单");
const isEdit = ref(false);
const formRef = ref();
const form = reactive<Menu>({
  title: "",
  parentId: 0,
  type: 1,
  path: "",
  name: "",
  component: "",
  redirect: "",
  icon: "",
  isExternal: false,
  isCache: false,
  isHidden: false,
  permission: "",
  sort: 0,
  status: 1
});

const rules = {
  title: [{ required: true, message: "请输入菜单标题", trigger: "blur" }],
  path: [{ required: true, message: "请输入路由地址", trigger: "blur" }]
};

function openCreate(parent?: Menu) {
  isEdit.value = false;
  dialogTitle.value = "新增菜单";
  Object.assign(form, {
    id: undefined,
    title: "",
    parentId: parent?.id ?? 0,
    type: 1,
    path: "",
    name: "",
    component: "",
    redirect: "",
    icon: "",
    isExternal: false,
    isCache: false,
    isHidden: false,
    permission: "",
    sort: 0,
    status: 1
  });
  dialogVisible.value = true;
}

function openEdit(row: Menu) {
  isEdit.value = true;
  dialogTitle.value = "编辑菜单";
  Object.assign(form, {
    id: row.id,
    title: row.title,
    parentId: row.parentId ?? 0,
    type: row.type,
    path: row.path,
    name: row.name,
    component: row.component,
    redirect: row.redirect,
    icon: row.icon,
    isExternal: row.isExternal,
    isCache: row.isCache,
    isHidden: row.isHidden,
    permission: row.permission,
    sort: row.sort,
    status: row.status as number
  });
  dialogVisible.value = true;
}

async function handleSubmit() {
  await formRef.value.validate();
  const payload = {
    title: form.title,
    parentId: form.parentId,
    type: form.type,
    path: form.type === 3 ? undefined : form.path,
    name: form.type === 3 ? undefined : form.name,
    component: form.type === 3 ? undefined : form.component,
    redirect: form.type === 3 ? undefined : form.redirect,
    icon: form.type === 3 ? undefined : form.icon,
    isExternal: form.type === 3 ? undefined : form.isExternal,
    isCache: form.type === 3 ? undefined : form.isCache,
    isHidden: form.type === 3 ? undefined : form.isHidden,
    permission: form.type === 3 ? form.permission : undefined,
    sort: form.sort,
    status: form.status
  };
  if (isEdit.value) {
    await menuApi.update(form.id as number, payload as Menu);
  } else {
    await menuApi.create(payload as Menu);
  }
  message(isEdit.value ? "修改成功" : "新增成功", { type: "success" });
  dialogVisible.value = false;
  loadData();
}

async function handleDelete(row: Menu) {
  if (row.children && row.children.length > 0) {
    message("请先删除其子级菜单", { type: "warning" });
    return;
  }
  await ElMessageBox.confirm(`确定删除菜单「${row.title}」吗？`, "删除确认", {
    type: "warning"
  });
  await menuApi.batchDelete([row.id as number]);
  message("删除成功", { type: "success" });
  loadData();
}

onMounted(loadData);
</script>

<template>
  <div class="p-4">
    <el-card shadow="never">
      <el-form :inline="true" class="search-form mb-4">
        <el-form-item label="菜单标题">
          <el-input
            v-model="query.title"
            placeholder="请输入菜单标题"
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

      <div class="mb-4">
        <el-button type="primary" @click="openCreate()">新增</el-button>
      </div>

      <el-table
        v-loading="loading"
        :data="treeData"
        row-key="id"
        default-expand-all
        :tree-props="{ children: 'children' }"
      >
        <el-table-column prop="title" label="菜单标题" min-width="160" show-overflow-tooltip />
        <el-table-column label="类型" width="80">
          <template #default="{ row }">
            <el-tag :type="typeMap[row.type]?.tag">{{ typeMap[row.type]?.label }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="path" label="路由地址" min-width="150" show-overflow-tooltip />
        <el-table-column prop="component" label="组件路径" min-width="170" show-overflow-tooltip />
        <el-table-column prop="permission" label="权限标识" min-width="150" show-overflow-tooltip />
        <el-table-column prop="sort" label="排序" width="70" />
        <el-table-column label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? "启用" : "禁用" }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="190" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="openCreate(row)">新增子级</el-button>
            <el-button type="primary" link @click="openEdit(row)">编辑</el-button>
            <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="560px" destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="90px">
        <el-form-item label="上级菜单">
          <el-tree-select
            v-model="form.parentId"
            :data="treeData"
            :props="{ label: 'title', children: 'children' }"
            node-key="id"
            check-strictly
            placeholder="顶级菜单"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="菜单类型">
          <el-radio-group v-model="form.type">
            <el-radio :value="1">目录</el-radio>
            <el-radio :value="2">菜单</el-radio>
            <el-radio :value="3">按钮</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="菜单标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入菜单标题" />
        </el-form-item>
        <template v-if="form.type !== 3">
          <el-form-item label="路由地址" prop="path">
            <el-input v-model="form.path" placeholder="如 /system/user" />
          </el-form-item>
          <el-form-item label="路由名称">
            <el-input v-model="form.name" placeholder="如 SystemUser" />
          </el-form-item>
          <el-form-item label="组件路径">
            <el-input v-model="form.component" placeholder="如 system/user/index" />
          </el-form-item>
          <el-form-item label="重定向">
            <el-input v-model="form.redirect" placeholder="重定向地址" />
          </el-form-item>
          <el-form-item label="图标">
            <el-input v-model="form.icon" placeholder="如 user" />
          </el-form-item>
        </template>
        <el-form-item v-if="form.type === 3" label="权限标识">
          <el-input v-model="form.permission" placeholder="如 system:user:list" />
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
        <template v-if="form.type !== 3">
          <el-form-item label="外链">
            <el-switch v-model="form.isExternal" />
          </el-form-item>
          <el-form-item label="缓存">
            <el-switch v-model="form.isCache" />
          </el-form-item>
          <el-form-item label="隐藏">
            <el-switch v-model="form.isHidden" />
          </el-form-item>
        </template>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>
