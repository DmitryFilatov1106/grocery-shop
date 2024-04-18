ALTER TABLE provider_order
    ADD COLUMN created_at TIMESTAMP;

ALTER TABLE provider_order
    ADD COLUMN modified_at TIMESTAMP;

ALTER TABLE provider_order
    ADD COLUMN created_by VARCHAR(32);

ALTER TABLE provider_order
    ADD COLUMN modified_by VARCHAR(32);

ALTER TABLE customer_order
    ADD COLUMN created_at TIMESTAMP;

ALTER TABLE customer_order
    ADD COLUMN modified_at TIMESTAMP;

ALTER TABLE customer_order
    ADD COLUMN created_by VARCHAR(32);

ALTER TABLE customer_order
    ADD COLUMN modified_by VARCHAR(32);

ALTER TABLE provider_order_line
    ADD COLUMN created_at TIMESTAMP;

ALTER TABLE provider_order_line
    ADD COLUMN modified_at TIMESTAMP;

ALTER TABLE provider_order_line
    ADD COLUMN created_by VARCHAR(32);

ALTER TABLE provider_order_line
    ADD COLUMN modified_by VARCHAR(32);

ALTER TABLE customer_order_line
    ADD COLUMN created_at TIMESTAMP;

ALTER TABLE customer_order_line
    ADD COLUMN modified_at TIMESTAMP;

ALTER TABLE customer_order_line
    ADD COLUMN created_by VARCHAR(32);

ALTER TABLE customer_order_line
    ADD COLUMN modified_by VARCHAR(32);