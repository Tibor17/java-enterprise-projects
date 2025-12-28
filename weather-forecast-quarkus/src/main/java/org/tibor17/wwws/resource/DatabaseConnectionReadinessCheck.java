package org.tibor17.wwws.resource;

import io.agroal.api.AgroalDataSource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;

import java.sql.SQLException;

import static org.eclipse.microprofile.health.HealthCheckResponse.down;
import static org.eclipse.microprofile.health.HealthCheckResponse.up;

@Readiness
@Slf4j
@ApplicationScoped
public class DatabaseConnectionReadinessCheck implements HealthCheck {
    @Inject
    Instance<AgroalDataSource> dataSource;

    @Override
    public HealthCheckResponse call() {
        var ds = dataSource.get();
        if (ds != null) {
            try {
                var md = ds.getConnection().getMetaData();
                log.info("Readiness checked database {} {}",
                        md.getDatabaseProductName(), md.getDatabaseProductVersion());
            } catch (SQLException e) {
                log.error(e.getMessage());
                return down("readiness (windfinder application)");
            }
        }

        return up("readiness (windfinder application)");
    }
}
