package tech.maze.commons.grpc;

import net.devh.boot.grpc.server.advice.GrpcAdvice;
import io.grpc.Status;
import io.grpc.StatusException;
import jakarta.validation.ConstraintViolationException;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;

@GrpcAdvice
public class GlobalGrpcExceptionHandler {
  /**
   * Handle ConstraintViolationException raised by validation and pipe it to GRPC.
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
   */
  @GrpcExceptionHandler(IllegalArgumentException.class)
  public StatusException handleIllegalArgumentException(IllegalArgumentException e) {
    return Status.INVALID_ARGUMENT
        .withDescription(e.getMessage())
        .withCause(e)
        .asException();
  }
}
