package io.snyk.woof.server

import com.datasift.dropwizard.scala.ScalaApplication
import io.dropwizard.Application
import io.dropwizard.assets.AssetsBundle
import io.dropwizard.forms.MultiPartBundle
import io.dropwizard.setup.Bootstrap
import io.dropwizard.setup.Environment
import io.snyk.woof.app.ZipHandler
import io.snyk.woof.cli.CliUnzip

class WoofApplication extends ScalaApplication[WoofConfig] {
  override def init(bootstrap: Bootstrap[WoofConfig]): Unit = {
    bootstrap.addBundle(new MultiPartBundle)
    bootstrap.addBundle(new AssetsBundle("/assets", "/", "index.html"))
    bootstrap.addCommand(new CliUnzip)
  }

  @throws[Exception]
  override def run(configuration: WoofConfig, environment: Environment): Unit = {
    environment.jersey.setUrlPattern("/api/*")
    environment.jersey.register(new UnzipResource(new ZipHandler))
    environment.jersey.register(new JsonExceptionMapper)
    environment.healthChecks.register("woof", new WoofCheck)
    environment.lifecycle.addServerLifecycleListener(new WelcomeBanner)
  }
}
