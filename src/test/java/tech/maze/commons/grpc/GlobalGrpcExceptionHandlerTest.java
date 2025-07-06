package tech.maze.commons.grpc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.grpc.StatusException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class GlobalGrpcExceptionHandlerTest {
  private GlobalGrpcExceptionHandler handler;

  @Mock
  private ConstraintViolation<?> constraintViolation;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    handler = new GlobalGrpcExceptionHandler();
  }

  @Test
  @DisplayName("Should return INVALID_ARGUMENT status when handling ConstraintViolationException")
  void handleConstraintViolationExceptionShouldReturnInvalidArgumentStatus() {
    // Arrange
    final String errorMessage = "Validation failed";
    final Set<ConstraintViolation<?>> violations = new HashSet<>();
    violations.add(constraintViolation);
    final ConstraintViolationException exception = new ConstraintViolationException(
        errorMessage,
        violations
    );

    // Act
    final StatusException result = handler.handleConstraintViolationException(exception);

    // Assert
    assertNotNull(result);
    assertEquals(
        io.grpc.Status.INVALID_ARGUMENT.getCode(),
        result.getStatus().getCode()
    );
    assertEquals(errorMessage, result.getStatus().getDescription());
  }

  @Test
  @DisplayName("Should return INVALID_ARGUMENT status when handling IllegalArgumentException")
  void handleIllegalArgumentExceptionShouldReturnInvalidArgumentStatus() {
    // Arrange
    final String errorMessage = "Invalid argument provided";
    final IllegalArgumentException exception = new IllegalArgumentException(errorMessage);

    // Act
    final StatusException result = handler.handleIllegalArgumentException(exception);

    // Assert
    assertNotNull(result);
    assertEquals(io.grpc.Status.INVALID_ARGUMENT.getCode(), result.getStatus().getCode());
    assertEquals(errorMessage, result.getStatus().getDescription());
  }
}
