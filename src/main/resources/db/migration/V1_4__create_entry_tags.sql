CREATE TABLE IF NOT EXISTS entry_tags
(
    entry_id VARCHAR(255) NOT NULL,
    tag_id VARCHAR(255) NOT NULL,
    CONSTRAINT entry_tags_pkey PRIMARY KEY (entry_id, tag_id),
    CONSTRAINT fknsh3miwca48y82dgvmj54qu47 FOREIGN KEY (entry_id)
    REFERENCES entry (entry_id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION,
    CONSTRAINT fksv6gpt2u397jhon6lwe4iw0j6 FOREIGN KEY (tag_id)
    REFERENCES tag (tag_id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    )