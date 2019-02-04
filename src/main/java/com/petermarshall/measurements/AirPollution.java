package com.petermarshall.measurements;

import com.petermarshall.measurements.info.AirPollutionInfo;

public class AirPollution {

    private int score;
    private AirPollutionInfo airPollutionInfo;

    public AirPollution(int score, AirPollutionInfo airPollutionInfo) {
        this.score = score;
        this.airPollutionInfo = airPollutionInfo;
    }

    public int getScore() {
        return score;
    }

    public String getAdviceGeneralPopulation() {
        return this.airPollutionInfo.getAdviceForGeneralPopulation();
    }

    public String getAdviceForThoseAtRisk() {
        return this.airPollutionInfo.getAdviceForThoseAtRisk();
    }

}
