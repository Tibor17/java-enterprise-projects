package org.tibor17.wwws.client;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.tibor17.wwws.support.PostgresResource;

import java.math.BigDecimal;
import java.net.URI;

import static io.quarkus.rest.client.reactive.QuarkusRestClientBuilder.newBuilder;
import static java.time.Duration.ofSeconds;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
//@QuarkusTestResource(value = PostgresResource.class, restrictToAnnotatedClass = true)
public class WeatherForecastRestClientServiceTest {

    @Test
    void shouldMatchPolishLocationWithGeoCoordinates() throws Exception {
        var service = newBuilder()
                .baseUri(new URI("http://api.weatherbit.io"))
                .connectTimeout(10, SECONDS)
                .readTimeout(10, SECONDS)
                .build(ForecastService.class);

        var dto = service.doGetForecast(new BigDecimal("54.69606"), new BigDecimal("18.67873"),
                        7,
                        System.getenv("APIKEY"))
                .await()
                .atMost(ofSeconds(30));

        assertThat(dto)
                .isNotNull();

        assertThat(dto.getCountryCode())
                .isEqualTo("PL");

        assertThat(dto.getCityName())
                .isEqualTo("Jastarnia");

        assertThat(dto.getLatitude())
                .isBetween(new BigDecimal(54), new BigDecimal(55));

        assertThat(dto.getLongitude())
                .isBetween(new BigDecimal(18), new BigDecimal(19));

        assertThat(dto.getForecast())
                .hasSize(7);
    }
}
