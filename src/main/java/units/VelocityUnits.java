package units;

public enum VelocityUnits {
    NO_UNIT("N/A"),
    MPH("mph"),
    KPH("kph");

    final String descriptor;

    VelocityUnits(String descriptor) {
        this.descriptor = descriptor;
    }

    public static VelocityUnits getVelocityUnit(String theirDesc) {
        VelocityUnits[] units = VelocityUnits.values();
        for (VelocityUnits unit: units) {
            if (unit.descriptor.equals(theirDesc)) {
                return unit;
            }
        }
        return VelocityUnits.NO_UNIT;
    }

    public static double convert(double val, VelocityUnits current, VelocityUnits desired) {
        if (current.equals(desired)) return val;
        else if (current.equals(VelocityUnits.MPH)) {
            if (desired.equals(VelocityUnits.KPH)) {
                return milesToKm(val);
            }
        }
        else if (current.equals(VelocityUnits.KPH)) {
            if (desired.equals(VelocityUnits.MPH)) {
                return kmToMiles(val);
            }
        }

        return -9999999;
    }

    private static double milesToKm(double miles) {
        return miles * 1.609;
    }

    private static double kmToMiles(double km) {
        return km / 1.609;
    }
}
