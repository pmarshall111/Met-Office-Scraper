package com.petermarshall.measurements.info;

public enum VisibilityInfo {
    UNKNOWN("u", new int[]{-1,-1}),
    VERY_POOR("vp", new int[]{0,1}),
    POOR("p", new int[]{1,4}),
    MODERATE("m", new int[]{4,10}),
    GOOD("g", new int[]{10,20}),
    VERY_GOOD("vg", new int[]{20,40}),
    EXCELLENT("e", new int[]{40,-1});
    
    final String symbol;
    private final int[] rangeBoundariesInKm;

    VisibilityInfo(String symbol, int[] rangeBoundariesInKm) {
        this.symbol = symbol;
        this.rangeBoundariesInKm = rangeBoundariesInKm;
    }

    public static VisibilityInfo getVisibilityUnit(String theirSymbol) {
        VisibilityInfo[] units = VisibilityInfo.values();
        for (VisibilityInfo unit: units) {
            if (unit.symbol.equals(theirSymbol)) {
                return unit;
            }
        }
        return VisibilityInfo.UNKNOWN;
    }

    public int[] getRangeBoundariesInKm() {
        return new int[]{rangeBoundariesInKm[0], rangeBoundariesInKm[1]};
    }

    public String getRange() {
        int[] rangeBoundaries = rangeBoundariesInKm;
        int lower = rangeBoundaries[0];
        int upper = rangeBoundaries[1];

        if (lower == -1 && upper == -1) {
            return "-";
        } else if (lower == -1) {
            return "Less than " + upper;
        } else if (upper == -1) {
            return "More than " + lower;
        } else {
            return "Between " + lower + "-" + upper + " km";
        }
    }
}
