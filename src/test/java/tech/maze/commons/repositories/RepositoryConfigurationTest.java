package tech.maze.commons.repositories;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

class RepositoryConfigurationTest {
  @Test
  @DisplayName("Should have correct configuration annotations and repository settings")
  void shouldHaveCorrectAnnotations() {
    // Verify the class has required annotations
    assertTrue(RepositoryConfiguration.class.isAnnotationPresent(Configuration.class));
    assertTrue(RepositoryConfiguration.class.isAnnotationPresent(EnableJpaRepositories.class));

    // Verify EnableJpaRepositories configuration
    EnableJpaRepositories annotation = RepositoryConfiguration.class
        .getAnnotation(EnableJpaRepositories.class);
    assertTrue(annotation.entityManagerFactoryRef().equals("defaultEntityManagerFactory"));
    assertTrue(annotation.transactionManagerRef().equals("defaultTransactionManager"));
  }
}
