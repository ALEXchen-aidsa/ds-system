<script setup>
import { ref, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getOrderList, createOrder, updateOrderStatus, getOrderItems, getProductList } from '../api'

const loading = ref(false)
const tableData = ref([])
const searchForm = ref({
  orderNo: '',
  status: ''
})
const pagination = ref({
  current: 1,
  pageSize: 10,
  total: 0
})

const statusOptions = [
  { label: '待支付', value: 0 },
  { label: '已支付', value: 1 },
  { label: '已发货', value: 2 },
  { label: '已完成', value: 3 },
  { label: '已取消', value: 4 }
]

const statusTagType = {
  0: 'warning',
  1: 'primary',
  2: 'info',
  3: 'success',
  4: 'danger'
}

const statusLabel = (val) => statusOptions.find(item => item.value === val)?.label || '未知'

// ===== 创建订单 =====
const createDialogVisible = ref(false)
const productList = ref([])
const orderItems = ref([])
const submitLoading = ref(false)

const openCreateDialog = async () => {
  orderItems.value = []
  const res = await getProductList({ page: 1, size: 100 })
  productList.value = res.data.records.filter(p => p.status === 1 && p.stock > 0)
  addOrderItem()
  createDialogVisible.value = true
}

const addOrderItem = () => {
  orderItems.value.push({ productId: null, quantity: 1 })
}

const removeOrderItem = (index) => {
  orderItems.value.splice(index, 1)
}

const selectedItem = (item) => {
  const product = productList.value.find(p => p.id === item.productId)
  return product
}

const computedTotal = computed(() => {
  return orderItems.value.reduce((sum, item) => {
    const product = selectedItem(item)
    return sum + (product ? product.price * item.quantity : 0)
  }, 0)
})

const handleCreateOrder = async () => {
  const validItems = orderItems.value.filter(i => i.productId && i.quantity > 0)
  if (validItems.length === 0) {
    ElMessage.warning('请至少选择一个商品')
    return
  }
  submitLoading.value = true
  try {
    await createOrder({ items: validItems })
    ElMessage.success('订单创建成功')
    createDialogVisible.value = false
    fetchData()
  } catch (error) {
    // error message already shown by interceptor
  } finally {
    submitLoading.value = false
  }
}

// ===== 订单状态操作 =====
const handleUpdateStatus = async (row, newStatus) => {
  const labels = { 1: '支付', 2: '发货', 3: '完成', 4: '取消' }
  try {
    await ElMessageBox.confirm(
      `确定要将订单 ${row.orderNo} 标记为"${labels[newStatus]}"吗？`,
      '确认操作',
      { type: 'warning' }
    )
    await updateOrderStatus(row.id, newStatus)
    ElMessage.success('状态更新成功')
    fetchData()
  } catch (error) {
    if (error !== 'cancel') {
      // error message already shown by interceptor
    }
  }
}

// 可执行的状态操作
const availableActions = (status) => {
  const actions = []
  if (status === 0) {
    actions.push({ label: '支付', type: 'success', status: 1 })
    actions.push({ label: '取消', type: 'danger', status: 4 })
  } else if (status === 1) {
    actions.push({ label: '发货', type: 'primary', status: 2 })
    actions.push({ label: '取消', type: 'danger', status: 4 })
  } else if (status === 2) {
    actions.push({ label: '完成', type: 'success', status: 3 })
  }
  return actions
}

// ===== 订单详情 =====
const detailDialogVisible = ref(false)
const detailOrder = ref(null)
const detailItems = ref([])
const detailLoading = ref(false)

const handleViewDetail = async (row) => {
  detailOrder.value = row
  detailDialogVisible.value = true
  detailLoading.value = true
  try {
    const res = await getOrderItems(row.id)
    detailItems.value = res.data
  } catch (error) {
    console.error(error)
  } finally {
    detailLoading.value = false
  }
}

