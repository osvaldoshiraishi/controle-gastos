package br.org.spcbrasil.recuperacao.client.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@EnableZuulProxy
@SpringBootApplication
public class UiApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(UiApplication.class, args);//NOSONAR
    }
}