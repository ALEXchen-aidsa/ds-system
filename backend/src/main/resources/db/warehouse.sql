-- 仓库表
CREATE TABLE IF NOT EXISTS warehouse (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    code VARCHAR(50) UNIQUE NOT NULL,
    address VARCHAR(200),
    manager VARCHAR(50),
    capacity INT DEFAULT 0,
    status TINYINT DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 库存表
CREATE TABLE IF NOT EXISTS inventory (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    warehouse_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT DEFAULT 0,
    frozen_quantity INT DEFAULT 0,
    safety_stock INT DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_warehouse_product (warehouse_id, product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 出入库记录表
CREATE TABLE IF NOT EXISTS stock_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    record_no VARCHAR(50) UNIQUE NOT NULL,
    type TINYINT NOT NULL COMMENT '1入库 2出库',
    warehouse_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    price DECIMAL(10,2),
    remark VARCHAR(500),
    status TINYINT DEFAULT 1,
    operator_id BIGINT,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 盘点表
CREATE TABLE IF NOT EXISTS stock_check (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    check_no VARCHAR(50) UNIQUE NOT NULL,
    warehouse_id BIGINT NOT NULL,
    status TINYINT DEFAULT 0 COMMENT '0待盘点 1盘点中 2已完成',
    operator_id BIGINT,
    remark VARCHAR(500),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    finish_time DATETIME
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 盘点详情表
CREATE TABLE IF NOT EXISTS stock_check_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    check_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    system_quantity INT DEFAULT 0,
    actual_quantity INT DEFAULT 0,
    difference INT DEFAULT 0,
    remark VARCHAR(500),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;