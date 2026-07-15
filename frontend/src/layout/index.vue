<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessageBox } from 'element-plus'

const router = useRouter()
const isCollapse = ref(false)
const username = computed(() => localStorage.getItem('username') || '用户')

const menuItems = [
  { path: '/home', icon: 'HomeFilled', title: '首页' },
  { path: '/dashboard', icon: 'DataAnalysis', title: '数据大屏' },
  { path: '/warehouse', icon: 'House', title: '仓库管理' },
  { path: '/inventory', icon: 'Box', title: '库存管理' },
  { path: '/product', icon: 'ShoppingCart', title: '商品管理' },
  { path: '/supplier', icon: 'Van', title: '供应商' },
  { path: '/customer', icon: 'UserFilled', title: '客户管理' },
  { path: '/order', icon: 'Document', title: '订单管理' },
  { path: '/seckill', icon: 'Timer', title: '秒杀活动' },
  { path: '/user', icon: 'User', title: '用户管理' },
  { path: '/log', icon: 'Notebook', title: '操作日志' }
]

const toggleCollapse = () => {
  isCollapse.value = !isCollapse.value
}

const handleLogout = () => {
  ElMessageBox.confirm('确定要退出登录吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    localStorage.removeItem('token')
    localStorage.removeItem('username')
    router.push('/login')
  })
}
</script>

<template>
  <el-container class="layout-container">
    <el-aside :width="isCollapse ? '64px' : '220px'" class="aside">
      <div class="logo">
        <div class="logo-icon">DS</div>
        <span v-if="!isCollapse" class="logo-text">智能仓储</span>
      </div>
      <el-menu
        :default-active="router.currentRoute.value.path"
        :collapse="isCollapse"
        router
        class="menu"
      >
        <el-menu-item v-for="item in menuItems" :key="item.path" :index="item.path">
          <el-icon><component :is="item.icon" /></el-icon>
          <template #title>{{ item.title }}</template>
        </el-menu-item>
      </el-menu>
    </el-aside>
    
    <el-container>
      <el-header class="header">
        <div class="header-left">
          <el-icon class="collapse-btn" @click="toggleCollapse">
            <Fold v-if="!isCollapse" />
            <Expand v-else />
          </el-icon>
        </div>
        <div class="header-right">
          <div class="avatar">
            <span>{{ username.charAt(0).toUpperCase() }}</span>
          </div>
          <span class="username">{{ username }}</span>
          <el-button type="danger" link @click="handleLogout" class="logout-btn">退出</el-button>
        </div>
      </el-header>
      
      <el-main class="main">
        <RouterView />
      </el-main>
    </el-container>
  </el-container>
</template>

<style scoped>
.layout-container {
  height: 100vh;
}

.aside {
  background: linear-gradient(180deg, #1a1a3e 0%, #0d0d2b 100%);
  transition: width 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  overflow: hidden;
  box-shadow: 4px 0 15px rgba(0, 0, 0, 0.3);
}

.logo {
  height: 70px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  padding: 0 15px;
}

.logo-icon {
  width: 36px;
  height: 36px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-weight: bold;
  font-size: 14px;
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);
}

.logo-text {
  color: #fff;
  font-size: 18px;
  font-weight: bold;
  white-space: nowrap;
}

.menu {
  border-right: none;
  background: transparent;
  padding: 10px 0;
}

:deep(.el-menu-item) {
  color: rgba(255, 255, 255, 0.6);
  margin: 4px 10px;
  border-radius: 10px;
  transition: all 0.3s;
}

:deep(.el-menu-item:hover) {
  background: rgba(102, 126, 234, 0.2);
  color: #fff;
}

:deep(.el-menu-item.is-active) {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);
}

.header {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  display: flex;
  align-items: center;
  justify-content: space-between;
  box-shadow: 0 2px 20px rgba(0, 0, 0, 0.1);
  padding: 0 25px;
}

.collapse-btn {
  font-size: 22px;
  cursor: pointer;
  color: #303133;
  transition: all 0.3s;
}

.collapse-btn:hover {
  color: #667eea;
  transform: scale(1.1);
}

.header-right {
  display: flex;
  align-items: center;
  gap: 15px;
}

.avatar {
  width: 38px;
  height: 38px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-weight: bold;
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.3);
  transition: transform 0.3s;
}

.avatar:hover {
  transform: scale(1.1) rotate(5deg);
}

.username {
  font-size: 14px;
  color: #303133;
  font-weight: 500;
}

.logout-btn {
  transition: all 0.3s;
}

.logout-btn:hover {
  transform: scale(1.05);
}

.main {
  background: linear-gradient(135deg, #f5f7fa 0%, #e4e8eb 100%);
  padding: 25px;
  min-height: calc(100vh - 70px);
}
</style>