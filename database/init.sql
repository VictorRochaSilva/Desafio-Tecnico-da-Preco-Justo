-- Database initialization script for Duck Farm
-- Execute this script after creating the database

-- Create database (execute as superuser)
-- CREATE DATABASE duck_farm;

-- Connect to database
-- \c duck_farm;

-- Tables will be created automatically by Hibernate
-- This script contains only comments and example data

-- Example data insertion after first application execution:

-- Insert sample sellers
-- INSERT INTO sellers (name, cpf, registration_number, registration_date) VALUES
-- ('João Silva', '12345678901', 'S001', NOW()),
-- ('Maria Santos', '98765432109', 'S002', NOW()),
-- ('Pedro Oliveira', '45678912345', 'S003', NOW());

-- Insert sample customers
-- INSERT INTO customers (name, cpf, phone, address, discount_eligible, registration_date) VALUES
-- ('Carlos Ferreira', '11122233344', '(11) 99999-1111', 'Rua A, 123', true, NOW()),
-- ('Ana Costa', '55566677788', '(11) 99999-2222', 'Rua B, 456', false, NOW()),
-- ('Roberto Lima', '99988877766', '(11) 99999-3333', 'Rua C, 789', true, NOW());

-- Insert sample ducks
-- INSERT INTO ducks (name, mother_name, price, status, registration_date) VALUES
-- ('Donald Duck', NULL, 150.00, 'AVAILABLE', NOW()),
-- ('Daisy Duck', NULL, 180.00, 'AVAILABLE', NOW()),
-- ('Duckling 1', 'Donald Duck', 120.00, 'AVAILABLE', NOW()),
-- ('Duckling 2', 'Donald Duck', 120.00, 'AVAILABLE', NOW()),
-- ('Duckling 3', 'Daisy Duck', 130.00, 'AVAILABLE', NOW());

-- Example sale
-- INSERT INTO sales (duck_id, customer_id, seller_id, original_value, discount_value, final_value, sale_date) VALUES
-- (1, 1, 1, 150.00, 30.00, 120.00, NOW());

-- Update sold duck status
-- UPDATE ducks SET status = 'SOLD' WHERE id = 1;

-- Useful queries to check data:

-- List all available ducks
-- SELECT * FROM ducks WHERE status = 'AVAILABLE';

-- List sales with details
-- SELECT 
--     s.id,
--     d.name as duck,
--     c.name as customer,
--     sl.name as seller,
--     s.original_value,
--     s.discount_value,
--     s.final_value,
--     s.sale_date
-- FROM sales s
-- JOIN ducks d ON s.duck_id = d.id
-- JOIN customers c ON s.customer_id = c.id
-- JOIN sellers sl ON s.seller_id = sl.id;

-- Seller ranking
-- SELECT 
--     sl.name,
--     COUNT(s.id) as total_sales,
--     SUM(s.final_value) as total_value
-- FROM sellers sl
-- LEFT JOIN sales s ON sl.id = s.seller_id
-- GROUP BY sl.id, sl.name
-- ORDER BY total_value DESC NULLS LAST;

-- Insert initial admin user (password: admin123)
-- INSERT INTO users (username, password, name, role, active, registration_date) VALUES
-- ('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'Administrator', 'ADMIN', true, NOW());
