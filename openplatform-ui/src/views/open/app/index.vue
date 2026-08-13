<template>
  <div class="page">
    <el-card shadow="never" class="table-card">
      <div class="toolbar">
        <div class="toolbar-left">
          <el-button type="primary" @click="openDialog(null)">
            <el-icon><Plus /></el-icon>新增应用
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
        <el-table-column prop="accessKey" label="Access Key" show-overflow-tooltip />
        <el-table-column prop="expireTime" label="失效时间" width="170" />
        <el-table-column prop="rateLimit" label="速率限制" width="100" align="center" />
        <el-table-column prop="status" label="状态" width="80" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === 'ENABLE' ? 'success' : 'danger'" size="small" effect="plain">
              {{ row.status === 'ENABLE' ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="密钥" width="180" fixed="right" align="center">
          <template #default="{ row }">
            <el-button type="info" link size="small" @click="handleViewSecret(row)">查看密钥</el-button>
            <el-button type="warning" link size="small" @click="handleResetSecret(row)">重置</el-button>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="openDialog(row)">编辑</el-button>
            <el-popconfirm title="确认删除该应用？" @confirm="handleDelete(row)">
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

    <el-dialog v-model="secretVisible" title="应用密钥" width="500px">
      <el-form label-width="110px">
        <el-form-item label="Access Key">
          <el-input v-model="secret.accessKey" readonly>
            <template #append>
              <el-button @click="copyText(secret.accessKey)">复制</el-button>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item label="Secret Key">
          <el-input v-model="secret.secretKey" readonly>
            <template #append>
              <el-button @click="copyText(secret.secretKey)">复制</el-button>
            </template>
          </el-input>
        </el-form-item>
      </el-form>
    </el-dialog>

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑应用' : '新增应用'" width="550px" @close="resetForm" destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px" status-icon>
        <el-form-item label="名称" prop="name">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="失效时间" prop="expireTime">
              <el-date-picker v-model="form.expireTime" type="datetime" placeholder="永久有效" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="速率限制" prop="rateLimit">
              <el-input-number v-model="form.rateLimit" :min="0" placeholder="不限" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="IP 黑名单" prop="ipBlacklist">
          <el-input v-model="form.ipBlacklist" placeholder="多个IP用逗号分隔" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio value="ENABLE">启用</el-radio>
            <el-radio value="DISABLE">禁用</el-radio>
          </el-radio-group>
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
import { page, get, create, update, remove, batchDelete, getSecret, resetSecret } from '@/api/app'
import { Plus, Delete } from '@element-plus/icons-vue'

const tableData = ref([])
const total = ref(0)
const selected = ref([])
const dialogVisible = ref(false)
const secretVisible = ref(false)
const isEdit = ref(false)
const saving = ref(false)
const formRef = ref(null)
const currentId = ref(null)
const secret = ref({ accessKey: '', secretKey: '' })

const query = reactive({ page: 1, size: 10 })
const form = reactive({
  name: '', expireTime: null, rateLimit: null, ipBlacklist: '', status: 'ENABLE', description: '',
})

const rules = {
  name: [{ required: true, message: '请输入名称', trigger: 'blur' }],
}

function copyText(text) {
  navigator.clipboard.writeText(text).then(() => ElMessage.success('已复制'))
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
  Object.assign(form, { name: '', expireTime: null, rateLimit: null, ipBlacklist: '', status: 'ENABLE', description: '' })
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
    await ElMessageBox.confirm(`确认删除 ${selected.value.length} 个应用？`)
    await batchDelete(selected.value)
    ElMessage.success('删除成功')
    selected.value = []
    loadData()
  } catch {}
}

async function handleViewSecret(row) {
  const res = await getSecret(row.id)
  secret.value = res
  secretVisible.value = true
}

async function handleResetSecret(row) {
  try {
    await ElMessageBox.confirm('确认重置密钥？重置后旧密钥将立即失效。')
    await resetSecret(row.id)
    ElMessage.success('重置成功')
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