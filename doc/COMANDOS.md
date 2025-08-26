# 📋 **COMANDOS ÚTEIS - Granja de Patos API**

## 🐳 **Docker - Banco de Dados**

### **Verificar Status**
```bash
# Ver containers rodando
docker ps

# Ver todos os containers (incluindo parados)
docker ps -a

# Ver logs do PostgreSQL
docker logs duck_farm_db

# Ver uso de recursos
docker stats duck_farm_db
```

### **Gerenciar Container**
```bash
# Criar container PostgreSQL
docker run --name duck_farm_db -e POSTGRES_DB=duck_farm -e POSTGRES_PASSWORD=postgres -p 5432:5432 -d postgres:15

# Parar container
docker stop duck_farm_db

# Iniciar container parado
docker start duck_farm_db

# Remover container
docker rm duck_farm_db

# Recriar container (parar + remover + criar)
docker stop duck_farm_db && docker rm duck_farm_db && docker run --name duck_farm_db -e POSTGRES_DB=duck_farm -e POSTGRES_PASSWORD=postgres -p 5432:5432 -d postgres:15
```

### **Conectar no Banco**
```bash
# Conectar como postgres
docker exec -it duck_farm_db psql -U postgres

# Conectar direto no banco duck_farm
docker exec -it duck_farm_db psql -U postgres -d duck_farm

# Listar databases
docker exec -it duck_farm_db psql -U postgres -c "\l"

# Listar tabelas
docker exec -it duck_farm_db psql -U postgres -d duck_farm -c "\dt"
```

## 📊 **Maven - Build e Execução**

### **Build e Compilação**
```bash
# Limpar e compilar
./mvnw clean compile

# Limpar, compilar e empacotar
./mvnw clean package

# Instalar no repositório local
./mvnw clean install

# Ver dependências
./mvnw dependency:tree
```

### **Executar Aplicação**
```bash
# Rodar com Maven Wrapper
./mvnw spring-boot:run

# Rodar com Maven instalado
mvn spring-boot:run

# Rodar em background
./mvnw spring-boot:run &
```

### **Testes**
```bash
# Executar todos os testes
./mvnw test

# Executar testes específicos
./mvnw test -Dtest=DuckServiceTest
./mvnw test -Dtest=*ServiceTest

# Executar com cobertura
./mvnw jacoco:report

# Executar testes de integração
./mvnw verify
```

## 🗄️ **Flyway - Migrações de Banco**

### **Executar Migrações**
```bash
# Executar migrações pendentes
./mvnw flyway:migrate

# Ver status das migrações
./mvnw flyway:info

# Ver histórico detalhado
./mvnw flyway:info -X

# Validar migrações
./mvnw flyway:validate
```

### **Manutenção**
```bash
# Limpar banco (CUIDADO - apaga tudo!)
./mvnw flyway:clean

# Reparar migrações corrompidas
./mvnw flyway:repair

# Fazer baseline (para bancos existentes)
./mvnw flyway:baseline

# Desfazer última migração
./mvnw flyway:undo
```

### **Informações**
```bash
# Ver versão do Flyway
./mvnw flyway:version

# Ver ajuda
./mvnw flyway:help

# Ver configurações
./mvnw flyway:info -X
```

## 🌐 **API - Testes e Verificação**

### **Health Checks**
```bash
# Health check básico
curl http://localhost:8080/actuator/health

# Health check detalhado
curl http://localhost:8080/actuator/health -v

# Health check específico
curl http://localhost:8080/actuator/health/db
```

### **Swagger e Documentação**
```bash
# Abrir Swagger UI
start http://localhost:8080/swagger-ui.html

# Abrir OpenAPI JSON
curl http://localhost:8080/v3/api-docs

# Abrir OpenAPI YAML
curl http://localhost:8080/v3/api-docs.yaml
```

### **Testes de Endpoints**
```bash
# Testar endpoint público
curl http://localhost:8080/api/auth/users/create

# Testar com JWT
curl -H "Authorization: Bearer SEU_JWT" http://localhost:8080/api/ducks

# Testar com dados
curl -X POST http://localhost:8080/api/ducks \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer SEU_JWT" \
  -d '{"name":"Pato Teste","breed":"Pekin","age":2}'
```

## 🔧 **Sistema - Verificações**

### **Portas e Processos**
```bash
# Verificar porta 8080
netstat -an | findstr 8080

# Verificar porta 5432
netstat -an | findstr 5432

# Ver processos Java
jps -l

# Ver uso de memória
jstat -gc <pid>
```

### **Logs e Debug**
```bash
# Ver logs da aplicação (se rodando)
# Os logs aparecem no terminal onde executou ./mvnw spring-boot:run

# Ver logs do Docker
docker logs duck_farm_db

# Ver logs com timestamp
docker logs -t duck_farm_db

# Seguir logs em tempo real
docker logs -f duck_farm_db
```

## 🚨 **Solução de Problemas**

### **Problemas Comuns**
```bash
# Erro: "Port already in use"
netstat -ano | findstr 8080
taskkill /PID <PID> /F

# Erro: "Database connection failed"
docker ps
docker logs duck_farm_db

# Erro: "Flyway migration failed"
./mvnw flyway:info
./mvnw flyway:repair

# Erro: "JWT invalid"
# Verificar se token não expirou
# Fazer login novamente
```

### **Reset Completo**
```bash
# Parar aplicação (Ctrl+C)
# Parar e remover container
docker stop duck_farm_db && docker rm duck_farm_db

# Recriar container
docker run --name duck_farm_db -e POSTGRES_DB=duck_farm -e POSTGRES_PASSWORD=postgres -p 5432:5432 -d postgres:15

# Executar migrações
./mvnw flyway:migrate

# Rodar aplicação
./mvnw spring-boot:run
```

## 📚 **Comandos Úteis do PostgreSQL**

### **Dentro do psql**
```sql
-- Listar databases
\l

-- Conectar em database
\c duck_farm

-- Listar tabelas
\dt

-- Ver estrutura da tabela
\d ducks

-- Ver dados
SELECT * FROM ducks LIMIT 5;

-- Ver usuários
SELECT * FROM users;

-- Sair
\q
```

## 🎯 **Fluxo de Desenvolvimento**

### **Desenvolvimento Diário**
```bash
# 1. Verificar se Docker está rodando
docker ps

# 2. Executar migrações (se houver mudanças)
./mvnw flyway:migrate

# 3. Rodar aplicação
./mvnw spring-boot:run

# 4. Testar endpoints
# Usar Swagger ou Postman

# 5. Parar aplicação (Ctrl+C)
```

### **Deploy/Produção**
```bash
# 1. Build da aplicação
./mvnw clean package

# 2. Executar migrações
./mvnw flyway:migrate

# 3. Rodar JAR
java -jar target/granja-patos-1.0.0.jar
```

---

**💡 Dica:** Use `./mvnw` em vez de `mvn` para garantir que está usando a versão correta do Maven!
