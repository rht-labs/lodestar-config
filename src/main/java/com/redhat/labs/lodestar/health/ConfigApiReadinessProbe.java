package com.redhat.labs.lodestar.health;

import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;
import org.eclipse.microprofile.health.Readiness;

import com.redhat.labs.lodestar.service.RuntimeConfigService;

@Readiness
@ApplicationScoped
public class ConfigApiReadinessProbe implements HealthCheck {

    @Inject
    RuntimeConfigService service;

    @Override
    public HealthCheckResponse call() {

        HealthCheckResponseBuilder healthCheckResponseBuilder = HealthCheckResponse.named("Runtime Config Available");

        String config = service.getRuntimeConfiguration(Optional.empty());
        if (null == config || config.isBlank()) {
            healthCheckResponseBuilder.down().withData("OK", "\uD83D\uDC4E");
        } else {
            healthCheckResponseBuilder.up().withData("OK", "\uD83D\uDC4D");
        }

        return healthCheckResponseBuilder.build();
    }

}
