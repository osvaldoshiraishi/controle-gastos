package br.org.spcbrasil.recuperacao.client.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;

public class AuthorizationLiveTest {

    private String obterToken(String clientId, String username, String password) {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("grant_type", "password");
        params.put("client_id", clientId);
        params.put("username", username);
        params.put("password", password);
        final Response response = RestAssured.given().auth().preemptive().basic(clientId, "secret").and().with().params(params).when().post("http://localhost:8081/recuperacao-autorizacao-oauth/oauth/token");
        return response.jsonPath().getString("access_token");
    }

    
    @Test
    public void autenticacaoUsuario(){
    	final String accessToken = obterToken("clientAssociado", "usuarioTeste", "abc123");
    	assertNotNull(accessToken);
    	
    }
    
    
    @Test
    public void testeRecursos() {
        final String accessToken = obterToken("clientAssociado", "usuarioTeste", "abc123");
        final Response response = RestAssured.given().get("http://localhost:8081/recuperacao-recurso/api/teste/aberto");
        assertEquals(200, response.getStatusCode());
        final Response responseAutenticado = RestAssured.given().header("Authorization", "Bearer " + accessToken).get("http://localhost:8081/recuperacao-recurso/api/teste/autenticado");
        assertEquals(200, responseAutenticado.getStatusCode());
    }

}
