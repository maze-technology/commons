package tech.maze.commons.mappers;

import com.google.protobuf.ByteString;
import com.google.protobuf.Duration;
import com.google.protobuf.Timestamp;
import java.math.BigInteger;
import java.time.Instant;
import org.mapstruct.MapperConfig;
import org.mapstruct.Named;
import tech.maze.dtos.commons.math.BigDecimal;

/**
 * ProtobufMapperConfig is an interface that provides mapping configurations for Protobuf classes.
 */
@MapperConfig
public interface ProtobufMapperConfig {
  /**
   * Converts an {@link Instant} to a {@link Timestamp}.
   *
   * @param instant the Instant to convert
   * @return the converted Timestamp
   */
  @Named("instantToTimestamp")
  default Timestamp instantToTimestamp(Instant instant) {
    if (instant == null) {
      return null;
    }

    return Timestamp.newBuilder()
      .setSeconds(instant.getEpochSecond())
      .setNanos(instant.getNano())
      .build();
  }

  /**
   * Converts a {@link Timestamp} object to an {@link Instant} object.
   *
   * @param timestamp the Timestamp to convert
   * @return the converted Instant
   */
  @Named("timestampToInstant")
  default Instant timestampToInstant(Timestamp timestamp) {
    if (timestamp == null) {
      return null;
    }

    return Instant.ofEpochSecond(
      timestamp.getSeconds(),
      timestamp.getNanos()
    );
  }

  /**
   * Converts an {@link java.time.Duration} to a {@link Duration}.
   *
   * @param duration the Duration to convert
   * @return the converted Protobuf Duration
   */
  @Named("DurationToProtoDuration")
  default Duration durationToProtoDuration(java.time.Duration duration) {
    if (duration == null) {
      return null;
    }

    return Duration.newBuilder()
      .setSeconds(duration.getSeconds())
      .setNanos(duration.getNano())
      .build();
  }

  /**
   * Converts a {@link Duration} object to an {@link java.time.Duration} object.
   *
   * @param protoDuration the Protobuf Duration to convert
   * @return the converted Duration
   */
  @Named("protoDurationToDuration")
  default java.time.Duration protoDurationToDuration(Duration protoDuration) {
    if (protoDuration == null) {
      return null;
    }

    return java.time.Duration
      .ofSeconds(
        protoDuration.getSeconds(),
        protoDuration.getNanos()
      );
  }

  /**
   * Converts a {@link java.math.BigDecimal} to a {@link BigDecimal}.
   *
   * @param bigDecimal the BigDecimal to convert
   * @return the converted Protobuf BigDecimal
   */
  @Named("BigDecimalToProtoBigDecimal")
  default BigDecimal bigDecimalToProtoBigDecimal(java.math.BigDecimal bigDecimal) {
    if (bigDecimal == null) {
      return null;
    }

    return BigDecimal.newBuilder()
      .setScale(bigDecimal.scale())
      .setUnscaledValue(ByteString.copyFrom(bigDecimal
        .unscaledValue()
        .toByteArray()))
      .build();
  }

  /**
   * Converts a {@link BigDecimal} object to a {@link java.math.BigDecimal} object.
   *
   * @param protoBigDecimal the Protobuf BigDecimal to convert
   * @return the converted BigDecimal
   */
  @Named("protoBigDecimalToBigDecimal")
  default java.math.BigDecimal protoBigDecimalToBigDecimal(BigDecimal protoBigDecimal) {
    if (protoBigDecimal == null) {
      return null;
    }

    final byte[] unscaledValueBytes = protoBigDecimal
        .getUnscaledValue()
        .toByteArray();

    final BigInteger unscaledValue = unscaledValueBytes.length == 0
        ? BigInteger.ZERO
        : new BigInteger(unscaledValueBytes);

    return new java.math.BigDecimal(
      unscaledValue,
      protoBigDecimal.getScale()
    );
  }
}
