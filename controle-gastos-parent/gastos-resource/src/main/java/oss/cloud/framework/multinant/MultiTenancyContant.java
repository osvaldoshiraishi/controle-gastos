package oss.cloud.framework.multinant;

/**
 * Classe de constants do multitenant
 * 
 * @author osvaldo.shiraishi
 *
 */
public final class MultiTenancyContant {
	
	private MultiTenancyContant(){		
	}
	
	/**
	 * Chave para pegar o current tenant id
	 */
	public static final String CURRENT_TENANT_IDENTIFIER = "CURRENT_TENANT_IDENTIFIER";
	
	/**
	 * default tenant id
	 */
	public static final String DEFAULT_TENANT_ID = "tenant_1";
	
	/**
	 * tenant id do client 2
	 */
	public static final String CLIENT2_TENANT_ID = "tenant_2";
	
	
	/**
	 * tenant id do client 3
	 */
	public static final String CLIENT3_TENANT_ID = "tenant_3";
	
	

}
