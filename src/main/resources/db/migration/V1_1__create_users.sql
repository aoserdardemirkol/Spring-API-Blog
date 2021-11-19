CREATE TABLE IF NOT EXISTS users
(
    user_id     VARCHAR(255) NOT NULL,
    username    VARCHAR(255),
    gender      VARCHAR(255),
    CONSTRAINT users_pkey PRIMARY KEY (user_id)
    );
