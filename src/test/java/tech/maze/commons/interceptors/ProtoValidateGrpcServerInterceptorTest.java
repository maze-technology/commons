package tech.maze.commons.interceptors;

import build.buf.protovalidate.ValidationResult;
import build.buf.protovalidate.Validator;
import build.buf.protovalidate.exceptions.ValidationException;
import com.google.protobuf.Message;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProtoValidateGrpcServerInterceptorTest {
  private ProtoValidateGrpcServerInterceptor interceptor;

  @Mock
  private Validator validator;

  @Mock
  private ServerCall<Message, Message> serverCall;

  @Mock
  private Metadata headers;

  @Mock
  private ServerCallHandler<Message, Message> next;

  @Mock
  private ServerCall.Listener<Message> listener;

  @Mock
  private Message message;

  @Mock
  private Object nonProtobufMessage;

  @Mock
  private ServerCall<Object, Object> serverCallForNonProtobuf;

  @Mock
  private ServerCallHandler<Object, Object> nextForNonProtobuf;

  @Mock
  private ServerCall.Listener<Object> listenerForNonProtobuf;

  @BeforeEach
  void setUp() {
      MockitoAnnotations.openMocks(this);
      interceptor = new ProtoValidateGrpcServerInterceptor(validator);
      when(next.startCall(any(), any())).thenReturn(listener);
      doNothing().when(serverCall).close(any(), any());
  }

  @Test
  @DisplayName("Should proceed with valid protobuf message")
  void interceptCall_WithValidMessage_ShouldProceed() throws ValidationException {
    // Arrange
    final ValidationResult validResult = mock(ValidationResult.class);
    when(validResult.isSuccess()).thenReturn(true);
    when(validator.validate(any())).thenReturn(validResult);

    // Act
    final ServerCall.Listener<Message> result = interceptor.interceptCall(serverCall, headers, next);
    result.onMessage(message);

    // Assert
    assertNotNull(result);
    verify(next).startCall(serverCall, headers);
    verify(validator).validate(message);
    verify(listener).onMessage(message);
  }

  @Test
  @DisplayName("Should reject with INTERNAL error when validation exception occurs")
  void interceptCall_WithValidationException_ShouldRejectWithInternalError() throws ValidationException {
    // Arrange
    when(validator.validate(any())).thenThrow(new ValidationException("Validation error"));

    // Act
    final ServerCall.Listener<Message> result = interceptor.interceptCall(serverCall, headers, next);
    result.onMessage(message);

    // Assert
    assertNotNull(result);
    verify(validator).validate(message);
    verify(serverCall).close(argThat(status ->
        status.getCode() == io.grpc.Status.INTERNAL.getCode()), any(Metadata.class));
    verify(listener, never()).onMessage(any());
  }

  @Test
  @DisplayName("Should reject with INTERNAL error when message is not protobuf")
  void interceptCall_WithNonProtobufMessage_ShouldRejectWithInternalError() {
    // Arrange
    when(nextForNonProtobuf.startCall(any(), any())).thenReturn(listenerForNonProtobuf);
    doNothing().when(serverCallForNonProtobuf).close(any(), any());

    // Act
    final ServerCall.Listener<Object> result = interceptor.interceptCall(serverCallForNonProtobuf, headers, nextForNonProtobuf);
    result.onMessage(nonProtobufMessage);

    // Assert
    assertNotNull(result);
    verify(serverCallForNonProtobuf).close(any(), any());
    verify(listenerForNonProtobuf, never()).onMessage(any());
  }

  @Test
  @DisplayName("Should call onHalfClose when not rejected")
  void onHalfClose_WhenNotRejected_ShouldCallDelegate() {
    // Act
    final ServerCall.Listener<Message> result = interceptor.interceptCall(serverCall, headers, next);
    result.onHalfClose();

    // Assert
    verify(listener).onHalfClose();
  }

  @Test
  @DisplayName("Should call onCancel when not rejected")
  void onCancel_WhenNotRejected_ShouldCallDelegate() {
    // Act
    final ServerCall.Listener<Message> result = interceptor.interceptCall(serverCall, headers, next);
    result.onCancel();

    // Assert
    verify(listener).onCancel();
  }
}
