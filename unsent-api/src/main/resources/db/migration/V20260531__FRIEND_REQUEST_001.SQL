CREATE SEQUENCE fr_record_seq START WITH 1000 INCREMENT BY 1 CACHE 1;

CREATE TABLE friend_requests
(
    record_id      BIGINT NOT NULL DEFAULT nextval('fr_record_seq'),
    sender_id      VARCHAR(100) NOT NULL,
    receiver_id    VARCHAR(100) NOT NULL,
    request_status VARCHAR(20)  NOT NULL,
    crud_value     VARCHAR(20) NOT NULL,
    local_ts       TIMESTAMP   NOT NULL,
    host_ts        TIMESTAMP   NOT NULL,
    uuid           UUID        NOT NULL,

    CONSTRAINT friend_requests_pkey PRIMARY KEY (record_id)
);

CREATE INDEX idx_friend_requests_sender ON friend_requests(sender_id);
CREATE INDEX idx_friend_requests_receiver ON friend_requests(receiver_id);