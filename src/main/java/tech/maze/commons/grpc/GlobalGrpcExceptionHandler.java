package tech.maze.commons.grpc;

import io.grpc.Status;
import io.grpc.StatusException;
import jakarta.validation.ConstraintViolationException;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;

/**
 * Global exception handler for gRPC services that converts various exceptions to appropriate gRPC
 * status responses. This class provides centralized exception handling for validation and illegal
 * argument exceptions.
 */
@GrpcAdvice
public class GlobalGrpcExceptionHandler {
  /**
   * Handle ConstraintViolationException raised by validation and pipe it to GRPC.
   *
   * @param e The constraint violation exception that was thrown
   * @return A StatusException with INVALID_ARGUMENT status containing the validation error message
   */
  @GrpcExceptionHandler(ConstraintViolationException.class)
  public StatusException handleConstraintViolationException(ConstraintViolationException e) {
    return Status.INVALID_ARGUMENT
        .withDescription(e.getMessage())
        .withCause(e)
        .asException();
  }

  /**
   * Handle IllegalArgumentException raised and pipe it to GRPC.
   *
   * @param e The illegal argument exception that was thrown
   * @return A StatusException with INVALID_ARGUMENT status containing the error message
   */
  @GrpcExceptionHandler(IllegalArgumentException.class)
  public StatusException handleIllegalArgumentException(IllegalArgumentException e) {
    return Status.INVALID_ARGUMENT
        .withDescription(e.getMessage())
        .withCause(e)
        .asException();
  }
}