// ===== 列表查询 =====
const fetchData = async () => {
  loading.value = true
  try {
    const res = await getOrderList({
      ...searchForm.value,
      page: pagination.value.current,
      size: pagination.value.pageSize
    })
    tableData.value = res.data.records
    pagination.value.total = res.data.total
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  pagination.value.current = 1
  fetchData()
}

const handleReset = () => {
  searchForm.value = { orderNo: '', status: '' }
  handleSearch()
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
  <div class="order-container">
    <h2 class="page-title">订单管理</h2>
    
    <el-card class="search-card">
      <el-form :model="searchForm" inline>
        <el-form-item label="订单号">
          <el-input v-model="searchForm.orderNo" placeholder="请输入订单号" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable style="width: 120px">
            <el-option 
              v-for="item in statusOptions" 
              :key="item.value" 
              :label="item.label" 
              :value="item.value" 
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="handleReset">重置</el-button>
          <el-button type="success" @click="openCreateDialog">+ 创建订单</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="table-card">
      <template #header>
        <span>订单列表</span>
      </template>
      
      <el-table :data="tableData" v-loading="loading" border stripe>
        <el-table-column prop="orderNo" label="订单号" width="200" />
        <el-table-column prop="totalAmount" label="订单金额" width="120">
          <template #default="{ row }">
            ¥{{ row.totalAmount }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="statusTagType[row.status]">
              {{ statusLabel(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column prop="payTime" label="支付时间" width="180">
          <template #default="{ row }">
            {{ row.payTime || '—' }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="280" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="handleViewDetail(row)">详情</el-button>
            <el-button 
              v-for="action in availableActions(row.status)"
              :key="action.status"
              :type="action.type"
              size="small"
              @click="handleUpdateStatus(row, action.status)"
            >
              {{ action.label }}
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

    <!-- 创建订单弹窗 -->
    <el-dialog v-model="createDialogVisible" title="创建订单" width="650px">
      <el-table :data="orderItems" border>
        <el-table-column label="商品" min-width="200">
          <template #default="{ row }">
            <el-select v-model="row.productId" placeholder="选择商品" filterable>
              <el-option 
                v-for="p in productList" 
                :key="p.id" 
                :label="`${p.name}（库存${p.stock}）`" 
                :value="p.id"
              />
            </el-select>
          </template>
        </el-table-column>
        <el-table-column label="单价" width="100">
          <template #default="{ row }">
            <span v-if="selectedItem(row)">¥{{ selectedItem(row).price }}</span>
            <span v-else>—</span>
          </template>
        </el-table-column>
        <el-table-column label="数量" width="120">
          <template #default="{ row, $index }">
            <el-input-number v-model="row.quantity" :min="1" :max="selectedItem(row)?.stock || 99" size="small" />
          </template>
        </el-table-column>
        <el-table-column label="小计" width="100">
          <template #default="{ row }">
            <span v-if="selectedItem(row)">¥{{ (selectedItem(row).price * row.quantity).toFixed(2) }}</span>
            <span v-else>—</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="80">
          <template #default="{ $index }">
            <el-button type="danger" size="small" link :disabled="orderItems.length <= 1" @click="removeOrderItem($index)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <div style="margin-top: 12px; display: flex; justify-content: space-between; align-items: center;">
        <el-button type="primary" link @click="addOrderItem">+ 添加商品</el-button>
        <div style="font-size: 16px; font-weight: bold;">
          合计：<span style="color: #f56c6c;">¥{{ computedTotal.toFixed(2) }}</span>
        </div>
      </div>

      <template #footer>
        <el-button @click="createDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleCreateOrder">提交订单</el-button>
      </template>
    </el-dialog>

    <!-- 订单详情弹窗 -->
    <el-dialog v-model="detailDialogVisible" title="订单详情" width="600px">
      <el-descriptions :column="2" border v-if="detailOrder">
        <el-descriptions-item label="订单号">{{ detailOrder.orderNo }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="statusTagType[detailOrder.status]">{{ statusLabel(detailOrder.status) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="总金额">¥{{ detailOrder.totalAmount }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ detailOrder.createTime }}</el-descriptions-item>
        <el-descriptions-item label="支付时间">{{ detailOrder.payTime || '未支付' }}</el-descriptions-item>
      </el-descriptions>
      
      <h4 style="margin-top: 20px; margin-bottom: 10px;">商品明细</h4>
      <el-table :data="detailItems" border v-loading="detailLoading">
        <el-table-column prop="productName" label="商品名称" />
        <el-table-column prop="price" label="单价" width="100">
          <template #default="{ row }">¥{{ row.price }}</template>
        </el-table-column>
        <el-table-column prop="quantity" label="数量" width="80" />
        <el-table-column label="小计" width="100">
          <template #default="{ row }">¥{{ (row.price * row.quantity).toFixed(2) }}</template>
        </el-table-column>
      </el-table>

      <template #footer>
        <el-button @click="detailDialogVisible = false">关闭</el-button>
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

.pagination {
  margin-top: 20px;
  justify-content: flex-end;
}
</style>
