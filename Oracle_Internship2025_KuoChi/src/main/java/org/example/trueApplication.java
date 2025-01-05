package org.example;

import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import org.example.resources.BasicHealthCheck;
import org.example.resources.CorsFilter;
import org.example.resources.MinCoinCalculator;

public class trueApplication extends Application<trueConfiguration> {

    public static void main(final String[] args) throws Exception {
        new trueApplication().run(args);
    }

    @Override
    public String getName() {
        return "true";
    }

    @Override
    public void initialize(final Bootstrap<trueConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final trueConfiguration configuration,
                    final Environment environment) {

        CorsFilter corsFilter = new CorsFilter();
        environment.jersey().register(corsFilter);

        MinCoinCalculator minCoinCalculator = new MinCoinCalculator();
        environment.jersey().register(minCoinCalculator);

        BasicHealthCheck basicHealthCheck = new BasicHealthCheck();
        environment.jersey().register(basicHealthCheck);
    }

}
