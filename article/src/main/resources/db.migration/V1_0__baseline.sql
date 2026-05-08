CREATE TABLE articles (
                          id BIGINT PRIMARY KEY AUTO_INCREMENT,
                          user_id BIGINT NOT NULL,           -- 作者ID（多用户关键）
                          title VARCHAR(255) NOT NULL,
                          summary VARCHAR(500),
                          content LONGTEXT NOT NULL,         -- 正文（支持Markdown/HTML）
                          cover_image VARCHAR(255),

                          category_id BIGINT,                -- 分类
                          status TINYINT DEFAULT 0,          -- 0草稿 1已发布 2已下架

                          view_count INT DEFAULT 0,
                          like_count INT DEFAULT 0,

                          is_top TINYINT DEFAULT 0,          -- 是否置顶
                          is_comment TINYINT DEFAULT 1,      -- 是否允许评论

                          publish_time DATETIME,
                          create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
                          update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

                          INDEX idx_user_id (user_id),
                          INDEX idx_category_id (category_id),
                          INDEX idx_status (status)
);

CREATE TABLE categories (
                            id BIGINT PRIMARY KEY AUTO_INCREMENT,
                            name VARCHAR(50) NOT NULL,
                            description VARCHAR(255),
                            create_time DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE tags (
                      id BIGINT PRIMARY KEY AUTO_INCREMENT,
                      name VARCHAR(50) NOT NULL UNIQUE,
                      create_time DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE article_tags (
                              id BIGINT PRIMARY KEY AUTO_INCREMENT,
                              article_id BIGINT NOT NULL,
                              tag_id BIGINT NOT NULL,

                              UNIQUE KEY uk_article_tag (article_id, tag_id),
                              INDEX idx_article_id (article_id),
                              INDEX idx_tag_id (tag_id)
);