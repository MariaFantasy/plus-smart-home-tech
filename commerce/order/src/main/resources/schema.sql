CREATE TABLE IF NOT EXISTS orders (
    order_id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    username VARCHAR NOT NULL,
    shopping_cart_id UUID,
    payment_id UUID,
    delivery_id UUID,
    state VARCHAR,
    delivery_weight DOUBLE PRECISION,
    delivery_volume DOUBLE PRECISION,
    fragile BOOLEAN,
    total_price DOUBLE PRECISION,
    delivery_price DOUBLE PRECISION,
    product_price DOUBLE PRECISION
);

CREATE TABLE IF NOT EXISTS order_x_product (
    order_id UUID NOT NULL,
    product_id UUID NOT NULL,
    quantity BIGINT NOT NULL,
    PRIMARY KEY (order_id, product_id),
    FOREIGN KEY (order_id) REFERENCES orders(order_id)
);