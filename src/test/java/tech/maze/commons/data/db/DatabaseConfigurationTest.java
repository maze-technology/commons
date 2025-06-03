package tech.maze.commons.data.db;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import javax.persistence.EntityManagerFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

  @Test
  @DisplayName("Should create default entity manager factory using builder")
  void shouldCreateDefaultEntityManagerFactory() {
    DatabaseConfiguration config = new DatabaseConfiguration();
    DataSource dataSource = mock(DataSource.class);
    EntityManagerFactoryBuilder builder = mock(EntityManagerFactoryBuilder.class);
    LocalContainerEntityManagerFactoryBean emfBean = new LocalContainerEntityManagerFactoryBean();

    when(builder.dataSource(dataSource)).thenReturn(builder);
    when(builder.packages("tech.maze")).thenReturn(builder);
    when(builder.build()).thenReturn(emfBean);

    LocalContainerEntityManagerFactoryBean result =
        config.defaultEntityManagerFactory(dataSource, builder);

    assertSame(emfBean, result);
    verify(builder).dataSource(dataSource);
    verify(builder).packages("tech.maze");
    verify(builder).build();
  }

  @Test
  @DisplayName("Should create default transaction manager")
  void shouldCreateDefaultTransactionManager() {
    DatabaseConfiguration config = new DatabaseConfiguration();
    LocalContainerEntityManagerFactoryBean emfBean = mock(LocalContainerEntityManagerFactoryBean.class);
    EntityManagerFactory emf = mock(EntityManagerFactory.class);
    when(emfBean.getObject()).thenReturn(emf);

    PlatformTransactionManager txManager = config.defaultTransactionManager(emfBean);

    assertNotNull(txManager);
    assertTrue(txManager instanceof JpaTransactionManager);
    assertSame(emf, ((JpaTransactionManager) txManager).getEntityManagerFactory());
  }

  @Test
  @DisplayName("Should throw NullPointerException when entity manager factory is null")
  void shouldThrowWhenEntityManagerFactoryNull() {
    DatabaseConfiguration config = new DatabaseConfiguration();
    LocalContainerEntityManagerFactoryBean emfBean = mock(LocalContainerEntityManagerFactoryBean.class);
    when(emfBean.getObject()).thenReturn(null);

    assertThrows(NullPointerException.class, () -> config.defaultTransactionManager(emfBean));
  }
}
