# language: pt
@regressivo
Funcionalidade: Autenticação e gestão de usuários
  Como administrador do sistema
  Quero autenticar e autorizar acessos à API
  Para assegurar a governança e proteção de dados ESG

  Cenário: Registro de novo usuário bem-sucedido
    Dado que eu informe os seguintes dados para novo usuário:
      | campo | valor                     |
      | nome  | Auditor ESG               |
      | email | auditor.temp@fiap.com     |
      | senha | Senha@123                 |
      | role  | ADMIN                     |
    Quando eu enviar a requisição de autenticação para o endpoint "/auth/registrar"
    Então o status code da resposta de autenticação deve ser 200

  Cenário: Login com credenciais válidas
    Dado que eu tenha credenciais válidas de login
    Quando eu enviar a requisição de autenticação para o endpoint "/auth/login"
    Então o status code da resposta de autenticação deve ser 200
    E a resposta deve conter o token JWT de acesso

  Cenário: Login com credenciais inválidas
    Dado que eu informe credenciais inválidas de email "invalido@fiap.com" e senha "errada"
    Quando eu enviar a requisição de autenticação para o endpoint "/auth/login"
    Então o status code da resposta de autenticação deve ser 403
