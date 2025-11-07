package tech.maze.commons.configuration;

import io.grpc.Status;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.grpc.server.exception.GrpcExceptionHandler;

/**
 * Configuration class for gRPC exception handling.
 */
@Slf4j
@Configuration
public class GrpcExceptionConfiguration {
  /**
   * Handles ConstraintViolationException and converts it to a gRPC INVALID_ARGUMENT status.
   *
   * @param e The ConstraintViolationException that was thrown
   * @return A StatusException with INVALID_ARGUMENT status containing the error message
   */
  @Bean
  GrpcExceptionHandler constraintViolationExceptionHandler() {
    return e -> (e instanceof ConstraintViolationException)
      ? Status.INVALID_ARGUMENT
      .withDescription(e.getMessage())
      .withCause(e)
      .asException()
      : null;
  }

  /**
   * Handles IllegalArgumentException and converts it to a gRPC INVALID_ARGUMENT status.
   *
   * @param e The IllegalArgumentException that was thrown
   * @return A StatusException with INVALID_ARGUMENT status containing the error message
   */
  @Bean
  GrpcExceptionHandler illegalArgumentExceptionHandler() {
    return e -> (e instanceof IllegalArgumentException)
      ? Status.INVALID_ARGUMENT
      .withDescription(e.getMessage())
      .withCause(e)
      .asException()
      : null;
  }

  /**
   * Default exception handler that catches all unhandled exceptions.
   * This handler runs last (highest order) and ensures no exceptions go unhandled.
   *
   * <p>Converts unhandled exceptions to INTERNAL status to prevent exposing
   * internal implementation details to clients while still providing error information.
   *
   * @param e The unhandled exception
   * @return A StatusException with INTERNAL status containing a safe error message
   */
  @Bean
  @Order(Integer.MAX_VALUE)
  GrpcExceptionHandler defaultExceptionHandler() {
    return e -> {
      log.error("Unhandled exception in gRPC service", e);

      return Status.INTERNAL
          .withDescription("An internal error occurred")
          .withCause(e)
          .asException();
    };
  }
}
