package tech.maze.commons.mappers;

import com.google.protobuf.ByteString;
import com.google.protobuf.Duration;
import com.google.protobuf.Timestamp;
import java.math.BigInteger;
import java.time.Instant;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.maze.dtos.commons.math.BigDecimal;

/**
 * BaseDtoMapper is a mapper interface that provides mapping for DTO classes.
 */
@Mapper(componentModel = "spring")
public interface BaseDtoMapper {
  /**
   * Singleton instance of the BaseDtoMapper.
   */
  BaseDtoMapper INSTANCE = Mappers.getMapper(BaseDtoMapper.class);

  /**
   * Converts an {@link Instant} to a DTO {@link Timestamp}.
   *
   * @param instant The Java Instant to convert
   * @return The corresponding DTO Timestamp, or null if the input is null
   */
  default Timestamp map(Instant instant) {
    if (instant == null) {
      return null;
    }

    return Timestamp.newBuilder()
      .setSeconds(instant.getEpochSecond())
      .setNanos(instant.getNano())
      .build();
  }

  /**
   * Converts a DTO {@link Timestamp} to an {@link Instant}.
   *
   * @param timestamp The DTO Timestamp to convert
   * @return The corresponding Java Instant, or null if the input is null
   */
  default Instant map(Timestamp timestamp) {
    if (timestamp == null) {
      return null;
    }

    return Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos());
  }

  /**
   * Converts a Java {@link java.time.Duration} to a DTO {@link Duration}.
   *
   * @param duration The Java Duration to convert
   * @return The corresponding DTO Duration, or null if the input is null
   */
  default Duration map(java.time.Duration duration) {
    if (duration == null) {
      return null;
    }

    return Duration.newBuilder()
      .setSeconds(duration.getSeconds())
      .setNanos(duration.getNano())
      .build();
  }

  /**
   * Converts a DTO {@link Duration} object to a Java {@link java.time.Duration} object.
   *
   * @param dtoDuration The DTO Duration to convert
   * @return The corresponding Java Duration, or null if the input is null
   */
  default java.time.Duration map(Duration dtoDuration) {
    if (dtoDuration == null) {
      return null;
    }

    return java.time.Duration
      .ofSeconds(
        dtoDuration.getSeconds(),
        dtoDuration.getNanos()
      );
  }

  /**
   * Converts a Java {@link java.math.BigDecimal} to a DTO {@link BigDecimal}.
   *
   * @param bigDecimal The Java BigDecimal to convert
   * @return The corresponding DTO BigDecimal, or null if the input is null
   */
  default BigDecimal map(java.math.BigDecimal bigDecimal) {
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
   * Converts a DTO {@link BigDecimal} object to a Java {@link java.math.BigDecimal} object.
   *
   * @param dtoBigDecimal The DTO BigDecimal to convert
   * @return The corresponding Java BigDecimal, or null if the input is null
   */
  default java.math.BigDecimal map(BigDecimal dtoBigDecimal) {
    if (dtoBigDecimal == null) {
      return null;
    }

    final byte[] unscaledValueBytes = dtoBigDecimal
        .getUnscaledValue()
        .toByteArray();

    final BigInteger unscaledValue = unscaledValueBytes.length == 0
        ? BigInteger.ZERO
        : new BigInteger(unscaledValueBytes);

    return new java.math.BigDecimal(
      unscaledValue,
      dtoBigDecimal.getScale()
    );
  }
}
