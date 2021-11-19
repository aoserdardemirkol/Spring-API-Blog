INSERT INTO tag
(tag_id, name)
VALUES ('0', 'tag name flyway')
    ON CONFLICT DO NOTHING;