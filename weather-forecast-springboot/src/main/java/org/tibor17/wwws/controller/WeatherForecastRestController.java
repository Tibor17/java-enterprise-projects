package org.tibor17.wwws.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.tibor17.wwws.client.WeatherForecastRestClientService;
import org.tibor17.wwws.model.restservice.OptimalWeatherDTO;
import org.tibor17.wwws.service.ConfigService;
import org.tibor17.wwws.service.OptimalWeatherCalculatorService;
import org.tibor17.wwws.util.OptimalWeatherUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.Optional;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.ResponseEntity.noContent;

@RestController
@RequestMapping("/api/v0.1/windfinder/forecast")
@Tag(name = "REST API for windsurfers", description = "Worldwide Windsurfer's Weather Service")
@Validated
@Slf4j
public class WeatherForecastRestController {
    @Autowired
    private OptimalWeatherCalculatorService optimalWeatherCalculatorService;

    @Autowired
    private WeatherForecastRestClientService restClientService;

    @Autowired
    private ConfigService configService;

    @Operation(summary = "Get the best weather conditions for making windsurfing.",
            description = """
                    Retrieves detained information (city_name, country_code, lat, lon, temp, wind_spd)
                    according to the date specified (in the form of yyyy-MM-dd).
                    Example with URI: /api/v0.1/windfinder/forecast/dates/2025-11-02
                    """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resource found",
                    content = @Content(schema = @Schema(implementation = OptimalWeatherDTO.class))),
            @ApiResponse(responseCode = "204", description = "No content found",
                    content = @Content(schema = @Schema()))
    })
    @GetMapping(value = "/dates/{date}", produces = {"application/json"})
    @ResponseStatus(OK)
    /* Works with dependency org.springframework.boot:spring-boot-starter-web which is Embedded Apache Tomcat */
    public ResponseEntity<OptimalWeatherDTO> doGetByDate(@PathVariable("date")
                                                         @Parameter(required = true, description =
                                                                 "Date format [yyyy-MM-dd] for weather forecast.")
                                                         @NotNull LocalDate date)
            throws URISyntaxException {

        log.info("/dates/" + date);

        var remoteUrl = configService.findConnectionUrl();
        var authKey = configService.findAuthKey();

        log.info("Remote access URL {} for the REST client. Authorization key {}.",
                remoteUrl.orElse("missing"),
                authKey.map(OptimalWeatherUtil::maskAuthKey).orElse("missing"));

        var locations = configService.findAllLocations();
        log.info("Loaded {} locations from the database.", locations.size());

        var factory = restClientService.buildHttpServiceProxyFactory(checkURL(remoteUrl));

        var examinedLocations = locations.stream().map(location -> {
            var locationResource = restClientService.readWeatherForecast(factory,
                    location.latitude(), location.longitude(), checkAuthKey(authKey));

            log.info("Found weather forecast location by date {}: {}", date, locationResource);
            return locationResource;
        }).toList();

        var resource = optimalWeatherCalculatorService.findBestWeatherForecastLocation(examinedLocations, date);

        log.info("Found optimal weather conditions for windsurfing: {}", resource.isPresent());

        return resource.map(ResponseEntity::ok)
                .orElseGet(() -> noContent().build());
    }

    private static URI checkURL(Optional<String> remoteUrl) throws URISyntaxException {
        if (remoteUrl.isEmpty()) {
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, "Missing database record for remote URL access.");
        }
        return new URI(remoteUrl.get());
    }

    private static String checkAuthKey(Optional<String> authKey) {
        return authKey.orElseThrow(() -> new ResponseStatusException(INTERNAL_SERVER_ERROR,
                                                                       "Missing database record for ApiKey."));
    }
}
