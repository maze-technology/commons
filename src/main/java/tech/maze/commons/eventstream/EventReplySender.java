package tech.maze.commons.eventstream;

import static io.cloudevents.protobuf.ProtobufFormat.PROTO_DATA_CONTENT_TYPE;

import com.google.protobuf.MessageLite;
import io.cloudevents.CloudEvent;
import io.cloudevents.core.builder.CloudEventBuilder;
import io.cloudevents.protobuf.ProtoCloudEventData;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.springframework.cloud.stream.function.StreamBridge;

/**
 * Helper for replying to request events through the event stream.
 */
public final class EventReplySender {
  private EventReplySender() {
  }

  /**
   * Sends a reply to the destination specified by the request's reply-to extension.
   *
   * @param streamBridge the stream bridge to use
   * @param requestEvent the incoming request event
   * @param replyToExtension the extension name to read the reply destination from
   * @param responseType the CloudEvent type for the response
   * @param source the CloudEvent source for the response
   * @param responseMessage the protobuf response payload
   * @return true if a reply was sent, false otherwise
   */
  public static boolean sendReply(
      StreamBridge streamBridge,
      CloudEvent requestEvent,
      String replyToExtension,
      String responseType,
      URI source,
      MessageLite responseMessage
  ) {
    if (streamBridge == null || requestEvent == null || responseMessage == null) {
      throw new IllegalArgumentException("streamBridge, requestEvent, and responseMessage are required");
    }

    final String replyTo = getExtensionAsString(requestEvent, replyToExtension);
    if (replyTo == null || replyTo.isBlank()) {
      return false;
    }

    final CloudEvent responseEvent = CloudEventBuilder.v1()
        .withId(UUID.randomUUID().toString())
        .withSource(source)
        .withType(responseType)
        .withTime(OffsetDateTime.now())
        .withDataContentType(PROTO_DATA_CONTENT_TYPE)
        .withData(ProtoCloudEventData.wrap(responseMessage))
        .withExtension("correlationid", requestEvent.getId())
        .build();

    return streamBridge.send(replyTo, responseEvent);
  }

  private static String getExtensionAsString(CloudEvent event, String key) {
    if (key == null || key.isBlank()) {
      return null;
    }

    final Object value = event.getExtension(key);
    if (value == null) {
      return null;
    }

    return value.toString();
  }
}
