package org.tibor17.wwws.model.restclient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.List;

/**
 * Notice: Due to we need to have a simple business logic without any additional
 * wrapper and complex sorter, we have to get rid of Java Records.
 * The Java Records use to set <@code NULL> in back reference of bi-directional mapping.
 */
@Validated
@Getter
@ToString(exclude = "forecast")
public final class WeatherbitRootResourceDTO {

    public WeatherbitRootResourceDTO(@NotBlank
                                     @Size(max = 255)
                                     @JsonProperty("city_name")
                                     String cityName,

                                     @NotBlank
                                     @Size(max = 255)
                                     @JsonProperty("country_code")
                                     String countryCode,

                                     @NotNull
                                     @Digits(integer = 2, fraction = 5)
                                     @JsonProperty("lat")
                                     BigDecimal latitude,

                                     @NotNull
                                     @Digits(integer = 3, fraction = 5)
                                     @JsonProperty("lon")
                                     BigDecimal longitude) {
        this.cityName = cityName;
        this.countryCode = countryCode;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // for testing purposes
    @JsonIgnore
    public WeatherbitRootResourceDTO(String cityName, String countryCode, BigDecimal latitude, BigDecimal longitude,
                                     List<WeatherbitDayResourceDTO> forecast) {
        this(cityName, countryCode, latitude, longitude);
        this.forecast = forecast;
    }

    @JsonProperty("data")
    @JsonManagedReference
    private List<WeatherbitDayResourceDTO> forecast;

    private final String cityName;

    private final String countryCode;

    private final BigDecimal latitude;

    private final BigDecimal longitude;
}
