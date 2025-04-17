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
   * Converts a Java {@link Instant} to a Protobuf {@link Timestamp}.
   *
   * @param instant the Java Instant to convert
   * @return the converted Protobuf Timestamp
   */
  @Named("instantToProtobufTimestamp")
  default Timestamp instantToProtobufTimestamp(Instant instant) {
    if (instant == null) {
      return null;
    }

    return Timestamp.newBuilder()
      .setSeconds(instant.getEpochSecond())
      .setNanos(instant.getNano())
      .build();
  }

  /**
   * Converts a Protobuf {@link Timestamp} object to a Java {@link Instant} object.
   *
   * @param protobufTimestamp the Protobuf Timestamp to convert
   * @return the converted Java Instant
   */
  @Named("protobufTimestampToInstant")
  default Instant protobufTimestampToInstant(Timestamp protobufTimestamp) {
    if (protobufTimestamp == null) {
      return null;
    }

    return Instant.ofEpochSecond(
      protobufTimestamp.getSeconds(),
      protobufTimestamp.getNanos()
    );
  }

  /**
   * Converts a Java {@link java.time.Duration} to a Protobuf {@link Duration}.
   *
   * @param duration the Java Duration to convert
   * @return the converted Protobuf Duration
   */
  @Named("durationToProtobufDuration")
  default Duration durationToProtobufDuration(java.time.Duration duration) {
    if (duration == null) {
      return null;
    }

    return Duration.newBuilder()
      .setSeconds(duration.getSeconds())
      .setNanos(duration.getNano())
      .build();
  }

  /**
   * Converts a Protobuf {@link Duration} object to a Java {@link java.time.Duration} object.
   *
   * @param protobufDuration the Protobuf Duration to convert
   * @return the converted Java Duration
   */
  @Named("protobufDurationToDuration")
  default java.time.Duration protobufDurationToDuration(Duration protobufDuration) {
    if (protobufDuration == null) {
      return null;
    }

    return java.time.Duration
      .ofSeconds(
        protobufDuration.getSeconds(),
        protobufDuration.getNanos()
      );
  }

  /**
   * Converts a Java {@link java.math.BigDecimal} to a Protobuf {@link BigDecimal}.
   *
   * @param bigDecimal the Java BigDecimal to convert
   * @return the converted Protobuf BigDecimal
   */
  @Named("bigDecimalToProtobufBigDecimal")
  default BigDecimal bigDecimalToProtobufBigDecimal(java.math.BigDecimal bigDecimal) {
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
   * Converts a Protobuf {@link BigDecimal} object to a Java {@link java.math.BigDecimal} object.
   *
   * @param protobufBigDecimal the Protobuf BigDecimal to convert
   * @return the converted Java BigDecimal
   */
  @Named("protobufBigDecimalToBigDecimal")
  default java.math.BigDecimal protobufBigDecimalToBigDecimal(BigDecimal protobufBigDecimal) {
    if (protobufBigDecimal == null) {
      return null;
    }

    final byte[] unscaledValueBytes = protobufBigDecimal
        .getUnscaledValue()
        .toByteArray();

    final BigInteger unscaledValue = unscaledValueBytes.length == 0
        ? BigInteger.ZERO
        : new BigInteger(unscaledValueBytes);

    return new java.math.BigDecimal(
      unscaledValue,
      protobufBigDecimal.getScale()
    );
  }
}
