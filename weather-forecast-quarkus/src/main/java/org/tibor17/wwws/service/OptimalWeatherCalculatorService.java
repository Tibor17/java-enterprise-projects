package org.tibor17.wwws.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.tibor17.wwws.model.restclient.WeatherbitDayResourceDTO;
import org.tibor17.wwws.model.restclient.WeatherbitRootResourceDTO;
import org.tibor17.wwws.model.restservice.OptimalWeatherDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static java.util.Comparator.naturalOrder;
import static org.tibor17.wwws.util.OptimalWeatherUtil.hasOptimalWeatherConditions;

@ApplicationScoped
public class OptimalWeatherCalculatorService {

    @Valid
    public Optional<OptimalWeatherDTO> findBestWeatherForecastLocation(
            @NotNull Collection<WeatherbitRootResourceDTO> examinedLocations,
            @NotNull LocalDate expectedDate) {
        var resourcesMatchedByDate = findResourcesByDate(examinedLocations, expectedDate);
        return findBestMatch(resourcesMatchedByDate)
                .map(res -> {
                    var location = res.getRoot();
                    return new OptimalWeatherDTO(location.getCityName(), location.getCountryCode(),
                            location.getLatitude(), location.getLongitude(),
                            res.getTemperature(), res.getWindSpeed());
                });
    }

    List<WeatherbitDayResourceDTO> findResourcesByDate(Collection<WeatherbitRootResourceDTO> examinedLocations,
                                                       LocalDate expectedDate) {
        return examinedLocations.stream().flatMap(location ->
                        location.getForecast().stream()
                                .filter(res -> res.getDatetime().equals(expectedDate))
                                .findFirst()
                                .stream()
                ).toList();
    }

    /**
     * This algorithm is sorting given {@code forecasts}.
     * Optimal forecast is determined by
     * {@link org.tibor17.wwws.util.OptimalWeatherUtil#hasOptimalWeatherConditions(BigDecimal, BigDecimal)}.
     * Only optimal forecast is processed.
     * Each forecast {@link WeatherbitDayResourceDTO} is evaluated by the mathematical formula in
     * {@link org.tibor17.wwws.util.OptimalWeatherUtil#computeWindsurfingRatingIndex(BigDecimal, BigDecimal)}
     * and returns weather rating. The forecasts are sorted by the rating from small rating index to big index.
     * Returns the forecast with the highest rating index.
     *
     * @param forecasts each {@link WeatherbitDayResourceDTO} is sortable
     * @return one weather forecast with the best conditions (rating index); if any
     */
    Optional<WeatherbitDayResourceDTO> findBestMatch(Collection<WeatherbitDayResourceDTO> forecasts) {
        var validForecastResources =
                forecasts.stream().filter(dto ->
                        hasOptimalWeatherConditions(dto.getWindSpeed(), dto.getTemperature()));

        return validForecastResources.max(naturalOrder());
    }
}
