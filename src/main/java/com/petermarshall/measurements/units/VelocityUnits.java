package com.petermarshall.measurements.units;

public enum VelocityUnits {
    NO_UNIT("N/A"),
    MPH("mph"),
    KPH("kph"),
    MPS("mps");

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
                return mphToKmph(val);
            } else if (desired.equals(VelocityUnits.MPS)) {
                return mphToMps(val);
            }
        }
        else if (current.equals(VelocityUnits.KPH)) {
            if (desired.equals(VelocityUnits.MPH)) {
                return kmphToMph(val);
            } else if (desired.equals(VelocityUnits.MPS)) {
                return kmphToMps(val);
            }
        }
        else if (current.equals(VelocityUnits.MPS)) {
            if (desired.equals(VelocityUnits.KPH)) {
                return mpsToKmph(val);
            } else if (desired.equals(VelocityUnits.MPH)) {
                return mpsToMph(val);
            }
        }

        return -9999999;
    }

    public static void main(String[] args) {
        System.out.println(convert(5, VelocityUnits.MPH, VelocityUnits.KPH));
    }

    private static double mphToKmph(double miles) {
        return miles * 1.609;
    }

    private static double kmphToMph(double kmph) {
        return kmph / 1.609;
    }

    private static double mpsToKmph(double mps) {
        return mps * 3.6;
    }

    private static double kmphToMps(double kmph) {
        return kmph / 3.6;
    }

    private static double mpsToMph(double mps) {
        double kmph = mpsToKmph(mps);
        return kmphToMph(kmph);
    }

    private static double mphToMps(double mph) {
        double kmph = mphToMps(mph);
        return kmphToMps(kmph);
    }
}
