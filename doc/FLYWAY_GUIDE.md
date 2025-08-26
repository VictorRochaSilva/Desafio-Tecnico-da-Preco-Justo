# 🗄️ Guia Completo do Flyway - Granja de Patos

## 🎯 **O que é o Flyway?**

O **Flyway** é uma ferramenta de migração de banco de dados que permite:
- **Versionar** todas as mudanças no banco
- **Reproduzir** o mesmo banco em qualquer ambiente
- **Controlar** o histórico de alterações
- **Colaborar** com outros desenvolvedores de forma segura

## 🚀 **Por que Implementamos?**

### **Antes (Problemas):**
- ❌ Apenas `init.sql` básico
- ❌ Hibernate gerando tabelas automaticamente
- ❌ Sem controle de versão do banco
- ❌ Difícil de manter em produção
- ❌ Riscos de perda de dados

### **Depois (Soluções):**
- ✅ **Migrações versionadas** com histórico completo
- ✅ **Schema controlado** e reproduzível
- ✅ **Deploy seguro** em produção
- ✅ **Colaboração** entre desenvolvedores
- ✅ **Auditoria** de todas as mudanças

## 📁 **Estrutura de Migrações**

```
src/main/resources/db/migration/
├── V1__Initial_Schema.sql      # Schema inicial completo
├── V2__Initial_Data.sql        # Dados iniciais para testes
└── V3__Future_Features.sql     # Funcionalidades futuras
```

### **Convenção de Nomenclatura:**
- **V1**: Versão 1
- **__**: Separador duplo underscore
- **Initial_Schema**: Descrição da migração
- **.sql**: Extensão SQL

## 🔧 **Configuração**

### **1. Dependência Maven:**
```xml
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-core</artifactId>
</dependency>
```

### **2. Configuração no `application.yml`:**
```yaml
spring:
  flyway:
    enabled: true
    baseline-on-migrate: true
    locations: classpath:db/migration
    validate-on-migrate: true
    clean-disabled: false
```

### **3. Configuração do Banco:**
```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: validate  # Flyway gerencia o schema
```

## 🚀 **Como Usar**

### **1. Primeira Execução:**
```bash
# 1. Iniciar banco de dados
docker-compose up -d

# 2. Executar migrações
mvn flyway:migrate

# 3. Verificar status
mvn flyway:info
```

### **2. Comandos Principais:**

#### **Verificar Status:**
```bash
mvn flyway:info
```
**Saída:**
```
+-----------+---------+---------------------+------+---------------------+---------+
| Category  | Version | Description         | Type | Installed On        | State   |
+-----------+---------+---------------------+------+---------------------+---------+
| Versioned | 1       | Initial Schema      | SQL  | 2024-01-01 10:00:00 | Success |
| Versioned | 2       | Initial Data        | SQL  | 2024-01-01 10:00:00 | Success |
| Versioned | 3       | Future Features     | SQL  | 2024-01-01 10:00:00 | Success |
+-----------+---------+---------------------+------+---------------------+---------+
```

#### **Executar Migrações:**
```bash
mvn flyway:migrate
```

#### **Limpar Banco (Desenvolvimento):**
```bash
mvn flyway:clean
```

#### **Reparar Migrações:**
```bash
mvn flyway:repair
```

#### **Validar Migrações:**
```bash
mvn flyway:validate
```

## 📝 **Criando Novas Migrações**

### **1. Estrutura da Migração:**
```sql
-- =====================================================
-- MIGRAÇÃO V4 - NOVA FUNCIONALIDADE
-- Versão: V4
-- Descrição: Adicionar nova tabela para relatórios
-- =====================================================

-- Criar nova tabela
CREATE TABLE reports (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Adicionar índices
CREATE INDEX idx_reports_name ON reports(name);

-- Comentários
COMMENT ON TABLE reports IS 'Tabela para armazenar relatórios gerados';
```

### **2. Regras Importantes:**
- ✅ **Nunca altere** migrações já executadas
- ✅ **Sempre incremente** o número da versão
- ✅ **Use transações** para operações complexas
- ✅ **Adicione comentários** explicativos
- ✅ **Teste** antes de commitar

### **3. Exemplo de Migração com Rollback:**
```sql
-- =====================================================
-- MIGRAÇÃO V5 - ADICIONAR COLUNA COM ROLLBACK
-- Versão: V5
-- Descrição: Adicionar coluna de observações em vendas
-- =====================================================

-- Adicionar nova coluna
ALTER TABLE sales ADD COLUMN observations TEXT;

-- Adicionar comentário
COMMENT ON COLUMN sales.observations IS 'Observações adicionais sobre a venda';

-- Para rollback (se necessário):
-- ALTER TABLE sales DROP COLUMN observations;
```

## 🔍 **Monitoramento e Logs**

### **1. Logs do Flyway:**
```yaml
logging:
  level:
    org.flywaydb: DEBUG
```

### **2. Tabela de Controle:**
O Flyway cria automaticamente a tabela `flyway_schema_history`:
```sql
SELECT * FROM flyway_schema_history ORDER BY installed_rank DESC;
```

### **3. Verificar Migrações Pendentes:**
```bash
mvn flyway:info
```

## 🚨 **Problemas Comuns e Soluções**

### **1. Migração Falhou:**
```bash
# Verificar logs
tail -f logs/application.log

# Reparar migração
mvn flyway:repair

# Verificar status
mvn flyway:info
```

### **2. Banco em Estado Inconsistente:**
```bash
# Limpar banco (CUIDADO: perde todos os dados)
mvn flyway:clean

# Executar migrações novamente
mvn flyway:migrate
```

### **3. Conflito de Versões:**
```bash
# Verificar histórico
mvn flyway:info

# Reparar se necessário
mvn flyway:repair
```

## 🏗️ **Ambientes**

### **1. Desenvolvimento:**
```yaml
spring:
  flyway:
    clean-disabled: false  # Permite limpar banco
    baseline-on-migrate: true
```

### **2. Produção:**
```yaml
spring:
  flyway:
    clean-disabled: true   # NUNCA permitir limpar em produção
    baseline-on-migrate: false
    validate-on-migrate: true
```

## 📊 **Benefícios para o Projeto**

### **1. Para Desenvolvedores:**
- 🎯 **Schema consistente** em todos os ambientes
- 🔄 **Histórico completo** de mudanças
- 🚀 **Deploy seguro** e controlado
- 👥 **Colaboração** sem conflitos

### **2. Para Produção:**
- 🛡️ **Segurança** nas alterações do banco
- 📈 **Rastreabilidade** de mudanças
- 🔍 **Auditoria** completa
- ⚡ **Rollback** em caso de problemas

### **3. Para o Negócio:**
- 💰 **Redução de riscos** em produção
- 📊 **Controle total** do banco de dados
- 🚀 **Deploy mais rápido** e seguro
- 📈 **Escalabilidade** para múltiplos ambientes

## 🎉 **Conclusão**

A implementação do **Flyway** transforma o projeto de uma aplicação básica para uma **solução empresarial robusta** com:

- ✅ **Controle total** do banco de dados
- ✅ **Versionamento** de todas as mudanças
- ✅ **Segurança** em produção
- ✅ **Colaboração** entre desenvolvedores
- ✅ **Auditoria** completa

**Este é realmente um diferencial gigante** que coloca o projeto em outro patamar! 🚀

---

**📚 Para mais informações:**
- [Documentação Oficial do Flyway](https://flywaydb.org/documentation/)
- [Flyway com Spring Boot](https://docs.spring.io/spring-boot/docs/current/reference/html/howto.html#howto.data-initialization.migration-tool.flyway)
