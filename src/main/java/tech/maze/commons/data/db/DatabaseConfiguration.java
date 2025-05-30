package tech.maze.commons.data.db;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaxxer.hikari.HikariDataSource;
import java.util.Objects;
import javax.sql.DataSource;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.type.format.jackson.JacksonJsonFormatMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
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
    entityManagerFactoryRef = "defaultEntityManagerFactory",
    transactionManagerRef = "defaultTransactionManager")
@ConditionalOnProperty(name = "spring.datasource.enabled", havingValue = "true")
public class DatabaseConfiguration {
  /** Creates and configures the data source properties. */
  @Bean
  @Primary
  @ConfigurationProperties("spring.datasource.default")
  public DataSourceProperties dataSourceProperties() {
    return new DataSourceProperties();
  }

  /** Configures JSON format mapper for Hibernate. */
  @Bean
  HibernatePropertiesCustomizer jsonFormatMapperCustomizer(ObjectMapper objectMapper) {
    return (properties) -> properties.put(AvailableSettings.JSON_FORMAT_MAPPER,
        new JacksonJsonFormatMapper(objectMapper));
  }

  /** Creates and configures the primary data source. */
  @Bean
  @Primary
  @ConfigurationProperties("spring.datasource.default.configuration")
  public DataSource defaultDataSource(DataSourceProperties dataSourceProperties) {
    return dataSourceProperties
      .initializeDataSourceBuilder().type(HikariDataSource.class).build();
  }

  /** Creates and configures the entity manager factory. */
  @Bean(name = "defaultEntityManagerFactory")
  @Primary
  public LocalContainerEntityManagerFactoryBean defaultEntityManagerFactory(
      @Qualifier("defaultDataSource") DataSource dataSource,
      EntityManagerFactoryBuilder builder) {
    return builder.dataSource(dataSource).packages("tech.maze").build();
  }

  /** Creates and configures the transaction manager. */
  @Bean(name = "defaultTransactionManager")
  @Primary
  public PlatformTransactionManager defaultTransactionManager(
      @Qualifier("defaultEntityManagerFactory") LocalContainerEntityManagerFactoryBean em) {
    return new JpaTransactionManager(Objects.requireNonNull(em.getObject()));
  }
}
