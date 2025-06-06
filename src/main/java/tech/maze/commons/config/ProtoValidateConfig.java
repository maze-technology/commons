package tech.maze.commons.config;

import build.buf.protovalidate.Validator;
import build.buf.protovalidate.ValidatorFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for ProtoValidate.
 * This class provides Spring configuration for the ProtoValidate library,
 * which is used for validating Protocol Buffer messages.
 */
@Configuration
public class ProtoValidateConfig {
  /**
   * Creates and configures a new ProtoValidate Validator instance.
   * This validator can be used to validate Protocol Buffer messages against their schema
   * definitions.
   *
   * @return A configured Validator instance
   */
  @Bean
  public Validator validator() {
    return ValidatorFactory
      .newBuilder()
      .build();
  }
}
