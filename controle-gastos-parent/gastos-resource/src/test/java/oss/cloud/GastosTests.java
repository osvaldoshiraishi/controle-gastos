package oss.cloud;

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import oss.cloud.resource.gastos.model.Gasto;
import oss.cloud.resource.gastos.model.TipoConta;
import oss.cloud.resource.gastos.model.TipoDespesa;

//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = GastosGatewayApplication.class)
public class GastosTests {

	
	RestTemplate restTemplate = new RestTemplate();
	
	@Test
	public void testeFindAllGastos() {
		ResponseEntity<String> resposta = restTemplate.getForEntity("http://localhost:8081/gateway/cadastro/despesas/findAll", String.class);
		System.out.println(resposta.getBody());
		Assert.assertNotNull(resposta.getBody());
	}
	
	@Test
	public void testeCreateGastos() {
		Gasto g = new Gasto();
		g.setLugar("Extra");
		g.setData(new Timestamp(System.currentTimeMillis()));
		g.setTipo(TipoConta.CREDITO);
		g.setTipoDespesa(TipoDespesa.SUPERMERCADO);
		g.setValor(BigDecimal.valueOf(508.99D));

		ResponseEntity<Void> resposta = restTemplate.postForEntity("http://localhost:8081/gateway/cadastro/despesas/create", g,Void.class);
		Assert.assertTrue(HttpStatus.OK.equals(resposta.getStatusCode()));
		ResponseEntity<String> respostaF = restTemplate.getForEntity("http://localhost:8081/gateway/cadastro/despesas/findAll", String.class);
		System.out.println(respostaF.getBody());
		Assert.assertNotNull(respostaF.getBody());
	}
	
	

}
