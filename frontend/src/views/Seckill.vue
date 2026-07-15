<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import request from '../utils/request'

const loading = ref(false)
const tableData = ref([])
const dialogVisible = ref(false)
const formLoading = ref(false)
const pagination = ref({
  current: 1,
  pageSize: 10,
  total: 0
})

const form = ref({
  productId: '',
  seckillPrice: '',
  seckillStock: '',
  startTime: '',
  endTime: ''
})

const fetchData = async () => {
  loading.value = true
  try {
    const res = await request.get('/seckill/list', {
      params: {
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

const handleAdd = () => {
  form.value = {
    productId: '',
    seckillPrice: '',
    seckillStock: '',
    startTime: '',
    endTime: ''
  }
  dialogVisible.value = true
}

const handleSubmit = async () => {
  if (!form.value.productId || !form.value.seckillPrice || !form.value.seckillStock) {
    ElMessage.warning('请填写完整信息')
    return
  }
  formLoading.value = true
  try {
    await request.post('/seckill', form.value)
    ElMessage.success('创建成功')
    dialogVisible.value = false
    fetchData()
  } catch (error) {
    console.error(error)
  } finally {
    formLoading.value = false
  }
}

const handleSeckill = async (row) => {
  try {
    await request.post(`/seckill/do/${row.id}`)
    ElMessage.success('秒杀成功！')
    fetchData()
  } catch (error) {
    console.error(error)
  }
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
  <div class="seckill-container">
    <h2 class="page-title">秒杀活动</h2>
    
    <el-card class="table-card">
      <template #header>
        <div class="card-header">
          <span>秒杀商品列表</span>
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon> 创建秒杀
          </el-button>
        </div>
      </template>
      
      <el-table :data="tableData" v-loading="loading" border stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="productId" label="商品ID" width="100" />
        <el-table-column prop="seckillPrice" label="秒杀价格" width="120">
          <template #default="{ row }">
            <span class="price">¥{{ row.seckillPrice }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="seckillStock" label="剩余库存" width="100">
          <template #default="{ row }">
            <el-tag :type="row.seckillStock > 0 ? 'success' : 'danger'">
              {{ row.seckillStock }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="startTime" label="开始时间" width="180" />
        <el-table-column prop="endTime" label="结束时间" width="180" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'">
              {{ row.status === 0 ? '未开始' : row.status === 1 ? '进行中' : '已结束' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <el-button 
              type="danger" 
              size="small"
              @click="handleSeckill(row)"
              :disabled="row.seckillStock <= 0"
            >
              {{ row.seckillStock > 0 ? '秒杀' : '售罄' }}
            </el-button>
          </template>
        </el-table-column>
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

    <!-- 创建秒杀对话框 -->
    <el-dialog v-model="dialogVisible" title="创建秒杀商品" width="500px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="商品ID">
          <el-input v-model="form.productId" type="number" placeholder="请输入商品ID" />
        </el-form-item>
        <el-form-item label="秒杀价格">
          <el-input v-model="form.seckillPrice" type="number" placeholder="请输入秒杀价格" />
        </el-form-item>
        <el-form-item label="秒杀库存">
          <el-input v-model="form.seckillStock" type="number" placeholder="请输入秒杀库存" />
        </el-form-item>
        <el-form-item label="开始时间">
          <el-input v-model="form.startTime" type="datetime-local" />
        </el-form-item>
        <el-form-item label="结束时间">
          <el-input v-model="form.endTime" type="datetime-local" />
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

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.price {
  color: #f56c6c;
  font-weight: bold;
  font-size: 16px;
}

.pagination {
  margin-top: 20px;
  justify-content: flex-end;
}
</style>