import request from '../utils/request'

// 登录
export function login(data) {
  return request.post('/auth/login', data)
}

// 注册
export function register(data) {
  return request.post('/auth/register', data)
}

// 获取用户信息
export function getUserInfo() {
  return request.get('/user/info')
}

// 获取商品列表
export function getProductList(params) {
  return request.get('/product/list', { params })
}

// 获取商品详情
export function getProductDetail(id) {
  return request.get(`/product/${id}`)
}

// 创建订单
export function createOrder(data) {
  return request.post('/order/create', data)
}

// 获取订单列表
export function getOrderList(params) {
  return request.get('/order/list', { params })
}

// 获取统计数据
export function getStatistics() {
  return request.get('/statistics')
}