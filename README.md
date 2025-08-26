# 🦆 API de Gerenciamento de Granja de Patos

Uma API REST abrangente para gerenciar um negócio de granja de patos, construída com Spring Boot seguindo padrões de nível empresarial e princípios SOLID.

## 🚀 Funcionalidades

### Operações de Negócio Principais
- **Gerenciamento de Patos**: Gerenciamento completo do ciclo de vida de patos individuais com rastreamento de linhagem
- **Gerenciamento de Clientes**: Perfis de clientes com elegibilidade para desconto (20% automático)
- **Gerenciamento de Vendedores**: Acompanhamento de funcionários com métricas de performance e proteção contra exclusão
- **Operações de Venda**: Processamento de transações com cálculo automático de desconto e auditoria
- **Relatórios Excel**: Geração profissional de relatórios em Excel com Apache POI para análises de vendas e rankings de vendedores
- **Ranking de Vendedores**: Endpoint específico para ranking de vendedores por performance

### Funcionalidades Técnicas
- **Autenticação JWT**: Autenticação segura e stateless com controle de acesso baseado em roles
- **Arquitetura SOLID**: Código limpo e sustentável seguindo princípios SOLID
- **Validação Abrangente**: Validação de entrada com mensagens de erro detalhadas
- **Tratamento de Exceções**: Processamento centralizado de exceções com respostas de erro padronizadas
- **Documentação da API**: Documentação completa OpenAPI/Swagger
- **Logging**: Logging estruturado com diferentes níveis para debugging e monitoramento

## 🏗️ Arquitetura

### Implementação dos Princípios SOLID
- **Responsabilidade Única**: Cada classe tem uma responsabilidade clara
- **Aberto/Fechado**: Serviços são abertos para extensão, fechados para modificação
- **Substituição de Liskov**: Implementação adequada de herança e interfaces
- **Segregação de Interface**: Interfaces focadas para casos específicos
- **Inversão de Dependência**: Módulos de alto nível não dependem de módulos de baixo nível

### Arquitetura em Camadas
```
┌─────────────────────────────────────────────────────────────┐
│                    Camada de Controladores                  │
│                 (Endpoints da API REST)                    │
├─────────────────────────────────────────────────────────────┤
│                     Camada de Serviços                      │
│                (Lógica de Negócio)                         │
├─────────────────────────────────────────────────────────────┤
│                   Camada de Repositórios                    │
│                 (Acesso a Dados)                           │
├─────────────────────────────────────────────────────────────┤
│                    Camada de Entidades                      │
│                 (Modelos de Dados)                         │
└─────────────────────────────────────────────────────────────┘
```

## 🛠️ Stack Tecnológico

- **Java 17**: Java moderno com recursos mais recentes
- **Spring Boot 3.2.0**: Versão mais recente do Spring Boot
- **Spring Security**: Autenticação e autorização baseada em JWT
- **Spring Data JPA**: Persistência de dados com Hibernate
- **PostgreSQL**: Banco de dados relacional robusto
- **Flyway**: Migrações de banco de dados versionadas
- **Lombok**: Reduz código boilerplate
- **Apache POI**: Geração de relatórios Excel
- **Swagger/OpenAPI**: Documentação da API
- **Maven**: Automação de build e gerenciamento de dependências

## 📋 Pré-requisitos

- Java 17 ou superior
- Maven 3.6 ou superior
- PostgreSQL 12 ou superior
- Docker (opcional, para banco de dados containerizado)

## 🚀 Início Rápido

### 1. Configuração Automática do Banco de Dados (Recomendado)
```bash
# Windows
init-database.bat

# Linux/Mac
./init-database.sh

# Estes scripts fazem automaticamente:
# ✅ Verificam se o Docker está rodando
# ✅ Iniciam o container PostgreSQL se necessário
# ✅ Criam o banco 'duck_farm' se não existir
# ✅ Preparam tudo para o Flyway executar as migrações
```

### 2. Configuração Manual do Banco de Dados (Alternativa)
```bash
# Usando Docker
docker-compose up -d

# Criar banco manualmente
docker exec -it duck_farm_db psql -U postgres -c "CREATE DATABASE duck_farm;"

# O Flyway criará automaticamente o schema e dados iniciais
# Não é necessário criar as tabelas manualmente
```

