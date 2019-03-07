package com.petermarshall.measurements.units;

public enum TempUnits {
    NO_UNIT(" "),
    CELSIUS("C"),
    FAHRENHEIT("F");

    final String symbol;

    TempUnits(String symbol) {
        this.symbol = symbol;
    }

    public static TempUnits getTempUnit(String theirSign) {
        TempUnits[] units = TempUnits.values();
        for (TempUnits unit: units) {
            if (unit.symbol.equals(theirSign)) {
                return unit;
            }
        }
        return TempUnits.NO_UNIT;
    }

    public static double convert(double val, TempUnits curr, TempUnits desired) {
        if (curr.equals(desired)) return val;

        if (curr.equals(TempUnits.CELSIUS)) {
            if (desired.equals(TempUnits.FAHRENHEIT)) {
                return celsiusToFahrenheit(val);
            }
        }
        else if (curr.equals(TempUnits.FAHRENHEIT)) {
            if (desired.equals(TempUnits.CELSIUS)) {
                return fahrenheitToCelsius(val);
            }
        }

        return -9999999;
    }


    private static double celsiusToFahrenheit(double celsius) {
        return 32 + (9d/5d)*celsius;
    }

    private static double fahrenheitToCelsius(double fahrenheit) {
        return (5d/9d) * (fahrenheit-32);
    }

    public String getCapitalisedName() {
        String firstCharUpper = name().toUpperCase().substring(0,1);
        String restLower = name().toLowerCase().substring(1);
        return firstCharUpper + restLower;
    }
}
