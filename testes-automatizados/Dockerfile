FROM maven:3.9.6-eclipse-temurin-21

WORKDIR /app

# Copia as dependências primeiro para aproveitar cache de build
COPY pom.xml .

# Baixa as dependências
RUN mvn dependency:go-offline -B || true

# Copia o código-fonte de testes e recursos
COPY src ./src

# Comando padrão de execução dos testes
CMD ["mvn", "clean", "test"]
