package tech.maze.commons;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Unit tests for the CommonsConfiguration class.
 */
class CommonsConfigurationTest {
  @Test
  @DisplayName("Should have correct configuration annotations")
  void shouldHaveCorrectAnnotations() {
    // Verify the class has required annotations
    assertTrue(CommonsConfiguration.class.isAnnotationPresent(Configuration.class));
    assertTrue(CommonsConfiguration.class.isAnnotationPresent(ComponentScan.class));

    // Verify ComponentScan configuration
    final ComponentScan annotation = CommonsConfiguration.class.getAnnotation(ComponentScan.class);

    assertNotNull(annotation);
    assertEquals("tech.maze.commons", annotation.value()[0]);
  }

  @Test
  @DisplayName("Should create instance successfully")
  void shouldCreateInstance() {
    final CommonsConfiguration config = new CommonsConfiguration();

    assertNotNull(config);
  }
}
