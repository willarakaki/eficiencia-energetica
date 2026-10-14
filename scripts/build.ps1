# ==============================================================================
# Script de Build e Execução de Testes Local (PowerShell)
# ==============================================================================
Write-Host "Iniciando compilação e testes automatizados..." -ForegroundColor Cyan

# Executar testes
./mvnw.cmd test
if ($LASTEXITCODE -ne 0) {
    Write-Host "ERRO: Os testes falharam!" -ForegroundColor Red
    exit 1
}

# Gerar pacote
./mvnw.cmd clean package -DskipTests
if ($LASTEXITCODE -ne 0) {
    Write-Host "ERRO: O build falhou!" -ForegroundColor Red
    exit 1
}

# Construir imagem Docker
Write-Host "Construindo imagem Docker..." -ForegroundColor Cyan
docker build -t eficiencia-energetica:latest -t eficiencia-energetica:staging -t eficiencia-energetica:production .

Write-Host "Build e Containerização concluídos com sucesso!" -ForegroundColor Green
