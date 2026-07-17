# AI辅助开发日志（Prompt Log）

> 工具：OpenCode (mimo-v2.5-free)  
> IDE：VS Code  
> 项目：智能仓储管理系统 ds-system

---

## 1. 项目初始化

**Prompt：** 创建一个SpringBoot3 + Vue3前后端分离项目，后端用MyBatis-Plus + MySQL，前端用Element Plus + Vite

**AI输出：**
```xml
<!-- 后端 pom.xml 核心依赖 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-spring-boot3-starter</artifactId>
    <version>3.5.5</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.12.3</version>
</dependency>
```

**对应文件：** `backend/pom.xml`、`frontend/package.json`

---

## 2. 数据库设计

**Prompt：** 设计电商仓储管理系统的数据库表，包括用户、角色、商品、分类、订单、订单项、秒杀商品

**AI输出：**
```sql
-- 用户表
CREATE TABLE sys_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    nickname VARCHAR(50),
    status TINYINT DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 商品表
CREATE TABLE product (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    code VARCHAR(50) NOT NULL UNIQUE,
    category_id BIGINT,
    price DECIMAL(10,2),
    stock INT DEFAULT 0,
    description TEXT,
    status TINYINT DEFAULT 1
);

-- 订单表
CREATE TABLE orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_no VARCHAR(50) NOT NULL,
    user_id BIGINT NOT NULL,
    total_amount DECIMAL(10,2),
    status INT DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
);
```

**对应文件：** `backend/src/main/resources/db/schema.sql`

---

## 3. 用户认证模块（JWT + RBAC）

**Prompt：** 实现JWT登录认证和RBAC权限控制，用户登录返回Token，接口需要鉴权

**AI输出：**
```java
@Component
public class JwtTokenProvider {
    public String generateToken(String username, Long userId, List<String> roles) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);
        return Jwts.builder()
                .subject(username)
                .claim("userId", userId)
                .claim("roles", roles)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }
}
```

**对应文件：** `backend/src/main/java/com/ds/security/JwtTokenProvider.java`、`SecurityConfig.java`

---

## 4. 商品管理模块（CRUD + Redis缓存）

**Prompt：** 实现商品CRUD接口，用Redis缓存商品详情，查询时先查Redis再查MySQL

**AI输出：**
```java
@Override
public Product getProductById(Long id) {
    String cacheKey = "product:" + id;
    Object cached = redisTemplate.opsForValue().get(cacheKey);
    if (cached != null) {
        return (Product) cached;
    }
    Product product = productMapper.selectById(id);
    if (product != null) {
        redisTemplate.opsForValue().set(cacheKey, product, 30, TimeUnit.MINUTES);
    }
    return product;
}
```

**对应文件：** `backend/src/main/java/com/ds/service/impl/ProductServiceImpl.java`

---

## 5. 订单管理模块（定时任务 + 防超卖）

**Prompt：** 实现订单管理，包括创建订单、订单列表查询，添加定时任务自动取消超时未支付订单

**AI输出：**
```java
@Scheduled(fixedRate = 300000) // 每5分钟执行
public void cancelTimeoutOrders() {
    LocalDateTime timeout = LocalDateTime.now().minusMinutes(30);
    List<Order> timeoutOrders = orderMapper.selectList(
        new LambdaQueryWrapper<Order>()
            .eq(Order::getStatus, 0)
            .lt(Order::getCreateTime, timeout)
    );
    for (Order order : timeoutOrders) {
        order.setStatus(3); // 已取消
        orderMapper.updateById(order);
    }
}
```

**对应文件：** `backend/src/main/java/com/ds/service/impl/OrderServiceImpl.java`

---

## 6. 秒杀模块（Redis预扣库存 + 限流）

**Prompt：** 实现秒杀功能，用Redis预扣库存解决超卖，添加限流防止恶意刷单

**AI输出：**
```java
public Map<String, Object> doSeckill(Long userId, Long seckillProductId) {
    String stockKey = SECKILL_STOCK_KEY + seckillProductId;
    Long stock = redisTemplate.opsForValue().decrement(stockKey);
    if (stock < 0) {
        redisTemplate.opsForValue().increment(stockKey);
        throw new BusinessException("已售罄");
    }
    String lockKey = SECKILL_LOCK_KEY + seckillProductId;
    String lockValue = UUID.randomUUID().toString();
    Boolean locked = redisTemplate.opsForValue().setIfAbsent(lockKey, lockValue, 10, TimeUnit.SECONDS);
    // ...
}
```

**对应文件：** `backend/src/main/java/com/ds/service/impl/SeckillServiceImpl.java`

---

## 7. 仓储核心模块

**Prompt：** 实现仓储管理功能，包括仓库CRUD、库存管理、入库出库操作

**AI输出：**
```java
@RestController
@RequestMapping("/api/inventory")
public class InventoryController {
    @PostMapping("/in")
    public Result<Void> stockIn(@Valid @RequestBody StockRecordDTO dto) {
        inventoryService.stockIn(dto);
        return Result.success();
    }
    @PostMapping("/out")
    public Result<Void> stockOut(@Valid @RequestBody StockRecordDTO dto) {
        inventoryService.stockOut(dto);
        return Result.success();
    }
}
```

**对应文件：** `backend/src/main/java/com/ds/controller/InventoryController.java`

---

## 8. 进销存采购模块

**Prompt：** 实现进销存功能，包括供应商管理、客户管理、采购入库

