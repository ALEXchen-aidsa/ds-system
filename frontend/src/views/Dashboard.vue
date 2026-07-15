<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import * as echarts from 'echarts'
import request from '../utils/request'

const stats = ref({
  totalProduct: 0,
  totalOrder: 0,
  totalInventory: 0,
  todaySales: 0
})

let salesChart = null
let inventoryChart = null
let orderChart = null

const fetchStats = async () => {
  try {
    const res = await request.get('/statistics')
    stats.value = res.data
  } catch (error) {
    console.error(error)
  }
}

const initCharts = () => {
  salesChart = echarts.init(document.getElementById('salesChart'))
  inventoryChart = echarts.init(document.getElementById('inventoryChart'))
  orderChart = echarts.init(document.getElementById('orderChart'))

  salesChart.setOption({
    title: { text: '销售趋势', left: 'center' },
    tooltip: { trigger: 'axis' },
    xAxis: { type: 'category', data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日'] },
    yAxis: { type: 'value' },
    series: [{ data: [120, 200, 150, 80, 70, 110, 130], type: 'line', smooth: true, areaStyle: {} }]
  })

  inventoryChart.setOption({
    title: { text: '库存分布', left: 'center' },
    tooltip: { trigger: 'item' },
    series: [{
      type: 'pie',
      radius: '50%',
      data: [
        { value: 335, name: '电子产品' },
        { value: 310, name: '服装' },
        { value: 234, name: '食品' },
        { value: 135, name: '日用品' }
      ]
    }]
  })

  orderChart.setOption({
    title: { text: '订单状态', left: 'center' },
    tooltip: { trigger: 'item' },
    series: [{
      type: 'pie',
      radius: ['40%', '70%'],
      data: [
        { value: 10, name: '待支付' },
        { value: 25, name: '已支付' },
        { value: 15, name: '已发货' },
        { value: 50, name: '已完成' }
      ]
    }]
  })
}

onMounted(() => {
  fetchStats()
  initCharts()
  window.addEventListener('resize', () => {
    salesChart?.resize()
    inventoryChart?.resize()
    orderChart?.resize()
  })
})

onUnmounted(() => {
  salesChart?.dispose()
  inventoryChart?.dispose()
  orderChart?.dispose()
})
</script>

<template>
  <div class="dashboard-container">
    <h2 class="page-title">数据可视化大屏</h2>
    
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-icon" style="background: linear-gradient(135deg, #667eea, #764ba2)">
            <el-icon><ShoppingCart /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.totalProduct }}</div>
            <div class="stat-label">商品总数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-icon" style="background: linear-gradient(135deg, #f093fb, #f5576c)">
            <el-icon><Document /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.totalOrder }}</div>
            <div class="stat-label">订单总数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-icon" style="background: linear-gradient(135deg, #4facfe, #00f2fe)">
            <el-icon><Box /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.totalInventory }}</div>
            <div class="stat-label">库存总量</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-icon" style="background: linear-gradient(135deg, #43e97b, #38f9d7)">
            <el-icon><TrendCharts /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.todaySales }}</div>
            <div class="stat-label">今日销售额</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="8">
        <el-card>
          <div id="salesChart" style="height: 350px"></div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <div id="inventoryChart" style="height: 350px"></div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <div id="orderChart" style="height: 350px"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<style scoped>
.page-title {
  margin-bottom: 20px;
  color: #303133;
}

.stat-card {
  cursor: pointer;
}

.stat-card :deep(.el-card__body) {
  display: flex;
  align-items: center;
  padding: 20px;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20px;
}

.stat-icon .el-icon {
  font-size: 30px;
  color: #fff;
}

.stat-info {
  flex: 1;
}

.stat-value {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
}

.stat-label {
  font-size: 14px;
  color: #909399;
  margin-top: 5px;
}
</style>