package tech.maze.commons.data.db;

import com.zaxxer.hikari.HikariDataSource;
import java.util.Objects;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.jdbc.autoconfigure.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

/** Configuration class for database-related beans and settings. */
@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(
    basePackages = "tech.maze",
    entityManagerFactoryRef = "defaultEntityManagerFactory",
    transactionManagerRef = "defaultTransactionManager")
@ConditionalOnProperty(name = "spring.datasource.enabled", havingValue = "true")
public class DatabaseConfiguration {
  /**
   * Creates and configures the data source properties.
   *
   * @return A new instance of DataSourceProperties configured with default settings
   */
  @Bean
  @Primary
  @ConfigurationProperties("spring.datasource.default")
  public DataSourceProperties dataSourceProperties() {
    return new DataSourceProperties();
  }

  /**
   * Creates and configures the primary data source.
   *
   * @param dataSourceProperties The properties to configure the data source
   *
   * @return A configured HikariDataSource instance
   */
  @Bean
  @Primary
  @ConfigurationProperties("spring.datasource.default.configuration")
  public DataSource defaultDataSource(DataSourceProperties dataSourceProperties) {
    return dataSourceProperties
        .initializeDataSourceBuilder()
        .type(HikariDataSource.class)
        .build();
  }

  /**
   * Creates and configures the entity manager factory.
   *
   * @param dataSource The data source to use for the entity manager
   * @param builder The EntityManagerFactoryBuilder to create the factory
   *
   * @return A configured LocalContainerEntityManagerFactoryBean instance
   */
  @Bean(name = "defaultEntityManagerFactory")
  @Primary
  public LocalContainerEntityManagerFactoryBean defaultEntityManagerFactory(
      @Qualifier("defaultDataSource") DataSource dataSource,
      EntityManagerFactoryBuilder builder) {
    return builder
        .dataSource(dataSource)
        .packages("tech.maze")
        .build();
  }

  /**
   * Creates and configures the transaction manager.
   *
   * @param em The entity manager factory to use for transactions
   *
   * @return A configured JpaTransactionManager instance
   */
  @Bean(name = "defaultTransactionManager")
  @Primary
  public PlatformTransactionManager defaultTransactionManager(
      @Qualifier("defaultEntityManagerFactory") LocalContainerEntityManagerFactoryBean em) {
    return new JpaTransactionManager(Objects.requireNonNull(em.getObject()));
  }
}