**AI输出：**
```java
@RestController
@RequestMapping("/api/supplier")
public class SupplierController {
    @GetMapping("/list")
    public Result<Page<Supplier>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return Result.success(supplierService.listSuppliers(page, size));
    }
    @PostMapping
    public Result<Void> create(@Valid @RequestBody Supplier supplier) {
        supplierService.createSupplier(supplier);
        return Result.success();
    }
}
```

**对应文件：** `backend/src/main/java/com/ds/controller/SupplierController.java`

---

## 9. 数据可视化大屏

**Prompt：** 实现数据可视化大屏，用ECharts展示统计图表，包括折线图、柱状图、饼图，要有动态粒子背景效果

**AI输出：**
```vue
<template>
  <div class="dashboard">
    <canvas ref="particleCanvas" class="particle-bg"></canvas>
    <div class="stats-cards">
      <el-card v-for="item in stats" :key="item.label">
        <div class="stat-value">{{ item.value }}</div>
        <div class="stat-label">{{ item.label }}</div>
      </el-card>
    </div>
    <div class="charts">
      <div ref="lineChart" class="chart"></div>
      <div ref="barChart" class="chart"></div>
      <div ref="pieChart" class="chart"></div>
    </div>
  </div>
</template>
```

**对应文件：** `frontend/src/views/Dashboard.vue`

---

## 10. 前端UI升级

**Prompt：** 升级前端UI效果，要有高端商务风：动态粒子背景、悬浮光影、渐变色彩、hover动画

**AI输出：**
```css
.particle-bg {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: -1;
}
.stat-card {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 12px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}
.stat-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 12px 40px rgba(102, 126, 234, 0.4);
}
```

**对应文件：** `frontend/src/views/Login.vue`、`frontend/src/views/Dashboard.vue`

---

## 11. 操作日志（AOP自动记录）

**Prompt：** 实现操作日志功能，用AOP自动记录所有Controller的调用，记录模块、操作、IP、状态、耗时

**AI输出：**
```java
@Aspect
@Component
public class LogAspect {
    @Pointcut("execution(* com.ds.controller..*(..))")
    public void pointcut() {}

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        OperationLog log = new OperationLog();
        log.setModule(joinPoint.getTarget().getClass().getSimpleName());
        log.setOperation(joinPoint.getSignature().getName());
        Object result = joinPoint.proceed();
        log.setConsumeTime(System.currentTimeMillis() - startTime);
        logService.saveLog(log);
        return result;
    }
}
```

**对应文件：** `backend/src/main/java/com/ds/common/LogAspect.java`

---

## 12. Docker部署配置

**Prompt：** 生成Docker部署配置，包括Dockerfile和docker-compose.yml

**AI输出：**
```yaml
version: '3.8'
services:
  mysql:
    image: mysql:8
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: ds_system
    ports:
      - "3306:3306"
  redis:
    image: redis:7
    ports:
      - "6379:6379"
  backend:
    build: ./backend
    ports:
      - "8080:8080"
    depends_on:
      - mysql
      - redis
  frontend:
    build: ./frontend
    ports:
      - "80:80"
    depends_on:
      - backend
```

**对应文件：** `docker-compose.yml`、`backend/Dockerfile`

---

## 13. Bug修复：LogAspect编译错误

**Prompt：** 库存管理页面500错误，后端启动失败

**AI输出：**
```
排查发现LogAspect使用了AspectJ注解但pom.xml缺少spring-boot-starter-aop依赖。
添加依赖后编译通过，库存API恢复正常。
```

**对应文件：** `backend/pom.xml`（添加spring-boot-starter-aop）

---

## 14. Bug修复：数据库表未创建

**Prompt：** 新增的仓储模块表不存在导致500错误

**AI输出：**
```
执行warehouse.sql、purchase.sql、log.sql建表脚本，
创建warehouse、inventory、stock_record、stock_check、
supplier、customer、purchase_order、operation_log等表。
```

**对应文件：** `backend/src/main/resources/db/warehouse.sql`、`purchase.sql`、`log.sql`

---

## 15. Bug修复：3个严重Bug

**Prompt：** 修复注册缺角色保存、秒杀锁失效、JWT无角色权限

**AI输出：**
```java
// Bug1: 注册缺角色 - 添加userRoleMapper.insert(userRole)
SysUserRole userRole = new SysUserRole();
userRole.setUserId(user.getId());
userRole.setRoleId(3L);
userRoleMapper.insert(userRole);

// Bug2: 秒杀锁失效 - 使用UUID锁值，释放前校验
String lockValue = UUID.randomUUID().toString();
Boolean locked = redisTemplate.opsForValue().setIfAbsent(lockKey, lockValue, 10, TimeUnit.SECONDS);
// finally中: if (lockValue.equals(currentValue)) redisTemplate.delete(lockKey);

// Bug3: JWT无角色 - Token写入roles
String token = tokenProvider.generateToken(user.getUsername(), user.getId(), roles);
```

**对应文件：** `AuthServiceImpl.java`、`SeckillServiceImpl.java`、`JwtTokenProvider.java`

---

## 16. UI优化：远程搜索下拉框

**Prompt：** 库存入库时仓库ID和商品ID改为输入名称搜索选择，秒杀创建同样优化

**AI输出：**
```vue
<el-select
  v-model="form.warehouseId"
  placeholder="输入仓库名称搜索"
  filterable
  remote
  :remote-method="onWarehouseSearch"
  :loading="warehouseLoading"
>
  <el-option
    v-for="item in warehouseList"
    :key="item.id"
    :label="item.name"
    :value="item.id"
  />
</el-select>
```

**对应文件：** `frontend/src/views/Inventory.vue`、`Seckill.vue`
