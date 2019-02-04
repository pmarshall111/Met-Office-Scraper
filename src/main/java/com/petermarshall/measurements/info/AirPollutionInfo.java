package com.petermarshall.measurements.info;

import java.util.Arrays;

public enum AirPollutionInfo {
    UNKNOWN(new int[]{}, "N/A", "N/A"),
    LOW(new int[]{1,2,3}, "Enjoy your usual outdoor activities", "Enjoy your usual outdoor activities"),
    MODERATE(new int[]{4,5,6}, "Enjoy your usual outdoor activities", "Adults and children with lung problems, and adults with heart problems, who experience symptoms, should consider reducing strenuous physical activity, particularly outdoors."),
    HIGH(new int[]{7,8,9}, "Anyone experiencing discomfort such as sore eyes, cough or sore throat should consider reducing activity, particularly outdoors.", "Adults and children with lung problems, and adults with heart problems, should reduce strenuous physical exertion, particularly outdoors, and particularly if they experience symptoms. People with asthma may find they need to use their reliever inhaler more often. Older people should also reduce physical exertion"),
    VERY_HIGH(new int[]{10}, "Reduce physical exertion, particularly outdoors, especially if you experience symptoms such as cough or sore throat.", "Adults and children with lung problems, adults with heart problems, and older people, should avoid strenuous physical activity. People with asthma may find they need to use their reliever inhaler more often");

    private final int[] scoresInCategory;
    private final String adviceForGeneralPopulation;
    private final String adviceForThoseAtRisk;

    AirPollutionInfo(int[] scoresInCategory, String adviceForGeneralPopulation, String adviceForThoseAtRisk) {
        this.scoresInCategory = scoresInCategory;
        this.adviceForGeneralPopulation = adviceForGeneralPopulation;
        this.adviceForThoseAtRisk = adviceForThoseAtRisk;
    }

    public static AirPollutionInfo getAirPollFromIndex(int theirIndex) {
        AirPollutionInfo[] scale = AirPollutionInfo.values();
        for (AirPollutionInfo airPoll: scale) {
            for (int index: airPoll.getScoresInCategory()) {
                if (index == theirIndex) {
                    return airPoll;
                }
            }
        }

        return AirPollutionInfo.UNKNOWN;
    }

    public int[] getScoresInCategory() {
        return Arrays.copyOf(this.scoresInCategory, this.scoresInCategory.length);
    }

    public String getAdviceForGeneralPopulation() {
        return adviceForGeneralPopulation;
    }

    public String getAdviceForThoseAtRisk() {
        return adviceForThoseAtRisk;
    }
}
