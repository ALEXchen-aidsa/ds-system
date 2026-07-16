# AI辅助开发日志（Prompt Log）

> 工具：OpenCode (mimo-v2.5-free)  
> IDE：VS Code  
> 项目：智能仓储管理系统 ds-system

---

## 1. 项目初始化

**Prompt：** 创建一个SpringBoot3 + Vue3前后端分离项目，后端用MyBatis-Plus + MySQL，前端用Element Plus + Vite

**AI输出：** 生成了完整的后端pom.xml（含SpringBoot3.2.0、MyBatis-Plus、JWT、Knife4j等依赖）和前端package.json（含Vue3、Element Plus、Axios、ECharts等依赖），以及标准目录结构。

**对应文件：** `backend/pom.xml`、`frontend/package.json`

---

## 2. 数据库设计

**Prompt：** 设计电商仓储管理系统的数据库表，包括用户、角色、商品、分类、订单、订单项、秒杀商品

**AI输出：** 生成schema.sql，包含sys_user、sys_role、sys_user_role、product、category、orders、order_item、seckill_product等表，含建表语句和测试数据，密码使用BCrypt加密。

**对应文件：** `backend/src/main/resources/db/schema.sql`

---

## 3. 用户认证模块（JWT + RBAC）

**Prompt：** 实现JWT登录认证和RBAC权限控制，用户登录返回Token，接口需要鉴权

**AI输出：** 生成JwtUtil工具类（Token生成/解析）、JwtAuthenticationFilter（过滤器拦截请求）、SecurityConfig（Spring Security配置，放行登录注册接口）、AuthController（登录/注册API）、LoginDTO/RegisterDTO。

**对应文件：** `backend/src/main/java/com/ds/util/JwtUtil.java`、`backend/src/main/java/com/ds/security/JwtAuthenticationFilter.java`、`backend/src/main/java/com/ds/config/SecurityConfig.java`、`backend/src/main/java/com/ds/controller/AuthController.java`

---

## 4. 商品管理模块（CRUD + Redis缓存）

**Prompt：** 实现商品CRUD接口，用Redis缓存商品详情，查询时先查Redis再查MySQL

**AI输出：** 生成ProductController（5个接口）、ProductService/Impl（含Redis缓存逻辑）、ProductDTO、ProductQueryDTO。缓存策略：详情查询走Redis，列表查询直接走数据库，新增/编辑/删除时清除缓存。

**对应文件：** `backend/src/main/java/com/ds/controller/ProductController.java`、`backend/src/main/java/com/ds/service/impl/ProductServiceImpl.java`

---

## 5. 订单管理模块（定时任务 + 防超卖）

**Prompt：** 实现订单管理，包括创建订单、订单列表查询，添加定时任务自动取消超时未支付订单

**AI输出：** 生成OrderController、OrderService/Impl、OrderDTO、OrderQueryDTO。定时任务使用@Scheduled注解，每5分钟扫描status=0且创建时间超过30分钟的订单自动取消。使用Redis分布式锁防止并发超卖。

**对应文件：** `backend/src/main/java/com/ds/controller/OrderController.java`、`backend/src/main/java/com/ds/service/impl/OrderServiceImpl.java`

---

## 6. 秒杀模块（Redis预扣库存 + 限流）

**Prompt：** 实现秒杀功能，用Redis预扣库存解决超卖，添加限流防止恶意刷单

**AI输出：** 生成SeckillController（4个接口）、SeckillService/Impl。秒杀流程：1)Redis SETNX预扣库存 2)发送MQ消息异步创建订单 3)令牌桶限流（同一IP每秒最多10次请求）4)库存不足返回秒杀失败。

**对应文件：** `backend/src/main/java/com/ds/controller/SeckillController.java`、`backend/src/main/java/com/ds/service/impl/SeckillServiceImpl.java`

---

## 7. 仓储核心模块

