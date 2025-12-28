package org.tibor17.wwws.support;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static org.testcontainers.containers.PostgreSQLContainer.POSTGRESQL_PORT;
import static org.testcontainers.utility.DockerImageName.parse;

/**
 * <p>
 *     UnixSocketClientProviderStrategy: failed with exception BadRequestException (Status 400:
 *     {"message":"client version 1.32 is too old. Minimum supported API version is 1.44,
 *     please upgrade your client to a newer version"}
 * </p>
 *
 * Due to this error, see
 * <a href="https://stackoverflow.com/questions/79817033/sudden-docker-error-about-client-api-version">on Stackoverflow</a>,
 * we have to create the file <a>~/.docker-java.properties</a> just once for all
 * <p>
 *     echo api.version=1.44 >> ~/.docker-java.properties
 * </p>
 */
public class PostgresResource implements
        QuarkusTestResourceLifecycleManager {

    private static final Logger LOG = LoggerFactory.getLogger(PostgresResource.class);

    private PostgreSQLContainer<?> postgreSQLContainer;

    @Override
    public Map<String, String> start() {
        postgreSQLContainer = new PostgreSQLContainer<>(parse("postgres:17"))
                .withDatabaseName("test")
                .withExposedPorts(POSTGRESQL_PORT)
                .withUsername("admin")
                .withPassword("password")
                .withInitScript("postgres-init.sql")
                .withReuse(false);
        postgreSQLContainer.start();
        postgreSQLContainer.followOutput(new Slf4jLogConsumer(LOG));
        Map<String, String> config = new HashMap<>();
        config.put("quarkus.datasource.jdbc.url", postgreSQLContainer.getJdbcUrl());
        config.put("quarkus.datasource.username", postgreSQLContainer.getUsername());
        config.put("quarkus.datasource.password", postgreSQLContainer.getPassword());
        return config;
    }

    @Override
    public void stop() {
        if (postgreSQLContainer != null) {
            postgreSQLContainer.stop();
        }
    }
}


