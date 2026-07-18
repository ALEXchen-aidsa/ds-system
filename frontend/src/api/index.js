import request from '../utils/request'

// 登录
export function login(data) {
  return request.post('/auth/login', data)
}

// 注册
export function register(data) {
  return request.post('/auth/register', data)
}

// 获取用户列表
export function getUserList(params) {
  return request.get('/user/list', { params })
}

// 获取商品列表
export function getProductList(params) {
  return request.get('/product/list', { params })
}

// 获取商品详情
export function getProductDetail(id) {
  return request.get(`/product/${id}`)
}

// 创建商品
export function createProduct(data) {
  return request.post('/product', data)
}

// 更新商品
export function updateProduct(data) {
  return request.put('/product', data)
}

// 删除商品
export function deleteProduct(id) {
  return request.delete(`/product/${id}`)
}

// 创建订单
export function createOrder(data) {
  return request.post('/order/create', data)
}

// 获取订单列表
export function getOrderList(params) {
  return request.get('/order/list', { params })
}

// 获取订单商品明细
export function getOrderItems(id) {
  return request.get(`/order/${id}/items`)
}

// 修改订单状态
export function updateOrderStatus(id, status) {
  return request.put(`/order/${id}/status?status=${status}`)
}

// 获取统计数据
export function getStatistics() {
  return request.get('/statistics')
}