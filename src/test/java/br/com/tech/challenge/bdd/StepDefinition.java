package br.com.tech.challenge.bdd;

import br.com.tech.challenge.domain.dto.ClienteCpfDTO;
import br.com.tech.challenge.domain.dto.ClienteDTO;
import br.com.tech.challenge.domain.dto.CredencialDTO;
import br.com.tech.challenge.domain.dto.TokenDTO;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;


import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.http.HttpStatus.CREATED;

public class StepDefinition {

    private Response response;
    private ClienteDTO clienteResposta;
    private ClienteCpfDTO clienteCpfResposta;
    private TokenDTO tokenResposta;
    private final String ENDPOINT_API_USUARIOS = "http://localhost:8080/usuarios";
    private final String ENDPOINT_API_CLIENTES = "http://localhost:8080/clientes";

    @Quando("autenticar o usuário com credenciais válidas")
    public TokenDTO autenticar_o_usuário_com_credenciais_válidas() {
        CredencialDTO credencialDTO = new CredencialDTO("usuario", "senha");
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(credencialDTO)
                .when()
                .post(ENDPOINT_API_USUARIOS + "/auth");
        return response.then()
                .extract().as(TokenDTO.class);
    }

    @Então("o token é gerado com sucesso")
    public void o_token_é_gerado_com_sucesso() {
        response.then()
                .statusCode(HttpStatus.OK.value())
                .body("token", notNullValue());
    }

    @Quando("submeter um novo cliente")
    public ClienteDTO submeter_um_novo_cliente() {
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(setClienteDTO())
                .when()
                .post(ENDPOINT_API_CLIENTES);
        return response.then()
                .extract().as(ClienteDTO.class);
    }

    @Então("o cliente é registrado com sucesso")
    public void o_cliente_é_registrado_com_sucesso() {
        response.then()
                .statusCode(CREATED.value());
    }

    @E("retornado o cliente registrado")
    public void retornado_o_cliente_registrado() {
        response.body().as(ClienteDTO.class);
    }

    @Dado("que um cliente já foi cadastrado")
    public void que_um_cliente_já_foi_cadastrado() {
        clienteResposta = submeter_um_novo_cliente();
    }

    @Quando("submeter um novo cliente apenas com cpf")
    public ClienteCpfDTO submeter_um_novo_cliente_com_apenas_cpf() {
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(setClienteCpfDTO())
                .when()
                .post(ENDPOINT_API_CLIENTES  + "/cpf");
        return response.then()
                .extract().as(ClienteCpfDTO.class);
    }

    @Então("o cliente com apenas cpf é registrado com sucesso")
    public void o_cliente_com_apenas_cpf_é_registrado_com_sucesso() {
        response.then()
                .statusCode(CREATED.value());
    }

    @E("retornado o cliente com apenas cpf registrado")
    public void retornado_o_cliente_com_apenas_cpf_registrado() {
        response.body().as(ClienteCpfDTO.class);
    }

    @Dado("que um cliente com apenas cpf já foi cadastrado")
    public void que_um_cliente_com_apenas_cpf_já_foi_cadastrado() {
        clienteCpfResposta = submeter_um_novo_cliente_com_apenas_cpf();
    }

    @Quando("requisitar a busca de um cliente por cpf")
    public void requisitar_a_busca_de_um_cliente_por_cpf() {
        response = given()
                        .param("cpf", clienteResposta.getCpf())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                        .post(ENDPOINT_API_CLIENTES + "/check-in");
    }

    @Então("o cliente é retornado com sucesso")
    public void o_cliente_por_cpf_é_retornado_com_sucesso() {
        response
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("content.size()", greaterThan(0))
                .body("content[0].id", notNullValue())
                .body("content[0].cpf", notNullValue());
        ;
    }


    @Quando("requisitar a busca de um cliente")
    public void requisitar_a_busca_de_um_cliente() {
        response = given()
                .param("id", clienteResposta.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get(ENDPOINT_API_CLIENTES);
    }

    @Então("o cliente é retornado com sucesso")
    public void o_cliente_é_retornado_com_sucesso() {
        response
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("content.size()", greaterThan(0))
                .body("content[0].id", notNullValue())
                .body("content[0].cpf", notNullValue());
        ;
    }

    private ClienteDTO setClienteDTO() {
        return ClienteDTO.builder()
                .id(1L)
                .nome("Anthony Samuel Joaquim Teixeira")
                .email("anthony.samuel.teixeira@said.adv.br")
                .cpf("143.025.400-95")
                .build();
    }

    private ClienteCpfDTO setClienteCpfDTO() {
        return ClienteCpfDTO.builder()
                .id(1L)
                .cpf("143.025.400-95")
                .build();
    }
}
