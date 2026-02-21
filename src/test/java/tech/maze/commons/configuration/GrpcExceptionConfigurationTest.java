package tech.maze.commons.configuration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.grpc.Status;
import io.grpc.StatusException;
import jakarta.validation.ConstraintViolationException;
import java.lang.reflect.Method;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.grpc.server.exception.GrpcExceptionHandler;
import tech.maze.commons.exceptions.GrpcStatusException;

/**
 * Unit tests for the GrpcExceptionConfiguration class.
 */
class GrpcExceptionConfigurationTest {
  private GrpcExceptionConfiguration configuration;

  @BeforeEach
  void setUp() {
    configuration = new GrpcExceptionConfiguration();
  }

  @Test
  @DisplayName("Should have correct configuration annotations")
  void shouldHaveCorrectAnnotations() {
    // Verify the class has required annotations
    assertTrue(GrpcExceptionConfiguration.class.isAnnotationPresent(Configuration.class));
  }

  @Test
  @DisplayName("Should create constraintViolationExceptionHandler bean successfully")
  void shouldCreateConstraintViolationExceptionHandler() {
    // Act
    final GrpcExceptionHandler handler = configuration.constraintViolationExceptionHandler();

    // Assert
    assertNotNull(handler);
  }

  @Test
  @DisplayName("Should handle ConstraintViolationException and return INVALID_ARGUMENT status")
  void shouldHandleConstraintViolationException() throws Exception {
    // Arrange
    final String errorMessage = "Validation failed";
    final ConstraintViolationException exception =
        new ConstraintViolationException(errorMessage, null);
    final GrpcExceptionHandler handler = configuration.constraintViolationExceptionHandler();

    // Act
    final StatusException result = invokeHandler(handler, exception);

    // Assert
    assertNotNull(result);
    assertEquals(Status.Code.INVALID_ARGUMENT, result.getStatus().getCode());
    assertEquals(errorMessage, result.getStatus().getDescription());
    assertEquals(exception, result.getCause());
  }

  @Test
  @DisplayName("Should return null for non-ConstraintViolationException")
  void shouldReturnNullForNonConstraintViolationException() throws Exception {
    // Arrange
    final IllegalArgumentException exception =
        new IllegalArgumentException("Not a constraint violation");
    final GrpcExceptionHandler handler = configuration.constraintViolationExceptionHandler();

    // Act
    final StatusException result = invokeHandler(handler, exception);

    // Assert
    assertNull(result);
  }

  @Test
  @DisplayName("Should create grpcStatusExceptionHandler bean successfully")
  void shouldCreateGrpcStatusExceptionHandler() {
    final GrpcExceptionHandler handler = configuration.grpcStatusExceptionHandler();
    assertNotNull(handler);
  }

  @Test
  @DisplayName("Should handle GrpcStatusException INVALID_ARGUMENT")
  void shouldHandleGrpcStatusExceptionInvalidArgument() throws Exception {
    final String errorMessage = "Invalid request payload";
    final GrpcStatusException exception = GrpcStatusException.invalidArgument(errorMessage);
    final GrpcExceptionHandler handler = configuration.grpcStatusExceptionHandler();

    final StatusException result = invokeHandler(handler, exception);

    assertNotNull(result);
    assertEquals(Status.Code.INVALID_ARGUMENT, result.getStatus().getCode());
    assertEquals(errorMessage, result.getStatus().getDescription());
    assertEquals(exception, result.getCause());
  }

  @Test
  @DisplayName("Should handle GrpcStatusException NOT_FOUND")
  void shouldHandleGrpcStatusExceptionNotFound() throws Exception {
    final String errorMessage = "Asset not found";
    final GrpcStatusException exception = GrpcStatusException.notFound(errorMessage);
    final GrpcExceptionHandler handler = configuration.grpcStatusExceptionHandler();

    final StatusException result = invokeHandler(handler, exception);

    assertNotNull(result);
    assertEquals(Status.Code.NOT_FOUND, result.getStatus().getCode());
    assertEquals(errorMessage, result.getStatus().getDescription());
    assertEquals(exception, result.getCause());
  }

  @Test
  @DisplayName("Should return null for non-GrpcStatusException")
  void shouldReturnNullForNonGrpcStatusException() throws Exception {
    final RuntimeException exception = new RuntimeException("Not a grpc status exception");
    final GrpcExceptionHandler handler = configuration.grpcStatusExceptionHandler();

    final StatusException result = invokeHandler(handler, exception);

    assertNull(result);
  }

  @Test
  @DisplayName("Should create illegalArgumentExceptionHandler bean successfully")
  void shouldCreateIllegalArgumentExceptionHandler() {
    // Act
    final GrpcExceptionHandler handler = configuration.illegalArgumentExceptionHandler();

    // Assert
    assertNotNull(handler);
  }

