package com.petermarshall.measurements;

import com.petermarshall.measurements.info.VisibilityInfo;

public class Visibility {
    private double score;
    private VisibilityInfo units;

    public Visibility(double score, VisibilityInfo units) {
        this.score = score;
        this.units = units;
    }

    public String getRange() {
        return units.getRange();
    }

    public double getScore() {
        return score;
    }
}
