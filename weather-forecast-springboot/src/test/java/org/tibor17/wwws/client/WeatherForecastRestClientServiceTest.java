package org.tibor17.wwws.client;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;

public class WeatherForecastRestClientServiceTest {

    @Test
    void shouldMatchPolishLocationWithGeoCoordinates() throws URISyntaxException {
        var service = new WeatherForecastRestClientService();
        var restClientService = service.newRestClientService(new URI("http://api.weatherbit.io"));
        var dto = service.readWeatherForecast(restClientService,
                new BigDecimal("54.69606"), new BigDecimal("18.67873"),
                "6d5d58529a3f48e8a4749804f6343fd1");

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
