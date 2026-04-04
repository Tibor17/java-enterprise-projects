package org.tibor17.wwws.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "pg.db")
@Validated
public class DatabaseConfigPropsValidationChecks {
    @NotBlank
    @Size(min = 30, max = 300)
    @Value("${pg.db.url}")
    String url;

    @NotBlank
    @Size(min = 3)
    @Pattern(regexp = "[a-zA-Z0-9_-]+")
    @Value("${pg.db.username}")
    String username;

    @NotBlank
    @Size(min = 3)
    @Pattern(regexp = "[a-zA-Z0-9_-]+")
    @Value("${pg.db.password}")
    String password;
}
