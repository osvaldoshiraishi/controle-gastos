package oss.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

import oss.cloud.filter.SimpleFilter;

@SpringBootApplication
@EnableZuulProxy
public class GastosGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GastosGatewayApplication.class, args);
	}
	
	@Bean
	public SimpleFilter simpleFilter(){
		return new SimpleFilter();
	}
}
