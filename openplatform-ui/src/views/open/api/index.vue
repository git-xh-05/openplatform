<template>
  <div class="page">
    <el-card shadow="never" class="table-card">
      <div class="toolbar">
        <div class="toolbar-left">
          <el-button type="primary" @click="openDialog(null)">
            <el-icon><Plus /></el-icon>新增API
          </el-button>
          <el-button :disabled="selected.length === 0" @click="handleBatchDelete">
            <el-icon><Delete /></el-icon>批量删除
          </el-button>
        </div>
      </div>
      <el-table :data="tableData" border stripe @selection-change="(v) => (selected = v.map((r) => r.id))">
        <el-table-column type="selection" width="50" />
        <el-table-column prop="id" label="ID" width="70" />
        <el-table-column prop="name" label="名称" />
        <el-table-column prop="path" label="路径" show-overflow-tooltip />
        <el-table-column prop="method" label="方法" width="80" align="center">
          <template #default="{ row }">
            <el-tag size="small" :type="methodType(row.method)" effect="plain">{{ row.method }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="serviceUrl" label="后端服务地址" show-overflow-tooltip />
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
            <el-popconfirm title="确认删除该API？" @confirm="handleDelete(row)">
              <template #reference>
                <el-button type="danger" link size="small">删除</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination">
        <el-pagination
          v-model:current-page="query.page"
          v-model:page-size="query.size"
          :total="total"
          :page-sizes="[10, 20, 50]"
          layout="total, sizes, prev, pager, next"
          small
          @change="loadData"
        />
      </div>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑API' : '新增API'" width="550px" @close="resetForm" destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="110px" status-icon>
        <el-form-item label="名称" prop="name">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="请求方法" prop="method">
              <el-select v-model="form.method" style="width: 100%">
                <el-option label="GET" value="GET" />
                <el-option label="POST" value="POST" />
                <el-option label="PUT" value="PUT" />
                <el-option label="DELETE" value="DELETE" />
                <el-option label="PATCH" value="PATCH" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-radio-group v-model="form.status">
                <el-radio value="ENABLE">启用</el-radio>
                <el-radio value="DISABLE">禁用</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="路径" prop="path">
          <el-input v-model="form.path" placeholder="/api/example" />
        </el-form-item>
        <el-form-item label="后端服务地址" prop="serviceUrl">
          <el-input v-model="form.serviceUrl" placeholder="http://localhost:8081/example" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="form.description" type="textarea" :rows="3" />
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
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { page, get, create, update, remove, batchDelete } from '@/api/openapi'
import { Plus, Delete } from '@element-plus/icons-vue'

const tableData = ref([])
const total = ref(0)
const selected = ref([])
const dialogVisible = ref(false)
const isEdit = ref(false)
const saving = ref(false)
const formRef = ref(null)
const currentId = ref(null)

const query = reactive({ page: 1, size: 10 })
const form = reactive({
  name: '', method: 'GET', path: '', serviceUrl: '', status: 'ENABLE', description: '',
})

const rules = {
  name: [{ required: true, message: '请输入名称', trigger: 'blur' }],
  method: [{ required: true, message: '请选择请求方法', trigger: 'change' }],
  path: [{ required: true, message: '请输入路径', trigger: 'blur' }],
  serviceUrl: [{ required: true, message: '请输入后端服务地址', trigger: 'blur' }],
}

function methodType(method) {
  const map = { GET: '', POST: 'success', PUT: 'warning', DELETE: 'danger', PATCH: 'info' }
  return map[method] || ''
}

async function loadData() {
  const res = await page(query)
  tableData.value = res.list || res.rows || []
  total.value = res.total || 0
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
  Object.assign(form, { name: '', method: 'GET', path: '', serviceUrl: '', status: 'ENABLE', description: '' })
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

async function handleBatchDelete() {
  if (selected.value.length === 0) return
  try {
    await ElMessageBox.confirm(`确认删除 ${selected.value.length} 个API？`)
    await batchDelete(selected.value)
    ElMessage.success('删除成功')
    selected.value = []
    loadData()
  } catch {}
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
.pagination {
  margin-top: 16px;
  display: flex;
  justify-content: flex-end;
}
</style>