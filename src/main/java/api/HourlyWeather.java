package api;

import measurements.Wind;
import units.Conditions;
import units.TempUnits;
import units.VelocityUnits;
import measurements.Temperature;

import java.time.LocalTime;

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

    //also will need to store the units these values are measured in.

    private final LocalTime time;

    private Conditions outlook;
    private double chanceOfRain;
    private Temperature temp;
    private Temperature feelsLikeTemp;
    private Wind wind;

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
}
