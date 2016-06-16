package oss.cloud.framework.multinant;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * Recuperação do data source que será usado na sessão atual com base do current tenantid.
 * 
 * 
 * 
 * @author osvaldo.shiraishi
 *
 */
//@Component
public class DataSourceBasedMultiTenantConnectionProviderImpl extends
		AbstractDataSourceBasedMultiTenantConnectionProviderImpl {

	private static final long serialVersionUID = 8168907057647334460L;

	/**
	 * Data source do client 1
	 */
	//@Autowired
	private DataSource dataSource1; //NOSONAR

	/**
	 * Data source do client 2
	 */
	//@Autowired
	private DataSource dataSource2; //NOSONAR

	/**
	 * Data source do client 3
	 */
	//@Autowired
	private DataSource dataSource3; //NOSONAR

	private Map<String, DataSource> map; //NOSONAR
	
	/**
	 * Vincular os data sources com o tenant ID
	 */
	@PostConstruct
	public void load() {
		map = new HashMap<>();
		map.put(MultiTenancyContant.DEFAULT_TENANT_ID, dataSource1);
		map.put(MultiTenancyContant.CLIENT2_TENANT_ID, dataSource2);
		map.put(MultiTenancyContant.CLIENT3_TENANT_ID, dataSource3);
	}

	@Override
	protected DataSource selectAnyDataSource() {
		return map.get(MultiTenancyContant.DEFAULT_TENANT_ID);
	}

	@Override
	protected DataSource selectDataSource(String tenantIdentifier) {
		return map.get(tenantIdentifier);
	}

}
