CREATE TABLE IF NOT EXISTS products (
    product_id UUID PRIMARY KEY,
    fragile BOOLEAN,
    width DOUBLE PRECISION,
    height DOUBLE PRECISION,
    depth DOUBLE PRECISION,
    weight DOUBLE PRECISION,
    quantity BIGINT
);

CREATE TABLE IF NOT EXISTS order_bookings (
    booking_id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    order_id UUID NOT NULL UNIQUE,
    delivery_id UUID
);

CREATE TABLE IF NOT EXISTS order_booking_x_product (
    booking_id UUID NOT NULL,
    product_id UUID NOT NULL,
    quantity BIGINT NOT NULL,
    PRIMARY KEY (booking_id, product_id),
    FOREIGN KEY (product_id) REFERENCES products(product_id),
    FOREIGN KEY (booking_id) REFERENCES order_bookings(booking_id)
);