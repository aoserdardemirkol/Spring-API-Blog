CREATE TABLE IF NOT EXISTS entry
(
    entry_id VARCHAR(255) NOT NULL,
    content VARCHAR(255),
    create_date TIMESTAMP,
    title VARCHAR(255),
    update_date TIMESTAMP,
    author_id VARCHAR(255),
    CONSTRAINT entry_pkey PRIMARY KEY (entry_id),
    CONSTRAINT fk8cj4p59pb3imifrs3khagurjg FOREIGN KEY (author_id)
    REFERENCES users (user_id) MATCH SIMPLE
                          ON UPDATE NO ACTION
                          ON DELETE NO ACTION
    )