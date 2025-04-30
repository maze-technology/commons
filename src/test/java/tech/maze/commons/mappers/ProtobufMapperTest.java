package tech.maze.commons.mappers;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.google.protobuf.ByteString;
import com.google.protobuf.Duration;
import com.google.protobuf.Timestamp;
import java.math.BigInteger;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tech.maze.dtos.commons.math.BigDecimal;

/**
 * Unit tests for the ProtobufMapper class.
 */
public class ProtobufMapperTest {
  private final ProtobufMapper mapper = ProtobufMapper.INSTANCE;

  @Test
  @DisplayName("Should convert Instant to Protobuf Timestamp")
  public void testMapInstantToTimestamp() {
    // Given
    final Instant instant = Instant.now();

    // When
    final Timestamp protobufTimestamp = mapper.map(instant);

    // Then
    assertNotNull(protobufTimestamp);
    assertEquals(instant.getEpochSecond(), protobufTimestamp.getSeconds());
    assertEquals(instant.getNano(), protobufTimestamp.getNanos());
  }

  @Test
  @DisplayName("Should return null when converting null Instant to Timestamp")
  public void testMapNullInstantToTimestamp() {
    // When
    final Timestamp protobufTimestamp = mapper.map((Instant) null);

    // Then
    assertNull(protobufTimestamp);
  }

  @Test
  @DisplayName("Should convert Protobuf Timestamp to Instant")
  public void testMapTimestampToInstant() {
    // Given
    final Timestamp protobufTimestamp = Timestamp.newBuilder()
        .setSeconds(1234567890L)
        .setNanos(123456789)
        .build();

    // When
    final Instant instant = mapper.map(protobufTimestamp);

    // Then
    assertNotNull(instant);
    assertEquals(protobufTimestamp.getSeconds(), instant.getEpochSecond());
    assertEquals(protobufTimestamp.getNanos(), instant.getNano());
  }

  @Test
  @DisplayName("Should return null when converting null Timestamp to Instant")
  public void testMapNullTimestampToInstant() {
    // When
    final Instant instant = mapper.map((Timestamp) null);

    // Then
    assertNull(instant);
  }

  @Test
  @DisplayName("Should convert Java Duration to Protobuf Duration")
  public void testMapDurationToProtobufDuration() {
    // Given
    final java.time.Duration duration = java.time.Duration.ofSeconds(123, 456789);

    // When
    final Duration protobufDuration = mapper.map(duration);

    // Then
    assertNotNull(protobufDuration);
    assertEquals(duration.getSeconds(), protobufDuration.getSeconds());
    assertEquals(duration.getNano(), protobufDuration.getNanos());
  }

  @Test
  @DisplayName("Should return null when converting null Duration to Protobuf Duration")
  public void testMapNullDurationToProtobufDuration() {
    // When
    final Duration protobufDuration = mapper.map((java.time.Duration) null);

    // Then
    assertNull(protobufDuration);
  }

  @Test
  @DisplayName("Should convert Protobuf Duration to Java Duration")
  public void testMapProtobufDurationToDuration() {
    // Given
    final Duration protobufDuration = Duration.newBuilder()
        .setSeconds(1234567890L)
        .setNanos(123456789)
        .build();

    // When
    final java.time.Duration duration = mapper.map(protobufDuration);

    // Then
    assertNotNull(duration);
    assertEquals(protobufDuration.getSeconds(), duration.getSeconds());
    assertEquals(protobufDuration.getNanos(), duration.getNano());
  }

  @Test
  @DisplayName("Should return null when converting null Protobuf Duration to Java Duration")
  public void testMapNullProtobufDurationToDuration() {
    // When
    final java.time.Duration duration = mapper.map((Duration) null);

    // Then
    assertNull(duration);
  }

  @Test
  @DisplayName("Should convert Java BigDecimal to Protobuf BigDecimal")
  public void testMapBigDecimalToProtobufBigDecimal() {
    // Given
    final java.math.BigDecimal bigDecimal = new java.math.BigDecimal("12345.6789");

    // When
    final BigDecimal protobufBigDecimal = mapper.map(bigDecimal);

    // Then
    assertNotNull(protobufBigDecimal);
    assertEquals(bigDecimal.scale(), protobufBigDecimal.getScale());
    assertArrayEquals(
        bigDecimal.unscaledValue().toByteArray(),
        protobufBigDecimal.getUnscaledValue().toByteArray()
    );
  }

  @Test
  @DisplayName("Should handle zero value when converting Java BigDecimal to Protobuf BigDecimal")
  public void testMapZeroBigDecimalToProtobufBigDecimal() {
    // Given
    final java.math.BigDecimal bigDecimal = java.math.BigDecimal.ZERO;

    // When
    final BigDecimal protobufBigDecimal = mapper.map(bigDecimal);

    // Then
    assertNotNull(protobufBigDecimal);
    assertEquals(bigDecimal.scale(), protobufBigDecimal.getScale());
    assertArrayEquals(
        bigDecimal.unscaledValue().toByteArray(),
        protobufBigDecimal.getUnscaledValue().toByteArray()
    );
  }

  @Test
  @DisplayName("Should return null when converting null Java BigDecimal to Protobuf BigDecimal")
  public void testMapNullBigDecimalToProtobufBigDecimal() {
    // When
    final BigDecimal protobufBigDecimal = mapper.map((java.math.BigDecimal) null);

    // Then
    assertNull(protobufBigDecimal);
  }

  @Test
  @DisplayName("Should convert Protobuf BigDecimal to Java BigDecimal")
  public void testMapProtobufBigDecimalToBigDecimal() {
    // Given
    final BigDecimal protobufBigDecimal = BigDecimal.newBuilder()
        .setScale(4)
        .setUnscaledValue(ByteString.copyFrom(new BigInteger("123456789").toByteArray()))
        .build();

    // When
    final java.math.BigDecimal bigDecimal = mapper.map(protobufBigDecimal);

    // Then
    assertNotNull(bigDecimal);
    assertEquals(protobufBigDecimal.getScale(), bigDecimal.scale());
    assertEquals(
        new BigInteger(protobufBigDecimal.getUnscaledValue().toByteArray()),
        bigDecimal.unscaledValue()
    );
  }

  @Test
  @DisplayName("Should handle empty unscaled value in Protobuf BigDecimal conversion")
  public void testMapProtobufBigDecimalWithEmptyUnscaledValueToBigDecimal() {
    // Given
    final BigDecimal protobufBigDecimal = BigDecimal.newBuilder()
        .setScale(4)
        .setUnscaledValue(ByteString.EMPTY)
        .build();

    // When
    final java.math.BigDecimal bigDecimal = mapper.map(protobufBigDecimal);

    // Then
    assertNotNull(bigDecimal);
    assertEquals(protobufBigDecimal.getScale(), bigDecimal.scale());
    assertEquals(java.math.BigDecimal.ZERO.setScale(4), bigDecimal);
  }

  @Test
  @DisplayName("Should return null when converting null Protobuf BigDecimal to Java BigDecimal")
  public void testMapNullProtobufBigDecimalToBigDecimal() {
    // When
    final java.math.BigDecimal bigDecimal = mapper.map((BigDecimal) null);

    // Then
    assertNull(bigDecimal);
  }
}
