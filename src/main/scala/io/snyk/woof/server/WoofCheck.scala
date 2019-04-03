package io.snyk.woof.server

import com.codahale.metrics.health.HealthCheck
import com.codahale.metrics.health.HealthCheck.Result

class WoofCheck extends HealthCheck {
  override protected def check: HealthCheck.Result = Result.healthy
}
