CREATE TABLE sys_user (
                          id BIGINT PRIMARY KEY AUTO_INCREMENT,
                          username VARCHAR(50) NOT NULL UNIQUE,
                          password VARCHAR(255) NOT NULL,
                          nickname VARCHAR(50),
                          status TINYINT DEFAULT 1 COMMENT '1正常 0禁用',
                          create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
                          update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
CREATE TABLE sys_role (
                          id BIGINT PRIMARY KEY AUTO_INCREMENT,
                          role_name VARCHAR(50) NOT NULL UNIQUE,
                          role_code VARCHAR(50) NOT NULL UNIQUE,
                          description VARCHAR(255),
                          create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
                          update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
CREATE TABLE sys_permission (
                                id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                perm_name VARCHAR(50) NOT NULL,
                                perm_code VARCHAR(100) NOT NULL UNIQUE,
                                type VARCHAR(20) COMMENT 'menu/button/api',
                                parent_id BIGINT DEFAULT 0,
                                path VARCHAR(255),
                                method VARCHAR(10),
                                create_time DATETIME DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE sys_user_role (
                               id BIGINT PRIMARY KEY AUTO_INCREMENT,
                               user_id BIGINT NOT NULL,
                               role_id BIGINT NOT NULL
);
CREATE TABLE sys_role_permission (
                                     id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                     role_id BIGINT NOT NULL,
                                     permission_id BIGINT NOT NULL
);