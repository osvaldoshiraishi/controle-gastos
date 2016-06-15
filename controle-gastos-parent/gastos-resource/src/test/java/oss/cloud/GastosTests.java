package oss.cloud;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.Timestamp;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import oss.cloud.resource.gastos.model.Gasto;
import oss.cloud.resource.gastos.model.TipoConta;
import oss.cloud.resource.gastos.model.TipoDespesa;

public class GastosTests {
	
	private static final String GATEWAY_URL = "http://localhost:8081/gateway";
	
	RestTemplate restTemplate = new RestTemplate();
	
	/**
	 * Find all test
	 */
	@Test
	public void testeFindAllGastos() {
		ResponseEntity<String> resposta = restTemplate.getForEntity(GATEWAY_URL + "/cadastro/despesas/findAll", String.class);
		System.out.println(resposta.getBody());
		Assert.assertNotNull(resposta.getBody());
	}
	
	/**
	 * Create Test
	 */
	@Test
	public void testeCreateGastos() {
		Gasto g = new Gasto();
		g.setLugar("Extra");
		g.setData(new Timestamp(System.currentTimeMillis()));
		g.setTipo(TipoConta.CREDITO);
		g.setTipoDespesa(TipoDespesa.SUPERMERCADO);
		g.setValor(BigDecimal.valueOf(508.99D));
		ResponseEntity<Gasto> resposta = restTemplate.postForEntity(GATEWAY_URL + "/cadastro/despesas/create", g,Gasto.class);
		Assert.assertTrue(HttpStatus.OK.equals(resposta.getStatusCode()));
		Assert.assertNotNull(resposta.getBody());
		Gasto incluido = resposta.getBody();
		Long id = incluido.getId();
		ResponseEntity<Gasto> respostaDetail = restTemplate.getForEntity(GATEWAY_URL + "/cadastro/despesas/detail/"+id, Gasto.class);
		Gasto detail = respostaDetail.getBody();
		Assert.assertTrue(detail.getId().equals(id));
	}
	
	/**
	 * Detail Test
	 */
	@Test
	public void testeDetailGasto() {
		Long id = 1L;
		ResponseEntity<Gasto> resposta = restTemplate.getForEntity(GATEWAY_URL + "/cadastro/despesas/detail/"+id, Gasto.class);
		Gasto gasto = resposta.getBody();
		Assert.assertTrue(gasto.getId().equals(id));
		System.out.println(resposta.getBody());
		Assert.assertNotNull(resposta.getBody());
	}
	
	/**
	 * Update Test
	 */
	@Test
	public void testeUpdateGasto() {
		Long id = 1L;
		ResponseEntity<Gasto> resposta = restTemplate.getForEntity(GATEWAY_URL + "/cadastro/despesas/detail/"+id, Gasto.class);
		Gasto gasto = resposta.getBody();
		Assert.assertNotNull(gasto);
		String valorAntigo = gasto.getLugar();
		String valorNovo = proximoLugar();
		gasto.setLugar(valorNovo);
		ResponseEntity<Void> respostaUpdate = restTemplate.postForEntity(GATEWAY_URL + "/cadastro/despesas/update/"+id, gasto,Void.class);
		Assert.assertTrue(HttpStatus.OK.equals(respostaUpdate.getStatusCode()));
		
		ResponseEntity<Gasto> respostaDetailNovo = restTemplate.getForEntity(GATEWAY_URL + "/cadastro/despesas/detail/"+id, Gasto.class);
		Gasto novo = respostaDetailNovo.getBody();
		
		Assert.assertNotNull(novo);
		Assert.assertFalse(valorAntigo.equals(novo.getLugar()));
		Assert.assertTrue(valorNovo.equals(novo.getLugar()));
	}
	
	/**
	 * Create Test
	 */
	@Test
	public void testeRemoveGasto() {
		//create
		Gasto g = new Gasto();
		g.setLugar("Extra");
		g.setData(new Timestamp(System.currentTimeMillis()));
		g.setTipo(TipoConta.CREDITO);
		g.setTipoDespesa(TipoDespesa.SUPERMERCADO);
		g.setValor(BigDecimal.valueOf(508.99D));
		ResponseEntity<Gasto> resposta = restTemplate.postForEntity(GATEWAY_URL + "/cadastro/despesas/create", g,Gasto.class);
		Assert.assertTrue(HttpStatus.OK.equals(resposta.getStatusCode()));
		Assert.assertNotNull(resposta.getBody());
		Gasto incluido = resposta.getBody();
		//detail for id of created bean
		Long id = incluido.getId();
		ResponseEntity<Gasto> respostaDetail = restTemplate.getForEntity(GATEWAY_URL + "/cadastro/despesas/detail/"+id, Gasto.class);
		Gasto detail = respostaDetail.getBody();
		Assert.assertTrue(detail.getId().equals(id));
		//remove
		ResponseEntity<Void> respostaRemove = restTemplate.postForEntity(GATEWAY_URL + "/cadastro/despesas/remove/"+id, detail,Void.class);
		Assert.assertTrue(HttpStatus.OK.equals(respostaRemove.getStatusCode()));
		ResponseEntity<Gasto> respostaCheck = restTemplate.getForEntity(GATEWAY_URL + "/cadastro/despesas/detail/"+id, Gasto.class);
		Assert.assertTrue(HttpStatus.NO_CONTENT.equals(respostaCheck.getStatusCode()));
	}
	
	/**
	 * random string
	 * @return
	 */
	private String proximoLugar(){
		SecureRandom random = new SecureRandom();
		return new BigInteger(130, random).toString(32);
	}
	
	

}
