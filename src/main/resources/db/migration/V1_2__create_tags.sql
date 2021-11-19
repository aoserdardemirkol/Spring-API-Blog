CREATE TABLE IF NOT EXISTS tag
(
    tag_id VARCHAR(255) NOT NULL,
    name VARCHAR(255),
    CONSTRAINT tag_pkey PRIMARY KEY (tag_id)
    )