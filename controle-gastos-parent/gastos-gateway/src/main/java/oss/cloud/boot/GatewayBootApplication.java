package oss.cloud.boot;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.trace.InMemoryTraceRepository;
import org.springframework.boot.actuate.trace.TraceRepository;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@EnableZuulProxy
@EnableAutoConfiguration
@SpringBootApplication
@ComponentScan("oss.cloud")
public class GatewayBootApplication  extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(GatewayBootApplication.class, args);//NOSONAR
    }
    
    @Bean
	public TraceRepository traceRepository() {
		return new InMemoryTraceRepository() {
			private final Logger logger = LoggerFactory.getLogger(InMemoryTraceRepository.class);
			@Override
			public void add(Map<String, Object> map) {
				logger.info(map.toString());
			}
		};
	}
}
