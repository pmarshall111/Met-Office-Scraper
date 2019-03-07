package com.petermarshall.measurements;

import com.petermarshall.CreateWeatherData;
import com.petermarshall.helpers.AccessAllowed;
import com.petermarshall.measurements.units.VelocityUnits;

public class WindGust {
    private double gust;
    private VelocityUnits gustUnits;

    private AccessAllowed accessAllowed = new AccessAllowed(CreateWeatherData.class);

    public boolean setGust(double gust, VelocityUnits gustUnits) {
        if (accessAllowed.callerCanUseMethod()) {
            this.gust = gust;
            this.gustUnits = gustUnits;
            return true;
        }
        return false;
    }

    public double getGust(VelocityUnits desiredUnits) {
        if (desiredUnits.equals(this.gustUnits)) {
            return gust;
        } else {
            return VelocityUnits.convert(gust, this.gustUnits, desiredUnits);
        }
    }
}
