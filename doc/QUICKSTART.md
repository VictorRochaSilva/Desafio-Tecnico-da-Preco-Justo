# 🚀 Início Rápido - Granja de Patos API

**Configure e execute o projeto em 5 minutos!** ⚡

## 📋 **Pré-requisitos**

- ✅ **Java 17** ou superior
- ✅ **Docker Desktop** instalado e rodando
- ✅ **Git** para clonar o repositório

## 🚀 **Passo a Passo Rápido**

### **1. Clone o Repositório**
```bash
git clone <url-do-repositorio>
cd preco-justo
```

### **2. Inicialize o Banco Automaticamente** 🎯
```bash
# Windows (duplo clique ou executar como admin)
init-database.bat

# Linux/Mac
chmod +x init-database.sh
./init-database.sh
```

**✨ Este script faz TUDO automaticamente:**
- Verifica se o Docker está rodando
- Inicia o PostgreSQL
- Cria o banco `duck_farm`
- Prepara para o Flyway

### **3. Execute a Aplicação**
```bash
# Usando Maven Wrapper (não precisa instalar Maven)
./mvnw spring-boot:run
```

### **4. Acesse a API**
- 🌐 **URL Base**: http://localhost:8080
- 📚 **Swagger UI**: http://localhost:8080/swagger-ui.html
- 🔍 **Health Check**: http://localhost:8080/api/health

## 🔐 **Primeiro Acesso**

### **1. Criar Usuário Admin**
```bash
curl -X POST "http://localhost:8080/api/auth/users/create?password=admin123" \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","name":"Administrador","role":"ADMIN"}'
```

### **2. Fazer Login**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```

### **3. Usar a API**
```bash
# Incluir token no cabeçalho
curl -X GET http://localhost:8080/api/ducks \
  -H "Authorization: Bearer SEU_TOKEN_JWT"
```

## 🧪 **Teste Rápido**

### **Importar Collection do Postman**
1. Abra o Postman
2. Importe: `doc/Granja_Patos_API.postman_collection.json`
3. Configure a variável `base_url` como `http://localhost:8080`
4. Execute o endpoint "Login" para obter o token
5. Teste os outros endpoints!

## 🆘 **Problemas Comuns**

### **Erro: "database duck_farm does not exist"**
```bash
# Execute o script de inicialização
./init-database.sh  # Linux/Mac
# ou
init-database.bat   # Windows
```

### **Erro: "Docker not running"**
- Inicie o Docker Desktop
- Aguarde o ícone ficar verde
- Execute o script novamente

### **Erro: "Port 8080 already in use"**
```bash
# Pare outros serviços na porta 8080
# Ou mude a porta no application.yml
server:
  port: 8081
```

## 📚 **Próximos Passos**

- 📖 **Leia o README principal** para entender a arquitetura
- 🗄️ **Consulte o FLYWAY_GUIDE.md** para migrações de banco
- 🔌 **Use a collection do Postman** para testar todos os endpoints
- 📊 **Teste os relatórios Excel** em `/api/reports/sales`

## 🎯 **O que foi criado automaticamente?**

- 🐘 **PostgreSQL** rodando na porta 5432
- 🗄️ **Banco `duck_farm`** com todas as tabelas
- 📊 **Dados de exemplo** (usuários, patos, clientes, vendedores)
- 🔐 **Usuário admin** pronto para uso
- 📈 **Relatórios Excel** funcionando
- 🚀 **API completa** com autenticação JWT

---

**🎉 Parabéns! Sua API está rodando e pronta para uso!**
