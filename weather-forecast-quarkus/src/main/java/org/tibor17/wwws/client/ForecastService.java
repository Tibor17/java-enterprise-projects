package org.tibor17.wwws.client;

import io.quarkus.rest.client.reactive.ClientExceptionMapper;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.constraints.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tibor17.wwws.model.restclient.WeatherbitRootResourceDTO;
import org.tibor17.wwws.resource.TooManyRequestsException;

import java.math.BigDecimal;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.Response.Status.*;

@ApplicationScoped
@Path("/v2.0/forecast/daily")
public interface ForecastService {
    static final Logger LOG = LoggerFactory.getLogger(ForecastService.class);

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
     * @throws NotAuthorizedException if no resource found
     * @throws ForbiddenException if e.g. wrong {@code key}
     */
    @GET
    @Produces(APPLICATION_JSON)
    Uni<WeatherbitRootResourceDTO> doGetForecast(@QueryParam("lat") @NotNull @Min(-90) @Max (90) BigDecimal latitude,
                                                 @QueryParam("lon") @NotNull @Min(-180) @Max (180) BigDecimal longitude,
                                                 @QueryParam("days") @Min(1) int days,
                                                 @QueryParam("key") @NotBlank String key);

    @ClientExceptionMapper
    static RuntimeException toException(Response response) {
        LOG.info("REST Client call has handled the status code HTTP/{} '{}'.",
                response.getStatusInfo().getStatusCode(),
                response.getStatusInfo().getReasonPhrase());

        return switch (response.getStatusInfo()) {
            case FORBIDDEN ->
                    throw new ForbiddenException("The remote service responded with HTTP "
                            + FORBIDDEN.getStatusCode()
                            + ". "
                            + response.getStatusInfo().getReasonPhrase());
            case UNAUTHORIZED ->
                    throw new ForbiddenException("The remote service responded with HTTP "
                            + UNAUTHORIZED.getStatusCode()
                            + ". "
                            + response.getStatusInfo().getReasonPhrase());
            case BAD_REQUEST ->
                    throw new BadRequestException(response.getStatusInfo().getReasonPhrase());
            case NOT_FOUND ->
                    throw new NotFoundException(response.getStatusInfo().getReasonPhrase());
            case TOO_MANY_REQUESTS ->
                    throw new TooManyRequestsException(response.getStatusInfo().getReasonPhrase());
            default -> null;
        };
    }
}
