# 智能仓储管理系统

基于 SpringBoot3 + Vue3 的前后端分离电商/仓储管理系统。

## 线上部署

- **前端**: https://ds-system-xi.vercel.app
- **后端API**: http://193.112.219.202:8080
- **API文档**: http://193.112.219.202:8080/doc.html

### 测试账号
- 管理员: admin / 123456
- 测试用户: user1 / 123456

## 技术栈

- **后端**: Spring Boot 3.2.0 + MyBatis-Plus 3.5.5 + Spring Security + JWT + Spring AOP
- **前端**: Vue 3 + Element Plus + Vite + ECharts
- **数据库**: MySQL 8 (TiDB Cloud)
- **缓存**: Redis 7 (Upstash)
- **接口文档**: Knife4j (OpenAPI 3.0)
- **部署**: Docker + Vercel

## 技术亮点（答辩可讲）

| 技术点 | 说明 |
|--------|------|
| JWT 无状态认证 | 登录返回 token，后续请求携带 Bearer token |
| BCrypt 密码加密 | 用户密码使用 BCrypt 不可逆加密存储 |
| Redis 分布式锁 | 秒杀场景防止超卖 |
| Redis 库存预扣 | 秒杀库存先扣 Redis 再扣数据库 |
| 令牌桶限流 | RedisRateLimiter 实现接口限流 |
| AOP 操作日志 | LogAspect 切面自动记录操作日志 |
| MyBatis-Plus 分页 | 统一分页查询封装 |
| 全局异常处理 | GlobalExceptionHandler 统一异常返回 |
| CORS 跨域处理 | CorsConfig 允许跨域 |
| Docker 容器化 | MySQL + Redis + 后端 + 前端四容器部署 |
| ECharts 可视化 | 销售趋势、库存分布、订单状态图表 |
| Vue3 组合式 API | 前端使用 Vue3 + Element Plus |

## 核心功能

- ✅ 用户认证（JWT + RBAC权限控制）
- ✅ 商品管理（Redis缓存）
- ✅ 订单管理（定时任务自动取消超时订单）
- ✅ 秒杀功能（Redis预扣库存 + 限流 + 防超卖）
- ✅ 仓储核心（仓库管理、库存管理、出入库操作）
- ✅ 进销存模块（供应商管理、客户管理）
- ✅ 数据可视化大屏（ECharts图表 + 粒子背景）
- ✅ 操作日志（AOP自动记录）
- ✅ Docker一键部署

## 安装/运行指南

### 环境要求
- JDK 17+
- Node.js 18+
- MySQL 8
- Redis 7
- Maven 3.8+

### 后端启动
```bash
cd backend
# 先执行建表脚本
mysql -u root -p ds_system < src/main/resources/db/schema.sql
mysql -u root -p ds_system < src/main/resources/db/warehouse.sql
mysql -u root -p ds_system < src/main/resources/db/purchase.sql
mysql -u root -p ds_system < src/main/resources/db/log.sql
# 启动
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

## 本地访问地址

- 前端: http://localhost:5173
- 后端API文档: http://localhost:8080/doc.html

## API文档

### 认证接口 `/api/auth`

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/api/auth/login` | 用户登录，返回JWT Token |
| POST | `/api/auth/register` | 用户注册 |

### 用户管理 `/api/user`

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/user/list` | 分页查询用户列表 |
| PUT | `/api/user/{id}/status` | 修改用户状态 |
| DELETE | `/api/user/{id}` | 删除用户 |

### 商品管理 `/api/product`

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/product/list` | 分页查询商品列表 |
| GET | `/api/product/{id}` | 查询商品详情（Redis缓存） |
| POST | `/api/product` | 新增商品 |
| PUT | `/api/product` | 编辑商品 |
| DELETE | `/api/product/{id}` | 删除商品 |

### 商品分类 `/api/category`

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/category/list` | 查询分类列表 |
| POST | `/api/category` | 新增分类 |

### 仓库管理 `/api/warehouse`

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/warehouse/list` | 分页查询仓库列表 |
| GET | `/api/warehouse/{id}` | 查询仓库详情 |
| POST | `/api/warehouse` | 新增仓库 |
| PUT | `/api/warehouse` | 编辑仓库 |
| DELETE | `/api/warehouse/{id}` | 删除仓库 |

### 库存管理 `/api/inventory`

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/inventory/list` | 查询库存列表 |
| POST | `/api/inventory/in` | 入库操作 |
| POST | `/api/inventory/out` | 出库操作 |
| GET | `/api/inventory/records` | 出入库记录查询 |

### 供应商管理 `/api/supplier`

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/supplier/list` | 分页查询供应商 |
| POST | `/api/supplier` | 新增供应商 |
| PUT | `/api/supplier` | 编辑供应商 |
| DELETE | `/api/supplier/{id}` | 删除供应商 |

### 客户管理 `/api/customer`

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/customer/list` | 分页查询客户 |
| POST | `/api/customer` | 新增客户 |
| PUT | `/api/customer` | 编辑客户 |
| DELETE | `/api/customer/{id}` | 删除客户 |

### 订单管理 `/api/order`

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/api/order/create` | 创建订单 |
| GET | `/api/order/list` | 分页查询订单 |
| GET | `/api/order/{id}` | 查询订单详情 |

### 秒杀模块 `/api/seckill`

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/seckill/list` | 秒杀商品列表 |
| GET | `/api/seckill/{id}` | 秒杀商品详情 |
| POST | `/api/seckill/do/{id}` | 参与秒杀（Redis预扣库存） |
| POST | `/api/seckill` | 新增秒杀商品 |

### 数据统计 `/api/statistics`

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/statistics` | 获取大屏统计数据 |

### 操作日志 `/api/log`

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/log/list` | 分页查询操作日志 |

## 项目结构

```
ds-system/
├── backend/                    # 后端SpringBoot项目
│   └── src/main/java/com/ds/  # Controller/Service/Mapper/Entity
├── frontend/                   # 前端Vue3项目
│   └── src/views/              # 页面组件
├── docker-compose.yml          # Docker编排文件
├── checklist/                  # 考核提交材料
│   ├── API接口文档.docx
│   ├── 个人总结报告.docx
│   └── 截图/
├── API接口文档.docx             # API接口文档（Word）
├── prompt_log.md               # AI辅助开发日志
└── README.md                   # 项目文档
```
