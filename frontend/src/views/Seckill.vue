<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import request from '../utils/request'

const loading = ref(false)
const tableData = ref([])
const pagination = ref({
  current: 1,
  pageSize: 10,
  total: 0
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
        <span>秒杀商品列表</span>
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
              @click="handleSeckill(row)"
              :disabled="row.seckillStock <= 0"
            >
              {{ row.seckillStock > 0 ? '立即秒杀' : '已售罄' }}
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
  </div>
</template>

<style scoped>
.page-title {
  margin-bottom: 20px;
  color: #303133;
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