<template>
  <div class="page">
    <el-card shadow="never" class="table-card">
      <el-table :data="tableData" border stripe>
        <el-table-column prop="id" label="ID" width="70" />
        <el-table-column prop="appId" label="应用ID" width="80" />
        <el-table-column prop="apiId" label="API ID" width="80" />
        <el-table-column prop="statusCode" label="状态码" width="80" align="center">
          <template #default="{ row }">
            <el-tag :type="row.statusCode >= 200 && row.statusCode < 300 ? 'success' : 'danger'" size="small" effect="plain">
              {{ row.statusCode }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="costTime" label="耗时(ms)" width="90" align="right">
          <template #default="{ row }">
            <span>{{ row.costTime }}ms</span>
          </template>
        </el-table-column>
        <el-table-column prop="clientIp" label="客户端IP" width="140" />
        <el-table-column prop="createTime" label="调用时间" width="170" />
        <el-table-column label="操作" width="100" fixed="right" align="center">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="handleDetail(row)">详情</el-button>
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

    <el-dialog v-model="detailVisible" title="调用详情" width="650px">
      <el-form label-width="100px" v-if="detail">
        <el-form-item label="ID">
          <span>{{ detail.id }}</span>
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="应用ID">
              <span>{{ detail.appId }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="API ID">
              <span>{{ detail.apiId }}</span>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="状态码">
              <el-tag :type="detail.statusCode >= 200 && detail.statusCode < 300 ? 'success' : 'danger'" size="small" effect="plain">
                {{ detail.statusCode }}
              </el-tag>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="耗时">
              <span>{{ detail.costTime }}ms</span>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="客户端IP">
          <span>{{ detail.clientIp }}</span>
        </el-form-item>
        <el-form-item label="请求参数">
          <el-input type="textarea" :rows="3" :model-value="formatJson(detail.requestParams)" readonly />
        </el-form-item>
        <el-form-item label="响应体">
          <el-input type="textarea" :rows="3" :model-value="formatJson(detail.responseBody)" readonly />
        </el-form-item>
        <el-form-item v-if="detail.errorMessage" label="错误信息">
          <el-input type="textarea" :rows="2" :model-value="detail.errorMessage" readonly />
        </el-form-item>
        <el-form-item label="调用时间">
          <span>{{ detail.createTime }}</span>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { page, get } from '@/api/log'

const tableData = ref([])
const total = ref(0)
const detailVisible = ref(false)
const detail = ref(null)
const query = reactive({ page: 1, size: 10 })

function formatJson(str) {
  if (!str) return ''
  try {
    return JSON.stringify(JSON.parse(str), null, 2)
  } catch {
    return str
  }
}

async function loadData() {
  const res = await page(query)
  tableData.value = res.list || res.rows || []
  total.value = res.total || 0
}

async function handleDetail(row) {
  const res = await get(row.id)
  detail.value = res
  detailVisible.value = true
}

onMounted(loadData)
</script>

<style scoped>
.pagination {
  margin-top: 16px;
  display: flex;
  justify-content: flex-end;
}
</style>