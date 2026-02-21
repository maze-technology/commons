package tech.maze.commons.mappers;

import com.google.protobuf.Value;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

/**
 * Shared mapper for protobuf document values.
 */
@Mapper(componentModel = "spring")
public interface ProtobufValueMapper {
  /**
   * Converts a UUID to protobuf Value.
   */
  @Named("uuidToValue")
  default Value uuidToValue(UUID value) {
    if (value == null) {
      return Value.getDefaultInstance();
    }

    return Value.newBuilder().setStringValue(value.toString()).build();
  }

  /**
   * Converts a string to protobuf Value.
   */
  @Named("stringToValue")
  default Value stringToValue(String value) {
    if (value == null) {
      return Value.getDefaultInstance();
    }

    return Value.newBuilder().setStringValue(value).build();
  }

  /**
   * Reads a non-blank string from protobuf Value.
   */
  default Optional<String> toString(Value value) {
    if (value == null || !value.hasStringValue()) {
      return Optional.empty();
    }

    final String raw = value.getStringValue();
    if (raw == null || raw.isBlank()) {
      return Optional.empty();
    }

    return Optional.of(raw);
  }

  /**
   * Reads a UUID from protobuf Value when valid.
   */
  default Optional<UUID> toUuid(Value value) {
    return toString(value).flatMap(this::parseUuid);
  }

  /**
   * Reads all valid distinct UUIDs from protobuf values.
   */
  default List<UUID> toUuids(List<Value> values) {
    if (values == null || values.isEmpty()) {
      return List.of();
    }

    return values.stream()
        .map(this::toUuid)
        .flatMap(Optional::stream)
        .distinct()
        .toList();
  }

  private Optional<UUID> parseUuid(String raw) {
    try {
      return Optional.of(UUID.fromString(raw));
    } catch (IllegalArgumentException ignored) {
      return Optional.empty();
    }
  }
}
