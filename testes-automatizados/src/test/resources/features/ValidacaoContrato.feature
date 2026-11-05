# language: pt
@regressivo
Funcionalidade: Validar o contrato JSON Schema das APIs de consumo
  Como desenvolvedor integrador
  Quero garantir a conformidade dos esquemas de resposta da API
  Para evitar quebras de integração entre microsserviços

  Cenário: Validar contrato do cadastro bem-sucedido de consumo
    Dado que eu tenha os seguintes dados de consumo:
      | campo           | valor                  |
      | nomeEquipamento | Compressor Inteligente |
      | setor           | Manutenção             |
      | consumoKwh      | 120.30                 |
      | dataRegistro    | 2026-11-03             |
    Quando eu enviar a requisição para o endpoint "/consumo-energia" de cadastro de consumo
    Então o status code da resposta deve ser 201
    E que o arquivo de contrato esperado é o "Cadastro bem-sucedido de consumo"
    Então a resposta da requisição deve estar em conformidade com o contrato selecionado

  Cenário: Validar contrato da autenticação e login de usuário
    Dado que eu tenha credenciais válidas de login
    Quando eu enviar a requisição de autenticação para o endpoint "/auth/login"
    Então o status code da resposta de autenticação deve ser 200
    E que o arquivo de contrato esperado para autenticação é o "Login bem-sucedido"
    Então a resposta da autenticação deve estar em conformidade com o contrato selecionado
