CREATE TABLE IF NOT EXISTS comment
(
    comment_id VARCHAR(255) NOT NULL,
    content VARCHAR(255),
    create_date TIMESTAMP,
    update_date TIMESTAMP,
    entry_id VARCHAR(255),
    author_id VARCHAR(255),
    CONSTRAINT comment_pkey PRIMARY KEY (comment_id),
    CONSTRAINT fkb2vr0m6nf3xd38gsicgthk62q FOREIGN KEY (entry_id)
    REFERENCES entry (entry_id) MATCH SIMPLE
                          ON UPDATE NO ACTION
                          ON DELETE NO ACTION,
    CONSTRAINT fkir20vhrx08eh4itgpbfxip0s1 FOREIGN KEY (author_id)
    REFERENCES users (user_id) MATCH SIMPLE
                          ON UPDATE NO ACTION
                          ON DELETE NO ACTION
    )