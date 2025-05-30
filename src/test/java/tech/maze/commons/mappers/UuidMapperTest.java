package tech.maze.commons.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the UuidMapper class.
 */
public class UuidMapperTest {
  private UuidMapper mapper;

  @BeforeEach
  void setUp() {
    mapper = UuidMapper.INSTANCE;
  }

  @Test
  @DisplayName("Should convert UUID to string correctly")
  void mapUuidToStringShouldReturnCorrectString() {
    // Given
    final UUID uuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

    // When
    final String result = mapper.map(uuid);

    // Then
    assertEquals("123e4567-e89b-12d3-a456-426614174000", result);
  }

  @Test
  @DisplayName("Should return null when mapping null UUID")
  void mapNullUuidShouldReturnNull() {
    // Given
    final UUID uuid = null;

    // When
    final String result = mapper.map(uuid);

    // Then
    assertNull(result);
  }

  @Test
  @DisplayName("Should convert string to UUID correctly")
  void mapStringToUuidShouldReturnCorrectUuid() {
    // Given
    final String uuidStr = "123e4567-e89b-12d3-a456-426614174000";

    // When
    final UUID result = mapper.map(uuidStr);

    // Then
    assertEquals(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), result);
  }

  @Test
  @DisplayName("Should return null when mapping null string")
  void mapNullStringShouldReturnNull() {
    // Given
    final String uuidStr = null;

    // When
    final UUID result = mapper.map(uuidStr);

    // Then
    assertNull(result);
  }

  @Test
  @DisplayName("Should throw IllegalArgumentException for invalid UUID string")
  void mapInvalidUuidStringShouldThrowException() {
    // Given
    final String invalidUuidStr = "invalid-uuid";

    // When & Then
    assertThrows(IllegalArgumentException.class, () -> mapper.map(invalidUuidStr));
  }
}
