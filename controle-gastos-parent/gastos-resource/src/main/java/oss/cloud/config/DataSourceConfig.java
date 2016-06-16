package oss.cloud.config;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import javax.sql.DataSource;
import org.hibernate.MultiTenancyStrategy;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.AnnotationTransactionAttributeSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;
import org.springframework.transaction.interceptor.CompositeTransactionAttributeSource;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import oss.cloud.framework.multinant.DataSourceBasedMultiTenantConnectionProviderImpl;
import oss.cloud.framework.multinant.MultiTenancyContant;

/**
 * Configurações dos data sources e do transaction manager do sistema
 * 
 * @see DataSourceBasedMultiTenantConnectionProviderImpl
 * @see MultiTenancyContant
 */
/*@Configuration
@ComponentScan("org.spcbrasil.recuperacaocredito")
@EnableTransactionManagement
@EnableAspectJAutoProxy
@ImportResource(locations = { "classpath:applicationAspect.xml" })
@PropertySource({
		"classpath:application.properties"})
@EnableConfigurationProperties(JpaProperties.class)*/
public class DataSourceConfig implements TransactionManagementConfigurer {

	@Autowired
	Environment environment;

	@Autowired
	private JpaProperties jpaProperties;

	@Autowired
	private MultiTenantConnectionProvider multiTenantConnectionProvider;

	@Autowired
	private CurrentTenantIdentifierResolver currentTenantIdentifierResolver;

	@Autowired
	private EntityManagerFactoryBuilder builder;

	/**
	 * Data source do Cliente 1 (Default)
	 * 
	 * @return
	 * @throws FileNotFoundException
	 * @throws SQLException
	 */
	@Bean(name = { "dataSource", "dataSource1" }, destroyMethod = "shutdown")
	public DataSource dataSource() // NOSONAR
			throws FileNotFoundException, SQLException {
		//TODO CONFIGURAR OS DATA SOURCES.		
		return null;
	}

	/**
	 * Data source do Cliente 2
	 * 
	 * @return
	 * @throws FileNotFoundException
	 * @throws SQLException
	 */
	@Bean(name = "dataSource2", destroyMethod = "shutdown")
	public DataSource dataSource2()// NOSONAR
			throws FileNotFoundException, SQLException {
		//TODO CONFIGURAR OS DATA SOURCES.
		return null;
	}

	/**
	 * Data source do Cliente 3
	 * 
	 * @return
	 * @throws FileNotFoundException
	 * @throws SQLException
	 */
	@Bean(name = "dataSource3", destroyMethod = "shutdown")
	public DataSource dataSource3()// NOSONAR
			throws FileNotFoundException, SQLException {
		//TODO CONFIGURAR OS DATA SOURCES.
		return null;
	}

	/**
	 * Configuração do Entity Manager do JPA
	 * 
	 * @return LocalContainerEntityManagerFactoryBean
	 * @throws FileNotFoundException
	 *             caso não encontre o key do banco de dados
	 * @throws SQLException
	 */
	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory()//NOSONAR
			throws FileNotFoundException, SQLException {
		Map<String, Object> hibernateProperties = new LinkedHashMap<>();
		hibernateProperties.putAll(jpaProperties
				.getHibernateProperties(dataSource()));
		hibernateProperties.put(org.hibernate.cfg.Environment.DIALECT,
				"org.hibernate.dialect.Oracle10gDialect");
		hibernateProperties.put(org.hibernate.cfg.Environment.SHOW_SQL,
				environment.getProperty("hibernate.show.sql"));
		hibernateProperties.put(org.hibernate.cfg.Environment.FORMAT_SQL,
				"true");
		hibernateProperties.put(org.hibernate.cfg.Environment.USE_SQL_COMMENTS,
				"false");
		hibernateProperties.put(
				org.hibernate.cfg.Environment.GENERATE_STATISTICS, "false");
		hibernateProperties.put(org.hibernate.cfg.Environment.MULTI_TENANT,
				MultiTenancyStrategy.DATABASE);
		hibernateProperties.put(
				org.hibernate.cfg.Environment.MULTI_TENANT_CONNECTION_PROVIDER,
				multiTenantConnectionProvider);
		hibernateProperties.put(
				org.hibernate.cfg.Environment.MULTI_TENANT_IDENTIFIER_RESOLVER,
				currentTenantIdentifierResolver);

		//TODO CONFIGURAR OS PACKAGES QUE SERAO SCANEADOS
		return builder.dataSource(dataSource())
				.packages("oss.cloud")
				.properties(hibernateProperties).jta(false).build();

	}

	/**
	 * JPA Transaction Manager
	 * 
	 * @return
	 * @throws FileNotFoundException
	 * @throws SQLException
	 */
	@Bean
	public JpaTransactionManager txManager() throws FileNotFoundException, //NOSONAR
			SQLException {

		JpaTransactionManager h = new JpaTransactionManager();
		h.setEntityManagerFactory(this.entityManagerFactory().getObject());
		h.setNestedTransactionAllowed(true);
		return h;
	}

	/**
	 * Return the default transaction manager bean to use for annotation-driven
	 * database transaction management @link
	 * TransactionManagementConfigurer.annotationDrivenTransactionManager()
	 */
	@Override
	public PlatformTransactionManager annotationDrivenTransactionManager() {
		try {
			return txManager();
		} catch (FileNotFoundException | SQLException e) {
			throw new IllegalStateException(e);
		}

	}

	/**
	 * Transaction Intercepator
	 * 
	 * @return
	 * @throws FileNotFoundException
	 * @throws SQLException
	 */
	@Bean(name = "transactionInterceptor")
	public TransactionInterceptor transactionInterceptor() //NOSONAR
			throws FileNotFoundException, SQLException {
		TransactionInterceptor interceptor = new TransactionInterceptor();
		interceptor.setTransactionManager(txManager());
		TransactionAttributeSource[] transactionAttributeSources = new TransactionAttributeSource[2];
		transactionAttributeSources[0] = new AnnotationTransactionAttributeSource();
		NameMatchTransactionAttributeSource n = new NameMatchTransactionAttributeSource();
		Properties transactionAttributes = new Properties();
		transactionAttributes.setProperty("find*",
				"PROPAGATION_REQUIRED,readOnly");
		transactionAttributes.setProperty("detail*",
				"PROPAGATION_REQUIRED,readOnly");
		transactionAttributes.setProperty("*", "PROPAGATION_REQUIRED");
		n.setProperties(transactionAttributes);
		transactionAttributeSources[1] = n;
		CompositeTransactionAttributeSource transactionAttributeSource = new CompositeTransactionAttributeSource(
				transactionAttributeSources);
		interceptor.setTransactionAttributeSource(transactionAttributeSource);
		return interceptor;
	}

}
