package steps;

import com.networknt.schema.ValidationMessage;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import model.ErrorMessageModel;
import org.junit.Assert;
import services.ConsumoEnergiaService;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ConsumoEnergiaSteps {

    ConsumoEnergiaService consumoEnergiaService = new ConsumoEnergiaService();
    private String idInformado;

    @Dado("que eu tenha os seguintes dados de consumo:")
    public void queEuTenhaOsSeguintesDadosDeConsumo(List<Map<String, String>> rows) {
        for (Map<String, String> columns : rows) {
            consumoEnergiaService.setFieldsConsumo(columns.get("campo"), columns.get("valor"));
        }
    }

    @Quando("eu enviar a requisição para o endpoint {string} de cadastro de consumo")
    public void euEnviarARequisicaoParaOEndpointDeCadastroDeConsumo(String endPoint) {
        consumoEnergiaService.createConsumo(endPoint);
    }

    @Então("o status code da resposta deve ser {int}")
    public void oStatusCodeDaRespostaDeveSer(int statusCode) {
        Assert.assertEquals(statusCode, consumoEnergiaService.response.statusCode());
    }

    @E("o corpo de resposta de erro da api deve conter o campo {string} com a mensagem {string}")
    public void oCorpoDeRespostaDeErroDaApiDeveConterOCampoComAMensagem(String campoEsperado, String mensagemEsperada) {
        List<Map<String, String>> erros = consumoEnergiaService.response.jsonPath().getList("$");
        boolean encontrado = false;
        for (Map<String, String> erro : erros) {
            if (campoEsperado.equals(erro.get("campo")) && mensagemEsperada.equals(erro.get("mensagem"))) {
                encontrado = true;
                break;
            }
        }
        Assert.assertTrue("Erro esperado não encontrado para o campo: " + campoEsperado, encontrado);
    }

    @Dado("que eu recupere o ID do consumo criado no contexto")
    public void queEuRecupereOIDDoConsumoCriadoNoContexto() {
        consumoEnergiaService.retrieveIdConsumo();
        this.idInformado = consumoEnergiaService.idConsumo;
    }

    @Dado("que eu informe o ID de consumo inexistente {string}")
    public void queEuInformeOIDDeConsumoInexistente(String id) {
        this.idInformado = id;
    }

    @Quando("eu enviar a requisição com o ID para o endpoint {string} de deleção de consumo")
    public void euEnviarARequisicaoComOIDParaOEndpointDeDelecaoDeConsumo(String endPoint) {
        consumoEnergiaService.deleteConsumoComId(endPoint, idInformado);
    }

    @Quando("eu enviar uma requisição GET para o endpoint {string} de listagem")
    public void euEnviarUmaRequisicaoGETParaOEndpointDeListagem(String endPoint) {
        consumoEnergiaService.getConsumos(endPoint);
    }

    @E("a lista de consumos não deve estar vazia")
    public void aListaDeConsumosNaoDeveEstarVazia() {
        List<?> lista = consumoEnergiaService.response.jsonPath().getList("$");
        Assert.assertNotNull("A lista retornada não deve ser nula", lista);
        Assert.assertFalse("A lista de consumos não deve estar vazia", lista.isEmpty());
    }

    @Quando("eu enviar uma requisição GET com o ID para o endpoint {string} de busca por ID")
    public void euEnviarUmaRequisicaoGETComOIDParaOEndpointDeBuscaPorID(String endPoint) {
        consumoEnergiaService.getConsumoPorId(endPoint, idInformado);
    }

    @E("o campo {string} da resposta deve ser {string}")
    public void oCampoDaRespostaDeveSer(String campo, String valorEsperado) {
        String valorObtido = consumoEnergiaService.response.jsonPath().getString(campo);
        Assert.assertEquals(valorEsperado, valorObtido);
    }

    @E("que o arquivo de contrato esperado é o {string}")
    public void queOArquivoDeContratoEsperadoEO(String contract) throws IOException {
        consumoEnergiaService.setContract(contract);
    }

    @Então("a resposta da requisição deve estar em conformidade com o contrato selecionado")
    public void aRespostaDaRequisicaoDeveEstarEmConformidadeComOContratoSelecionado() throws IOException {
        Set<ValidationMessage> validateResponse = consumoEnergiaService.validateResponseAgainstSchema();
        Assert.assertTrue("O contrato está inválido. Erros encontrados: " + validateResponse, validateResponse.isEmpty());
    }
}
