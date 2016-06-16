package oss.cloud.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import oss.cloud.framework.multinant.MultiTenancyInterceptor;

@Configuration
@EnableWebMvc
public class ResourceServerWebConfig extends WebMvcConfigurerAdapter {
    //
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new MultiTenancyInterceptor());
	}
	
}
