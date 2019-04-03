package io.snyk.woof.server;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.forms.MultiPartBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.snyk.woof.app.ZipHandler;
import io.snyk.woof.cli.CliUnzip;

public class WoofApplication extends Application<WoofConfig> {
    public static void main(String[] args) throws Exception {
        new WoofApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<WoofConfig> bootstrap) {
        bootstrap.addBundle(new MultiPartBundle());
        bootstrap.addBundle(new AssetsBundle("/assets", "/", "index.html"));
        bootstrap.addCommand(new CliUnzip());
    }

    @Override
    public void run(WoofConfig configuration, Environment environment) throws Exception {
        environment.jersey().setUrlPattern("/api/*");
        environment.jersey().register(new UnzipResource(new ZipHandler()));
        environment.jersey().register(new JsonExceptionMapper());
        environment.healthChecks().register("woof", new WoofCheck());
        environment.lifecycle().addServerLifecycleListener(new WelcomeBanner());
    }
}
