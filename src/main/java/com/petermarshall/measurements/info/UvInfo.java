package com.petermarshall.measurements.info;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashSet;

public enum UvInfo {
    UNKNOWN("N/A",                                        new int[]{},       new int[]{}),
    LOW("You can safely stay outside",                    new int[]{0,1,2},  new int[]{}),
    MODERATE("Take care during midday hours",             new int[]{3,4,5},  new int[]{}),
    HIGH("Seek shade during midday hours",                new int[]{6,7},    new int[]{11,15}),
    VERY_HIGH("Spend time in the shade between 11 and 3", new int[]{8,9,10}, new int[]{11,15}),
    EXTREME("Avoid being outside during midday hours",    new int[]{11},     new int[]{11,15});

    final String message;
    private final int[] uvIndexes;
    private final int[] dontGoOutsideBetween;

    UvInfo(String message, int[] uvIndexes, int[] dontGoOutsideBetween) {
        this.message = message;
        this.uvIndexes = uvIndexes;
        this.dontGoOutsideBetween = dontGoOutsideBetween;
    }

    public static UvInfo getUvFromIndex(int theirIndex) {
        UvInfo[] scale = UvInfo.values();
        for (UvInfo uv: scale) {
            for (int index: uv.getUvIndexes()) {
                if (index == theirIndex) {
                    return uv;
                }
            }
        }

        return UvInfo.UNKNOWN;
    }

    public int[] getUvIndexes() {
        //Arrays.stream(this.uvIndexes).toArray(); //alternative
        return Arrays.copyOf(this.uvIndexes, this.uvIndexes.length);
    }

    public HashSet<LocalTime> getTimesOfDayNotToGoOutside() {
        HashSet<LocalTime> times = new HashSet<>();

        try {
            int minHour = this.dontGoOutsideBetween[0];
            int maxHour = this.dontGoOutsideBetween[1];
            while (minHour < maxHour) {
                times.add(LocalTime.of(minHour, 0));
                minHour++;
            }
        } catch(ArrayIndexOutOfBoundsException e) {
            //no limits of when we can go outside
        }

        return times;
    }

    public String getRecommendation() {
        return this.message;
    }
}
