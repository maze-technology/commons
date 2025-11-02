package tech.maze.commons.configuration;

import io.grpc.Status;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.grpc.server.exception.GrpcExceptionHandler;
import jakarta.validation.ConstraintViolationException;

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
}
