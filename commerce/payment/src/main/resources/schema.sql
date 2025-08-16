DROP TABLE IF EXISTS payments (
    payment_id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    order_id UUID NOT NULL,
    total_payment DOUBLE PRECISION,
    delivery_total DOUBLE PRECISION,
    fee_total DOUBLE PRECISION,
    state VARCHAR NOT NULL
);