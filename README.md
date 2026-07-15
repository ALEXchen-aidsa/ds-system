# 智能仓储管理系统

基于 SpringBoot3 + Vue3 的前后端分离电商/仓储管理系统。

## 技术栈

- **后端**: SpringBoot 3 + MyBatis-Plus + Spring Security + JWT
- **前端**: Vue 3 + Element Plus + Vite
- **数据库**: MySQL 8
- **缓存**: Redis 7
- **部署**: Docker + Docker Compose

## 核心功能

- ✅ 用户认证（JWT + RBAC权限控制）
- ✅ 商品管理（Redis缓存）
- ✅ 订单管理（定时任务自动取消超时订单）
- ✅ 秒杀功能（Redis预扣库存 + 限流 + 防超卖）

## 本地运行

### 环境要求
- JDK 17+
- Node.js 18+
- MySQL 8
- Redis 7
- Maven 3.8+

### 后端启动
```bash
cd backend
mvn spring-boot:run
```

### 前端启动
```bash
cd frontend
npm install
npm run dev
```

### Docker启动
```bash
docker-compose up -d
```

## 访问地址

- 前端: http://localhost:5173
- 后端API: http://localhost:8080/doc.html

### 测试账号
- 管理员: admin / 123456
- 测试用户: user1 / 123456

## 项目结构

```
ds-system/
├── backend/              # 后端SpringBoot项目
├── frontend/             # 前端Vue3项目
├── docker-compose.yml    # Docker编排文件
└── README.md
```

## 面试亮点

1. **Redis缓存**: 商品详情缓存，减少数据库查询
2. **Redis限流**: 令牌桶算法，防止恶意刷单
3. **Redis预扣库存**: 秒杀场景解决超卖问题
4. **定时任务**: 30分钟未支付自动取消订单
5. **JWT认证**: 无状态Token认证 + RBAC权限控制