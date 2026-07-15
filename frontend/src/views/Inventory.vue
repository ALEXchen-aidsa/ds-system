<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import request from '../utils/request'

const loading = ref(false)
const tableData = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('入库')
const formLoading = ref(false)

const searchForm = ref({
  warehouseId: ''
})

const form = ref({
  warehouseId: '',
  productId: '',
  quantity: '',
  remark: ''
})

const pagination = ref({
  current: 1,
  pageSize: 10,
  total: 0
})

const fetchData = async () => {
  loading.value = true
  try {
    const res = await request.get('/inventory/list', {
      params: {
        warehouseId: searchForm.value.warehouseId,
        page: pagination.value.current,
        size: pagination.value.pageSize
      }
    })
    tableData.value = res.data.records
    pagination.value.total = res.data.total
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

const handleIn = () => {
  dialogTitle.value = '入库'
  form.value = { warehouseId: '', productId: '', quantity: '', remark: '' }
  dialogVisible.value = true
}

const handleOut = () => {
  dialogTitle.value = '出库'
  form.value = { warehouseId: '', productId: '', quantity: '', remark: '' }
  dialogVisible.value = true
}

const handleSubmit = async () => {
  if (!form.value.warehouseId || !form.value.productId || !form.value.quantity) {
    ElMessage.warning('请填写完整信息')
    return
  }
  formLoading.value = true
  try {
    if (dialogTitle.value === '入库') {
      await request.post('/inventory/in', form.value)
      ElMessage.success('入库成功')
    } else {
      await request.post('/inventory/out', form.value)
      ElMessage.success('出库成功')
    }
    dialogVisible.value = false
    fetchData()
  } catch (error) {
    console.error(error)
  } finally {
    formLoading.value = false
  }
}

const handleSearch = () => {
  pagination.value.current = 1
  fetchData()
}

const handleSizeChange = (val) => {
  pagination.value.pageSize = val
  fetchData()
}

const handleCurrentChange = (val) => {
  pagination.value.current = val
  fetchData()
}

onMounted(() => {
  fetchData()
})
</script>

<template>
  <div class="inventory-container">
    <h2 class="page-title">库存管理</h2>
    
    <el-card class="search-card">
      <el-form :model="searchForm" inline>
        <el-form-item label="仓库ID">
          <el-input v-model="searchForm.warehouseId" placeholder="请输入仓库ID" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card>
      <template #header>
        <div class="card-header">
          <span>库存列表</span>
          <el-space>
            <el-button type="success" @click="handleIn">
              <el-icon><Plus /></el-icon> 入库
            </el-button>
            <el-button type="warning" @click="handleOut">
              <el-icon><Minus /></el-icon> 出库
            </el-button>
          </el-space>
        </div>
      </template>
      
      <el-table :data="tableData" v-loading="loading" border stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="warehouseId" label="仓库ID" width="100" />
        <el-table-column prop="productId" label="商品ID" width="100" />
        <el-table-column prop="quantity" label="库存数量" width="120">
          <template #default="{ row }">
            <el-tag :type="row.quantity > 10 ? 'success' : row.quantity > 0 ? 'warning' : 'danger'">
              {{ row.quantity }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="frozenQuantity" label="冻结数量" width="120" />
        <el-table-column prop="safetyStock" label="安全库存" width="120" />
        <el-table-column prop="updateTime" label="更新时间" width="180" />
      </el-table>

      <el-pagination
        class="pagination"
        v-model:current-page="pagination.current"
        v-model:page-size="pagination.pageSize"
        :page-sizes="[10, 20, 50]"
        :total="pagination.total"
        layout="total, sizes, prev, pager, next"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </el-card>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="仓库ID">
          <el-input v-model="form.warehouseId" type="number" placeholder="请输入仓库ID" />
        </el-form-item>
        <el-form-item label="商品ID">
          <el-input v-model="form.productId" type="number" placeholder="请输入商品ID" />
        </el-form-item>
        <el-form-item label="数量">
          <el-input v-model="form.quantity" type="number" placeholder="请输入数量" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="formLoading" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.page-title {
  margin-bottom: 20px;
  color: #303133;
}

.search-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.pagination {
  margin-top: 20px;
  justify-content: flex-end;
}
</style>