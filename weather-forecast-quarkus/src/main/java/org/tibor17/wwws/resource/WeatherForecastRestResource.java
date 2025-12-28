package org.tibor17.wwws.resource;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.tibor17.wwws.client.ForecastService;
import org.tibor17.wwws.model.restservice.OptimalWeatherDTO;
import org.tibor17.wwws.service.ConfigService;
import org.tibor17.wwws.service.OptimalWeatherCalculatorService;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;

import static io.quarkus.rest.client.reactive.QuarkusRestClientBuilder.newBuilder;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static java.time.Duration.ofSeconds;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.tibor17.wwws.util.OptimalWeatherUtil.maskAuthKey;

@ApplicationScoped
@Path("/api/v0.1/forecast")
@Tag(name = "REST API for windsurfers", description = "Worldwide Windsurfer's Weather Service")
@Slf4j
public class WeatherForecastRestResource {
    @Inject
    OptimalWeatherCalculatorService optimalWeatherCalculatorService;

    @Inject
    ConfigService configService;

    @Operation(summary = "Get the best weather conditions for making windsurfing.",
            description = """
                    Retrieves detained information (city_name, country_code, lat, lon, temp, wind_spd)
                    according to the date specified (in the form of yyyy-MM-dd).
                    Example with URI: /windfinder/api/v0.1/forecast/dates/2025-11-02
                    """)
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Resource found",
                    content = @Content(schema = @Schema(implementation = OptimalWeatherDTO.class))),
            @APIResponse(responseCode = "204", description = "No content found",
                    content = @Content(schema = @Schema()))
    })
    @GET
    @Path("/dates/{date}")
    @Produces(APPLICATION_JSON)
    public Response doGetByDate(
            @PathParam("date")
            @Parameter(required = true, description = "Date format [yyyy-MM-dd] for weather forecast.")
            @NotNull LocalDate date) throws URISyntaxException {

        log.info("REST Api call: GET /windfinder/api/v0.1/forecast/dates/" + date);

        var remoteUrl = configService.findConnectionUrl();
        var authKey = configService.findAuthKey();
        log.info("""
                 Remote access URL {} for the REST client.
                 Authorization key {}.""", remoteUrl, maskAuthKey(authKey));

        var locations = configService.findAllLocations();
        log.info("Loaded {} locations from the database.", locations.size());


        var restClientService = newBuilder()
                .baseUri(new URI(remoteUrl))
                .connectTimeout(10, SECONDS)
                .readTimeout(10, SECONDS)
                .build(ForecastService.class);

        var examinedLocations = locations.stream().map(location -> {
            var locationResource = restClientService.doGetForecast(
                    location.latitude(),
                    location.longitude(),
                    7,
                    authKey);

            var resource = locationResource.await()
                    .atMost(ofSeconds(30));

            log.info("Found weather forecast location by date {}: {}", date, resource);
            return resource;
        }).toList();

        if (log.isDebugEnabled()) {
            examinedLocations.forEach(loc ->
                            loc.getForecast()
                                    .forEach(f ->
                                            log.debug("temp: {}, wind_spd: {}, datetime: {}, {}",
                                                    f.getTemperature(), f.getWindSpeed(), f.getDatetime(),
                                                    loc.getCityName()))
            );
        }

        var bestResource = optimalWeatherCalculatorService.findBestWeatherForecastLocation(examinedLocations, date);

        log.info("Found optimal weather conditions for windsurfing: {}", bestResource.isPresent());

        return bestResource.map(Response::ok)
                .orElseGet(Response::noContent)
                .build();
    }
}
