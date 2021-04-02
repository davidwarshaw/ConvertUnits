package com.dave.ConvertUnits;

import com.dave.ConvertUnits.resources.SiResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class ConvertUnitsApplication extends Application<ConvertUnitsConfiguration> {
    public static void main(String[] args) throws Exception {
        new ConvertUnitsApplication().run(args);
    }

    @Override
    public String getName() {
        return "ConvertUnits";
    }

    @Override
    public void run(ConvertUnitsConfiguration configuration,
                    Environment environment) {
        final SiResource resource = new SiResource();
        environment.jersey().register(resource);
    }

}

