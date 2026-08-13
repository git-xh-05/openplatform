<template>
  <div class="page">
    <el-card shadow="never" class="table-card">
      <div class="toolbar">
        <div class="toolbar-left">
          <el-button type="primary" @click="openDialog(null)">
            <el-icon><Plus /></el-icon>新增菜单
          </el-button>
        </div>
      </div>
      <el-table :data="tableData" border stripe row-key="id" default-expand-all :tree-props="{ children: 'children' }">
        <el-table-column prop="title" label="菜单名称" />
        <el-table-column prop="type" label="类型" width="80" align="center">
          <template #default="{ row }">
            <el-tag v-if="row.type === 1" size="small" effect="plain">目录</el-tag>
            <el-tag v-else-if="row.type === 2" type="success" size="small" effect="plain">菜单</el-tag>
            <el-tag v-else type="warning" size="small" effect="plain">按钮</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="path" label="路由路径" />
        <el-table-column prop="component" label="组件路径" show-overflow-tooltip />
        <el-table-column prop="icon" label="图标" width="60" align="center">
          <template #default="{ row }">
            <el-icon v-if="row.icon"><component :is="row.icon" /></el-icon>
          </template>
        </el-table-column>
        <el-table-column prop="permission" label="权限标识" show-overflow-tooltip />
        <el-table-column prop="sort" label="排序" width="60" align="center" />
        <el-table-column prop="status" label="状态" width="80" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === 'ENABLE' ? 'success' : 'danger'" size="small" effect="plain">
              {{ row.status === 'ENABLE' ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="160" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="openDialog(row)">编辑</el-button>
            <el-popconfirm title="确认删除该菜单？" @confirm="handleDelete(row)">
              <template #reference>
                <el-button type="danger" link size="small">删除</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑菜单' : '新增菜单'" width="600px" @close="resetForm" destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px" status-icon>
        <el-form-item label="菜单类型" prop="type">
          <el-radio-group v-model="form.type">
            <el-radio :value="1">目录</el-radio>
            <el-radio :value="2">菜单</el-radio>
            <el-radio :value="3">按钮</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="父级菜单" prop="parentId">
          <el-tree-select :data="menuTreeData" :props="{ label: 'title', value: 'id' }" placeholder="顶级菜单"
            check-strictly clearable v-model="form.parentId" style="width: 100%" />
        </el-form-item>
        <el-form-item label="菜单名称" prop="title">
          <el-input v-model="form.title" />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item v-if="form.type !== 3" label="路由地址" prop="path">
              <el-input v-model="form.path" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item v-if="form.type === 2" label="组件路径" prop="component">
              <el-input v-model="form.component" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item v-if="form.type !== 3" label="路由名称" prop="name">
              <el-input v-model="form.name" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item v-if="form.type !== 3" label="图标" prop="icon">
              <el-input v-model="form.icon" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="权限标识" prop="permission">
              <el-input v-model="form.permission" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="排序" prop="sort">
              <el-input-number v-model="form.sort" :min="1" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio value="ENABLE">启用</el-radio>
            <el-radio value="DISABLE">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="handleSave">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { page, get, create, update, remove, batchDelete, listAll } from '@/api/menu'
import { Plus } from '@element-plus/icons-vue'

const tableData = ref([])
const dialogVisible = ref(false)
const isEdit = ref(false)
const saving = ref(false)
const formRef = ref(null)
const currentId = ref(null)
const allMenus = ref([])

const form = reactive({
  title: '', parentId: null, type: 1, path: '', name: '',
  component: '', icon: '', permission: '', sort: 1, status: 'ENABLE',
})

const rules = {
  title: [{ required: true, message: '请输入菜单名称', trigger: 'blur' }],
}

const menuTreeData = computed(() => {
  function buildTree(list, parentId) {
    return list
      .filter((m) => m.parentId === parentId && m.type !== 3)
      .map((m) => ({ id: m.id, title: m.title, children: buildTree(list, m.id) }))
  }
  return buildTree(allMenus.value, 0)
})

async function loadData() {
  const res = await page({ page: 1, size: 999 })
  const list = res.list || res.rows || []
  tableData.value = buildTree(list, 0)
  allMenus.value = list
}

function buildTree(list, parentId) {
  return list
    .filter((m) => m.parentId === parentId)
    .map((m) => ({ ...m, children: buildTree(list, m.id) }))
}

function openDialog(row) {
  if (row) {
    isEdit.value = true
    currentId.value = row.id
    Object.assign(form, row)
  } else {
    isEdit.value = false
    currentId.value = null
    resetForm()
  }
  dialogVisible.value = true
}

function resetForm() {
  Object.assign(form, {
    title: '', parentId: null, type: 1, path: '', name: '',
    component: '', icon: '', permission: '', sort: 1, status: 'ENABLE',
  })
  formRef.value?.resetFields()
}

async function handleSave() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  saving.value = true
  try {
    if (isEdit.value) {
      await update(currentId.value, form)
      ElMessage.success('修改成功')
    } else {
      await create(form)
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false
    loadData()
  } finally {
    saving.value = false
  }
}

async function handleDelete(row) {
  await remove(row.id)
  ElMessage.success('删除成功')
  loadData()
}

onMounted(loadData)
</script>

<style scoped>
.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}
.toolbar-left {
  display: flex;
  gap: 8px;
}
</style>