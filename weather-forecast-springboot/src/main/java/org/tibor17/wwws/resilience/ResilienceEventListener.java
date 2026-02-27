package org.tibor17.wwws.resilience;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class ResilienceEventListener {

    private final CircuitBreakerRegistry circuitBreakerRegistry;
    private final RetryRegistry retryRegistry;
    private final RateLimiterRegistry rateLimiterRegistry;

    public ResilienceEventListener(
            CircuitBreakerRegistry circuitBreakerRegistry,
            RetryRegistry retryRegistry,
            RateLimiterRegistry rateLimiterRegistry) {
        this.circuitBreakerRegistry = circuitBreakerRegistry;
        this.retryRegistry = retryRegistry;
        this.rateLimiterRegistry = rateLimiterRegistry;
    }

    @PostConstruct
    public void setupEventListeners() {
        setupCircuitBreakerListeners();
        setupRetryListeners();
        setupRateLimiterListeners();
    }

    private void setupCircuitBreakerListeners() {
        circuitBreakerRegistry.getEventPublisher()
                .onEntryAdded(event -> registerCircuitBreakerEvents(event.getAddedEntry()));

        circuitBreakerRegistry.getAllCircuitBreakers()
                .forEach(this::registerCircuitBreakerEvents);
    }

    private void registerCircuitBreakerEvents(CircuitBreaker circuitBreaker) {
        circuitBreaker.getEventPublisher()
                .onSuccess(event -> log.debug("CircuitBreaker '{}' recorded a successful call",
                        circuitBreaker.getName()))
                .onError(event -> log.warn("CircuitBreaker '{}' recorded an error: {}",
                        circuitBreaker.getName(), event.getThrowable().getMessage()))
                .onStateTransition(event -> log.info("CircuitBreaker '{}' changed state from {} to {}",
                        circuitBreaker.getName(),
                        event.getStateTransition().getFromState(),
                        event.getStateTransition().getToState()))
                .onSlowCallRateExceeded(event -> log.warn("CircuitBreaker '{}' slow call rate exceeded: {}%",
                        circuitBreaker.getName(),
                        event.getSlowCallRate()))
                .onFailureRateExceeded(event -> log.warn("CircuitBreaker '{}' failure rate exceeded: {}%",
                        circuitBreaker.getName(),
                        event.getFailureRate()));
    }

    private void setupRetryListeners() {
        retryRegistry.getEventPublisher()
                .onEntryAdded(event -> registerRetryEvents(event.getAddedEntry()));

        retryRegistry.getAllRetries()
                .forEach(this::registerRetryEvents);
    }

    private void registerRetryEvents(Retry retry) {
        retry.getEventPublisher()
                .onRetry(event -> log.info("Retry '{}' - Attempt #{}: {}",
                        retry.getName(),
                        event.getNumberOfRetryAttempts(),
                        event.getLastThrowable().getMessage()))
                .onSuccess(event -> log.debug("Retry '{}' succeeded after {} attempts",
                        retry.getName(),
                        event.getNumberOfRetryAttempts()))
                .onError(event -> log.error("Retry '{}' failed after {} attempts: {}",
                        retry.getName(),
                        event.getNumberOfRetryAttempts(),
                        event.getLastThrowable().getMessage()));
    }

    private void setupRateLimiterListeners() {
        rateLimiterRegistry.getEventPublisher()
                .onEntryAdded(event -> registerRateLimiterEvents(event.getAddedEntry()));

        rateLimiterRegistry.getAllRateLimiters()
                .forEach(this::registerRateLimiterEvents);
    }

    private void registerRateLimiterEvents(RateLimiter rateLimiter) {
        rateLimiter.getEventPublisher()
                .onSuccess(event -> log.debug("RateLimiter '{}' permitted a call",
                        rateLimiter.getName()))
                .onFailure(event -> log.warn("RateLimiter '{}' rejected a call",
                        rateLimiter.getName()));
    }
}
