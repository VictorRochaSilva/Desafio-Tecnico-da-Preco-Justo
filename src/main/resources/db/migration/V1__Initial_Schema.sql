-- =====================================================
-- MIGRAÇÃO INICIAL - SCHEMA COMPLETO DO SISTEMA
-- Versão: V1
-- Descrição: Criação inicial de todas as tabelas
-- =====================================================

-- Tabela de usuários do sistema
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL CHECK (role IN ('ADMIN', 'SELLER', 'MANAGER')),
    active BOOLEAN NOT NULL DEFAULT true,
    registration_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Tabela de vendedores
CREATE TABLE sellers (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    cpf VARCHAR(14) NOT NULL UNIQUE,
    employee_id VARCHAR(20) NOT NULL UNIQUE,
    registration_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Tabela de clientes
CREATE TABLE customers (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    cpf VARCHAR(14) NOT NULL UNIQUE,
    phone VARCHAR(20) NOT NULL,
    address TEXT NOT NULL,
    discount_eligible BOOLEAN NOT NULL DEFAULT false,
    registration_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Tabela de patos
CREATE TABLE ducks (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    mother_id BIGINT,
    price DECIMAL(10,2) NOT NULL CHECK (price > 0),
    status VARCHAR(20) NOT NULL DEFAULT 'AVAILABLE' CHECK (status IN ('AVAILABLE', 'SOLD', 'RESERVED')),
    registration_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Tabela de vendas
CREATE TABLE sales (
    id BIGSERIAL PRIMARY KEY,
    duck_id BIGINT NOT NULL REFERENCES ducks(id),
    customer_id BIGINT NOT NULL REFERENCES customers(id),
    seller_id BIGINT NOT NULL REFERENCES sellers(id),
    original_price DECIMAL(10,2) NOT NULL CHECK (original_price > 0),
    discount_amount DECIMAL(10,2) DEFAULT 0 CHECK (discount_amount >= 0),
    final_price DECIMAL(10,2) NOT NULL CHECK (final_price > 0),
    sale_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Índices para performance
CREATE INDEX idx_ducks_status ON ducks(status);
CREATE INDEX idx_ducks_mother_id ON ducks(mother_id);
CREATE INDEX idx_sales_duck_id ON sales(duck_id);
CREATE INDEX idx_sales_customer_id ON sales(customer_id);
CREATE INDEX idx_sales_seller_id ON sales(seller_id);
CREATE INDEX idx_sales_sale_date ON sales(sale_date);
CREATE INDEX idx_customers_cpf ON customers(cpf);
CREATE INDEX idx_sellers_cpf ON sellers(cpf);
CREATE INDEX idx_sellers_employee_id ON sellers(employee_id);

-- Comentários das tabelas
COMMENT ON TABLE users IS 'Usuários do sistema para autenticação e autorização';
COMMENT ON TABLE sellers IS 'Vendedores da granja com CPF e matrícula únicos';
COMMENT ON TABLE customers IS 'Clientes com elegibilidade para desconto';
COMMENT ON TABLE ducks IS 'Patos individuais com rastreamento de linhagem';
COMMENT ON TABLE sales IS 'Transações de venda com cálculo de desconto';

-- Comentários das colunas principais
COMMENT ON COLUMN ducks.mother_id IS 'ID da mãe do pato para rastreamento de linhagem';
COMMENT ON COLUMN ducks.status IS 'Status atual: AVAILABLE, SOLD, RESERVED';
COMMENT ON COLUMN sales.discount_amount IS 'Valor do desconto aplicado (20% para clientes elegíveis)';
COMMENT ON COLUMN sales.final_price IS 'Preço final após aplicar desconto';
