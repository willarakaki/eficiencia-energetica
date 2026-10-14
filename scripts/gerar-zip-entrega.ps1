# ==============================================================================
# Script para Gerar o Pacote .ZIP de Entrega da Atividade Avaliativa
# ==============================================================================
$ErrorActionPreference = "Stop"

$destinationZip = "entrega-cidades-esg-devops.zip"
$tempFolder = "cidades-esg-inteligentes-devops"

Write-Host "Preparando arquivos para o pacote de entrega..." -ForegroundColor Cyan

# Remove arquivos anteriores se existirem
if (Test-Path $destinationZip) { Remove-Item $destinationZip -Force }
if (Test-Path $tempFolder) { Remove-Item $tempFolder -Recurse -Force }

# Cria pasta temporária
New-Item -ItemType Directory -Path $tempFolder | Out-Null

# Lista de arquivos e pastas para inclusão
$itemsToCopy = @(
    "Dockerfile",
    "docker-compose.yml",
    "docker-compose.staging.yml",
    "docker-compose.prod.yml",
    ".env.example",
    ".env",
    "pom.xml",
    "mvnw",
    "mvnw.cmd",
    "README.md",
    "DOCUMENTACAO.md",
    "src",
    ".mvn",
    "k8s",
    "scripts",
    ".github"
)

foreach ($item in $itemsToCopy) {
    if (Test-Path $item) {
        Copy-Item -Path $item -Destination $tempFolder -Recurse -Force
        Write-Host "  [+] Incluído: $item" -ForegroundColor Green
    }
}

# Compacta no arquivo ZIP final
Write-Host "Compactando em $destinationZip..." -ForegroundColor Cyan
Compress-Archive -Path "$tempFolder\*" -DestinationPath $destinationZip -Force

# Remove pasta temporária
Remove-Item $tempFolder -Recurse -Force

Write-Host "==========================================================" -ForegroundColor Green
Write-Host "Arquivo de entrega gerado com sucesso: $destinationZip" -ForegroundColor Green
Write-Host "Tamanho: $((Get-Item $destinationZip).Length / 1KB) KB" -ForegroundColor Cyan
Write-Host "==========================================================" -ForegroundColor Green
