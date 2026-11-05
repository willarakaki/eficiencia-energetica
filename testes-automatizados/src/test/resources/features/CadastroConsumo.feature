# language: pt
@regressivo
Funcionalidade: Cadastro de registro de consumo de energia
  Como gestor de sustentabilidade
  Quero registrar o consumo de equipamentos elétricos
  Para monitorar a eficiência energética e metas ESG

  Cenário: Cadastro bem-sucedido de consumo de energia
    Dado que eu tenha os seguintes dados de consumo:
      | campo           | valor             |
      | nomeEquipamento | Servidor Blade 01 |
      | setor           | TI / Data Center  |
      | consumoKwh      | 250.75            |
      | dataRegistro    | 2026-11-03        |
    Quando eu enviar a requisição para o endpoint "/consumo-energia" de cadastro de consumo
    Então o status code da resposta deve ser 201

  Cenário: Cadastro de consumo de energia sem sucesso ao passar consumo inválido
    Dado que eu tenha os seguintes dados de consumo:
      | campo           | valor             |
      | nomeEquipamento | Servidor Blade 01 |
      | setor           | TI / Data Center  |
      | consumoKwh      | -10.0             |
      | dataRegistro    | 2026-11-03        |
    Quando eu enviar a requisição para o endpoint "/consumo-energia" de cadastro de consumo
    Então o status code da resposta deve ser 400
    E o corpo de resposta de erro da api deve conter o campo "consumoKwh" com a mensagem "O consumo deve ser maior que zero"
