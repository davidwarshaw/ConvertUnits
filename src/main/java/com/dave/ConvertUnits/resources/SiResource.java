package com.dave.ConvertUnits.resources;

import com.codahale.metrics.annotation.Timed;
import com.dave.ConvertUnits.api.Conversion;
import com.dave.ConvertUnits.core.UnitExpression;

import javax.validation.constraints.NotEmpty;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/units/si")
@Produces(MediaType.APPLICATION_JSON)
public class SiResource {
    private final AtomicLong counter;
    private final Logger LOGGER = Logger.getLogger(SiResource.class.getName());

    public SiResource() {
        this.counter = new AtomicLong();
    }

    @GET
    @Timed
    public Conversion convert(@QueryParam("units") @NotEmpty String units) {
        System.out.println("units: " + units);
        try {
            UnitExpression siUnits = new UnitExpression(units);
            String unitName = siUnits.getUnitName();
            Double multiplicationFactor = siUnits.getMultiplicationFactor();
            LOGGER.info(String.format("unit expression conversion: %s -> %s, %f", units, unitName, multiplicationFactor));
            return new Conversion(unitName, multiplicationFactor);

        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
            throw new BadRequestException(e.getMessage());
        }
    }
}