package oss.cloud;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class GastosGatewayApplicationTests {
	
	RestTemplate restTemplate = new RestTemplate();
	
	@Test
	public void testeFindAllGastos() {
		ResponseEntity<String> resposta = restTemplate.getForEntity("http://localhost:8081/gateway/cadastro/despesas/findAll", String.class);
		System.out.println(resposta.getBody());
		Assert.assertNotNull(resposta.getBody());
	}
	
}
