#!/usr/bin/env bash
# ==============================================================================
# Script de Build e Execução de Testes Local (Bash/Linux/Mac)
# ==============================================================================
set -e

echo "Iniciando compilação e testes automatizados..."

chmod +x mvnw
./mvnw test

./mvnw clean package -DskipTests

echo "Construindo imagem Docker..."
docker build -t eficiencia-energetica:latest -t eficiencia-energetica:staging -t eficiencia-energetica:production .

echo "Build e Containerização concluídos com sucesso!"
