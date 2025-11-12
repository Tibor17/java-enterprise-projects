package org.tibor17.wwws.client;

import jakarta.validation.constraints.*;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.tibor17.wwws.model.restclient.WeatherbitRootResourceDTO;

import java.math.BigDecimal;

@HttpExchange(url = "/v2.0/forecast/daily", accept = "application/json")
public interface ForecastService {

    /**
     * Synchronous HTTP call to Weatherbit weather forecast.
     * Optional query parameters with default values:
     * <ul>
     *     <li>lang=[language](optional), en - [DEFAULT] English</li>
     *     <li>units=[units](optional), M - [DEFAULT] Metric (Celsius, m/s, mm)</li>
     * </ul>
     *
     *
     * @param latitude GEO coordinate - Latitude
     * @param longitude GEO coordinate - Longitude
     * @param days weather forecast daily period
     * @param key authorization key
     * @return weather forecast for {@code days}
     * @throws org.springframework.web.client.HttpClientErrorException - a subtype of exception
     * corresponding to HTTP error code
     * @throws org.springframework.web.client.HttpClientErrorException.NotFound if no resource found
     * @throws org.springframework.web.client.HttpClientErrorException.Forbidden if e.g. wrong {@code key}
     */
    @GetExchange
    WeatherbitRootResourceDTO doGetForecast(@RequestParam("lat") @NotNull @Min(-90) @Max (90) BigDecimal latitude,
                                            @RequestParam("lon") @NotNull @Min(-180) @Max (180) BigDecimal longitude,
                                            @RequestParam("days") @Min(1) int days,
                                            @RequestParam("key") @NotBlank String key);
}
