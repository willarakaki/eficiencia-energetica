@echo off
echo ========================================================
echo Executando Testes Automatizados BDD - FIAP ESG
echo ========================================================
where mvn >nul 2>nul
if %ERRORLEVEL% EQU 0 (
    mvn clean test
) else (
    mvnw.cmd clean test
)
pause
