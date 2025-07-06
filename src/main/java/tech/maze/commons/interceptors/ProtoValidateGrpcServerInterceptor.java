package tech.maze.commons.interceptors;

import build.buf.protovalidate.ValidationResult;
import build.buf.protovalidate.Validator;
import build.buf.protovalidate.exceptions.ValidationException;
import com.google.protobuf.Any;
import com.google.protobuf.Message;
import com.google.rpc.Code;
import com.google.rpc.Status;
import io.grpc.ForwardingServerCallListener.SimpleForwardingServerCallListener;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.StatusRuntimeException;
import io.grpc.protobuf.StatusProto;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor;
import org.springframework.stereotype.Component;

/**
 * A gRPC server interceptor that validates incoming protobuf messages using the protovalidate
 * library.
 *
 * <p>This interceptor:
 * - Validates all incoming protobuf messages against their defined validation rules
 * - Returns INVALID_ARGUMENT status with validation details if validation fails
 * - Returns INTERNAL status if validation process encounters an error
 * - Allows the request to proceed if validation succeeds
 * </p>
 *
 * <p>This interceptor is registered as a global server interceptor in the application context.
 * </p>
 */
@Component
@GrpcGlobalServerInterceptor
@RequiredArgsConstructor
public class ProtoValidateGrpcServerInterceptor implements ServerInterceptor {
  private final Validator validator;

  @Override
  public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
      ServerCall<ReqT, RespT> call,
      Metadata headers,
      ServerCallHandler<ReqT, RespT> next) {

    final ServerCall.Listener<ReqT> delegate = next.startCall(call, headers);

    return new SimpleForwardingServerCallListener<ReqT>(delegate) {
      private boolean rejected = false;

      @Override
      public void onMessage(ReqT message) {
        if (rejected) {
          return;
        }

        if (!(message instanceof Message protoMsg)) {
          reject(io.grpc.Status.INTERNAL.withDescription(
              "Expected protobuf Message, got " + message.getClass().getName()));

          return;
        }

        try {
          final ValidationResult result = validator.validate(protoMsg);

          if (result.isSuccess()) {
            super.onMessage(message);
          } else {
            final Status status = Status.newBuilder()
                .setCode(Code.INVALID_ARGUMENT.getNumber())
                .setMessage(Code.INVALID_ARGUMENT.name())
                .addDetails(Any.pack(result.toProto()))
                .build();

            reject(StatusProto.toStatusRuntimeException(status));
          }
        } catch (ValidationException ex) {
          final Status status = Status.newBuilder()
              .setCode(Code.INTERNAL.getNumber())
              .setMessage(ex.getMessage())
              .build();

          reject(StatusProto.toStatusRuntimeException(status));
        }
      }

      @Override
      public void onHalfClose() {
        if (!rejected) {
          super.onHalfClose();
        }
        // else: swallow – we've already closed the call
      }

      @Override
      public void onCancel() {
        if (!rejected) {
          super.onCancel();
        }
      }

      private void reject(io.grpc.Status status) {
        reject(status.asRuntimeException());
      }

      private void reject(StatusRuntimeException ex) {
        rejected = true;

        call.close(ex.getStatus(), ex.getTrailers());
      }
    };
  }
}
