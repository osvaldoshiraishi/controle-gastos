package oss.cloud.config;

import java.lang.reflect.Method;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import oss.cloud.resource.gastos.model.Gasto;

//@Configuration
//@EnableCaching
public class RedisCacheConfig extends CachingConfigurerSupport {

	private static final String HOST = "127.0.0.1";
	private static final int PORTA = 6379;
	private static final int EXPIRAR_CACHE = 300;

	@Bean
	  public JedisConnectionFactory redisConnectionFactory() {
	    JedisConnectionFactory redisConnectionFactory = new JedisConnectionFactory();

	    redisConnectionFactory.setHostName(HOST);
	    redisConnectionFactory.setPort(PORTA);
	    return redisConnectionFactory;
	  }
	
	
	 @Bean
	  public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory cf) {
	    RedisTemplate<String, String> redisTemplate = new RedisTemplate<String, String>();
	    redisTemplate.setConnectionFactory(cf);
	    return redisTemplate;
	  }

	
	 @Bean
	  public CacheManager cacheManager(RedisTemplate<String, String> redisTemplate) {
	    RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);

	    // Valor em segundos antes de expirar. Valor default para não expirar: (0)
	    cacheManager.setDefaultExpiration(EXPIRAR_CACHE);
	    return cacheManager;
	  }
	 
	 @Bean
	  public KeyGenerator keyGenerator() {
	    return new KeyGenerator() {
	      @Override
	      public Object generate(Object o, Method method, Object... objects) {
	    	  
	    	/*Será gerado uma key unica com o nome da classe nome do metodo e todos os valores passados como parametro no metodo
	    	 *é necessário gerar essa key unica pois a implementação default gera as keys apenas com o valor do parametro
	    	 *nesse caso teriamos problema com metodos diferentes que utilizam o mesmo parametro
	    	 */
	        StringBuilder sb = new StringBuilder();
	        sb.append(o.getClass().getName());
	        sb.append(method.getName());
	        for (Object obj : objects) {
	          sb.append(obj.toString());
	        }
	        return sb.toString();
	      }
	    };
	  }
	 
	 
	 @Bean
	  public KeyGenerator keyGeneratorDispositivo() {
	    return new KeyGenerator() {
	      @Override
	      public Object generate(Object o, Method method, Object... objects) {
	    	  
	    	/*Será gerado uma key unica com o codigo e o nome do dispositivo 
	    	 *é necessário gerar essa key unica pois a implementação default gera as keys apenas com o valor do parametro
	    	 *nesse caso teriamos problema com metodos diferentes que utilizam o mesmo parametro
	    	 */
	    	  
	        StringBuilder sb = new StringBuilder();
	        	        
	        for (Object obj : objects) {
	         
	        	if(obj instanceof Gasto){		
	        		Gasto dispositivo = (Gasto)obj;
	        		sb.append(dispositivo.getLugar());
	        		sb.append(dispositivo.getData());
	        		sb.append(dispositivo.getId());
	        		break;
	        	}
	        }
	        return sb.toString();
	      }
	    };
	  }
}
