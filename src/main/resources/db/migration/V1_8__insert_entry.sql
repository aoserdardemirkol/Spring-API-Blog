INSERT INTO entry
(entry_id, content, create_date, title, update_date, author_id)
VALUES ('0', 'entry content flyway', localtimestamp, 'Entry title flyway', null, '0')
    ON CONFLICT DO NOTHING;