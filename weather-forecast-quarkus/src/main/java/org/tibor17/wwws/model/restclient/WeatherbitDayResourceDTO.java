package org.tibor17.wwws.model.restclient;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;

import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static java.math.RoundingMode.HALF_UP;
import static org.tibor17.wwws.util.OptimalWeatherUtil.computeWindsurfingRatingIndex;

/**
 * Notice: Due to we need to have a simple business logic without any additional
 * wrapper and complex sorter, we have to get rid of Java Records.
 * The Java Records use to set <@code NULL> in back reference of bi-directional mapping.
 */
@Getter
@ToString(exclude = "root")
public final class WeatherbitDayResourceDTO implements Comparable<WeatherbitDayResourceDTO> {
    private static final BigDecimal MAX_INT = new BigDecimal(MAX_VALUE);
    private static final BigDecimal MIN_INT = new BigDecimal(MIN_VALUE);
    private static final MathContext MATH_ROUND_UP = new MathContext(1, HALF_UP);

    public WeatherbitDayResourceDTO(@NotNull
                                    @JsonFormat(pattern = "yyyy-MM-dd")
                                    LocalDate datetime,

                                    @NotNull
                                    @Digits(integer = 2, fraction = 1)
                                    @JsonProperty("temp")
                                    BigDecimal temperature,

                                    @NotNull
                                    @Digits(integer = 3, fraction = 1)
                                    @JsonProperty("wind_spd")
                                    BigDecimal windSpeed) {
        this.datetime = datetime;
        this.temperature = temperature;
        this.windSpeed = windSpeed;
    }

    // for testing purposes
    @JsonIgnore
    public WeatherbitDayResourceDTO(LocalDate datetime, BigDecimal temperature, BigDecimal windSpeed,
                                    WeatherbitRootResourceDTO root) {
        this.datetime = datetime;
        this.temperature = temperature;
        this.windSpeed = windSpeed;
        this.root = root;
    }

    @Valid
    @JsonBackReference
    private WeatherbitRootResourceDTO root;

    private final LocalDate datetime;

    private final BigDecimal temperature;

    private final BigDecimal windSpeed;

    @Override
    public int compareTo(WeatherbitDayResourceDTO o) {
        return computeWindsurfingRatingIndex(windSpeed, temperature)
                .subtract(computeWindsurfingRatingIndex(o.windSpeed, o.temperature))
                .round(MATH_ROUND_UP)
                .min(MAX_INT)
                .max(MIN_INT)
                .intValue();
    }
}
