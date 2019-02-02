package measurements;

import units.TempUnits;

public class Temperature {
    private double value;
    private TempUnits units;

    public Temperature(double value, TempUnits units) {
        this.value = value;
        this.units = units;
    }

    public double getTemp(TempUnits units) {
        if (units.equals(this.units)) {
            return this.value;
        } else {
            return TempUnits.convert(this.value, this.units, TempUnits.CELSIUS);
        }
    }
}
