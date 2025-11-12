package org.tibor17.wwws.model.restservice;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;

@Validated
public record OptimalWeatherDTO (

    @NotBlank
    @Size(max = 255)
    @JsonProperty("city_name")
    @Schema(description = "Name of the city.")
    String cityName,

    @NotBlank
    @Size(max = 255)
    @JsonProperty("country_code")
    @Schema(description = "Country code.")
    String countryCode,

    @NotNull
    @Digits(integer = 2, fraction = 5)
    @JsonProperty("lat")
    @Schema(description = "GEO latitude.")
    BigDecimal latitude,

    @NotNull
    @Digits(integer = 3, fraction = 5)
    @JsonProperty("lon")
    @Schema(description = "GEO longitude.")
    BigDecimal longitude,

    @NotNull
    @Digits(integer = 2, fraction = 1)
    @JsonProperty("temp")
    @Schema(description = "Average air temperature [°C].")
    BigDecimal temperature,

    @NotNull
    @Digits(integer = 3, fraction = 1)
    @JsonProperty("wind_spd")
    @Schema(description = "Daily speed of the wind [m/s].")
    BigDecimal windSpeed) {
}
