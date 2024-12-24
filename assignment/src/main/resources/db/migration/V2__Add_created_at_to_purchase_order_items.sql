-- Add created_at column to purchase_order_items table
ALTER TABLE purchase_order_items
ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
