package org.tibor17.wwws.resource;

import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@OpenAPIDefinition(
        tags = {
                @Tag(name="REST", description="Representational State Transfer"),
                @Tag(name="API", description="Application Programming Interface")
        },
        info = @Info(
                title="Worldwide Windsurfer's Weather Service",
                version = "v0.1",
                contact = @Contact(
                        name = "Tibor Digana",
                        url = "http://localhost:8080/api/v0.1/windfinder/forecast/dates/{date}"),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0.html"),
                description = """
                                This application exposes REST API with weather forecast useful for making windsurfing.
                                This API provides you with best conditions for given date made upon weather forecast
                                within 7 days across several places in the world. When you query the API, it would
                                respond with the place, GEO positions, air temperature and wind speed.
                                """)
)
public class WeatherForecastApplication extends Application {
}
