-- =====================================================
-- MIGRAÇÃO V3 - FUNCIONALIDADES FUTURAS
-- Versão: V3
-- Descrição: Preparação para funcionalidades avançadas
-- =====================================================

-- Tabela de auditoria para rastrear mudanças
CREATE TABLE audit_logs (
    id BIGSERIAL PRIMARY KEY,
    table_name VARCHAR(50) NOT NULL,
    record_id BIGINT NOT NULL,
    action VARCHAR(20) NOT NULL CHECK (action IN ('INSERT', 'UPDATE', 'DELETE')),
    old_values JSONB,
    new_values JSONB,
    user_id BIGINT REFERENCES users(id),
    timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Tabela de métricas de performance dos vendedores
CREATE TABLE seller_metrics (
    id BIGSERIAL PRIMARY KEY,
    seller_id BIGINT NOT NULL REFERENCES sellers(id),
    total_sales BIGINT NOT NULL DEFAULT 0,
    total_revenue DECIMAL(12,2) NOT NULL DEFAULT 0,
    average_sale_value DECIMAL(10,2) NOT NULL DEFAULT 0,
    last_sale_date TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Tabela de configurações do sistema
CREATE TABLE system_config (
    id BIGSERIAL PRIMARY KEY,
    config_key VARCHAR(100) NOT NULL UNIQUE,
    config_value TEXT NOT NULL,
    description TEXT,
    updated_by BIGINT REFERENCES users(id),
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Índices para performance
CREATE INDEX idx_audit_logs_table_record ON audit_logs(table_name, record_id);
CREATE INDEX idx_audit_logs_timestamp ON audit_logs(timestamp);
CREATE INDEX idx_seller_metrics_seller_id ON seller_metrics(seller_id);
CREATE INDEX idx_seller_metrics_total_sales ON seller_metrics(total_sales DESC);

-- Inserir configurações padrão do sistema
INSERT INTO system_config (config_key, config_value, description) VALUES
('DISCOUNT_PERCENTAGE', '20', 'Percentual de desconto para clientes elegíveis'),
('MIN_DUCK_PRICE', '50.00', 'Preço mínimo para patos no sistema'),
('MAX_DUCK_PRICE', '1000.00', 'Preço máximo para patos no sistema'),
('SYSTEM_MAINTENANCE_MODE', 'false', 'Modo de manutenção do sistema');

-- Comentários das novas tabelas
COMMENT ON TABLE audit_logs IS 'Log de auditoria para rastrear todas as mudanças no sistema';
COMMENT ON TABLE seller_metrics IS 'Métricas de performance dos vendedores para relatórios';
COMMENT ON TABLE system_config IS 'Configurações configuráveis do sistema';

-- Trigger para atualizar timestamp de atualização
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_seller_metrics_updated_at 
    BEFORE UPDATE ON seller_metrics 
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_system_config_updated_at 
    BEFORE UPDATE ON system_config 
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
