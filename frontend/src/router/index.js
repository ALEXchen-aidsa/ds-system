import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue')
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/Register.vue')
  },
  {
    path: '/',
    name: 'Layout',
    component: () => import('../layout/index.vue'),
    redirect: '/home',
    children: [
      {
        path: 'home',
        name: 'Home',
        component: () => import('../views/Home.vue'),
        meta: { title: '首页' }
      },
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('../views/Dashboard.vue'),
        meta: { title: '数据大屏' }
      },
      {
        path: 'warehouse',
        name: 'Warehouse',
        component: () => import('../views/Warehouse.vue'),
        meta: { title: '仓库管理' }
      },
      {
        path: 'inventory',
        name: 'Inventory',
        component: () => import('../views/Inventory.vue'),
        meta: { title: '库存管理' }
      },
      {
        path: 'product',
        name: 'Product',
        component: () => import('../views/Product.vue'),
        meta: { title: '商品管理' }
      },
      {
        path: 'supplier',
        name: 'Supplier',
        component: () => import('../views/Supplier.vue'),
        meta: { title: '供应商管理' }
      },
      {
        path: 'customer',
        name: 'Customer',
        component: () => import('../views/Customer.vue'),
        meta: { title: '客户管理' }
      },
      {
        path: 'order',
        name: 'Order',
        component: () => import('../views/Order.vue'),
        meta: { title: '订单管理' }
      },
      {
        path: 'seckill',
        name: 'Seckill',
        component: () => import('../views/Seckill.vue'),
        meta: { title: '秒杀活动' }
      },
      {
        path: 'user',
        name: 'User',
        component: () => import('../views/User.vue'),
        meta: { title: '用户管理' }
      },
      {
        path: 'log',
        name: 'OperationLog',
        component: () => import('../views/OperationLog.vue'),
        meta: { title: '操作日志' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.path !== '/login' && to.path !== '/register' && !token) {
    next('/login')
  } else {
    next()
  }
})

export default router