### 2. Configuração da Aplicação
Edite `src/main/resources/application.yml` se necessário (configurações padrão já estão corretas).

### 3. **IMPORTANTE: Criar Primeira Conta de Usuário**
Após executar as migrações, você **DEVE** criar sua primeira conta de usuário:

```bash
# Criar usuário administrador
curl -X POST http://localhost:8080/api/auth/users/create \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123",
    "name": "Administrador",
    "role": "ADMIN"
  }'
```

**⚠️ ATENÇÃO:** Sem criar um usuário, você não conseguirá acessar a API!
  datasource:
    url: jdbc:postgresql://localhost:5432/duck_farm
    username: postgres
    password: postgres
```

## 🚀 **Executando a Aplicação**

### **🐳 Opção 1: Docker Compose (Recomendado)**

#### **Setup completo com um comando:**
```bash
# Subir apenas o banco de dados
docker-compose up -d postgres

# Aguardar banco ficar saudável (5-10 segundos)
# Executar migrações
./mvnw flyway:migrate

# Rodar aplicação
./mvnw spring-boot:run
```

#### **Setup completo (banco + aplicação):**
```bash
# Subir tudo (banco + app)
docker-compose --profile full-stack up -d

# A API estará disponível em http://localhost:8080
# Swagger em http://localhost:8080/swagger-ui.html
```

#### **Comandos úteis:**
```bash
# Ver logs
docker-compose logs -f

# Parar tudo
docker-compose down

# Parar e remover volumes (reset completo)
docker-compose down -v

# Rebuild e subir
docker-compose up --build
```

### **🔧 Opção 2: Setup Manual**

#### **1. Configuração do Banco de Dados**
```bash
# Verificar se o Docker está rodando
docker ps

# Se não houver container, criar um novo
docker run --name duck_farm_db -e POSTGRES_DB=duck_farm -e POSTGRES_PASSWORD=postgres -p 5432:5432 -d postgres:15
```

#### **2. Executar Migrações Flyway**
```bash
# Executar migrações do banco de dados
./mvnw flyway:migrate

# Verificar status das migrações
./mvnw flyway:info

# Limpar banco (cuidado - apaga todos os dados!)
./mvnw flyway:clean
```

#### **3. Executar a Aplicação**
```bash
# Executar com Maven Wrapper
./mvnw spring-boot:run

# Ou executar com Maven instalado
mvn spring-boot:run
```

#### **4. Verificar se está funcionando**
```bash
# Health check
curl http://localhost:8080/actuator/health

# Swagger UI
http://localhost:8080/swagger-ui.html
```

### 4. **CRIAR PRIMEIRA CONTA DE USUÁRIO (OBRIGATÓRIO!)**
Após executar as migrações, você **DEVE** criar sua primeira conta:

```bash
# Criar usuário administrador
curl -X POST http://localhost:8080/api/auth/users/create \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123",
    "name": "Administrador",
    "role": "ADMIN"
  }'
```

### 5. Acessar API
- **URL Base**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html

## 🔐 Autenticação

### 1. Fazer Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```

### 3. Usar Token
```bash
# Incluir token no cabeçalho Authorization
curl -X GET http://localhost:8080/api/ducks \
  -H "Authorization: Bearer SEU_TOKEN_JWT"
```

## 📚 Documentação e Endpoints da API

### 📖 **Documentação Técnica**
A documentação completa está organizada na pasta [`doc/`](doc/):

- **🚀 [Início Rápido](doc/QUICKSTART.md)** - Configuração em 5 minutos
- **🗄️ [Guia Flyway](doc/FLYWAY_GUIDE.md)** - Migrações de banco de dados
- **🔌 [Collection Postman](Granja_Patos_API.postman_collection.json)** - Testes completos da API
- **📚 [README da Documentação](doc/README.md)** - Estrutura e organização

### 📋 **Endpoints da API**

### Autenticação
- `POST /api/auth/login` - Autenticação de usuário
- `POST /api/auth/users/create` - Criar novo usuário

