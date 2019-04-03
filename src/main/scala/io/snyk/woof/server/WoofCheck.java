package io.snyk.woof.server;

import com.codahale.metrics.health.HealthCheck;

public class WoofCheck extends HealthCheck {
    @Override
    protected Result check() {
        return Result.healthy();
    }
}
