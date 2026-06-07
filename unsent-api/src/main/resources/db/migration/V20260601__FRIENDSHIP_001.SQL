CREATE SEQUENCE fds_record_seq START WITH 1000 INCREMENT BY 1 CACHE 1;

CREATE TABLE friendships
(
    record_id  BIGINT NOT NULL DEFAULT nextval('fds_record_seq'),
    user_id    VARCHAR(100) NOT NULL,
    friend_id  VARCHAR(100) NOT NULL,
    crud_value VARCHAR(20) NOT NULL,
    local_ts   TIMESTAMP   NOT NULL,
    host_ts    TIMESTAMP   NOT NULL,
    uuid       UUID        NOT NULL,

    CONSTRAINT friendships_pkey PRIMARY KEY (record_id)
);

CREATE INDEX idx_friendships_user ON friendships(user_id);
CREATE INDEX idx_friendships_friend ON friendships(friend_id);