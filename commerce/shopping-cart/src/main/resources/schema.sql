CREATE TABLE IF NOT EXISTS shopping_carts (
    cart_id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    username VARCHAR NOT NULL,
    active_flg BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS shopping_cart_x_products (
    product_id UUID PRIMARY KEY,
    quantity BIGINT NOT NULL,
    cart_id UUID NOT NULL,
    FOREIGN KEY (cart_id) REFERENCES carts(cart_id) ON DELETE CASCADE
);