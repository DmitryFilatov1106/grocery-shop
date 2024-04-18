CREATE TABLE IF NOT EXISTS revision
(
    id        SERIAL PRIMARY KEY,
    timestamp BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS provider_order_aud
(
    rev         INT REFERENCES revision (id),
    revtype     SMALLINT,
    id          INTEGER,
    comment     VARCHAR(255),
    commit      BOOLEAN,
    order_DATE  DATE,
    created_at  TIMESTAMP,
    modified_at TIMESTAMP,
    created_by  VARCHAR(32),
    modified_by VARCHAR(32)
);

CREATE TABLE IF NOT EXISTS customer_order_aud
(
    rev         INT REFERENCES revision (id),
    revtype     SMALLINT,
    id          INTEGER,
    comment     VARCHAR(255),
    commit      BOOLEAN,
    order_DATE  DATE,
    total_sum   NUMERIC(12, 2),
    customer_id INTEGER,
    project_id  INTEGER,
    created_at  TIMESTAMP,
    modified_at TIMESTAMP,
    created_by  VARCHAR(32),
    modified_by VARCHAR(32)
);

CREATE TABLE IF NOT EXISTS provider_order_line_aud
(
    rev               INT REFERENCES revision (id),
    revtype           SMALLINT,
    id                INTEGER,
    amount            INTEGER,
    product_id        INTEGER,
    product_unit_id   INTEGER,
    provider_order_id INTEGER,
    quality_id        INTEGER,
    created_at        TIMESTAMP,
    modified_at       TIMESTAMP,
    created_by        VARCHAR(32),
    modified_by       VARCHAR(32)
);

CREATE TABLE IF NOT EXISTS customer_order_line_aud
(
    rev               INT REFERENCES revision (id),
    revtype           SMALLINT,
    id                INTEGER,
    amount            INTEGER,
    price             NUMERIC(12, 2),
    sum               NUMERIC(12, 2),
    customer_order_id INTEGER,
    product_id        INTEGER,
    product_unit_id   INTEGER,
    quality_id        INTEGER,
    created_at        TIMESTAMP,
    modified_at       TIMESTAMP,
    created_by        VARCHAR(32),
    modified_by       VARCHAR(32)
);