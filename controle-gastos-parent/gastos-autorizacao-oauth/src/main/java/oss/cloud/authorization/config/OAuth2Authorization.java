package oss.cloud.authorization.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import oss.cloud.authorization.security.CustomTokenEnhancer;
import oss.cloud.authorization.security.CustomUserDetailsService;

@Configuration
@EnableAuthorizationServer
public class OAuth2Authorization extends AuthorizationServerConfigurerAdapter {

	
	private static final String CLIENT_ASSOCIADO = "clientAssociado";
	private static final int VALIDADE_TOKEN = 3600;
	private static final String PASS = "password";
	private static final String SCOPE_WRITE = "write";
	private static final String SCOPE_READ = "read";
	private static final String REFRESH_TOKEN = "refresh_token";
	private static final String SECRET = "secret";
	private static final String KEYPASS = "mypass";
	private static final String KEY_PAIR = "mytest";
	private static final String KEYSTORE = "mytest.jks";
	private static final int REFRESH_TOKEN_VALIDADE = 2592000;
	

	@Autowired
	@Qualifier("authenticationManagerBean")
	private AuthenticationManager authenticationManager;

	@Autowired
    private CustomUserDetailsService userDetailsService;	        
	
	@Override
	public void configure(final AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
		oauthServer.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
	}

	@Override
	public void configure(final ClientDetailsServiceConfigurer clients) throws Exception {
		clients
		.inMemory()
		.withClient(CLIENT_ASSOCIADO)
		.secret(SECRET)
//		.resourceIds(RESOURCE)
		.authorizedGrantTypes(PASS, REFRESH_TOKEN)
		.scopes(SCOPE_READ, SCOPE_WRITE)
		.accessTokenValiditySeconds(VALIDADE_TOKEN)
		.refreshTokenValiditySeconds(REFRESH_TOKEN_VALIDADE)
//		.authorities(ROLE_ASSOCIADO)
		
		;
	}

	@Override
	public void configure(final AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

		final TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
		tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer(),accessTokenConverter()));
		endpoints.tokenStore(tokenStore())
		.tokenEnhancer(tokenEnhancerChain)
		.authenticationManager(authenticationManager).userDetailsService(userDetailsService);
	}

	@Bean
	public TokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}

	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		final JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		final KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource(KEYSTORE), KEYPASS.toCharArray());
		converter.setKeyPair(keyStoreKeyFactory.getKeyPair(KEY_PAIR));
		return converter;
	}

	@Bean
	@Primary
	public DefaultTokenServices tokenServices() {
		final DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
		defaultTokenServices.setTokenStore(tokenStore());
		defaultTokenServices.setSupportRefreshToken(true);
		return defaultTokenServices;
	}

	@Bean
	public TokenEnhancer tokenEnhancer() {
		return new CustomTokenEnhancer();
	}

}
