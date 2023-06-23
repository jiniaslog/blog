CREATE TABLE taggings
(
    tagging_id   BIGINT NOT NULL PRIMARY KEY,
    article_id   BIGINT NOT NULL,
    tag_id       BIGINT NOT NULL,
    created_date DATETIME(6),
    updated_date DATETIME(6)
);

ALTER TABLE taggings
    ADD FOREIGN KEY (article_id) REFERENCES articles (article_id);
