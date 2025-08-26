# Multi-stage build para otimizar o tamanho da imagem
FROM maven:3.9.6-eclipse-temurin-17 AS build

# Definir diretório de trabalho
WORKDIR /app

# Copiar arquivos de dependências primeiro (para cache)
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .

# Baixar dependências
RUN mvn dependency:go-offline -B

# Copiar código fonte
COPY src src

# Compilar e empacotar
RUN mvn clean package -DskipTests

# Imagem de produção
FROM eclipse-temurin:17-jre-alpine

# Criar usuário não-root
RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup

# Definir diretório de trabalho
WORKDIR /app

# Copiar JAR da aplicação
COPY --from=build /app/target/*.jar app.jar

# Mudar proprietário dos arquivos
RUN chown -R appuser:appgroup /app

# Mudar para usuário não-root
USER appuser

# Expor porta
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Comando para executar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
