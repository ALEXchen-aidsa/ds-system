-- 操作日志表
CREATE TABLE IF NOT EXISTS operation_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    module VARCHAR(50),
    operation VARCHAR(100),
    method VARCHAR(200),
    params TEXT,
    user_id BIGINT,
    username VARCHAR(50),
    ip VARCHAR(50),
    status TINYINT DEFAULT 1,
    error_msg VARCHAR(500),
    consume_time BIGINT,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;