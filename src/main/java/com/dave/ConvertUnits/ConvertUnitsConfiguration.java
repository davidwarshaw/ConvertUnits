package com.dave.ConvertUnits;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

import javax.validation.constraints.NotNull;

public class ConvertUnitsConfiguration extends Configuration {
    @NotNull
    private Integer port = 8080;

    @JsonProperty
    public Integer getPort() {
        return port;
    }

    @JsonProperty
    public void SetPort(Integer port) {
        this.port = port;
    }
}
