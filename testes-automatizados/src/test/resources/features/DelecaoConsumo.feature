# language: pt
@regressivo
Funcionalidade: Deletar um registro de consumo de energia
  Como gestor de eficiência energética
  Quero excluir uma medição incorreta
  Para manter a confiabilidade dos relatórios ESG

  Contexto: Cadastro prévio de consumo para teste
    Dado que eu tenha os seguintes dados de consumo:
      | campo           | valor               |
      | nomeEquipamento | Ar Condicionado Q01 |
      | setor           | Operações           |
      | consumoKwh      | 85.50               |
      | dataRegistro    | 2026-11-03          |
    Quando eu enviar a requisição para o endpoint "/consumo-energia" de cadastro de consumo
    Então o status code da resposta deve ser 201

  Cenário: Deve ser possível deletar um registro de consumo
    Dado que eu recupere o ID do consumo criado no contexto
    Quando eu enviar a requisição com o ID para o endpoint "/consumo-energia" de deleção de consumo
    Então o status code da resposta deve ser 204

  Cenário: Falha ao deletar registro de consumo com ID inexistente
    Dado que eu informe o ID de consumo inexistente "999999"
    Quando eu enviar a requisição com o ID para o endpoint "/consumo-energia" de deleção de consumo
    Então o status code da resposta deve ser 404
