INSERT INTO comment
(comment_id, content, create_date, update_date, entry_id, author_id)
VALUES ('0', 'comment content flyway', localtimestamp, null, '0', '0')
    ON CONFLICT DO NOTHING;