CREATE TABLE customer
(
    id    BIGSERIAL NOT NULL,
    name  VARCHAR   NOT NULL,
    email VARCHAR   NOT NULL,
    age   INT       NOT NULL,
    CONSTRAINT pk_customer_id PRIMARY KEY (id),
    CONSTRAINT unique_customer_email UNIQUE (email)
);