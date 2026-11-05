package services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import model.ConsumoEnergiaModel;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;

import static io.restassured.RestAssured.given;

public class ConsumoEnergiaService {

    public final ConsumoEnergiaModel consumoModel = new ConsumoEnergiaModel();
    public final Gson gson = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .create();

    public Response response;
    String baseUrl = "http://localhost:8080";
    public String idConsumo;

    String schemasPath = "src/test/resources/schemas/";
    JSONObject jsonSchema;
    private final ObjectMapper mapper = new ObjectMapper();

    private final AutenticacaoService autenticacaoService = new AutenticacaoService();

    private void garantirAutenticacao() {
        if (AutenticacaoService.token == null) {
            autenticacaoService.autenticarUsuarioPadrao();
        }
    }

    public void setFieldsConsumo(String field, String value) {
        switch (field) {
            case "nomeEquipamento" -> consumoModel.setNomeEquipamento(value);
            case "setor" -> consumoModel.setSetor(value);
            case "consumoKwh" -> consumoModel.setConsumoKwh(Double.parseDouble(value));
            case "dataRegistro" -> consumoModel.setDataRegistro(value);
            default -> throw new IllegalStateException("Campo inesperado: " + field);
        }
    }

    public void createConsumo(String endPoint) {
        garantirAutenticacao();
        String url = baseUrl + endPoint;
        String bodyToSend = gson.toJson(consumoModel);

        response = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header("Authorization", "Bearer " + AutenticacaoService.token)
                .body(bodyToSend)
                .when()
                .post(url)
                .then()
                .extract()
                .response();
    }

    public void retrieveIdConsumo() {
        idConsumo = String.valueOf(gson.fromJson(response.jsonPath().prettify(),
                ConsumoEnergiaModel.class).getId());
    }

    public void deleteConsumo(String endPoint) {
        garantirAutenticacao();
        String url = String.format("%s%s/%s", baseUrl, endPoint, idConsumo);

        response = given()
                .accept(ContentType.JSON)
                .header("Authorization", "Bearer " + AutenticacaoService.token)
                .when()
                .delete(url)
                .then()
                .extract()
                .response();
    }

    public void deleteConsumoComId(String endPoint, String id) {
        garantirAutenticacao();
        String url = String.format("%s%s/%s", baseUrl, endPoint, id);

        response = given()
                .accept(ContentType.JSON)
                .header("Authorization", "Bearer " + AutenticacaoService.token)
                .when()
                .delete(url)
                .then()
                .extract()
                .response();
    }

    public void getConsumos(String endPoint) {
        garantirAutenticacao();
        String url = baseUrl + endPoint;

        response = given()
                .accept(ContentType.JSON)
                .header("Authorization", "Bearer " + AutenticacaoService.token)
                .when()
                .get(url)
                .then()
                .extract()
                .response();
    }

    public void getConsumoPorId(String endPoint, String id) {
        garantirAutenticacao();
        String url = String.format("%s%s/%s", baseUrl, endPoint, id);

        response = given()
                .accept(ContentType.JSON)
                .header("Authorization", "Bearer " + AutenticacaoService.token)
                .when()
                .get(url)
                .then()
                .extract()
                .response();
    }

    private JSONObject loadJsonFromFile(String filePath) throws IOException {
        try (InputStream inputStream = Files.newInputStream(Paths.get(filePath))) {
            JSONTokener tokener = new JSONTokener(inputStream);
            return new JSONObject(tokener);
        }
    }

    public void setContract(String contract) throws IOException {
        switch (contract) {
            case "Cadastro bem-sucedido de consumo" ->
                    jsonSchema = loadJsonFromFile(schemasPath + "cadastro-consumo-schema.json");
            case "Consulta de consumo por ID" ->
                    jsonSchema = loadJsonFromFile(schemasPath + "consulta-consumo-schema.json");
            default -> throw new IllegalStateException("Contrato inesperado: " + contract);
        }
    }

    public Set<ValidationMessage> validateResponseAgainstSchema() throws IOException {
        JSONObject jsonResponse = new JSONObject(response.getBody().asString());
        JsonSchemaFactory schemaFactory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V4);
        JsonSchema schema = schemaFactory.getSchema(jsonSchema.toString());
        JsonNode jsonResponseNode = mapper.readTree(jsonResponse.toString());
        Set<ValidationMessage> schemaValidationErrors = schema.validate(jsonResponseNode);
        return schemaValidationErrors;
    }
}
