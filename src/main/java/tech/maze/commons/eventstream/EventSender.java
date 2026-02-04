package tech.maze.commons.eventstream;

import static io.cloudevents.protobuf.ProtobufFormat.PROTO_DATA_CONTENT_TYPE;

import com.google.protobuf.Message;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.cloudevents.CloudEvent;
import io.cloudevents.core.builder.CloudEventBuilder;
import io.cloudevents.protobuf.ProtoCloudEventData;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

/**
 * Helper for sending CloudEvents on the event stream.
 */
@Component
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@ConditionalOnProperty(prefix = MazeEventProperties.PREFIX, name = "enabled", havingValue = "true")
@SuppressFBWarnings(
    value = "EI_EXPOSE_REP2",
    justification = "Spring-managed dependencies are safe to store in fields."
)
public class EventSender {
  public static final String REPLY_TO_EXTENSION = "replyto";

  StreamBridge streamBridge;
  MazeEventProperties eventProperties;
  RetryTemplate retryTemplate;

  /**
   * Creates an event sender for CloudEvents publishing.
   *
   * @param streamBridge spring cloud stream bridge
   * @param eventProperties event configuration
   * @param retryTemplate retry policy for publishing
   */
  public EventSender(
      StreamBridge streamBridge,
      MazeEventProperties eventProperties,
      RetryTemplate retryTemplate
  ) {
    this.streamBridge = streamBridge;
    this.eventProperties = eventProperties;
    this.retryTemplate = retryTemplate;
  }

  /**
   * Resolves the reply-to destination from an incoming CloudEvent.
   *
   * @param requestEvent the incoming request event
   * @return the reply-to destination or null when not present
   */
  public String resolveReplyTo(CloudEvent requestEvent) {
    if (requestEvent == null) {
      return null;
    }

    return getExtensionAsString(requestEvent, REPLY_TO_EXTENSION);
  }

  /**
   * Sends a CloudEvent to a destination with a derived type from the message.
   *
   * @param destination the target destination (e.g., Kafka topic)
   * @param message the protobuf payload
   * @return true if the event was sent, false otherwise
   */
  public boolean send(String destination, Message message) {
    if (destination == null || destination.isBlank()) {
      throw new IllegalArgumentException("destination is required");
    }
    if (message == null) {
      throw new IllegalArgumentException("message is required");
    }

    final CloudEvent event = CloudEventBuilder.v1()
        .withId(UUID.randomUUID().toString())
        .withSource(eventProperties.getSource())
        .withType(message.getDescriptorForType().getFullName())
        .withTime(OffsetDateTime.now())
        .withDataContentType(PROTO_DATA_CONTENT_TYPE)
        .withData(ProtoCloudEventData.wrap(message))
        .build();

    return retryTemplate.execute(context -> {
      final boolean sent = streamBridge.send(destination, event);
      if (!sent) {
        throw new IllegalStateException("Failed to send event to " + destination);
      }
      return true;
    }, context -> false);
  }

  private String getExtensionAsString(CloudEvent event, String key) {
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
