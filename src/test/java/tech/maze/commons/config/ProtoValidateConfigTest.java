package tech.maze.commons.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import build.buf.protovalidate.Validator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Configuration;

/**
 * Unit tests for the ProtoValidateConfig class.
 */
class ProtoValidateConfigTest {
  @Test
  @DisplayName("Should have correct configuration annotations")
  void shouldHaveCorrectAnnotations() {
    // Verify the class has required annotations
    assertTrue(ProtoValidateConfig.class.isAnnotationPresent(Configuration.class));
  }

  @Test
  @DisplayName("Should create validator bean successfully")
  void shouldCreateValidatorBean() {
    final ProtoValidateConfig config = new ProtoValidateConfig();
    final Validator validator = config.validator();

    assertNotNull(validator);
  }
}
