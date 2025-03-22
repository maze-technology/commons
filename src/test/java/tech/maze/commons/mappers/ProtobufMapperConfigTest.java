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
import tech.maze.commons.mappers.ProtobufMapperConfig;
import tech.maze.dtos.commons.math.BigDecimal;

/**
 * Unit tests for the ProtobufMapperConfig class.
 */
public class ProtobufMapperConfigTest {
  private final ProtobufMapperConfig mapper = new ProtobufMapperConfig() {};

  @Test
  public void testInstantToTimestamp() {
    final Instant instant = Instant.now();
    final Timestamp timestamp = mapper.instantToTimestamp(instant);

    assertNotNull(timestamp);
    assertEquals(instant.getEpochSecond(), timestamp.getSeconds());
    assertEquals(instant.getNano(), timestamp.getNanos());
  }

  @Test
  public void testTimestampToInstant() {
    final Timestamp timestamp = Timestamp.newBuilder()
        .setSeconds(1234567890L)
        .setNanos(123456789)
        .build();
    final Instant instant = mapper.timestampToInstant(timestamp);

    assertNotNull(instant);
    assertEquals(timestamp.getSeconds(), instant.getEpochSecond());
    assertEquals(timestamp.getNanos(), instant.getNano());
  }

  @Test
  public void testDurationToProtoDuration() {
    final java.time.Duration duration = java.time.Duration.ofSeconds(123, 456789);
    final Duration protoDuration = mapper.durationToProtoDuration(duration);

    assertNotNull(protoDuration);
    assertEquals(duration.getSeconds(), protoDuration.getSeconds());
    assertEquals(duration.getNano(), protoDuration.getNanos());
  }

  @Test
  public void testProtoDurationToDuration() {
    final Duration protoDuration = Duration.newBuilder()
        .setSeconds(1234567890L)
        .setNanos(123456789)
        .build();
    final java.time.Duration duration = mapper.protoDurationToDuration(protoDuration);

    assertNotNull(duration);
    assertEquals(protoDuration.getSeconds(), duration.getSeconds());
    assertEquals(protoDuration.getNanos(), duration.getNano());
  }

  @Test
  public void testBigDecimalToProtoBigDecimal() {
    final java.math.BigDecimal bigDecimal = new java.math.BigDecimal("12345.6789");
    final BigDecimal protoBigDecimal = mapper.bigDecimalToProtoBigDecimal(bigDecimal);

    assertNotNull(protoBigDecimal);
    assertEquals(bigDecimal.scale(), protoBigDecimal.getScale());
    assertArrayEquals(
        bigDecimal.unscaledValue().toByteArray(),
        protoBigDecimal.getUnscaledValue().toByteArray()
    );
  }

  @Test
  public void testProtoBigDecimalToBigDecimal() {
    final BigDecimal protoBigDecimal = BigDecimal.newBuilder()
        .setScale(4)
        .setUnscaledValue(ByteString.copyFrom(new BigInteger("123456789").toByteArray()))
        .build();
    final java.math.BigDecimal bigDecimal = mapper.protoBigDecimalToBigDecimal(protoBigDecimal);

    assertNotNull(bigDecimal);
    assertEquals(protoBigDecimal.getScale(), bigDecimal.scale());
    assertEquals(
        new BigInteger(protoBigDecimal.getUnscaledValue().toByteArray()),
        bigDecimal.unscaledValue()
    );
  }
}
