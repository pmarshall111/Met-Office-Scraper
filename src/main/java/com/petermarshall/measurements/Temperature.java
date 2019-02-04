package com.petermarshall.measurements;

import com.petermarshall.measurements.units.TempUnits;

public class Temperature {
    private double value;
    private TempUnits units;

    public Temperature(double value, TempUnits units) {
        this.value = value;
        this.units = units;
    }

    public double getTemp(TempUnits desiredUnits) {
        if (desiredUnits.equals(this.units)) {
            return this.value;
        } else {
            return TempUnits.convert(this.value, this.units, desiredUnits);
        }
    }
}
