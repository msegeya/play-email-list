package configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.Play;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

/**
 * Configures the Hibernate to manage the database connection.
 */
@Configuration
@EnableTransactionManagement
public class DataConfig {

    final Logger log = LoggerFactory.getLogger(DataConfig.class);

    /**
     * Bean of a Entity Manager Factory that creates an entity manager.
     */
    @Bean
    public EntityManagerFactory entityManagerFactory() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setShowSql(false);
        vendorAdapter.setGenerateDdl(true);
        vendorAdapter.setDatabasePlatform(Play.application().configuration().getString("db.default.dialect"));
        LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactory.setPackagesToScan("models");
        entityManagerFactory.setJpaVendorAdapter(vendorAdapter);
        entityManagerFactory.setDataSource(dataSource());
        entityManagerFactory.afterPropertiesSet();
        return entityManagerFactory.getObject();
    }

    /**
     * Bean of a transactionManager to handle the transactions.
     */
    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager(entityManagerFactory());

        return transactionManager;
    }

    /**
     * Data source bean, stores and handles the data source connection.
     */
    @Bean
    public DataSource dataSource() {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(Play.application().configuration().getString("db.default.driver"));
        dataSource.setUrl(Play.application().configuration().getString("db.default.url"));
        dataSource.setUsername(Play.application().configuration().getString("db.default.user"));
        dataSource.setPassword(Play.application().configuration().getString("db.default.password"));
        log.info("Created DataSource");
        return dataSource;
    }
}