**Prompt：** 实现仓储管理功能，包括仓库CRUD、库存管理、入库出库操作

**AI输出：** 生成WarehouseController（5个接口）、InventoryController（4个接口：库存列表/入库/出库/出入库记录）、对应Service和Mapper。入库增加库存数量，出库减少库存并校验库存充足。

**对应文件：** `backend/src/main/java/com/ds/controller/WarehouseController.java`、`backend/src/main/java/com/ds/controller/InventoryController.java`

---

## 8. 进销存采购模块

**Prompt：** 实现进销存功能，包括供应商管理、客户管理、采购入库

**AI输出：** 生成SupplierController（4个接口）、CustomerController（4个接口），对应Entity、Service、Mapper。供应商和客户支持完整CRUD操作。

**对应文件：** `backend/src/main/java/com/ds/controller/SupplierController.java`、`backend/src/main/java/com/ds/controller/CustomerController.java`

---

## 9. 数据可视化大屏

**Prompt：** 实现数据可视化大屏，用ECharts展示统计图表，包括折线图、柱状图、饼图，要有动态粒子背景效果

**AI输出：** 生成StatisticsController（返回统计数据）、前端Dashboard.vue（含ECharts折线图展示销售趋势、柱状图展示分类销量TOP5、饼图展示订单状态分布），以及Canvas粒子动画背景。

**对应文件：** `backend/src/main/java/com/ds/controller/StatisticsController.java`、`frontend/src/views/Dashboard.vue`

---

## 10. 前端UI升级

**Prompt：** 升级前端UI效果，要有高端商务风：动态粒子背景、悬浮光影、渐变色彩、hover动画

**AI输出：** 修改了Login.vue和Dashboard.vue，添加了Canvas粒子动画、CSS渐变背景、卡片悬浮阴影效果、按钮hover发光动画、表格行hover高亮等视觉效果。

**对应文件：** `frontend/src/views/Login.vue`、`frontend/src/views/Dashboard.vue`

---

## 11. 操作日志（AOP自动记录）

**Prompt：** 实现操作日志功能，用AOP自动记录所有Controller的调用，记录模块、操作、IP、状态、耗时

**AI输出：** 生成LogAspect.java（AOP切面，@Around环绕通知拦截所有Controller方法）、OperationLog实体、OperationLogMapper、OperationLogService、OperationLogController。自动记录每次请求的模块名、方法名、IP地址、执行状态、耗时。

**对应文件：** `backend/src/main/java/com/ds/common/LogAspect.java`、`backend/src/main/java/com/ds/controller/OperationLogController.java`

---

## 12. Docker部署配置

**Prompt：** 生成Docker部署配置，包括Dockerfile和docker-compose.yml

**AI输出：** 生成backend/Dockerfile（基于openjdk:17-jdk-slim）、frontend/Dockerfile（基于node:18-alpine构建+nginx运行）、docker-compose.yml（编排MySQL8、Redis7、后端、前端四个服务）。

**对应文件：** `backend/Dockerfile`、`frontend/Dockerfile`、`docker-compose.yml`

---

## 13. Bug修复：LogAspect编译错误

**Prompt：** 库存管理页面500错误，后端启动失败

**AI输出：** 排查发现LogAspect使用了AspectJ注解但pom.xml缺少spring-boot-starter-aop依赖，添加依赖后编译通过，库存API恢复正常。

**对应文件：** `backend/pom.xml`

---

## 14. Bug修复：数据库表未创建

**Prompt：** 新增的仓储模块表不存在导致500错误

**AI输出：** 执行warehouse.sql、purchase.sql、log.sql建表脚本，创建warehouse、inventory、stock_record、stock_check、supplier、customer、purchase_order、operation_log等表。

**对应文件：** `backend/src/main/resources/db/warehouse.sql`、`backend/src/main/resources/db/purchase.sql`、`backend/src/main/resources/db/log.sql`
