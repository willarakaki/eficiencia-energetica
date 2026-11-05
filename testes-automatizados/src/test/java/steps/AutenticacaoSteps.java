package steps;

import com.networknt.schema.ValidationMessage;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import org.junit.Assert;
import services.AutenticacaoService;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class AutenticacaoSteps {

    AutenticacaoService autenticacaoService = new AutenticacaoService();

    @Dado("que eu informe os seguintes dados para novo usuário:")
    public void queEuInformeOsSeguintesDadosParaNovoUsuario(List<Map<String, String>> rows) {
        for (Map<String, String> columns : rows) {
            String campo = columns.get("campo");
            String valor = columns.get("valor");
            // Se for e-mail temporário, adiciona sufixo único para evitar duplicação em reexecuções
            if ("email".equals(campo) && valor.contains("temp")) {
                valor = "user_" + UUID.randomUUID().toString().substring(0, 8) + "@fiap.com";
            }
            autenticacaoService.setFieldsUsuario(campo, valor);
        }
    }

    @Quando("eu enviar a requisição de autenticação para o endpoint {string}")
    public void euEnviarARequisicaoDeAutenticacaoParaOEndpoint(String endPoint) {
        if (endPoint.contains("registrar")) {
            autenticacaoService.registrarUsuario(endPoint);
        } else {
            autenticacaoService.login(endPoint);
        }
    }

    @Dado("que eu tenha credenciais válidas de login")
    public void queEuTenhaCredenciaisValidasDeLogin() {
        // Garante que o usuário padrão exista no banco
        autenticacaoService.autenticarUsuarioPadrao();
        autenticacaoService.setFieldsLogin("email", "admin.esg@fiap.com.br");
        autenticacaoService.setFieldsLogin("senha", "Admin@123");
    }

    @Dado("que eu informe credenciais inválidas de email {string} e senha {string}")
    public void queEuInformeCredenciaisInvalidasDeEmailESenha(String email, String senha) {
        autenticacaoService.setFieldsLogin("email", email);
        autenticacaoService.setFieldsLogin("senha", senha);
    }

    @E("a resposta deve conter o token JWT de acesso")
    public void aRespostaDeveConterOTokenJWTDeAcesso() {
        String token = autenticacaoService.response.jsonPath().getString("token");
        Assert.assertNotNull("O token retornado não deve ser nulo", token);
        Assert.assertFalse("O token retornado não deve ser vazio", token.trim().isEmpty());
    }

    @E("que o arquivo de contrato esperado para autenticação é o {string}")
    public void queOArquivoDeContratoEsperadoParaAutenticacaoEO(String contract) throws IOException {
        autenticacaoService.setContract(contract);
    }

    @Então("a resposta da autenticação deve estar em conformidade com o contrato selecionado")
    public void aRespostaDaAutenticacaoDeveEstarEmConformidadeComOContratoSelecionado() throws IOException {
        Set<ValidationMessage> validateResponse = autenticacaoService.validateResponseAgainstSchema();
        Assert.assertTrue("O contrato de autenticação está inválido. Erros encontrados: " + validateResponse, validateResponse.isEmpty());
    }

    @Então("o status code da resposta de autenticação deve ser {int}")
    public void oStatusCodeDaRespostaDeAutenticacaoDeveSer(int statusCode) {
        Assert.assertEquals(statusCode, autenticacaoService.response.statusCode());
    }
}
