package org.tibor17.wwws.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.tibor17.wwws.model.restclient.WeatherbitDayResourceDTO;
import org.tibor17.wwws.model.restclient.WeatherbitRootResourceDTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static java.time.LocalDate.of;
import static java.util.Collections.shuffle;
import static org.assertj.core.api.Assertions.assertThat;

public class OptimalWeatherForecastLookupTest {
    private static OptimalWeatherCalculatorService service;

    private List<WeatherbitRootResourceDTO> locations;
    private WeatherbitRootResourceDTO location1;
    private WeatherbitRootResourceDTO location2;
    private WeatherbitRootResourceDTO location3;

    private List<WeatherbitDayResourceDTO> forecastsForLocation1;
    private List<WeatherbitDayResourceDTO> forecastForLocation2;
    private List<WeatherbitDayResourceDTO> forecastForLocation3;

    // DEFAULT rating is low - between <34, 36>.
    // The tests modify some rows to high rating (over 40).
    private WeatherbitDayResourceDTO forecast101 =
            new WeatherbitDayResourceDTO(of(2025, 10, 31), new BigDecimal(18), new BigDecimal(6));//rating: 36
    private WeatherbitDayResourceDTO forecast102 =
            new WeatherbitDayResourceDTO(of(2025, 11, 1), new BigDecimal(19), new BigDecimal(5));// rating: 34
    private WeatherbitDayResourceDTO forecast103 =
            new WeatherbitDayResourceDTO(of(2025, 11, 2), new BigDecimal(20), new BigDecimal(5));//rating: 35

    private WeatherbitDayResourceDTO forecast201 = forecast101;
    private WeatherbitDayResourceDTO forecast202 = forecast102;
    private WeatherbitDayResourceDTO forecast203 = forecast103;

    private WeatherbitDayResourceDTO forecast301 = forecast101;
    private WeatherbitDayResourceDTO forecast302 = forecast102;
    private WeatherbitDayResourceDTO forecast303 = forecast103;

    @BeforeAll
    public static void initService() {
        service = new OptimalWeatherCalculatorService();
    }

    @BeforeEach
    public void setUpResources() {
        locations = new ArrayList<>();

        forecastsForLocation1 = new ArrayList<>();

        location1 = new WeatherbitRootResourceDTO("city 1", "country 1",
                new BigDecimal(1), new BigDecimal(11), forecastsForLocation1);

        forecastForLocation2 = new ArrayList<>();

        location2 = new WeatherbitRootResourceDTO("city 2", "country 2",
                new BigDecimal(2), new BigDecimal(22), forecastForLocation2);

        forecastForLocation3 = new ArrayList<>();

        location3 = new WeatherbitRootResourceDTO("city 3", "country 3",
                new BigDecimal(3), new BigDecimal(33), forecastForLocation3);
    }

    @Test
    void shouldNotSelectWindVelocityRange() {
        forecastsForLocation1.add(forecast101);
        forecastsForLocation1.add(forecast102);
        forecastsForLocation1.add(forecast103);

        locations.add(location1);

        var root = service.findResourcesByDate(locations, of(2025, 11, 3));

        assertThat(root).isEmpty();
    }

    @Test
    void shouldSelectWindVelocityRange() {
        forecastsForLocation1.add(forecast101);
        forecastsForLocation1.add(forecast102);
        forecastsForLocation1.add(forecast103);

        forecastForLocation2.add(forecast201);
        forecastForLocation2.add(forecast202);
        forecastForLocation2.add(forecast203);

        forecastForLocation3.add(forecast301);
        forecastForLocation3.add(forecast302);
        forecastForLocation3.add(forecast303);

        locations.add(location1);
        locations.add(location2);
        locations.add(location3);

        var forecasts = service.findResourcesByDate(locations, of(2025, 11, 1));

        assertThat(forecasts).hasSize(3);
        forecasts.forEach(f ->
                assertThat(f.getDatetime())
                        .isEqualTo(of(2025, 11, 1)));
    }

    @Test
    void shouldSortForecasts() {
        var forecasts = List.of(forecast101, forecast102, forecast103);
        var randomForecasts = new ArrayList<>(forecasts);
        shuffle(randomForecasts);

        var match = service.findBestMatch(randomForecasts);

        assertThat(match)
                .isPresent();

        assertThat(match.get())
                .extracting(WeatherbitDayResourceDTO::getTemperature)
                .isEqualTo(new BigDecimal(18));

        assertThat(match.get())
                .extracting(WeatherbitDayResourceDTO::getWindSpeed)
                .isEqualTo(new BigDecimal(6));
    }

