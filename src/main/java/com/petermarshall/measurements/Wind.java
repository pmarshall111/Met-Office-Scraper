package com.petermarshall.measurements;

import com.petermarshall.CreateWeatherData;
import com.petermarshall.helpers.AccessAllowed;
import com.petermarshall.measurements.units.VelocityUnits;

//NOTE: would normally be put into measurements package, but Java does not allow access from above into sub-packages,
// so the options are either keeping it in the same package as CreateWeatherData, or making our setters public, which obviously isn't an option.

public class Wind {
    private String direction;

    private double speed;
    private VelocityUnits speedUnits;
    private double gust;
    private VelocityUnits gustUnits;

    private AccessAllowed accessAllowed = new AccessAllowed(CreateWeatherData.class);

    public boolean setDirection(String direction) {
        if (accessAllowed.callerCanUseMethod()) {
            this.direction = direction;
            return true;
        }
        return false;
    }

    public boolean setSpeed(double speed, VelocityUnits speedUnits) {
        if (accessAllowed.callerCanUseMethod()) {
            this.speed = speed;
            this.speedUnits = speedUnits;
            return true;
        }
        return false;
    }

    public boolean setGust(double gust, VelocityUnits gustUnits) {
        if (accessAllowed.callerCanUseMethod()) {
            this.gust = gust;
            this.gustUnits = gustUnits;
            return true;
        }
        return false;
    }


    public String getDirection() {
        return direction;
    }

    public double getSpeed(VelocityUnits desiredUnits) {
        if (desiredUnits.equals(this.speedUnits)) {
            return speed;
        } else {
            return VelocityUnits.convert(speed, this.speedUnits, desiredUnits);
        }
    }

    public double getGust(VelocityUnits desiredUnits) {
        if (desiredUnits.equals(this.gustUnits)) {
            return gust;
        } else {
            return VelocityUnits.convert(gust, this.gustUnits, desiredUnits);
        }
    }
}