### Gerenciamento de Patos
- `GET /api/ducks` - Listar todos os patos
- `GET /api/ducks/{id}` - Obter pato por ID
- `POST /api/ducks` - Criar novo pato
- `PUT /api/ducks/{id}` - Atualizar pato
- `DELETE /api/ducks/{id}` - Deletar pato
- `GET /api/ducks/status/{status}` - Filtrar patos por status
- `GET /api/ducks/available` - Obter patos disponíveis para venda

### Gerenciamento de Clientes
- `GET /api/customers` - Listar todos os clientes
- `GET /api/customers/{id}` - Obter cliente por ID
- `POST /api/customers` - Criar novo cliente
- `PUT /api/customers/{id}` - Atualizar cliente
- `DELETE /api/customers/{id}` - Deletar cliente

### Gerenciamento de Vendedores
- `GET /api/sellers` - Listar todos os vendedores
- `GET /api/sellers/{id}` - Obter vendedor por ID
- `POST /api/sellers` - Criar novo vendedor
- `PUT /api/sellers/{id}` - Atualizar vendedor
- `DELETE /api/sellers/{id}` - Deletar vendedor
- `GET /api/sellers/ranking` - Ranking de vendedores por performance

### Operações de Venda
- `GET /api/sales` - Listar todas as vendas
- `GET /api/sales/{id}` - Obter venda por ID
- `POST /api/sales` - Criar nova venda
- `GET /api/sales/customer/{customerId}` - Obter vendas por cliente
- `GET /api/sales/seller/{sellerId}` - Obter vendas por vendedor

### Relatórios Excel
- `GET /api/reports/sales` - Download relatório de vendas em Excel com layout profissional
- `GET /api/reports/seller-ranking` - Download relatório de ranking de vendedores em Excel com métricas

## 🗄️ Gerenciamento de Banco de Dados

### 🚀 **Scripts de Inicialização Automática**

Para facilitar o desenvolvimento e deploy, criamos scripts que automatizam a configuração do banco:

#### **Windows (`init-database.bat`)**
```batch
# Executar como administrador ou duplo clique
init-database.bat
```

#### **Linux/Mac (`init-database.sh`)**
```bash
# Dar permissão de execução (primeira vez)
chmod +x init-database.sh

# Executar
./init-database.sh
```

#### **O que os scripts fazem automaticamente:**
- ✅ **Verificam** se o Docker está rodando
- ✅ **Iniciam** o container PostgreSQL se necessário
- ✅ **Criam** o banco `duck_farm` se não existir
- ✅ **Preparam** tudo para o Flyway executar as migrações
- ✅ **Validam** se tudo está funcionando

### Migrações com Flyway
O projeto utiliza **Flyway** para gerenciar todas as mudanças no banco de dados de forma versionada e controlada.

#### **Estrutura de Migrações:**
```
src/main/resources/db/migration/
├── V1__Initial_Schema.sql      # Schema inicial completo
├── V2__Initial_Data.sql        # Dados iniciais para testes
└── V3__Future_Features.sql     # Funcionalidades futuras
```

#### **Benefícios:**
- ✅ **Versionamento**: Controle total das mudanças no banco
- ✅ **Reprodutibilidade**: Mesmo banco em todos os ambientes
- ✅ **Rollback**: Possibilidade de reverter mudanças
- ✅ **Auditoria**: Histórico completo de alterações
- ✅ **Colaboração**: Múltiplos desenvolvedores podem trabalhar
- ✅ **Produção**: Deploy seguro e controlado
- ✅ **Migrações Automáticas**: Schema criado automaticamente na primeira execução

#### **Comandos Flyway:**
```bash
# Verificar status das migrações
mvn flyway:info

# Executar migrações pendentes
mvn flyway:migrate

# Limpar banco (desenvolvimento)
mvn flyway:clean

# Reparar migrações corrompidas
mvn flyway:repair
```

## 🔒 Segurança

### Controle de Acesso Baseado em Roles
- **ADMIN**: Acesso completo ao sistema
- **SELLER**: Gerenciamento de vendas e clientes
- **MANAGER**: Relatórios e análises

