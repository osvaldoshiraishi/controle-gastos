package oss.cloud;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
	@Test
	public void testeFindAllGastosComGateway() {
		final String accessToken = obterToken("clientAssociado", "usuarioTeste", "abc123");
		System.out.println("TOKEN: " + accessToken);
		Assert.assertNotNull(accessToken);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization","Bearer " + accessToken);
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		ResponseEntity<String> resposta = restTemplate.exchange(GATEWAY_URL +"/cadastro/despesas/findAll", HttpMethod.GET,entity,String.class);
		Assert.assertTrue(HttpStatus.OK.equals(resposta.getStatusCode()));
		System.out.println(resposta.getBody());
		
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
		Assert.assertNotNull(accessToken);
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
	@Test
	public void testeDetailGasto() {
		Long id = 1L;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		final String accessToken = obterToken("clientAssociado", "usuarioTeste", "abc123");
		headers.set("Authorization","Bearer " + accessToken);
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		ResponseEntity<Gasto> resposta = restTemplate.exchange(GATEWAY_URL +"/cadastro/despesas/detail/"+id, HttpMethod.GET,entity,Gasto.class);
		Assert.assertEquals(HttpStatus.OK,resposta.getStatusCode());
		Gasto gasto = resposta.getBody();
		Assert.assertTrue(gasto.getId().equals(id));
		Assert.assertNotNull(resposta.getBody());
	}

	/**
	 * Update Test
	 */
	@Test
	public void testeUpdateGasto() {
		Long id = 1L;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		final String accessToken = obterToken("clientAssociado", "usuarioTeste", "abc123");
		headers.set("Authorization","Bearer " + accessToken);
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		ResponseEntity<Gasto> resposta = restTemplate.exchange(GATEWAY_URL +"/cadastro/despesas/detail/"+id, HttpMethod.GET,entity,Gasto.class);
		Assert.assertTrue(HttpStatus.OK.equals(resposta.getStatusCode()));
		Gasto gasto = resposta.getBody();
		Assert.assertTrue(gasto.getId().equals(id));
		String valorAntigo = gasto.getLugar();
		String valorNovo = proximoLugar();
		gasto.setLugar(valorNovo);
		HttpEntity<Gasto> entityUpdate = new HttpEntity<Gasto>(gasto, headers);
		ResponseEntity<Void> respostaUpdate = restTemplate
				.exchange(GATEWAY_URL + "/cadastro/despesas/update/" + id, HttpMethod.POST,entityUpdate, Void.class);
		Assert.assertTrue(HttpStatus.OK.equals(respostaUpdate.getStatusCode()));
		HttpEntity<String> entityNovo = new HttpEntity<String>(headers);
		ResponseEntity<Gasto> respostaNovo = restTemplate.exchange(GATEWAY_URL +"/cadastro/despesas/detail/"+id, HttpMethod.GET,entityNovo,Gasto.class);
		Assert.assertTrue(HttpStatus.OK.equals(respostaNovo.getStatusCode()));
		Gasto novo = respostaNovo.getBody();
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
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		final String accessToken = obterToken("clientAssociado", "usuarioTeste", "abc123");
		headers.set("Authorization","Bearer " + accessToken);
		HttpEntity<Gasto> entity = new HttpEntity<Gasto>(g,headers);
		ResponseEntity<Gasto> resposta = restTemplate.exchange(GATEWAY_URL + "/cadastro/despesas/create", HttpMethod.POST, entity,Gasto.class);
		Assert.assertTrue(HttpStatus.OK.equals(resposta.getStatusCode()));
		Assert.assertNotNull(resposta.getBody());
		Gasto incluido = resposta.getBody();
		// detail for id of created bean
		Long id = incluido.getId();
		HttpEntity<String> entityIncluido = new HttpEntity<String>(headers);
		ResponseEntity<Gasto> respostaDetail = restTemplate
				.exchange(GATEWAY_URL + "/cadastro/despesas/detail/" + id,HttpMethod.GET,entityIncluido, Gasto.class);
		Gasto detail = respostaDetail.getBody();
		Assert.assertTrue(detail.getId().equals(id));
		// remove
		HttpEntity<Gasto> entityRemove = new HttpEntity<Gasto>(detail,headers);
		ResponseEntity<Void> respostaRemove = restTemplate
				.postForEntity(GATEWAY_URL + "/cadastro/despesas/remove/" + id, entityRemove, Void.class);
		Assert.assertTrue(HttpStatus.OK.equals(respostaRemove.getStatusCode()));
		
		//detail no conte
		HttpEntity<String> entityNoContent = new HttpEntity<String>(headers);
		ResponseEntity<Gasto> respostaCheck = restTemplate.exchange(GATEWAY_URL + "/cadastro/despesas/detail/" + id, HttpMethod.GET,entityNoContent,
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
