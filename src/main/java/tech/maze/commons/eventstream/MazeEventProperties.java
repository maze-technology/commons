package tech.maze.commons.eventstream;

import java.net.URI;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for Maze event streaming.
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@ConfigurationProperties(prefix = MazeEventProperties.PREFIX)
@ConditionalOnProperty(prefix = MazeEventProperties.PREFIX, name = "source")
public class MazeEventProperties {
  public static final String PREFIX = "maze.events";

  /**
   * CloudEvents source for events emitted by this service.
   */
  URI source;
}
