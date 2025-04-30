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
 * Unit tests for the BaseDtoMapper class.
 */
public class BaseDtoMapperTest {
  private final BaseDtoMapper mapper = BaseDtoMapper.INSTANCE;

  @Test
  @DisplayName("Should convert Instant to DTO Timestamp")
  public void testMapInstantToTimestamp() {
    // Given
    final Instant instant = Instant.now();

    // When
    final Timestamp dtoTimestamp = mapper.map(instant);

    // Then
    assertNotNull(dtoTimestamp);
    assertEquals(instant.getEpochSecond(), dtoTimestamp.getSeconds());
    assertEquals(instant.getNano(), dtoTimestamp.getNanos());
  }

  @Test
  @DisplayName("Should return null when converting null Instant to Timestamp")
  public void testMapNullInstantToTimestamp() {
    // When
    final Timestamp dtoTimestamp = mapper.map((Instant) null);

    // Then
    assertNull(dtoTimestamp);
  }

  @Test
  @DisplayName("Should convert DTO Timestamp to Instant")
  public void testMapTimestampToInstant() {
    // Given
    final Timestamp dtoTimestamp = Timestamp.newBuilder()
        .setSeconds(1234567890L)
        .setNanos(123456789)
        .build();

    // When
    final Instant instant = mapper.map(dtoTimestamp);

    // Then
    assertNotNull(instant);
    assertEquals(dtoTimestamp.getSeconds(), instant.getEpochSecond());
    assertEquals(dtoTimestamp.getNanos(), instant.getNano());
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
  @DisplayName("Should convert Java Duration to DTO Duration")
  public void testMapDurationToDtoDuration() {
    // Given
    final java.time.Duration duration = java.time.Duration.ofSeconds(123, 456789);

    // When
    final Duration dtoDuration = mapper.map(duration);

    // Then
    assertNotNull(dtoDuration);
    assertEquals(duration.getSeconds(), dtoDuration.getSeconds());
    assertEquals(duration.getNano(), dtoDuration.getNanos());
  }

  @Test
  @DisplayName("Should return null when converting null Duration to DTO Duration")
  public void testMapNullDurationToDtoDuration() {
    // When
    final Duration dtoDuration = mapper.map((java.time.Duration) null);

    // Then
    assertNull(dtoDuration);
  }

  @Test
  @DisplayName("Should convert DTO Duration to Java Duration")
  public void testMapDtoDurationToDuration() {
    // Given
    final Duration dtoDuration = Duration.newBuilder()
        .setSeconds(1234567890L)
        .setNanos(123456789)
        .build();

    // When
    final java.time.Duration duration = mapper.map(dtoDuration);

    // Then
    assertNotNull(duration);
    assertEquals(dtoDuration.getSeconds(), duration.getSeconds());
    assertEquals(dtoDuration.getNanos(), duration.getNano());
  }

  @Test
  @DisplayName("Should return null when converting null DTO Duration to Java Duration")
  public void testMapNullDtoDurationToDuration() {
    // When
    final java.time.Duration duration = mapper.map((Duration) null);

    // Then
    assertNull(duration);
  }

  @Test
  @DisplayName("Should convert Java BigDecimal to DTO BigDecimal")
  public void testMapBigDecimalToDtoBigDecimal() {
    // Given
    final java.math.BigDecimal bigDecimal = new java.math.BigDecimal("12345.6789");

    // When
    final BigDecimal dtoBigDecimal = mapper.map(bigDecimal);

    // Then
    assertNotNull(dtoBigDecimal);
    assertEquals(bigDecimal.scale(), dtoBigDecimal.getScale());
    assertArrayEquals(
        bigDecimal.unscaledValue().toByteArray(),
        dtoBigDecimal.getUnscaledValue().toByteArray()
    );
  }

  @Test
  @DisplayName("Should handle zero value when converting Java BigDecimal to DTO BigDecimal")
  public void testMapZeroBigDecimalToDtoBigDecimal() {
    // Given
    final java.math.BigDecimal bigDecimal = java.math.BigDecimal.ZERO;

    // When
    final BigDecimal dtoBigDecimal = mapper.map(bigDecimal);

    // Then
    assertNotNull(dtoBigDecimal);
    assertEquals(bigDecimal.scale(), dtoBigDecimal.getScale());
    assertArrayEquals(
        bigDecimal.unscaledValue().toByteArray(),
        dtoBigDecimal.getUnscaledValue().toByteArray()
    );
  }

  @Test
  @DisplayName("Should return null when converting null Java BigDecimal to DTO BigDecimal")
  public void testMapNullBigDecimalToDtoBigDecimal() {
    // When
    final BigDecimal dtoBigDecimal = mapper.map((java.math.BigDecimal) null);

    // Then
    assertNull(dtoBigDecimal);
  }

  @Test
  @DisplayName("Should convert DTO BigDecimal to Java BigDecimal")
  public void testMapDtoBigDecimalToBigDecimal() {
    // Given
    final BigDecimal dtoBigDecimal = BigDecimal.newBuilder()
        .setScale(4)
        .setUnscaledValue(ByteString.copyFrom(new BigInteger("123456789").toByteArray()))
        .build();

    // When
    final java.math.BigDecimal bigDecimal = mapper.map(dtoBigDecimal);

    // Then
    assertNotNull(bigDecimal);
    assertEquals(dtoBigDecimal.getScale(), bigDecimal.scale());
    assertEquals(
        new BigInteger(dtoBigDecimal.getUnscaledValue().toByteArray()),
        bigDecimal.unscaledValue()
    );
  }

  @Test
  @DisplayName("Should handle empty unscaled value in DTO BigDecimal conversion")
  public void testMapDtoBigDecimalWithEmptyUnscaledValueToBigDecimal() {
    // Given
    final BigDecimal dtoBigDecimal = BigDecimal.newBuilder()
        .setScale(4)
        .setUnscaledValue(ByteString.EMPTY)
        .build();

    // When
    final java.math.BigDecimal bigDecimal = mapper.map(dtoBigDecimal);

    // Then
    assertNotNull(bigDecimal);
    assertEquals(dtoBigDecimal.getScale(), bigDecimal.scale());
    assertEquals(java.math.BigDecimal.ZERO.setScale(4), bigDecimal);
  }

  @Test
  @DisplayName("Should return null when converting null DTO BigDecimal to Java BigDecimal")
  public void testMapNullDtoBigDecimalToBigDecimal() {
    // When
    final java.math.BigDecimal bigDecimal = mapper.map((BigDecimal) null);

    // Then
    assertNull(bigDecimal);
  }
}
