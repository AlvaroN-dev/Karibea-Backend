-- =============================================================================
-- KARIBEA DATABASE INITIALIZATION SCRIPT
-- Creates the unified database and all schemas for microservices
-- =============================================================================

-- Create the unified database (only if not using the default 'postgres' database)
-- CREATE DATABASE karibea_db;

-- Connect to the database
-- \c karibea_db;

-- =============================================================================
-- CREATE SCHEMAS FOR EACH MICROSERVICE
-- Using schemas instead of separate databases for better resource usage
-- =============================================================================

-- Core Services
CREATE SCHEMA IF NOT EXISTS identity;
CREATE SCHEMA IF NOT EXISTS users;

-- Product & Catalog
CREATE SCHEMA IF NOT EXISTS catalog;
CREATE SCHEMA IF NOT EXISTS inventory;
CREATE SCHEMA IF NOT EXISTS stores;
CREATE SCHEMA IF NOT EXISTS reviews;
CREATE SCHEMA IF NOT EXISTS search;

-- Orders & Transactions
CREATE SCHEMA IF NOT EXISTS orders;
CREATE SCHEMA IF NOT EXISTS payments;
CREATE SCHEMA IF NOT EXISTS shopcart;
CREATE SCHEMA IF NOT EXISTS shipping;

-- Marketing & Communication
CREATE SCHEMA IF NOT EXISTS marketing;
CREATE SCHEMA IF NOT EXISTS notifications;
CREATE SCHEMA IF NOT EXISTS chatbot;

-- =============================================================================
-- GRANT PERMISSIONS
-- =============================================================================
-- Grant all privileges on schemas to the default user
DO $$
DECLARE
    schema_name TEXT;
BEGIN
    FOR schema_name IN 
        SELECT unnest(ARRAY['identity', 'users', 'catalog', 'inventory', 'stores', 
                            'reviews', 'search', 'orders', 'payments', 'shopcart', 
                            'shipping', 'marketing', 'notifications', 'chatbot'])
    LOOP
        EXECUTE format('GRANT ALL PRIVILEGES ON SCHEMA %I TO postgres', schema_name);
        EXECUTE format('GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA %I TO postgres', schema_name);
        EXECUTE format('ALTER DEFAULT PRIVILEGES IN SCHEMA %I GRANT ALL PRIVILEGES ON TABLES TO postgres', schema_name);
        EXECUTE format('ALTER DEFAULT PRIVILEGES IN SCHEMA %I GRANT ALL PRIVILEGES ON SEQUENCES TO postgres', schema_name);
    END LOOP;
END $$;

-- =============================================================================
-- EXTENSIONS (Optional but useful)
-- =============================================================================
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pg_trgm";  -- For text search

-- Log completion
DO $$ BEGIN RAISE NOTICE 'Karibea database initialization completed successfully!'; END $$;
