INSERT INTO users
(user_id, username, gender)
VALUES ('0', 'users username flyway', 0)
    ON CONFLICT DO NOTHING;