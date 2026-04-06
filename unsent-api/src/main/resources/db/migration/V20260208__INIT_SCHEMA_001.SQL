CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE SEQUENCE record_seq START WITH 1000 INCREMENT BY 1 CACHE 1;

CREATE TABLE IF NOT EXISTS users (

    user_id VARCHAR(100) NOT NULL,
    record_id BIGINT NOT NULL DEFAULT nextval('record_seq'),
    email VARCHAR(255) NOT NULL,
    hashed_password VARCHAR(255) NOT NULL,
    username VARCHAR(100) NOT NULL,
    display_name VARCHAR(150),
    gender VARCHAR(20),
    date_of_birth DATE,
    status VARCHAR(30) NOT NULL,
    crud_value VARCHAR(20) NOT NULL,
    local_ts TIMESTAMP NOT NULL,
    host_ts TIMESTAMP NOT NULL,
    uuid UUID NOT NULL,
    CONSTRAINT users_pkey PRIMARY KEY (user_id),
    CONSTRAINT ux_users_username UNIQUE (username)
    );

CREATE UNIQUE INDEX idx_users_01 ON users (user_id, email, username, status);

CREATE TABLE IF NOT EXISTS diary_entries (

    record_id BIGINT NOT NULL DEFAULT nextval('record_seq'),
    uuid UUID NOT NULL,
    user_id VARCHAR(100) NOT NULL,
    content TEXT NOT NULL,
    visibility VARCHAR(30),
    status VARCHAR(30) NOT NULL,
    crud_value VARCHAR(20) NOT NULL,
    local_ts TIMESTAMP NOT NULL,
    host_ts TIMESTAMP NOT NULL,
    CONSTRAINT diary_entries_pkey PRIMARY KEY (record_id),
    CONSTRAINT fk_diary_user
    FOREIGN KEY (user_id)
    REFERENCES users (user_id)
    ON DELETE CASCADE
    );

-- Indexes
CREATE INDEX idx_diary_01
    ON diary_entries (user_id);
