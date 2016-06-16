package oss.cloud.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

//@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@SpringBootApplication
@ComponentScan("oss.cloud")
public class ResourceBootApplication  extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(ResourceBootApplication.class, args);
	}
}
