package com.petermarshall;

//no pollen data right now. It said that was due to come out in march time.

import com.petermarshall.measurements.AirPollution;
import com.petermarshall.measurements.Conditions;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DailyWeather {
    private final LocalDate day;
    private final ArrayList<HourlyWeather> hourlyData;
    private LocalTime sunrise;
    private LocalTime sunset;
    private AirPollution airPollution;

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

    public Conditions getAvgConditionToday() {
        ArrayList<HourlyWeather> daytimeHours = getDaytimeHours();
        return getModeAvg(daytimeHours, HourlyWeather::getOutlook);
    }

    public ArrayList<HourlyWeather> getDaytimeHours() {
        ArrayList<HourlyWeather> daytimeHours = new ArrayList<>(this.hourlyData);
        daytimeHours.removeIf(hourlyWeather -> {
            LocalTime hour = hourlyWeather.getTime();
            return hour.isBefore(sunrise) || hour.isAfter(sunset);
        });
        return daytimeHours;
    }

    //no real need for generics, just practicing.
    private <T,R> R getModeAvg(ArrayList<T> list, Function<T,R> funcToCompareOn) {
//        Map<Conditions, Long> mode = daytimeHours.stream()
//                .collect(Collectors.groupingBy(HourlyWeather::getOutlook, Collectors.counting()));

        HashMap<R, Integer> map = list.stream()
                .collect(Collectors.groupingBy(funcToCompareOn, HashMap::new, Collectors.summingInt(t -> 1)));

        Optional<Map.Entry<R, Integer>> winner = map.entrySet().stream().max(Comparator.comparing(Map.Entry::getValue));

        if (winner.isPresent()) return winner.get().getKey();
        else return null;
    }

    public ArrayList<HourlyWeather> getHourlyWeather() {
        return new ArrayList<>(hourlyData);
    }

    public LocalDate getDay() {
        return day;
    }

    void setSunrise(LocalTime sunrise) {
        this.sunrise = sunrise;
    }

    void setSunset(LocalTime sunset) {
        this.sunset = sunset;
    }

    void setAirPollution(AirPollution airPollution) {
        this.airPollution = airPollution;
    }

    public LocalTime getSunrise() {
        return sunrise;
    }

    public LocalTime getSunset() {
        return sunset;
    }

    public String getAirAdviceForThoseAtRisk() {
        return airPollution.getAdviceForThoseAtRisk();
    }

    public String getAirAdviceForGeneralPopulation() {
        return airPollution.getAdviceGeneralPopulation();
    }

    public int getAirScore() {
        return airPollution.getScore();
    }
}