  @Test
  @DisplayName("Should handle IllegalArgumentException and return INVALID_ARGUMENT status")
  void shouldHandleIllegalArgumentException() throws Exception {
    // Arrange
    final String errorMessage = "Invalid argument provided";
    final IllegalArgumentException exception = new IllegalArgumentException(errorMessage);
    final GrpcExceptionHandler handler = configuration.illegalArgumentExceptionHandler();

    // Act
    final StatusException result = invokeHandler(handler, exception);

    // Assert
    assertNotNull(result);
    assertEquals(Status.Code.INVALID_ARGUMENT, result.getStatus().getCode());
    assertEquals(errorMessage, result.getStatus().getDescription());
    assertEquals(exception, result.getCause());
  }

  @Test
  @DisplayName("Should return null for non-IllegalArgumentException")
  void shouldReturnNullForNonIllegalArgumentException() throws Exception {
    // Arrange
    final RuntimeException exception = new RuntimeException("Not an illegal argument");
    final GrpcExceptionHandler handler = configuration.illegalArgumentExceptionHandler();

    // Act
    final StatusException result = invokeHandler(handler, exception);

    // Assert
    assertNull(result);
  }

  @Test
  @DisplayName("Should create defaultExceptionHandler bean successfully")
  void shouldCreateDefaultExceptionHandler() {
    // Act
    final GrpcExceptionHandler handler = configuration.defaultExceptionHandler();

    // Assert
    assertNotNull(handler);
  }

  @Test
  @DisplayName("Should have Order annotation with Integer.MAX_VALUE")
  void shouldHaveOrderAnnotation() throws NoSuchMethodException {
    // Arrange
    final var method = GrpcExceptionConfiguration.class
        .getDeclaredMethod("defaultExceptionHandler");

    // Assert
    assertTrue(method.isAnnotationPresent(Order.class));
    final Order orderAnnotation = method.getAnnotation(Order.class);
    assertEquals(Integer.MAX_VALUE, orderAnnotation.value());
  }

  @Test
  @DisplayName("Should handle any exception and return INTERNAL status")
  void shouldHandleAnyExceptionWithInternalStatus() throws Exception {
    // Arrange
    final String errorMessage = "Unexpected error";
    final RuntimeException exception = new RuntimeException(errorMessage);
    final GrpcExceptionHandler handler = configuration.defaultExceptionHandler();

    // Act
    final StatusException result = invokeHandler(handler, exception);

    // Assert
    assertNotNull(result);
    assertEquals(Status.Code.INTERNAL, result.getStatus().getCode());
    assertEquals("An internal error occurred", result.getStatus().getDescription());
    assertEquals(exception, result.getCause());
  }

  @Test
  @DisplayName("Should handle NullPointerException and return INTERNAL status")
  void shouldHandleNullPointerExceptionWithInternalStatus() throws Exception {
    // Arrange
    final NullPointerException exception = new NullPointerException("Null value");
    final GrpcExceptionHandler handler = configuration.defaultExceptionHandler();

    // Act
    final StatusException result = invokeHandler(handler, exception);

    // Assert
    assertNotNull(result);
    assertEquals(Status.Code.INTERNAL, result.getStatus().getCode());
    assertEquals("An internal error occurred", result.getStatus().getDescription());
    assertEquals(exception, result.getCause());
  }

  @Test
  @DisplayName("Should handle ConstraintViolationException in default handler "
      + "when not handled by specific handler")
  void shouldHandleConstraintViolationExceptionInDefaultHandler() throws Exception {
    // Arrange
    final ConstraintViolationException exception =
        new ConstraintViolationException("Constraint violation", null);
    final GrpcExceptionHandler handler = configuration.defaultExceptionHandler();

    // Act
    final StatusException result = invokeHandler(handler, exception);

    // Assert
    assertNotNull(result);
    assertEquals(Status.Code.INTERNAL, result.getStatus().getCode());
    assertEquals("An internal error occurred", result.getStatus().getDescription());
    assertEquals(exception, result.getCause());
  }

  /**
   * Helper method to invoke the GrpcExceptionHandler using reflection.
   * This is necessary because GrpcExceptionHandler is a functional interface
   * and we need to find and invoke its single abstract method.
   *
   * @param handler The GrpcExceptionHandler to invoke
   * @param exception The exception to handle
   * @return The StatusException returned by the handler, or null
   * @throws Exception If reflection fails
   */
  private StatusException invokeHandler(
      final GrpcExceptionHandler handler,
      final Throwable exception)
      throws Exception {
    // Find the single abstract method in the functional interface
    final Method[] methods = GrpcExceptionHandler.class.getMethods();
    Method handlerMethod = null;
    for (final Method method : methods) {
      if (method.getDeclaringClass() != Object.class && !method.isDefault()) {
        handlerMethod = method;
        break;
      }
    }

    if (handlerMethod == null) {
      throw new IllegalStateException("Could not find handler method in GrpcExceptionHandler");
    }

    return (StatusException) handlerMethod.invoke(handler, exception);
  }
}
