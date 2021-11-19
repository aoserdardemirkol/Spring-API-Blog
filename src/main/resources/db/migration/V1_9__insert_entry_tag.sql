INSERT INTO entry_tags
(entry_id, tag_id)
VALUES ('0', '0')
    ON CONFLICT DO NOTHING;