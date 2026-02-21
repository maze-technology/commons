package tech.maze.commons.exceptions;

import io.grpc.Status;

/**
 * Runtime exception carrying an explicit gRPC status to return to clients.
 */
public class GrpcStatusException extends RuntimeException {
  private final Status status;

  /**
   * Creates a status-aware runtime exception.
   *
   * @param status gRPC status to return
   * @param message error description to expose
   */
  public GrpcStatusException(Status status, String message) {
    super(message);
    this.status = status;
  }

  /**
   * Returns the mapped gRPC status.
   *
   * @return gRPC status
   */
  public Status status() {
    return status;
  }

  /**
   * Factory for INVALID_ARGUMENT errors.
   *
   * @param message error description
   * @return status-aware exception
   */
  public static GrpcStatusException invalidArgument(String message) {
    return new GrpcStatusException(Status.INVALID_ARGUMENT, message);
  }

  /**
   * Factory for NOT_FOUND errors.
   *
   * @param message error description
   * @return status-aware exception
   */
  public static GrpcStatusException notFound(String message) {
    return new GrpcStatusException(Status.NOT_FOUND, message);
  }
}
