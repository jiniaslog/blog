create table if not exists blog_core.article
(
    id         bigint                             primary key,
    writer_id  bigint                             not null,
    head       bigint                             not null,
    status     varchar(10)                        not null,
    created_at datetime default CURRENT_TIMESTAMP null,
    updated_at datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP
);

CREATE TABLE if not exists blog_core.article_commit (
                               id BIGINT PRIMARY KEY,
                               title VARCHAR(100) null,
                               delta BLOB,
                               thumbnail_url VARCHAR(255) null,
                               article_id BIGINT not null,
                               category_id BIGINT null,
    created_at datetime default CURRENT_TIMESTAMP null,
    updated_at datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP
);

CREATE TABLE if not exists blog_core.article_staging_snap_shot (
                                           id BIGINT PRIMARY KEY,
                                           article_id BIGINT not null,
                                           title VARCHAR(255) null,
                                           content TEXT not null,
    thumbnail_url VARCHAR(255) null,
                                           category_id BIGINT null,
    created_at datetime default CURRENT_TIMESTAMP null,
    updated_at datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP
);

CREATE TABLE article_view (
                              id BIGINT PRIMARY KEY,
                              writer_name VARCHAR(255) NOT NULL,
                              title VARCHAR(100) null,
                              thumbnail_url VARCHAR(255) null,
                              category_name VARCHAR(255) null,
                              content TEXT null,
                              status VARCHAR(50) NOT NULL,
                              created_at datetime default CURRENT_TIMESTAMP null,
                              updated_at datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP
);

CREATE TABLE category (
                          id BIGINT PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          `order` INT NOT NULL,
                          parent_id BIGINT null,
                          created_at datetime default CURRENT_TIMESTAMP null,
                          updated_at datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
                          FOREIGN KEY (parent_id) REFERENCES category(id)
);
