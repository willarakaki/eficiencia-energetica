# language: pt
@regressivo
Funcionalidade: Consulta de registros de consumo de energia
  Como analista ESG
  Quero consultar os dados de consumo cadastrados
  Para analisar o desempenho energético da empresa

  Contexto: Cadastro prévio de medição de energia
    Dado que eu tenha os seguintes dados de consumo:
      | campo           | valor             |
      | nomeEquipamento | Prensa Hidráulica |
      | setor           | Fábrica           |
      | consumoKwh      | 540.00            |
      | dataRegistro    | 2026-11-03        |
    Quando eu enviar a requisição para o endpoint "/consumo-energia" de cadastro de consumo
    Então o status code da resposta deve ser 201

  Cenário: Listar todos os registros de consumo
    Quando eu enviar uma requisição GET para o endpoint "/consumo-energia" de listagem
    Então o status code da resposta deve ser 200
    E a lista de consumos não deve estar vazia

  Cenário: Consultar consumo por ID com sucesso
    Dado que eu recupere o ID do consumo criado no contexto
    Quando eu enviar uma requisição GET com o ID para o endpoint "/consumo-energia" de busca por ID
    Então o status code da resposta deve ser 200
    E o campo "nomeEquipamento" da resposta deve ser "Prensa Hidráulica"

  Cenário: Consultar consumo com ID inexistente
    Dado que eu informe o ID de consumo inexistente "999999"
    Quando eu enviar uma requisição GET com o ID para o endpoint "/consumo-energia" de busca por ID
    Então o status code da resposta deve ser 404
