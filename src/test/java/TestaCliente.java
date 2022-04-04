import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.apache.http.protocol.HTTP;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class TestaCliente {

    String clienteAPI = "http://localhost:8080/";
    String endpointCliente = "cliente";
    String apagaTodos = "/apagaTodos";

    String listaVazia = "{}";

    @Test
    @DisplayName("Quando pegar todos os clientes sem cadastrar clientes, então a lista deve estar vazia")
    public void quandoPegarTodosOsClienteSemCadastrarClienteEntaoAListaDeveEstarVazia () {

        deletaTodosClientes();

        given()
                .contentType(ContentType.JSON)
        .when()
                .get(clienteAPI)
        .then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat().body(new IsEqual<>(listaVazia));
    }

    @Test
    @DisplayName("Quando cadastrado um novo cliente, ele deve estar disponível no resultado")
    public void cadastrarClientes () {

        String clienteParaCadastrar = "{\n" +
                "  \"id\": 1,\n" +
                "  \"idade\": 33,\n" +
                "  \"nome\": \"Arqueiro Verde\",\n" +
                "  \"risco\": 0\n" +
                "}";

        String respostaEsperada = "{\"1\"" +
                ":{\"nome\":\"Arqueiro Verde\"," +
                "\"idade\":33," +
                "\"id\":1," +
                "\"risco\":0}" +
                "}";

        given()
                .contentType(ContentType.JSON)
                .body(clienteParaCadastrar)
        .when()
                .post(clienteAPI+endpointCliente)
        .then()
                .statusCode(HttpStatus.SC_CREATED)
                .assertThat().body(containsString(respostaEsperada));
    }

    @Test
    @DisplayName("Quando alterar o cadastro do cliente, o mesmo deve ser apresentado atualizado")
    public void altualizaCliente () {

        String clienteParaCadastrar = "{\n" +
                "  \"id\": 1,\n" +
                "  \"idade\": 33,\n" +
                "  \"nome\": \"Arqueiro Verde\",\n" +
                "  \"risco\": 0\n" +
                "}";

        String clienteParaAlterar = "{\n" +
                "  \"id\": 1,\n" +
                "  \"idade\": 42,\n" +
                "  \"nome\": \"Homem de Ferro\",\n" +
                "  \"risco\": 0\n" +
                "}";

        String respostaEsperada = "{\"1\"" +
                ":{\"nome\":\"Homem de Ferro\"," +
                "\"idade\":42," +
                "\"id\":1," +
                "\"risco\":0}" +
                "}";

        given()
                .contentType(ContentType.JSON)
                .body(clienteParaCadastrar)
        .when()
                .post(clienteAPI+endpointCliente);

        given()
                .contentType(ContentType.JSON)
                .body(clienteParaAlterar)
        .when()
                .put(clienteAPI+endpointCliente)
        .then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat().body(containsString(respostaEsperada));
    }

    @Test
    @DisplayName("Quando deletar um cliente, o mesmo deve ser excluido da lista de cadastrado")
    public void deletarClientePorID () {

        String clienteParaCadastrar = "{\n" +
                "  \"id\": 1,\n" +
                "  \"idade\": 33,\n" +
                "  \"nome\": \"Arqueiro Verde\",\n" +
                "  \"risco\": 0\n" +
                "}";

        String respostaEsperada = "{ NOME: Arqueiro Verde, IDADE: 33, ID: 1 }";

        given()
                .contentType(ContentType.JSON)
                .body(clienteParaCadastrar)
        .when()
                .post(clienteAPI+endpointCliente);

        given()
                .contentType(ContentType.JSON)
        .when()
                .delete(clienteAPI+endpointCliente+"/1")
        .then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat().body(containsString(respostaEsperada));
        }

    //método de apoio
    public void deletaTodosClientes () {

        given()
                .contentType(ContentType.JSON)
        .when()
                .delete(clienteAPI+endpointCliente+apagaTodos)
        .then()
                .statusCode(HttpStatus.SC_OK)
                .body(new IsEqual<>(listaVazia));
    }
}
