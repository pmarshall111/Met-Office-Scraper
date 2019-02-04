package com.petermarshall.measurements;

import com.petermarshall.measurements.info.UvInfo;

import java.time.LocalTime;
import java.util.HashSet;

public class UV {
    private int uvScore;
    private UvInfo uvInfo;

    public UV(int uvScore, UvInfo uvInfo) {
        this.uvScore = uvScore;
        this.uvInfo = uvInfo;
    }

    public int getUvScore() {
        return uvScore;
    }

    public HashSet<LocalTime> getTimesToStayOutTheSun() {
        return uvInfo.getTimesOfDayNotToGoOutside();
    }

    public String getRecommendation() {
        return uvInfo.getRecommendation();
    }
}
