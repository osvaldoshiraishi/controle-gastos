package oss.cloud;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

import oss.cloud.resource.gastos.model.Gasto;
import oss.cloud.resource.gastos.model.TipoConta;
import oss.cloud.resource.gastos.model.TipoDespesa;

public class GastosTests {

	private static final String GATEWAY_URL = "http://localhost:8080/gateway";

	RestTemplate restTemplate = new RestTemplate();

	 private String obterToken(String clientId, String username, String password) {
	        final Map<String, String> params = new HashMap<String, String>();
	        params.put("grant_type", "password");
	        params.put("client_id", clientId);
	        params.put("username", username);
	        params.put("password", password);
	        final Response response = RestAssured.given().auth().preemptive().basic(clientId, "secret").and().with().params(params).when().post(GATEWAY_URL+"/oauth/token");
	        return response.jsonPath().getString("access_token");
	    }

	/**
	 * Find all test
	 */
	@Test
	public void testeFindAllGastos() {
		final String accessToken = obterToken("clientAssociado", "usuarioTeste", "abc123");
		System.out.println("TOKEN: " + accessToken);
		Assert.assertNotNull(accessToken);
	    final Response response = RestAssured.given().header("Authorization", "Bearer " + accessToken).get(GATEWAY_URL + "/cadastro/despesas/findAll");
	    System.out.println("############################## GASTOS: " + response.getBody().asString());
	    assertEquals(200, response.getStatusCode());
		
	}
	
	/**
	 * Find all test
	 */
	//@Test
	public void testeFindAllGastosSemGateway() {
		final String accessToken = obterToken("clientAssociado", "usuarioTeste", "abc123");
		System.out.println("TOKEN: " + accessToken);
		Assert.assertNotNull(accessToken);
		ResponseEntity<String> resposta = restTemplate.getForEntity("http://localhost:8081/gastos/cadastro/despesas/findAll", String.class);
		Assert.assertTrue(HttpStatus.OK.equals(resposta.getStatusCode()));
		System.out.println(resposta.getBody());
		
	}

	/**
	 * Create Test
	 */
	//@Test
	public void testeCreateGastosSemGateway() {
		Gasto g = new Gasto();
		g.setId(1L);
		g.setLugar("Extra");
		g.setData(new Timestamp(System.currentTimeMillis()));
		g.setTipo(TipoConta.CREDITO);
		g.setTipoDespesa(TipoDespesa.SUPERMERCADO);
		g.setValor(BigDecimal.valueOf(508.99D));
		//final String accessToken = obterToken("clientAssociado", "usuarioTeste", "abc123");
		final Response response = RestAssured.given().body(g).contentType(ContentType.JSON).post("http://localhost:8081/gastos/cadastro/despesas/create");
        assertEquals(200, response.getStatusCode());
		Gasto included = response.as(Gasto.class);
       Long id = included.getId();
		ResponseEntity<Gasto> respostaDetail = restTemplate
				.getForEntity("http://localhost:8081/gastos/cadastro/despesas/detail/" + id, Gasto.class);
		Gasto detail = respostaDetail.getBody();
		Assert.assertTrue(detail.getId().equals(id));
	}
	
	/**
	 * Create Test
	 */
	@Test
	public void testeCreateGastos() {
		Gasto g = new Gasto();
		g.setId(1L);
		g.setLugar("Extra");
		g.setData(new Timestamp(System.currentTimeMillis()));
		g.setTipo(TipoConta.CREDITO);
		g.setTipoDespesa(TipoDespesa.SUPERMERCADO);
		g.setValor(BigDecimal.valueOf(508.99D));
		final String accessToken = obterToken("clientAssociado", "usuarioTeste", "abc123");
		final Response response = RestAssured.given().header("Authorization", "Bearer " + accessToken).body(g).contentType(ContentType.JSON).post(GATEWAY_URL + "/cadastro/despesas/create");
        assertEquals(200, response.getStatusCode());
		Gasto included = response.as(Gasto.class);
       Long id = included.getId();
       final Response responseDetail = RestAssured.given().header("Authorization", "Bearer " + accessToken).get(GATEWAY_URL + "/cadastro/despesas/detail/"+id);
		Gasto detail = responseDetail.as(Gasto.class);
		Assert.assertTrue(detail.getId().equals(id));
	}

