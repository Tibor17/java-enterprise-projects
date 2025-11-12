package org.tibor17.wwws.service;

import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.tibor17.wwws.model.restclient.WeatherbitDayResourceDTO;
import org.tibor17.wwws.model.restclient.WeatherbitRootResourceDTO;
import org.tibor17.wwws.model.restservice.OptimalWeatherDTO;

import java.time.LocalDate;
import java.util.*;

import static java.util.Comparator.naturalOrder;
import static org.tibor17.wwws.util.OptimalWeatherUtil.hasOptimalWeatherConditions;

@Service
public class OptimalWeatherCalculatorService {

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

        var resourcesMatchedByDate = new ArrayList<WeatherbitDayResourceDTO>();
        examinedLocations.forEach(location ->
                location.getForecast().stream()
                .filter(res -> res.getDatetime().equals(expectedDate))
                .findFirst()
                .ifPresent(resourcesMatchedByDate::add));
        return resourcesMatchedByDate;
    }

    Optional<WeatherbitDayResourceDTO> findBestMatch(Collection<WeatherbitDayResourceDTO> forecasts) {
        var validForecastResources =
                forecasts.stream().filter(dto ->
                        hasOptimalWeatherConditions(dto.getWindSpeed(), dto.getTemperature()));

        return validForecastResources.max(naturalOrder());
    }
}
