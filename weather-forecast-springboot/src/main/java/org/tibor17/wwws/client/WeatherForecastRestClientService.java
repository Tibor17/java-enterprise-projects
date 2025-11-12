package org.tibor17.wwws.client;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import org.tibor17.wwws.model.restclient.WeatherbitRootResourceDTO;

import java.math.BigDecimal;
import java.net.URI;

@Service
public class WeatherForecastRestClientService {

    @NotNull
    public HttpServiceProxyFactory buildHttpServiceProxyFactory(@NotNull URI baseUri) {
        var restClient = RestClient.builder().baseUrl(baseUri).build();
        var adapter = RestClientAdapter.create(restClient);
        return HttpServiceProxyFactory.builderFor(adapter).build();
    }

    @NotNull
    public WeatherbitRootResourceDTO readWeatherForecast(@NotNull HttpServiceProxyFactory factory,
                                                         @NotNull BigDecimal latitude, @NotNull BigDecimal longitude,
                                                         @NotBlank String authKey) {
        var service = factory.createClient(ForecastService.class);
        return service.doGetForecast(latitude, longitude, 7, authKey);
    }
}
