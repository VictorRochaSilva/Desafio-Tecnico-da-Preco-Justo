@echo off
echo ========================================
echo    INICIALIZADOR DO BANCO DE DADOS
echo ========================================
echo.

echo Verificando se o Docker está rodando...
docker ps >nul 2>&1
if %errorlevel% neq 0 (
    echo ERRO: Docker não está rodando!
    echo Inicie o Docker Desktop e tente novamente.
    pause
    exit /b 1
)

echo Docker está rodando. Verificando container PostgreSQL...
docker ps | findstr "duck_farm_db" >nul 2>&1
if %errorlevel% neq 0 (
    echo Iniciando container PostgreSQL...
    docker-compose up -d
    timeout /t 5 /nobreak >nul
)

echo Verificando se o banco 'duck_farm' existe...
docker exec duck_farm_db psql -U postgres -lqt | findstr "duck_farm" >nul 2>&1
if %errorlevel% neq 0 (
    echo Criando banco 'duck_farm'...
    docker exec duck_farm_db psql -U postgres -c "CREATE DATABASE duck_farm;"
    if %errorlevel% equ 0 (
        echo Banco 'duck_farm' criado com sucesso!
    ) else (
        echo ERRO: Falha ao criar o banco!
        pause
        exit /b 1
    )
) else (
    echo Banco 'duck_farm' já existe!
)

echo.
echo ========================================
echo    BANCO INICIALIZADO COM SUCESSO!
echo ========================================
echo.
echo Agora você pode executar: ./mvnw spring-boot:run
echo.
pause
