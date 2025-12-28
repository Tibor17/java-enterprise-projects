package org.tibor17.wwws.resource;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;

import static org.eclipse.microprofile.health.HealthCheckResponse.up;

@Liveness
@ApplicationScoped
public class LivenessCheck implements HealthCheck {
    @Override
    public HealthCheckResponse call() {
        return up("liveness (windfinder application)");
    }
}
