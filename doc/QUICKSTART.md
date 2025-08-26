# 🚀 **QUICKSTART - Granja de Patos API**

## ⚡ **Início Rápido em 5 Minutos**

### **📋 Pré-requisitos**
- ✅ Java 17+ instalado
- ✅ Docker Desktop rodando
- ✅ Maven (opcional - projeto usa Maven Wrapper)

### **🚀 Opção 1: Docker Compose (Mais Fácil)**

#### **Setup automático:**
```bash
# 1. Subir banco de dados
docker-compose up -d postgres

# 2. Aguardar 5-10 segundos para banco inicializar
# 3. Executar migrações
./mvnw flyway:migrate

# 4. Rodar aplicação
./mvnw spring-boot:run
```

#### **Setup completo (banco + app):**
```bash
# Tudo em um comando
docker-compose --profile full-stack up -d

# API rodando em http://localhost:8080
# Swagger em http://localhost:8080/swagger-ui.html
```

### **🔧 Opção 2: Setup Manual**

#### **1. Clone e Entre no Projeto**
```bash
git clone <url-do-repositorio>
cd granja-patos
```

#### **2. Configure o Banco de Dados**
```bash
# Verificar se o Docker está rodando
docker ps

# Criar container PostgreSQL (se não existir)
docker run --name duck_farm_db -e POSTGRES_DB=duck_farm -e POSTGRES_PASSWORD=postgres -p 5432:5432 -d postgres:15
```

#### **3. Execute as Migrações**
```bash
# Executar migrações Flyway
./mvnw flyway:migrate

# Verificar status
./mvnw flyway:info
```

#### **4. Execute a Aplicação**
```bash
# Rodar com Maven Wrapper
./mvnw spring-boot:run

# Ou com Maven instalado
mvn spring-boot:run
```

#### **5. Teste a API**
```bash
# Health check
curl http://localhost:8080/actuator/health

# Swagger UI
http://localhost:8080/swagger-ui.html
```

### **🎯 Endpoints Principais**

#### **Autenticação (sem JWT)**
- `POST /api/auth/users/create` - Criar usuário
- `POST /api/auth/login` - Fazer login

#### **Patos (com JWT)**
- `GET /api/ducks` - Listar patos
- `POST /api/ducks` - Criar pato
- `GET /api/ducks/{id}` - Buscar pato
- `PUT /api/ducks/{id}` - Atualizar pato
- `DELETE /api/ducks/{id}` - Deletar pato

### **🔑 Fluxo de Autenticação**

#### **1. Criar Usuário**
```json
POST /api/auth/users/create
{
  "username": "admin",
  "password": "admin123",
  "name": "Administrador",
  "role": "ADMIN"
}
```

#### **2. Fazer Login**
```json
POST /api/auth/login
{
  "username": "admin",
  "password": "admin123"
}
```

#### **3. Usar JWT nas Requisições**
```bash
# Adicionar header Authorization
Authorization: Bearer <jwt-token>
```

### **🐳 Comandos Docker Úteis**

```bash
# Ver containers rodando
docker ps

# Ver logs do PostgreSQL
docker logs duck_farm_db

# Conectar no banco
docker exec -it duck_farm_db psql -U postgres -d duck_farm

# Parar container
docker stop duck_farm_db

# Remover container
docker rm duck_farm_db
```

### **📊 Comandos Maven Úteis**

```bash
# Limpar e compilar
./mvnw clean compile

# Executar testes
./mvnw test

# Executar migrações
./mvnw flyway:migrate

# Ver status das migrações
./mvnw flyway:info

# Limpar banco (cuidado!)
./mvnw flyway:clean

# Rodar aplicação
./mvnw spring-boot:run
```

### **🚨 Solução de Problemas**

#### **Erro de Conexão com Banco**
```bash
# Verificar se Docker está rodando
docker ps

# Verificar se container existe
docker ps -a

# Recriar container se necessário
docker stop duck_farm_db
docker rm duck_farm_db
docker run --name duck_farm_db -e POSTGRES_DB=duck_farm -e POSTGRES_PASSWORD=postgres -p 5432:5432 -d postgres:15
```

#### **Erro de Migração Flyway**
```bash
# Verificar configurações no pom.xml
# Verificar se banco está rodando
# Executar flyway:info para ver status
./mvnw flyway:info
```

#### **Erro de Porta em Uso**
```bash
# Verificar se porta 8080 está livre
netstat -an | findstr 8080

# Parar aplicação (Ctrl+C)
# Ou mudar porta no application.yml
```

### **🎉 Pronto!**

Sua API está rodando em `http://localhost:8080` com:
- ✅ **Banco PostgreSQL** configurado
- ✅ **Migrações Flyway** executadas
- ✅ **JWT** funcionando
- ✅ **Swagger** documentando
- ✅ **Endpoints** protegidos

**Agora é só usar!** 🚀✨
