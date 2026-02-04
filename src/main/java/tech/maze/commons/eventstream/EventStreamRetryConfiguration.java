package tech.maze.commons.eventstream;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

/**
 * Configures retry behavior for event publishing.
 */
@Configuration
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@ConditionalOnProperty(prefix = MazeEventProperties.PREFIX, name = "enabled", havingValue = "true")
public class EventStreamRetryConfiguration {
  MazeEventProperties eventProperties;

  /**
   * Retry template for event publishing.
   *
   * @return configured retry template
   */
  @Bean
  public RetryTemplate eventStreamRetryTemplate() {
    final int maxAttempts = Math.max(1, eventProperties.getRetry().getMaxAttempts());
    final long backoffMs = Math.max(0L, eventProperties.getRetry().getBackoff().toMillis());

    final SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy(maxAttempts);
    final FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
    backOffPolicy.setBackOffPeriod(backoffMs);

    final RetryTemplate retryTemplate = new RetryTemplate();
    retryTemplate.setRetryPolicy(retryPolicy);
    retryTemplate.setBackOffPolicy(backOffPolicy);
    return retryTemplate;
  }
}