	/**
	 * Detail Test
	 */
	//@Test
	public void testeDetailGasto() {
		Long id = 1L;
		ResponseEntity<Gasto> resposta = restTemplate.getForEntity(GATEWAY_URL + "/cadastro/despesas/detail/" + id,
				Gasto.class);
		Gasto gasto = resposta.getBody();
		Assert.assertTrue(gasto.getId().equals(id));
		System.out.println(resposta.getBody());
		Assert.assertNotNull(resposta.getBody());
	}

	/**
	 * Update Test
	 */
	//@Test
	public void testeUpdateGasto() {
		Long id = 1L;
		ResponseEntity<Gasto> resposta = restTemplate.getForEntity(GATEWAY_URL + "/cadastro/despesas/detail/" + id,
				Gasto.class);
		Gasto gasto = resposta.getBody();
		Assert.assertNotNull(gasto);
		String valorAntigo = gasto.getLugar();
		String valorNovo = proximoLugar();
		gasto.setLugar(valorNovo);
		ResponseEntity<Void> respostaUpdate = restTemplate
				.postForEntity(GATEWAY_URL + "/cadastro/despesas/update/" + id, gasto, Void.class);
		Assert.assertTrue(HttpStatus.OK.equals(respostaUpdate.getStatusCode()));

		ResponseEntity<Gasto> respostaDetailNovo = restTemplate
				.getForEntity(GATEWAY_URL + "/cadastro/despesas/detail/" + id, Gasto.class);
		Gasto novo = respostaDetailNovo.getBody();

		Assert.assertNotNull(novo);
		Assert.assertFalse(valorAntigo.equals(novo.getLugar()));
		Assert.assertTrue(valorNovo.equals(novo.getLugar()));
	}

	/**
	 * Create Test
	 */
	//@Test
	public void testeRemoveGasto() {
		// create
		Gasto g = new Gasto();
		g.setLugar("Extra");
		g.setData(new Timestamp(System.currentTimeMillis()));
		g.setTipo(TipoConta.CREDITO);
		g.setTipoDespesa(TipoDespesa.SUPERMERCADO);
		g.setValor(BigDecimal.valueOf(508.99D));
		ResponseEntity<Gasto> resposta = restTemplate.postForEntity(GATEWAY_URL + "/cadastro/despesas/create", g,
				Gasto.class);
		Assert.assertTrue(HttpStatus.OK.equals(resposta.getStatusCode()));
		Assert.assertNotNull(resposta.getBody());
		Gasto incluido = resposta.getBody();
		// detail for id of created bean
		Long id = incluido.getId();
		ResponseEntity<Gasto> respostaDetail = restTemplate
				.getForEntity(GATEWAY_URL + "/cadastro/despesas/detail/" + id, Gasto.class);
		Gasto detail = respostaDetail.getBody();
		Assert.assertTrue(detail.getId().equals(id));
		// remove
		ResponseEntity<Void> respostaRemove = restTemplate
				.postForEntity(GATEWAY_URL + "/cadastro/despesas/remove/" + id, detail, Void.class);
		Assert.assertTrue(HttpStatus.OK.equals(respostaRemove.getStatusCode()));
		ResponseEntity<Gasto> respostaCheck = restTemplate.getForEntity(GATEWAY_URL + "/cadastro/despesas/detail/" + id,
				Gasto.class);
		Assert.assertTrue(HttpStatus.NO_CONTENT.equals(respostaCheck.getStatusCode()));
	}

	/**
	 * random string
	 * 
	 * @return
	 */
	private String proximoLugar() {
		SecureRandom random = new SecureRandom();
		return new BigInteger(130, random).toString(32);
	}

}
