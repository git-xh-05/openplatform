<template>
  <div class="page">
    <el-card shadow="never" class="table-card">
      <div class="toolbar">
        <div class="toolbar-left">
          <el-button type="primary" @click="openDialog(null)">
            <el-icon><Plus /></el-icon>新增订阅
          </el-button>
          <el-button :disabled="selected.length === 0" @click="handleBatchDelete">
            <el-icon><Delete /></el-icon>批量删除
          </el-button>
        </div>
      </div>
      <el-table :data="tableData" border stripe @selection-change="(v) => (selected = v.map((r) => r.id))">
        <el-table-column type="selection" width="50" />
        <el-table-column prop="id" label="ID" width="70" />
        <el-table-column prop="appId" label="应用ID" width="80" />
        <el-table-column prop="apiId" label="API ID" width="80" />
        <el-table-column prop="quotaLimit" label="配额限制" />
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag v-if="row.status === 'APPROVED'" type="success" size="small" effect="plain">已通过</el-tag>
            <el-tag v-else-if="row.status === 'REJECTED'" type="danger" size="small" effect="plain">已拒绝</el-tag>
            <el-tag v-else type="warning" size="small" effect="plain">待审核</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="240" fixed="right">
          <template #default="{ row }">
            <el-button v-if="row.status === 'PENDING'" type="success" link size="small" @click="handleApprove(row)">通过</el-button>
            <el-button v-if="row.status === 'PENDING'" type="danger" link size="small" @click="handleReject(row)">拒绝</el-button>
            <el-button type="primary" link size="small" @click="openDialog(row)">编辑</el-button>
            <el-popconfirm title="确认删除该订阅？" @confirm="handleDelete(row)">
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

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑订阅' : '新增订阅'" width="500px" @close="resetForm" destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px" status-icon>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="应用ID" prop="appId">
              <el-input-number v-model="form.appId" :min="1" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="API ID" prop="apiId">
              <el-input-number v-model="form.apiId" :min="1" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="配额限制" prop="quotaLimit">
          <el-input-number v-model="form.quotaLimit" :min="0" placeholder="不限" style="width: 100%" />
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
import { page, get, create, update, remove, batchDelete, approve, reject } from '@/api/subscribe'
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
  appId: null, apiId: null, quotaLimit: null,
})

const rules = {
  appId: [{ required: true, message: '请输入应用ID', trigger: 'blur' }],
  apiId: [{ required: true, message: '请输入API ID', trigger: 'blur' }],
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
  Object.assign(form, { appId: null, apiId: null, quotaLimit: null })
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
    await ElMessageBox.confirm(`确认删除 ${selected.value.length} 个订阅？`)
    await batchDelete(selected.value)
    ElMessage.success('删除成功')
    selected.value = []
    loadData()
  } catch {}
}

async function handleApprove(row) {
  try {
    await ElMessageBox.confirm('确认通过该订阅申请？')
    await approve(row.id)
    ElMessage.success('已通过')
    loadData()
  } catch {}
}

async function handleReject(row) {
  try {
    await ElMessageBox.confirm('确认拒绝该订阅申请？')
    await reject(row.id)
    ElMessage.success('已拒绝')
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