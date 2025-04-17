package tech.maze.commons.mappers;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.google.protobuf.ByteString;
import com.google.protobuf.Duration;
import com.google.protobuf.Timestamp;
import java.math.BigInteger;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import tech.maze.dtos.commons.math.BigDecimal;

/**
 * Unit tests for the ProtobufMapperConfig class.
 */
public class ProtobufMapperConfigTest {
  private final ProtobufMapperConfig mapper = new ProtobufMapperConfig() {};

  @Test
  public void testInstantToProtobufTimestamp() {
    final Instant instant = Instant.now();
    final Timestamp protobufTimestamp = mapper.instantToProtobufTimestamp(instant);

    assertNotNull(protobufTimestamp);
    assertEquals(instant.getEpochSecond(), protobufTimestamp.getSeconds());
    assertEquals(instant.getNano(), protobufTimestamp.getNanos());
  }

  @Test
  public void testProtobufTimestampToInstant() {
    final Timestamp protobufTimestamp = Timestamp.newBuilder()
        .setSeconds(1234567890L)
        .setNanos(123456789)
        .build();
    final Instant instant = mapper.protobufTimestampToInstant(protobufTimestamp);

    assertNotNull(instant);
    assertEquals(protobufTimestamp.getSeconds(), instant.getEpochSecond());
    assertEquals(protobufTimestamp.getNanos(), instant.getNano());
  }

  @Test
  public void testDurationToProtobufDuration() {
    final java.time.Duration duration = java.time.Duration.ofSeconds(123, 456789);
    final Duration protobufDuration = mapper.durationToProtobufDuration(duration);

    assertNotNull(protobufDuration);
    assertEquals(duration.getSeconds(), protobufDuration.getSeconds());
    assertEquals(duration.getNano(), protobufDuration.getNanos());
  }

  @Test
  public void testProtobufDurationToDuration() {
    final Duration protobufDuration = Duration.newBuilder()
        .setSeconds(1234567890L)
        .setNanos(123456789)
        .build();
    final java.time.Duration duration = mapper.protobufDurationToDuration(protobufDuration);

    assertNotNull(duration);
    assertEquals(protobufDuration.getSeconds(), duration.getSeconds());
    assertEquals(protobufDuration.getNanos(), duration.getNano());
  }

  @Test
  public void testBigDecimalToProtobufBigDecimal() {
    final java.math.BigDecimal bigDecimal = new java.math.BigDecimal("12345.6789");
    final BigDecimal protobufBigDecimal = mapper.bigDecimalToProtobufBigDecimal(bigDecimal);

    assertNotNull(protobufBigDecimal);
    assertEquals(bigDecimal.scale(), protobufBigDecimal.getScale());
    assertArrayEquals(
        bigDecimal.unscaledValue().toByteArray(),
        protobufBigDecimal.getUnscaledValue().toByteArray()
    );
  }

  @Test
  public void testProtobufBigDecimalToBigDecimal() {
    final BigDecimal protobufBigDecimal = BigDecimal.newBuilder()
        .setScale(4)
        .setUnscaledValue(ByteString.copyFrom(new BigInteger("123456789").toByteArray()))
        .build();
    final java.math.BigDecimal bigDecimal = mapper
        .protobufBigDecimalToBigDecimal(protobufBigDecimal);

    assertNotNull(bigDecimal);
    assertEquals(protobufBigDecimal.getScale(), bigDecimal.scale());
    assertEquals(
        new BigInteger(protobufBigDecimal.getUnscaledValue().toByteArray()),
        bigDecimal.unscaledValue()
    );
  }
}
