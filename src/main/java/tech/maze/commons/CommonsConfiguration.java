package tech.maze.commons;

import lombok.NoArgsConstructor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for the Maze Commons library.
 * This class provides Spring configuration for the commons module,
 * enabling component scanning and auto-configuration of beans.
 */
@Configuration
@ComponentScan("tech.maze.commons")
@NoArgsConstructor
@SuppressWarnings("doclint:missing")
public class CommonsConfiguration {

}
