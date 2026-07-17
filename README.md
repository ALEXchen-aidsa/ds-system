# 智能仓储管理系统

基于 SpringBoot3 + Vue3 的前后端分离电商/仓储管理系统。

## 技术栈

- **后端**: Spring Boot 3.2.0 + MyBatis-Plus 3.5.5 + Spring Security + JWT + Spring AOP
- **前端**: Vue 3 + Element Plus + Vite + ECharts
- **数据库**: MySQL 8
- **缓存**: Redis 7
- **接口文档**: Knife4j (OpenAPI 3.0)
- **部署**: Docker + Docker Compose

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

## 项目结构

```
ds-system/
├── backend/                    # 后端SpringBoot项目
│   └── src/main/java/com/ds/  # Controller/Service/Mapper/Entity
├── frontend/                   # 前端Vue3项目
│   └── src/views/              # 页面组件
├── docker-compose.yml          # Docker编排文件
├── prompt_log.md               # AI辅助开发日志
└── README.md                   # 项目文档
```
