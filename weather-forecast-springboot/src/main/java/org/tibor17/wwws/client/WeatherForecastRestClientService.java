package org.tibor17.wwws.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import org.tibor17.wwws.model.restclient.WeatherbitRootResourceDTO;

import java.math.BigDecimal;
import java.net.URI;

@Service
@Slf4j
public class WeatherForecastRestClientService {

    @Value("${ext.services.service-b.timeouts.connection.millis}")
    private int connectionTimeout;

    @Value("${ext.services.service-b.timeouts.read.millis}")
    private int readTimeout;

    @NotNull
    public ForecastService newRestClientService(@NotNull URI baseUri) {
        var restClient = RestClient.builder()
                .baseUrl(baseUri)
                .requestFactory(createClientHttpRequestFactory())
                .build();

        var adapter = RestClientAdapter.create(restClient);

        var serviceFactory =
                HttpServiceProxyFactory.builderFor(adapter)
                        .build();

        return serviceFactory.createClient(ForecastService.class);
    }

    /**
     * Demonstrates Circuit Breaker pattern
     * The circuit will open after 50% failures in a sliding window of 10 calls
     */
    @CircuitBreaker(name = "externalService", fallbackMethod = "fallbackForExternalCall")
    @Retry(name = "externalService")
    @NotNull
    public WeatherbitRootResourceDTO readWeatherForecast(@NotNull ForecastService restClientService,
                                                         @NotNull BigDecimal latitude, @NotNull BigDecimal longitude,
                                                         @NotBlank String authKey) {
        return restClientService.doGetForecast(latitude, longitude, 7, authKey);
    }

    /**
     * Fallback for circuit breaker and retry failures.
     */
    private WeatherbitRootResourceDTO fallbackForExternalCall(HttpServiceProxyFactory factory,
                                                              BigDecimal latitude, BigDecimal longitude,
                                                              String authKey,
                                                              @NotNull Exception e)
            throws Exception {

        log.error("Fallback triggered for endpoint. Latitude: {}. Longitude: {}. Error: {}",
                latitude, longitude, e.toString());
        throw e;
    }

    private ClientHttpRequestFactory createClientHttpRequestFactory() {
        var factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(connectionTimeout);
        factory.setReadTimeout(readTimeout);
        return factory;
    }
}