### Estrutura do Token JWT
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": 1,
    "username": "admin",
    "name": "Administrador",
    "role": "ADMIN",
    "active": true
  },
  "message": "Login realizado com sucesso"
}
```

## 📊 Regras de Negócio

### Gerenciamento de Patos
- Cada pato deve ter um nome único
- Patos só podem ser vendidos uma vez
- Rastreamento de linhagem através do ID da mãe (campo `motherId`, opcional)
- Gerenciamento do ciclo de vida baseado em status (AVAILABLE, SOLD, RESERVED)

### Operações de Venda
- Desconto automático de 20% para clientes elegíveis
- Registro automático da data da venda
- Identificação do vendedor para todas as transações
- Validação e cálculo de preços
- Cálculo automático de preço final com desconto aplicado

### Integridade dos Dados
- Vendedores com histórico de vendas não podem ser deletados
- Restrições únicas em CPF e IDs de funcionários
- Trilhas de auditoria para todas as operações
- Validação automática de dados antes da persistência

## 🧪 Testes

### 🚀 **Executando os Testes**

#### **Testes Unitários (Recomendado para desenvolvimento)**
```bash
# Executar todos os testes unitários
./mvnw test

# Executar testes específicos
./mvnw test -Dtest=DuckServiceTest
./mvnw test -Dtest=SaleServiceTest
./mvnw test -Dtest=CustomerServiceTest
./mvnw test -Dtest=ReportServiceTest

# Executar com cobertura de código
./mvnw jacoco:report
```

#### **Testes de Integração**
```bash
# Executar testes de integração
./mvnw verify

# Executar apenas testes de integração
./mvnw test -Dtest=*IntegrationTest
```

#### **Testes com Cobertura**
```bash
# Gerar relatório de cobertura
./mvnw clean test jacoco:report

# Abrir relatório no navegador
# target/site/jacoco/index.html
```

### 📊 **Cobertura dos Testes**

#### **Testes Unitários Implementados:**
- ✅ **DuckServiceTest** - 15 testes cobrindo CRUD, validações e regras de negócio
- ✅ **SaleServiceTest** - 12 testes cobrindo vendas, desconto e validações
- ✅ **CustomerServiceTest** - 15 testes cobrindo clientes e validações de CPF
- ✅ **ReportServiceTest** - 8 testes cobrindo geração de relatórios Excel

#### **Testes de Integração:**
- ✅ **DuckIntegrationTest** - 7 testes cobrindo integração entre camadas

#### **Cenários Testados:**
- ✅ **Validações de Entrada** - CPF, nome, preço, datas
- ✅ **Regras de Negócio** - Cálculo de desconto, status de patos
- ✅ **Operações CRUD** - Criar, ler, atualizar, deletar
- ✅ **Tratamento de Erros** - Exceções e mensagens de erro
- ✅ **Integração de Camadas** - Controller → Service → Repository
- ✅ **Geração de Relatórios** - Excel com Apache POI

### 🎯 **Estrutura dos Testes**

```
src/test/java/com/granja/
├── service/                    # Testes unitários dos serviços
│   ├── DuckServiceTest.java   # Testes do DuckService
│   ├── SaleServiceTest.java   # Testes do SaleService
│   ├── CustomerServiceTest.java # Testes do CustomerService
│   └── ReportServiceTest.java # Testes do ReportService
└── integration/               # Testes de integração
    └── DuckIntegrationTest.java # Teste de integração do Duck

src/test/resources/
└── application-test.yml       # Configuração específica para testes
```

### 🔧 **Configuração de Teste**

- **Banco de Dados**: H2 em memória para testes rápidos
- **Flyway**: Desabilitado para testes (Hibernate cria schema)
- **Transações**: Rollback automático após cada teste
- **Mocks**: Mockito para isolamento de dependências
- **Assertions**: JUnit 5 com assertions expressivas

## 📈 Monitoramento e Logging

### Níveis de Log
- **DEBUG**: Informações detalhadas de debugging
- **INFO**: Fluxo geral da aplicação
- **WARN**: Condições de aviso
- **ERROR**: Condições de erro

### Logs Específicos
- **Flyway**: Execução de migrações de banco
- **Relatórios**: Geração de arquivos Excel
- **Transações**: Operações de vendas e desconto

### Formato do Log
```json
{
  "timestamp": "2024-01-01T10:00:00",
  "level": "INFO",
  "logger": "com.granja.service.DuckService",
  "message": "Criando novo pato com nome: Donald",
  "thread": "http-nio-8080-exec-1"
}
```

## 🚀 Deploy

### Deploy com Docker
```bash
# Construir imagem
docker build -t duck-farm-api .

