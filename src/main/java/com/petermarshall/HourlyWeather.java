package com.petermarshall;

import com.petermarshall.measurements.*;
import com.petermarshall.measurements.units.TempUnits;
import com.petermarshall.measurements.units.VelocityUnits;

import java.time.LocalTime;
import java.util.HashSet;

public class HourlyWeather {
    //chance of rain
    //overall outlook
    //temperature
    //feels like temp
    //wind speed
    //gust
    //wind direction
    //visibility
    //humidity
    //uv index
    //sunrise
    //sunset

    //also will need to store the measurements.units these values are measured in.

    private final LocalTime time;

    private Conditions outlook;
    private double chanceOfRain;
    private Temperature temp;
    private Temperature feelsLikeTemp;
    private Wind wind;
    private Visibility visibility;
    private double humidity;
    private UV uv;

    public HourlyWeather(LocalTime time) {
        this.time = time;
    }

    void setOutlook(Conditions outlook) {
        this.outlook = outlook;
    }

    void setChanceOfRain(double chanceOfRain) {
        this.chanceOfRain = chanceOfRain;
    }

    void setTemp(Temperature temp) {
        this.temp = temp;
    }

    void setFeelsLikeTemp(Temperature feelsLikeTemp) {
        this.feelsLikeTemp = feelsLikeTemp;
    }

    void setWind(Wind wind) {
        this.wind = wind;
    }

    void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    void setUv(UV uv) {this.uv = uv;}

    public LocalTime getTime() {
        return time;
    }

    public Conditions getOutlook() {
        return outlook;
    }

    public double getChanceOfRain() {
        return chanceOfRain;
    }

    public double getTemp(TempUnits units) {
        return temp.getTemp(units);
    }

    public double getFeelsLikeTemp(TempUnits units) {
        return feelsLikeTemp.getTemp(units);
    }

    public String getWindDirection() {
        return wind.getDirection();
    }

    public double getWindSpeed(VelocityUnits units) {
        return wind.getSpeed(units);
    }

    public double getWindGust(VelocityUnits units) {
        return wind.getGust(units);
    }

    public String getVisibilityRangeString() {return visibility.getRange();}

    public double getVisibilityScore() {return visibility.getScore();}

    public double getHumidity() {return humidity;}

    public int getUvScore() {return uv.getUvScore();}

    public String getUvRecommendation() {return uv.getRecommendation();}

    public HashSet<LocalTime> getTimesToStayOutTheSun() {return uv.getTimesToStayOutTheSun();}
}
