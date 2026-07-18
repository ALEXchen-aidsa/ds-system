-- ============================================
-- 智能仓储管理系统 - 完整建表 + 初始数据
-- 适用于 TiDB Cloud / MySQL 8.x
-- ============================================

CREATE DATABASE IF NOT EXISTS ds_system DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE ds_system;

-- ---------- 用户表 ----------
CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    nickname VARCHAR(50),
    email VARCHAR(100),
    phone VARCHAR(20),
    avatar VARCHAR(255),
    status TINYINT DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- ---------- 角色表 ----------
CREATE TABLE IF NOT EXISTS sys_role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    role_name VARCHAR(50) NOT NULL,
    role_key VARCHAR(50) UNIQUE NOT NULL,
    status TINYINT DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- ---------- 用户角色关联表 ----------
CREATE TABLE IF NOT EXISTS sys_user_role (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id)
);

-- ---------- 商品分类表 ----------
CREATE TABLE IF NOT EXISTS product_category (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    parent_id BIGINT DEFAULT 0,
    sort_order INT DEFAULT 0,
    status TINYINT DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- ---------- 商品表 ----------
CREATE TABLE IF NOT EXISTS product (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(200) NOT NULL,
    code VARCHAR(50) UNIQUE NOT NULL,
    category_id BIGINT,
    price DECIMAL(10,2),
    stock INT DEFAULT 0,
    description TEXT,
    image VARCHAR(255),
    status TINYINT DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- ---------- 订单表 ----------
CREATE TABLE IF NOT EXISTS orders (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_no VARCHAR(50) UNIQUE NOT NULL,
    user_id BIGINT NOT NULL,
    total_amount DECIMAL(10,2),
    status TINYINT DEFAULT 0 COMMENT '0待支付 1已支付 2已发货 3已完成 4已取消',
    pay_time DATETIME,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- ---------- 订单详情表 ----------
CREATE TABLE IF NOT EXISTS order_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    product_name VARCHAR(200),
    price DECIMAL(10,2),
    quantity INT,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- ---------- 秒杀商品表 ----------
CREATE TABLE IF NOT EXISTS seckill_product (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_id BIGINT NOT NULL,
    seckill_price DECIMAL(10,2),
    seckill_stock INT DEFAULT 0,
    start_time DATETIME,
    end_time DATETIME,
    status TINYINT DEFAULT 0 COMMENT '0未开始 1进行中 2已结束',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- ---------- 仓库表 ----------
CREATE TABLE IF NOT EXISTS warehouse (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    code VARCHAR(50) UNIQUE NOT NULL,
    address VARCHAR(500),
    manager VARCHAR(50),
    capacity INT DEFAULT 0,
    status TINYINT DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- ---------- 库存表 ----------
CREATE TABLE IF NOT EXISTS inventory (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    warehouse_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT DEFAULT 0,
    frozen_quantity INT DEFAULT 0,
    safety_stock INT DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- ---------- 库存流水表 ----------
CREATE TABLE IF NOT EXISTS stock_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    record_no VARCHAR(50) UNIQUE NOT NULL,
    type INT COMMENT '1入库 2出库 3调拨',
    warehouse_id BIGINT,
    product_id BIGINT,
    quantity INT,
    price DECIMAL(10,2),
    remark VARCHAR(500),
    status TINYINT DEFAULT 1,
    operator_id BIGINT,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- ---------- 盘点表 ----------
CREATE TABLE IF NOT EXISTS stock_check (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    check_no VARCHAR(50) UNIQUE NOT NULL,
    warehouse_id BIGINT,
    status TINYINT DEFAULT 0 COMMENT '0待盘点 1盘点中 2已完成',
    operator_id BIGINT,
    remark VARCHAR(500),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    finish_time DATETIME
);

-- ---------- 盘点明细表 ----------
CREATE TABLE IF NOT EXISTS stock_check_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    check_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    system_quantity INT,
    actual_quantity INT,
    difference INT,
    remark VARCHAR(500),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- ---------- 供应商表 ----------
CREATE TABLE IF NOT EXISTS supplier (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    code VARCHAR(50) UNIQUE NOT NULL,
    contact VARCHAR(50),
    phone VARCHAR(20),
    address VARCHAR(500),
    status TINYINT DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- ---------- 客户表 ----------
CREATE TABLE IF NOT EXISTS customer (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    code VARCHAR(50) UNIQUE NOT NULL,
    phone VARCHAR(20),
    address VARCHAR(500),
    type TINYINT DEFAULT 1 COMMENT '1个人 2企业',
    status TINYINT DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- ---------- 采购订单表 ----------
CREATE TABLE IF NOT EXISTS purchase_order (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_no VARCHAR(50) UNIQUE NOT NULL,
    supplier_id BIGINT,
    total_amount DECIMAL(10,2),
    status TINYINT DEFAULT 0 COMMENT '0待审核 1已审核 2已入库 3已取消',
    operator_id BIGINT,
    remark VARCHAR(500),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    finish_time DATETIME
);

-- ---------- 操作日志表 ----------
CREATE TABLE IF NOT EXISTS operation_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    module VARCHAR(50),
    operation VARCHAR(100),
    method VARCHAR(255),
    params TEXT,
    user_id BIGINT,
    username VARCHAR(50),
    ip VARCHAR(50),
    status TINYINT DEFAULT 1,
    error_msg TEXT,
    consume_time BIGINT,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
);


-- ============================================
-- 初始数据
-- ============================================

-- 角色
INSERT INTO sys_role (role_name, role_key) VALUES
('管理员', 'admin'),
('仓库员', 'warehouse'),
('普通用户', 'user');

-- 用户 (密码统一为 123456, BCrypt 加密)
INSERT INTO sys_user (username, password, nickname, email, phone) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '管理员', 'admin@ds.com', '13800000001'),
('user1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '测试用户', 'user1@ds.com', '13800000002'),
('warehouse1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '仓库管理员', 'wh1@ds.com', '13800000003');

-- 用户角色
INSERT INTO sys_user_role (user_id, role_id) VALUES
(1, 1),
(2, 3),
(3, 2);

-- 商品分类
INSERT INTO product_category (name, parent_id, sort_order) VALUES
('电子产品', 0, 1),
('食品饮料', 0, 2),
('办公用品', 0, 3),
('生活用品', 0, 4);

-- 商品
INSERT INTO product (name, code, category_id, price, stock, description, image) VALUES
('小米手机14', 'P001', 1, 3999.00, 500, '骁龙8 Gen3, 12GB+256GB', ''),
('华为MateBook X Pro', 'P002', 1, 9999.00, 100, '13.9英寸轻薄笔记本', ''),
('罗技无线鼠标', 'P003', 3, 129.00, 1000, '静音办公鼠标', ''),
('农夫山泉矿泉水', 'P004', 2, 2.00, 5000, '550ml 饮用水', ''),
('得力A4打印纸', 'P005', 3, 35.00, 800, '500张/包', ''),
('小米手环8', 'P006', 1, 249.00, 300, '运动健康监测', ''),
('蓝月亮洗衣液', 'P007', 4, 39.90, 600, '3kg 家庭装', ''),
('三只松鼠坚果礼盒', 'P008', 2, 89.90, 400, '750g 混合坚果', '');

-- 仓库
INSERT INTO warehouse (name, code, address, manager, capacity, status) VALUES
('北京中心仓', 'WH001', '北京市大兴区物流园A区', '张经理', 10000, 1),
('上海南仓', 'WH002', '上海市青浦区华新镇', '李经理', 8000, 1),
('广州东仓', 'WH003', '广州市黄埔区开发区', '王经理', 6000, 1);

-- 库存
INSERT INTO inventory (warehouse_id, product_id, quantity, frozen_quantity, safety_stock) VALUES
(1, 1, 200, 0, 50),
(1, 2, 50, 0, 10),
(1, 3, 500, 0, 100),
(1, 4, 2000, 0, 500),
(1, 5, 400, 0, 100),
(2, 1, 150, 0, 30),
(2, 6, 200, 0, 50),
(2, 7, 300, 0, 50),
(3, 4, 1500, 0, 300),
(3, 8, 200, 0, 50);

-- 供应商
INSERT INTO supplier (name, code, contact, phone, address, status) VALUES
('北京小米科技', 'SUP001', '刘经理', '13900000001', '北京市海淀区', 1),
('华为终端有限公司', 'SUP002', '陈经理', '13900000002', '深圳市龙岗区', 1),
('罗技(中国)科技', 'SUP003', '赵经理', '13900000003', '上海市浦东新区', 1),
('农夫山泉股份', 'SUP004', '孙经理', '13900000004', '杭州市西湖区', 1);

-- 客户
INSERT INTO customer (name, code, phone, address, type, status) VALUES
('张三', 'CUS001', '13700000001', '北京市朝阳区', 1, 1),
('李四', 'CUS002', '13700000002', '上海市徐汇区', 1, 1),
('腾讯科技', 'CUS003', '13700000003', '深圳市南山区', 2, 1),
('京东集团', 'CUS004', '13700000004', '北京市亦庄开发区', 2, 1);

-- 秒杀商品 (进行中)
INSERT INTO seckill_product (product_id, seckill_price, seckill_stock, start_time, end_time, status) VALUES
(1, 2999.00, 50, '2026-01-01 00:00:00', '2026-12-31 23:59:59', 1),
(6, 149.00, 100, '2026-01-01 00:00:00', '2026-12-31 23:59:59', 1);
