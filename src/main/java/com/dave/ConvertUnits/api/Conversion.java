package com.dave.ConvertUnits.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Conversion {
    private String unitName;
    private Double multiplicationFactor;


    public Conversion() {
        // Jackson deserialization
    }

    public Conversion(String unitName, Double multiplicationFactor) {
        this.unitName = unitName;
        this.multiplicationFactor = multiplicationFactor;
    }

    @JsonProperty
    public String getUnitName() {
        return unitName;
    }

    @JsonProperty
    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    @JsonProperty
    public Double getMultiplicationFactor() {
        return multiplicationFactor;
    }

    @JsonProperty
    public void setMultiplicationFactor(Double multiplicationFactor) {
        this.multiplicationFactor = multiplicationFactor;
    }
}