# ==============================================================================
# Dockerfile Multi-Stage Build: Eficiência Energética - Cidades ESGInteligentes
# ==============================================================================

# ------------------------------------------------------------------------------
# Estágio 1: Compilação e Empacotamento (Maven + JDK 21)
# ------------------------------------------------------------------------------
FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /build

# Aproveitamento do cache de camadas do Docker para dependências
COPY pom.xml .
RUN mvn dependency:go-offline -B || true

# Copia o código-fonte da aplicação
COPY src ./src

# Executa o empacotamento da aplicação sem executar os testes (já executados no CI)
RUN mvn clean package -DskipTests -B

# ------------------------------------------------------------------------------
# Estágio 2: Ambiente de Execução (JRE 21 Otimizado e Seguro)
# ------------------------------------------------------------------------------
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# Cria usuário e grupo não-root para execução segura do container
RUN groupadd -r esggroup && useradd -r -g esggroup esguser

# Copia apenas o binário executável gerado no estágio anterior
COPY --from=builder /build/target/*.jar app.jar

# Ajusta as permissões para o usuário não privilegiado
RUN chown -R esguser:esggroup /app

USER esguser

# Expõe a porta padrão da aplicação Spring Boot
EXPOSE 8080

# Parâmetros otimizados de execução da JVM
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]