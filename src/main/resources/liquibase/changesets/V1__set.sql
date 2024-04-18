CREATE TABLE IF NOT EXISTS category
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(32) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS quality
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(32) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS unit
(
    id         SERIAL PRIMARY KEY,
    name       VARCHAR(32) NOT NULL UNIQUE,
    short_name VARCHAR(8)  NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS product
(
    id             SERIAL PRIMARY KEY,
    name           VARCHAR(127)   NOT NULL UNIQUE,
    purchase_price NUMERIC(12, 2) NOT NULL,
    store_amount   NUMERIC(12, 2),
    version        BIGINT,
    unit_id        INTEGER REFERENCES unit (id),
    category_id    INTEGER REFERENCES category (id),
    image          VARCHAR(64)
);

CREATE TABLE IF NOT EXISTS product_comment
(
    product_id     INTEGER REFERENCES product (id),
    comment        VARCHAR(255) NOT NULL,
    comments_order INTEGER      NOT NULL,
    PRIMARY KEY (product_id, comments_order)
);

CREATE TABLE IF NOT EXISTS product_unit
(
    id         SERIAL PRIMARY KEY,
    ratio      NUMERIC(12, 2) NOT NULL,
    product_id INTEGER REFERENCES product (id),
    unit_id    INTEGER REFERENCES unit (id)
);

CREATE TABLE IF NOT EXISTS provider_order
(
    id         SERIAL PRIMARY KEY,
    comment    VARCHAR(255),
    commit     BOOLEAN,
    order_DATE DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS provider_order_line
(
    id                SERIAL PRIMARY KEY,
    amount            INTEGER NOT NULL,
    product_id        INTEGER REFERENCES product (id),
    product_unit_id   INTEGER REFERENCES product_unit (id),
    provider_order_id INTEGER REFERENCES provider_order (id),
    quality_id        INTEGER REFERENCES quality (id)
);

CREATE TABLE IF NOT EXISTS stock
(
    id           SERIAL PRIMARY KEY,
    store_amount NUMERIC(12, 2),
    product_id   INTEGER REFERENCES product (id),
    quality_id   INTEGER REFERENCES quality (id),
    version      BIGINT
);

CREATE TABLE IF NOT EXISTS users
(
    type        VARCHAR(16) NOT NULL,
    id          SERIAL PRIMARY KEY,
    username    VARCHAR(64) NOT NULL UNIQUE,
    password    VARCHAR(128),
    role        VARCHAR(16) NOT NULL,
    firstname   VARCHAR(64),
    lastname    VARCHAR(64),
    birth_day   DATE,
    region      VARCHAR(32),
    city        VARCHAR(32),
    street      VARCHAR(64),
    house       VARCHAR(8),
    postal_code VARCHAR(6),
    image       VARCHAR(64)
);

CREATE TABLE IF NOT EXISTS project
(
    id             SERIAL PRIMARY KEY,
    name           VARCHAR(64) NOT NULL UNIQUE,
    pro_manager_id INTEGER REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS customer
(
    id             SERIAL PRIMARY KEY,
    name           VARCHAR(127) NOT NULL UNIQUE,
    contract       VARCHAR(127),
    customer_since DATE         NOT NULL,
    status         VARCHAR(8)   NOT NULL,
    manager_id     INTEGER REFERENCES users (id),
    region         VARCHAR(32),
    city           VARCHAR(32),
    street         VARCHAR(64),
    house          VARCHAR(8),
    postal_code    VARCHAR(6)
);

CREATE TABLE IF NOT EXISTS customer_order
(
    id          SERIAL PRIMARY KEY,
    comment     VARCHAR(255),
    commit      BOOLEAN,
    order_DATE  DATE           NOT NULL,
    total_sum   NUMERIC(12, 2) NOT NULL,
    customer_id INTEGER REFERENCES customer (id),
    project_id  INTEGER REFERENCES project (id)
);

CREATE TABLE IF NOT EXISTS customer_order_line
(
    id                SERIAL PRIMARY KEY,
    amount            INTEGER        NOT NULL,
    price             NUMERIC(12, 2) NOT NULL,
    sum               NUMERIC(12, 2) NOT NULL,
    customer_order_id INTEGER REFERENCES customer_order (id),
    product_id        INTEGER REFERENCES product (id),
    product_unit_id   INTEGER REFERENCES product_unit (id),
    quality_id        INTEGER REFERENCES quality (id)
);
