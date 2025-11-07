package tech.maze.commons.configuration;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import build.buf.protovalidate.Validator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Configuration;

/**
 * Unit tests for the ProtoValidateConfiguration class.
 */
class ProtoValidateConfigurationTest {
  @Test
  @DisplayName("Should have correct configuration annotations")
  void shouldHaveCorrectAnnotations() {
    // Verify the class has required annotations
    assertTrue(ProtoValidateConfiguration.class.isAnnotationPresent(Configuration.class));
  }

  @Test
  @DisplayName("Should create validator bean successfully")
  void shouldCreateValidatorBean() {
    final ProtoValidateConfiguration config = new ProtoValidateConfiguration();
    final Validator validator = config.validator();

    assertNotNull(validator);
  }
}
