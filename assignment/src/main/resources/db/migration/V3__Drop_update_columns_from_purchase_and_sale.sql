-- Remove updated_by and updated_at from purchase_orders table
ALTER TABLE purchase_orders DROP COLUMN updated_by;
ALTER TABLE purchase_orders DROP COLUMN updated_at;

-- Remove updated_by and updated_at from sale_orders table
ALTER TABLE sale_orders DROP COLUMN updated_by;
ALTER TABLE sale_orders DROP COLUMN updated_at;
