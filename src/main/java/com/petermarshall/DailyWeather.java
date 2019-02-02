package com.petermarshall;//no pollen data right now. It said that was due to come out in march time.

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class DailyWeather {
    private final LocalDate day;
    private final ArrayList<HourlyWeather> hourlyData; //TODO: possibly check to see if data is sorted? can write a test for that.

    public DailyWeather(LocalDate day, ArrayList<HourlyWeather> hourlyData) {
        this.day = day;
        this.hourlyData = hourlyData;
    }

    public int getHoursSeparation() {
        LocalTime lastTime = hourlyData.get(hourlyData.size()-1).getTime();
        int lastHour = lastTime.getHour();
        return 24-lastHour;
    }

    public int getNumbRecordsToday() {
        return hourlyData.size();
    }

//    public measurements.Conditions getAvgConditionToday() {
//        //only look during the daytime.
//
//        //will need sunset data to filter out weather patterns outside of this.
//    }

    public ArrayList<HourlyWeather> getHourlyWeather() {
        return new ArrayList<>(hourlyData);
    }

    public String getDay() {
        return day.toString();
    }
}