# Executar container
docker run -p 8080:8080 duck-farm-api
```

### Deploy com Flyway
```bash
# Inicializar banco automaticamente
./init-database.sh  # Linux/Mac
# ou
init-database.bat   # Windows

# Verificar status das migrações
mvn flyway:info

# Executar migrações pendentes
mvn flyway:migrate

# Limpar banco (desenvolvimento)
mvn flyway:clean
```

### Configuração de Produção
```yaml
spring:
  profiles: production
  datasource:
    url: ${DATABASE_URL}
    username: ${DATABASE_USERNAME}
    password: ${DATABASE_PASSWORD}
  
jwt:
  secret: ${JWT_SECRET}
  expiration: ${JWT_EXPIRATION}
```

## 🤝 Contribuindo

### Configuração de Desenvolvimento
1. Faça um fork do repositório
2. Clone o repositório: `git clone <seu-fork>`
3. **Inicialize o banco automaticamente:**
   ```bash
   # Windows
   init-database.bat
   
   # Linux/Mac
   ./init-database.sh
   ```
4. Crie uma branch para sua feature: `git checkout -b feature/feature-incrivel`
5. Commit suas mudanças: `git commit -m 'Adicionar feature incrível'`
6. Push para a branch: `git push origin feature/feature-incrivel`
7. Abra um Pull Request

### Desenvolvimento com Flyway
- **Nunca altere** migrações já executadas
- **Sempre incremente** o número da versão (V1, V2, V3...)
- **Use transações** para operações complexas
- **Adicione comentários** explicativos nas migrações
- **Teste** antes de commitar

### Padrões de Código
- Siga os princípios SOLID
- Escreva testes unitários abrangentes
- Use mensagens de commit significativas
- Atualize a documentação para novas features
- Siga as convenções de nomenclatura Java
- **Documentação em Português** para todos os comentários Javadoc
- **Tradução de mensagens** de log e erro para português

## 📄 Licença

Este projeto está licenciado sob a Licença MIT - veja o arquivo [LICENSE](LICENSE) para detalhes.

## 🆘 Suporte

- **Documentação**: [Docs da API](http://localhost:8080/swagger-ui.html)
- **Swagger UI**: [Interface Interativa](http://localhost:8080/swagger-ui.html)
- **Documentação Técnica**: Pasta [`doc/`](doc/) com guias especializados
- **Collection Postman**: [`doc/Granja_Patos_API.postman_collection.json`](doc/Granja_Patos_API.postman_collection.json)
- **Issues**: [GitHub Issues](https://github.com/VictorRochaSilva/Desafio-Tecnico-da-Preco-Justo/issues)
- **Email**: victowrs.rocha@gmail.com

## 🙏 Agradecimentos

- Equipe do Spring Boot pelo excelente framework
- Comunidade PostgreSQL pelo banco de dados robusto
- Equipe do Flyway pelas migrações de banco versionadas
- Apache POI pela geração profissional de arquivos Excel
- Contribuidores open source pelas várias bibliotecas

---

**Construído com ❤️ por [Victor Rocha Silva](https://github.com/VictorRochaSilva)**

---

## 🔗 **Links do Projeto**

- **📁 Repositório**: [https://github.com/VictorRochaSilva/Desafio-Tecnico-da-Preco-Justo](https://github.com/VictorRochaSilva/Desafio-Tecnico-da-Preco-Justo)
- **👨‍💻 Desenvolvedor**: [https://github.com/VictorRochaSilva](https://github.com/VictorRochaSilva)
- **📧 Contato**: victowrs.rocha@gmail.com
