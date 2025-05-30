package tech.maze.commons.data.db;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.hibernate.cfg.AvailableSettings;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

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
  }

  @Test
  @DisplayName("Should properly configure JSON format mapper for Hibernate")
  void shouldConfigureJsonFormatMapper() {
    DatabaseConfiguration config = new DatabaseConfiguration();
    ObjectMapper objectMapper = new ObjectMapper();

    HibernatePropertiesCustomizer customizer = config.jsonFormatMapperCustomizer(objectMapper);
    assertNotNull(customizer);

    // Create a properties map and verify the customizer adds the JSON format mapper
    java.util.Map<String, Object> properties = new java.util.HashMap<>();
    customizer.customize(properties);

    assertTrue(properties.containsKey(AvailableSettings.JSON_FORMAT_MAPPER));
    assertNotNull(properties.get(AvailableSettings.JSON_FORMAT_MAPPER));
  }
}
