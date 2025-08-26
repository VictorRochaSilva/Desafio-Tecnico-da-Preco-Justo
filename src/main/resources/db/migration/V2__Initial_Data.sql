-- =====================================================
-- MIGRAÇÃO V2 - DADOS INICIAIS DO SISTEMA
-- Versão: V2
-- Descrição: Inserção de dados iniciais para testes
-- =====================================================

-- Inserir usuário administrador padrão
-- Senha: admin123 (deve ser alterada em produção)
INSERT INTO users (username, password, name, role, active, registration_date) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'Administrador', 'ADMIN', true, CURRENT_TIMESTAMP);

-- Inserir vendedores de exemplo
INSERT INTO sellers (name, cpf, employee_id, registration_date) VALUES
('João Silva', '123.456.789-01', 'V001', CURRENT_TIMESTAMP),
('Maria Santos', '987.654.321-00', 'V002', CURRENT_TIMESTAMP),
('Pedro Oliveira', '456.789.123-45', 'V003', CURRENT_TIMESTAMP);

-- Inserir clientes de exemplo
INSERT INTO customers (name, cpf, phone, address, discount_eligible, registration_date) VALUES
('Carlos Ferreira', '111.222.333-44', '(11) 99999-1111', 'Rua A, 123 - São Paulo/SP', true, CURRENT_TIMESTAMP),
('Ana Costa', '555.666.777-88', '(21) 88888-2222', 'Av. B, 456 - Rio de Janeiro/RJ', false, CURRENT_TIMESTAMP),
('Roberto Almeida', '999.888.777-66', '(31) 77777-3333', 'Rua C, 789 - Belo Horizonte/MG', true, CURRENT_TIMESTAMP);

-- Inserir patos de exemplo
INSERT INTO ducks (name, mother_id, price, status, registration_date) VALUES
('Donald Duck', NULL, 150.00, 'AVAILABLE', CURRENT_TIMESTAMP),
('Daisy Duck', NULL, 180.00, 'AVAILABLE', CURRENT_TIMESTAMP),
('Duckling 1', 1, 120.00, 'AVAILABLE', CURRENT_TIMESTAMP),
('Duckling 2', 1, 120.00, 'AVAILABLE', CURRENT_TIMESTAMP),
('Duckling 3', 2, 130.00, 'AVAILABLE', CURRENT_TIMESTAMP);

-- Comentários sobre os dados inseridos
COMMENT ON TABLE users IS 'Usuário admin padrão criado para acesso inicial ao sistema';
COMMENT ON TABLE sellers IS 'Vendedores de exemplo para demonstração do sistema';
COMMENT ON TABLE customers IS 'Clientes de exemplo com diferentes elegibilidades para desconto';
COMMENT ON TABLE ducks IS 'Patos de exemplo incluindo patos pais e filhotes para demonstração de linhagem';
