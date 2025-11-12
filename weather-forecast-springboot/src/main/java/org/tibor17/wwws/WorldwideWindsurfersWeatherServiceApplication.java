package org.tibor17.wwws;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import static org.springframework.boot.SpringApplication.run;

@SpringBootApplication
@EnableAutoConfiguration
@PropertySource("classpath:/application.properties")
@ComponentScan(basePackages = "org.tibor17.wwws")
@EntityScan(basePackages = "org.tibor17.wwws.domain")
@EnableJpaRepositories(basePackages = "org.tibor17.wwws.repository")
public class WorldwideWindsurfersWeatherServiceApplication {

    public static void main(String[] args) {
        run(WorldwideWindsurfersWeatherServiceApplication.class, args);
    }

    @Bean
    public CommonsRequestLoggingFilter logFilter() {
        CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
        filter.setIncludeClientInfo(true);
        filter.setIncludeQueryString(true);
        filter.setIncludeHeaders(true);
        filter.setIncludePayload(true);
        filter.setMaxPayloadLength(100);
        filter.setAfterMessageSuffix("...end-of-request");
        return filter;
    }

    @Bean
    public OpenAPI apiDesc() {
        return new OpenAPI()
                .info(new Info()
                        .title("Worldwide Windsurfer's Weather Service")
                        .version("v0.1")
                        .contact(new Contact().email("tibor.digana@gmail.com").name("Tibor Digana"))
                        .description("""
                                This application exposes REST API with weather forecast useful for making windsurfing.
                                This API provides you with best conditions for given date made upon weather forecast
                                within 7 days across several places in the world. When you query the API, it would
                                respond with the place, GEO positions, air temperature and wind speed.
                                """));
    }
}