    @Test
    void shouldNotMatchDate() {
        forecastsForLocation1.add(forecast101);
        forecastsForLocation1.add(forecast102);
        forecastsForLocation1.add(forecast103);

        locations.add(location1);
        var match = service.findBestWeatherForecastLocation(locations, of(2025, 11, 3));

        assertThat(match).isNotPresent();
    }

    @Test
    void shouldNotMatchWindVelocityRange() {
        forecast102 = new WeatherbitDayResourceDTO(of(2025, 11, 1), new BigDecimal(19), new BigDecimal(4));
        forecast202 = forecast102;
        forecast302 = forecast102;

        forecastsForLocation1.add(forecast102);
        forecastForLocation2.add(forecast202);
        forecastForLocation3.add(forecast302);

        locations.add(location1);
        locations.add(location2);
        locations.add(location3);

        var match = service.findBestWeatherForecastLocation(locations, of(2025, 11, 1));

        assertThat(match).isNotPresent();
    }

    /**
     * One record has very slow wind (4 m/s) however the temperature is very high (35C).
     * Only two weather forecast records are valid for the date (1st of October 2025).
     */
    @Test
    void shouldMatchOneOfTwo() {
        forecast102 = //rating: 47
                new WeatherbitDayResourceDTO(of(2025, 11, 1), new BigDecimal(35), new BigDecimal(4), location1);

        forecast202 = //rating: 45
                new WeatherbitDayResourceDTO(of(2025, 11, 1), new BigDecimal(30), new BigDecimal(5), location2);

        forecast302 = //rating: 46
                new WeatherbitDayResourceDTO(of(2025, 11, 1), new BigDecimal(31), new BigDecimal(5), location3);

        forecastsForLocation1.add(forecast101);
        forecastsForLocation1.add(forecast102);
        forecastsForLocation1.add(forecast103);

        forecastForLocation2.add(forecast201);
        forecastForLocation2.add(forecast202);
        forecastForLocation2.add(forecast203);

        forecastForLocation3.add(forecast301);
        forecastForLocation3.add(forecast302);
        forecastForLocation3.add(forecast303);

        locations.add(location1);
        locations.add(location2);
        locations.add(location3);

        var match = service.findBestWeatherForecastLocation(locations, of(2025, 11, 1));

        assertThat(match).isPresent();
        var foundLocation = match.get();
        assertThat(foundLocation.countryCode()).isEqualTo("country 3");
        assertThat(foundLocation.cityName()).isEqualTo("city 3");
        assertThat(foundLocation.temperature()).isEqualTo(new BigDecimal(31));
        assertThat(foundLocation.windSpeed()).isEqualTo(new BigDecimal(5));
        assertThat(foundLocation.latitude()).isEqualTo(new BigDecimal(3));
        assertThat(foundLocation.longitude()).isEqualTo(new BigDecimal(33));
    }

    @Test
    void shouldFindBestMatch() {
        forecast102 = //rating: 44
                new WeatherbitDayResourceDTO(of(2025, 11, 1), new BigDecimal(20), new BigDecimal(8), location1);

        forecast202 = //rating: 50
                new WeatherbitDayResourceDTO(of(2025, 11, 1), new BigDecimal(35), new BigDecimal(5), location2);

        forecast302 = //rating: 51
                new WeatherbitDayResourceDTO(of(2025, 11, 1), new BigDecimal(30), new BigDecimal(7), location3);

        forecastsForLocation1.add(forecast101);
        forecastsForLocation1.add(forecast102);
        forecastsForLocation1.add(forecast103);

        forecastForLocation2.add(forecast201);
        forecastForLocation2.add(forecast202);
        forecastForLocation2.add(forecast203);

        forecastForLocation3.add(forecast301);
        forecastForLocation3.add(forecast302);
        forecastForLocation3.add(forecast303);

        locations.add(location1);
        locations.add(location2);
        locations.add(location3);

        var match = service.findBestWeatherForecastLocation(locations, of(2025, 11, 1));

        assertThat(match).isPresent();
        var foundLocation = match.get();
        assertThat(foundLocation.countryCode()).isEqualTo("country 3");
        assertThat(foundLocation.cityName()).isEqualTo("city 3");
        assertThat(foundLocation.temperature()).isEqualTo(new BigDecimal(30));
        assertThat(foundLocation.windSpeed()).isEqualTo(new BigDecimal(7));
        assertThat(foundLocation.latitude()).isEqualTo(new BigDecimal(3));
        assertThat(foundLocation.longitude()).isEqualTo(new BigDecimal(33));
    }
}
