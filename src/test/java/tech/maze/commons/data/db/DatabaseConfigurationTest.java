package tech.maze.commons.data.db;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

class DatabaseConfigurationTest {
  @Test
  @DisplayName("Should have correct configuration annotations and repository settings")
  void shouldHaveCorrectAnnotations() {
    // Verify the class has required annotations
    assertTrue(DatabaseConfiguration.class.isAnnotationPresent(Configuration.class));
    assertTrue(DatabaseConfiguration.class.isAnnotationPresent(EnableJpaAuditing.class));
    assertTrue(DatabaseConfiguration.class.isAnnotationPresent(EnableJpaRepositories.class));

    // Verify EnableJpaRepositories configuration
    EnableJpaRepositories annotation = DatabaseConfiguration.class
        .getAnnotation(EnableJpaRepositories.class);
    assertEquals("defaultEntityManagerFactory", annotation.entityManagerFactoryRef());
    assertEquals("defaultTransactionManager", annotation.transactionManagerRef());
    assertEquals("tech.maze", annotation.basePackages()[0]);
  }

  @Test
  @DisplayName("Should create and configure data source properties")
  void shouldCreateDataSourceProperties() {
    DatabaseConfiguration config = new DatabaseConfiguration();
    DataSourceProperties properties = config.dataSourceProperties();

    assertNotNull(properties);
  }

  @Test
  @DisplayName("Should create and configure default data source")
  void shouldCreateDefaultDataSource() {
    DatabaseConfiguration config = new DatabaseConfiguration();
    DataSourceProperties properties = config.dataSourceProperties();

    // Set required properties for the data source
    properties.setUrl("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
    properties.setUsername("sa");
    properties.setPassword("");
    properties.setDriverClassName("org.h2.Driver");

    DataSource dataSource = config.defaultDataSource(properties);

    assertNotNull(dataSource);
    assertTrue(dataSource instanceof HikariDataSource);
  }
}
