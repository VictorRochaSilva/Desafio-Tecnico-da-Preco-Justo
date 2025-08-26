#!/bin/bash

echo "========================================"
echo "    INICIALIZADOR DO BANCO DE DADOS"
echo "========================================"
echo

echo "Verificando se o Docker está rodando..."
if ! docker info > /dev/null 2>&1; then
    echo "ERRO: Docker não está rodando!"
    echo "Inicie o Docker e tente novamente."
    exit 1
fi

echo "Docker está rodando. Verificando container PostgreSQL..."
if ! docker ps | grep -q "duck_farm_db"; then
    echo "Iniciando container PostgreSQL..."
    docker-compose up -d
    sleep 5
fi

echo "Verificando se o banco 'duck_farm' existe..."
if ! docker exec duck_farm_db psql -U postgres -lqt | grep -q "duck_farm"; then
    echo "Criando banco 'duck_farm'..."
    if docker exec duck_farm_db psql -U postgres -c "CREATE DATABASE duck_farm;"; then
        echo "Banco 'duck_farm' criado com sucesso!"
    else
        echo "ERRO: Falha ao criar o banco!"
        exit 1
    fi
else
    echo "Banco 'duck_farm' já existe!"
fi

echo
echo "========================================"
echo "    BANCO INICIALIZADO COM SUCESSO!"
echo "========================================"
echo
echo "Agora você pode executar: ./mvnw spring-boot:run"
echo
