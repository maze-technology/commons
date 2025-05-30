package tech.maze.commons.mappers;

// TODO: Move to commons
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * UuidMapper is a mapper interface that provides mapping for UUID class.
 */
@Mapper(componentModel = "spring")
public interface UuidMapper {
  /**
   * Singleton instance of the UuidMapper.
   */
  UuidMapper INSTANCE = Mappers.getMapper(UuidMapper.class);

  /**
   * Maps a UUID to its string representation.
   *
   * @param uuid the UUID to convert
   * @return the string representation of the UUID, or null if the input is null
   */
  default String map(UUID uuid) {
    return uuid == null ? null : uuid.toString();
  }

  /**
   * Maps a string to a UUID.
   *
   * @param uuidStr the string to convert to UUID
   * @return the UUID representation of the string, or null if the input is null
   */
  default UUID map(String uuidStr) {
    return uuidStr == null ? null : UUID.fromString(uuidStr);
  }
}
