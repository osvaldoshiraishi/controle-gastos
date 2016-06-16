package oss.cloud.authorization.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("oss.cloud")
public class Oauth2ServerBootApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(Oauth2ServerBootApplication.class, args);//NOSONAR
    }

}