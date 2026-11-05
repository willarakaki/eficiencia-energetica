# 🧪 Projeto de Testes Automatizados BDD - Eficiência Energética (ESG)

> **Atividade Avaliativa:** Fase "Testes Automatizados" - FIAP ON  
> **Tema ESG:** Eficiência Energética e Sustentabilidade Computacional  
> **Frameworks:** Cucumber BDD (Gherkin), RestAssured, JUnit, Google Gson, JSON Schema Validator (NetworkNT)  
> **Linguagem & Plataforma:** Java 21 / Maven  

---

## 📋 Descrição do Projeto

Este projeto implementa a suíte completa de **Testes Automatizados de API** para o microsserviço de **Eficiência Energética (ESG)**.

A aplicação testada gerencia e audita o consumo elétrico de equipamentos críticos corporativos e industriais, garantindo metas de eficiência energética, redução da pegada de carbono e conformidade com critérios ESG.

---

## 🏗️ Arquitetura da Automação

O projeto segue o padrão arquitetural em camadas ensinado no material:

```
testes-automatizados/
├── pom.xml                                   # Gerenciamento de dependências Maven e Surefire
├── Dockerfile                                # Containerização dos testes automatizados
├── run-tests.bat                             # Script de execução rápida para Windows
├── run-tests.sh                              # Script de execução rápida para Linux/macOS
├── .github/workflows/ci.yml                  # Pipeline de Integração Contínua (GitHub Actions)
└── src/test/
    ├── java/
    │   ├── hook/
    │   │   └── Hook.java                     # Hooks de ciclo de vida (@BeforeAll, @AfterAll, etc.)
    │   ├── model/
    │   │   ├── ConsumoEnergiaModel.java      # DTO de medição com @Data e @Expose
    │   │   ├── UsuarioModel.java             # DTO de usuário
    │   │   ├── LoginModel.java               # DTO de login
    │   │   ├── TokenModel.java               # DTO de token JWT
    │   │   └── ErrorMessageModel.java        # DTO de respostas de erro
    │   ├── services/
    │   │   ├── ConsumoEnergiaService.java    # Chamadas HTTP (RestAssured), Gson e JSON Schema
    │   │   └── AutenticacaoService.java      # Gestão de token e endpoints /auth
    │   ├── steps/
    │   │   ├── ConsumoEnergiaSteps.java      # Step Definitions Gherkin para Consumo
    │   │   └── AutenticacaoSteps.java        # Step Definitions Gherkin para Autenticação
    │   └── runner/
    │       └── TestRunner.java               # Classe executora Cucumber com JUnit
    └── resources/
        ├── features/
        │   ├── CadastroConsumo.feature       # Cenários positivo e negativo de cadastro (POST)
        │   ├── ConsultaConsumo.feature       # Cenários de listagem geral e busca por ID (GET)
        │   ├── DelecaoConsumo.feature        # Cenários de exclusão com Contexto dinâmico (DELETE)
        │   ├── ValidacaoContrato.feature     # Testes de contrato JSON Schema Draft-07
        │   └── Autenticacao.feature          # Cenários de registro e login de usuários
        └── schemas/
            ├── cadastro-consumo-schema.json  # Esquema JSON Schema para cadastro
            ├── consulta-consumo-schema.json  # Esquema JSON Schema para consulta por ID
            └── login-schema.json             # Esquema JSON Schema para emissão de JWT
```

---

## 🚀 Como Executar os Testes

### Pré-requisitos
1. **JDK 21** ou superior instalado e configurado no `PATH`.
2. **Apache Maven 3.9+** instalado.
3. A aplicação backend `eficiencia-energetica` deve estar em execução em `http://localhost:8080` (via Docker Compose ou Maven).

> **Para subir a aplicação backend com Docker:**
> ```bash
> cd ..
> docker compose up -d --build
> ```

---

### Opção 1: Execução pela Linha de Comando (Maven)

Navegue até a pasta `testes-automatizados` e execute:

```bash
mvn clean test
```

Ou utilize os scripts inclusos:
* **Windows:** dê dois cliques em `run-tests.bat`
* **Linux/macOS:** `./run-tests.sh`

O Maven acionará o `maven-surefire-plugin`, que por sua vez executará a classe `TestRunner`.

---

### Opção 2: Execução pelo IntelliJ IDEA

1. Abra a pasta `testes-automatizados` no IntelliJ IDEA.
2. Certifique-se de que os plugins **Cucumber for Java** e **Lombok** estão habilitados (`File > Settings > Plugins`).
3. Navegue até o arquivo `src/test/java/runner/TestRunner.java`.
4. Clique no ícone verde de **Play** ao lado do nome da classe e selecione **"Run 'TestRunner'"**.
5. Alternativamente, você pode abrir qualquer arquivo `.feature` dentro de `src/test/resources/features/` e clicar no Play ao lado do Cenário desejado.

---

### Opção 3: Execução via Docker

Para construir e executar os testes em um container isolado:

```bash
docker build -t testes-automatizados-esg .
docker run --rm --network="host" testes-automatizados-esg
```

---

## 📊 Relatório de Execução dos Testes

Ao finalizar a execução dos testes via `TestRunner` ou `mvn clean test`, o Cucumber gera automaticamente um relatório visual completo em HTML:

* **Caminho do relatório:** `target/cucumber-reports.html`

Abra este arquivo em qualquer navegador web para visualizar o status de cada cenário, tempos de resposta e evidências de validação.

---

## 🛡️ Pipeline de CI/CD (GitHub Actions)

O projeto inclui o workflow `.github/workflows/ci.yml` configurado para executar automaticamente a suíte de testes em cada `push` ou `pull_request`:

* Ambiente: `ubuntu-latest`
* JDK: `21` (Temurin)
* Comando de build e teste: `mvn clean test`
