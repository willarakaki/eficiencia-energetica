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
import model.LoginModel;
import model.TokenModel;
import model.UsuarioModel;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;

import static io.restassured.RestAssured.given;

public class AutenticacaoService {

    public static String token;

    public final UsuarioModel usuarioModel = new UsuarioModel();
    public final LoginModel loginModel = new LoginModel();
    public TokenModel tokenModel;

    public final Gson gson = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .create();

    public Response response;
    String baseUrl = "http://localhost:8080";

    String schemasPath = "src/test/resources/schemas/";
    JSONObject jsonSchema;
    private final ObjectMapper mapper = new ObjectMapper();

    public void setFieldsUsuario(String field, String value) {
        switch (field) {
            case "nome" -> usuarioModel.setNome(value);
            case "email" -> usuarioModel.setEmail(value);
            case "senha" -> usuarioModel.setSenha(value);
            case "role" -> usuarioModel.setRole(value);
            default -> throw new IllegalStateException("Campo inesperado: " + field);
        }
    }

    public void setFieldsLogin(String field, String value) {
        switch (field) {
            case "email" -> loginModel.setEmail(value);
            case "senha" -> loginModel.setSenha(value);
            default -> throw new IllegalStateException("Campo inesperado: " + field);
        }
    }

    public void registrarUsuario(String endPoint) {
        String url = baseUrl + endPoint;
        String bodyToSend = gson.toJson(usuarioModel);

        response = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(bodyToSend)
                .when()
                .post(url)
                .then()
                .extract()
                .response();
    }

    public void login(String endPoint) {
        String url = baseUrl + endPoint;
        String bodyToSend = gson.toJson(loginModel);

        response = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(bodyToSend)
                .when()
                .post(url)
                .then()
                .extract()
                .response();

        if (response.statusCode() == 200) {
            tokenModel = gson.fromJson(response.jsonPath().prettify(), TokenModel.class);
            token = tokenModel.getToken();
        }
    }

    public void autenticarUsuarioPadrao() {
        if (token != null) {
            return;
        }

        // Tenta registrar usuário padrão se ainda não existir
        UsuarioModel admin = new UsuarioModel();
        admin.setNome("Administrador ESG");
        admin.setEmail("admin.esg@fiap.com.br");
        admin.setSenha("Admin@123");
        admin.setRole("ADMIN");

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(gson.toJson(admin))
                .when()
                .post(baseUrl + "/auth/registrar");

        // Realiza o login
        LoginModel login = new LoginModel();
        login.setEmail("admin.esg@fiap.com.br");
        login.setSenha("Admin@123");

        Response resp = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(gson.toJson(login))
                .when()
                .post(baseUrl + "/auth/login")
                .then()
                .extract()
                .response();

        if (resp.statusCode() == 200) {
            TokenModel tm = gson.fromJson(resp.jsonPath().prettify(), TokenModel.class);
            token = tm.getToken();
        }
    }

    private JSONObject loadJsonFromFile(String filePath) throws IOException {
        try (InputStream inputStream = Files.newInputStream(Paths.get(filePath))) {
            JSONTokener tokener = new JSONTokener(inputStream);
            return new JSONObject(tokener);
        }
    }

    public void setContract(String contract) throws IOException {
        switch (contract) {
            case "Login bem-sucedido" ->
                    jsonSchema = loadJsonFromFile(schemasPath + "login-schema.json");
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
