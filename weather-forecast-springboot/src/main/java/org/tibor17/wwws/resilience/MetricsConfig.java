package org.tibor17.wwws.resilience;

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.micrometer.tagged.TaggedCircuitBreakerMetrics;
import io.github.resilience4j.micrometer.tagged.TaggedRateLimiterMetrics;
import io.github.resilience4j.micrometer.tagged.TaggedRetryMetrics;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.retry.RetryRegistry;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfig {

    @Bean
    public TaggedCircuitBreakerMetrics taggedCircuitBreakerMetrics(
            CircuitBreakerRegistry circuitBreakerRegistry,
            MeterRegistry meterRegistry) {
        return TaggedCircuitBreakerMetrics.ofCircuitBreakerRegistry(circuitBreakerRegistry);
    }

    @Bean
    public TaggedRetryMetrics taggedRetryMetrics(
            RetryRegistry retryRegistry,
            MeterRegistry meterRegistry) {
        return TaggedRetryMetrics.ofRetryRegistry(retryRegistry);
    }

    @Bean
    public TaggedRateLimiterMetrics taggedRateLimiterMetrics(
            RateLimiterRegistry rateLimiterRegistry,
            MeterRegistry meterRegistry) {
        return TaggedRateLimiterMetrics.ofRateLimiterRegistry(rateLimiterRegistry);
    }
}
