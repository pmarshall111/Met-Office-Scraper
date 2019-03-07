package com.petermarshall;

import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.petermarshall.measurements.*;
import com.petermarshall.measurements.info.AirPollutionInfo;
import com.petermarshall.measurements.units.TempUnits;
import com.petermarshall.measurements.info.UvInfo;
import com.petermarshall.measurements.units.VelocityUnits;
import com.petermarshall.measurements.info.VisibilityInfo;
import org.w3c.dom.NamedNodeMap;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class CreateWeatherData {
    private static DailyWeather[] futureWeather;
    private static DomElement currentDaysData;
    private static DomNode currentDaysHighlights;
    private static DailyWeather currentDaysWeather;
    private static ArrayList<HourlyWeather> currentDaysHourlyData;

    private CreateWeatherData() {
    }

    public static DailyWeather[] fromPage(HtmlPage page) {
        String baseId = "day";
        futureWeather = new DailyWeather[MetOfficeScraper.numbDaysOfWeather];
        DomNodeList<DomNode> daysHighlightsData = page.querySelectorAll("#highlightsContainer > div");

        for (int i = 0; i<MetOfficeScraper.numbDaysOfWeather; i++) {
            DomElement dayInDepthData = page.getElementById(baseId + i);

            setFieldsToCurrentDay(dayInDepthData, daysHighlightsData, i);
            addHoursThatHaveData();
            addOutlook();
            addTemp();
            addFeelsLikeTemp();
            addChanceOfRainData();
            addWindSpeedAndDirection();
            addWindGust();
            addVisibilityData();
            addHumidityData();
            addUvData();
            addSunriseSunsetTimes();
            addAirPollutionData();
        }

        return futureWeather;
    }

    private static void addChanceOfRainData() {
        DomNodeList<DomNode> hourlyChanceOfRain = currentDaysData.querySelectorAll(".step-pop td");
        for (int idx = 0; idx<hourlyChanceOfRain.size(); idx++) {
            String chanceString = hourlyChanceOfRain.get(idx).getTextContent().trim();
            double probability = convertPercentStringToProbability(chanceString);

            HourlyWeather weather = currentDaysHourlyData.get(idx);
            weather.setChanceOfRain(probability);
        };
    }

    private static LocalDate getTodaysDate(DomElement dayData) {
        String yyyyMmDd = dayData.getAttribute("data-content-id");

        String[] partsOfDate = yyyyMmDd.split("-");
        int year = Integer.parseInt(partsOfDate[0]);
        int month = Integer.parseInt(partsOfDate[1]);
        int day = Integer.parseInt(partsOfDate[2]);

        return LocalDate.of(year, month, day);
    }

    private static void setFieldsToCurrentDay(DomElement dayInDepthData, DomNodeList<DomNode> allDaysHighlights, int index) {
        LocalDate date = getTodaysDate(dayInDepthData);
        currentDaysData = dayInDepthData;
        currentDaysHighlights = allDaysHighlights.get(index);

        currentDaysHourlyData = new ArrayList<>();
        currentDaysWeather = new DailyWeather(date, currentDaysHourlyData);

        futureWeather[index] = currentDaysWeather;
    }

    private static void addHoursThatHaveData() {
        DomNodeList<DomNode> rowTimesInDay = currentDaysData.querySelectorAll(".step-time th");
        rowTimesInDay.forEach(timeRow -> {
            LocalTime time = getTimeFromHHMM(timeRow.getTextContent());
            HourlyWeather weather = new HourlyWeather(time);
            currentDaysHourlyData.add(weather);
        });
    }

    private static void addOutlook() {
        DomNodeList<DomNode> hourlyOutlook = currentDaysData.querySelectorAll(".step-symbol img");
        for (int idx = 0; idx<hourlyOutlook.size(); idx++) {
            String outlook = hourlyOutlook.get(idx).getAttributes().getNamedItem("title").getTextContent();
            Conditions condition = Conditions.getCondition(outlook);

            HourlyWeather weather = currentDaysHourlyData.get(idx);
            weather.setOutlook(condition);
        }
    }

    private static void addTemp() {
        DomNodeList<DomNode> hourlyTemp = currentDaysData.querySelectorAll(".step-temp div");
        for (int idx = 0; idx<hourlyTemp.size(); idx++) {
            NamedNodeMap attributes = hourlyTemp.get(idx).getAttributes();
            String temp = attributes.getNamedItem("data-value").getTextContent();
            double tempVal = Double.parseDouble(temp);
            Temperature temperature = new Temperature(tempVal, TempUnits.CELSIUS);

            HourlyWeather weather = currentDaysHourlyData.get(idx);
            weather.setTemp(temperature);
        }
    }

    private static void addFeelsLikeTemp() {
        DomNodeList<DomNode> hourlyFeelsLikeTemp = currentDaysData.querySelectorAll(".step-feels-like span");
        for (int idx = 0; idx<hourlyFeelsLikeTemp.size(); idx++) {
            NamedNodeMap attributes = hourlyFeelsLikeTemp.get(idx).getAttributes();
            String temp = attributes.getNamedItem("data-value").getTextContent();
            double tempVal = Double.parseDouble(temp);
            Temperature feelsLikeTemp = new Temperature(tempVal, TempUnits.CELSIUS);

            HourlyWeather weather = currentDaysHourlyData.get(idx);
            weather.setFeelsLikeTemp(feelsLikeTemp);
        }
    }

    private static void addWindSpeedAndDirection() {
        DomNodeList<DomNode> hourlyWind = currentDaysData.querySelectorAll(".step-wind td");
        for (int idx = 0; idx<hourlyWind.size(); idx++) {
            DomNode currHourWind = hourlyWind.get(idx);

            DomNode directionNode = currHourWind.querySelector(".direction");
            String direction = directionNode.getAttributes().getNamedItem("data-value").getNodeValue();

            DomNode speedNode = currHourWind.querySelector(".speed");
            String speedStr = speedNode.getAttributes().getNamedItem("data-value").getNodeValue();
            double speedInMetersPS = Double.parseDouble(speedStr);

            WindSpeedDirection windSpeedDirection = new WindSpeedDirection();
            windSpeedDirection.setDirection(direction);
            windSpeedDirection.setSpeed(speedInMetersPS, VelocityUnits.MPS);

            HourlyWeather weather = currentDaysHourlyData.get(idx);
            weather.setWindSpeedDirection(windSpeedDirection);
        }
    }

    private static void addWindGust() {
        DomNodeList<DomNode> hourlyWindGust = currentDaysData.querySelectorAll(".step-wind-gust span");
        for (int idx = 0; idx<hourlyWindGust.size(); idx++) {
            DomNode gustNode = hourlyWindGust.get(idx);
            String gustStr = gustNode.getAttributes().getNamedItem("data-value").getNodeValue();
            double gustInMetersPS = Double.parseDouble(gustStr);

            WindGust windGust = new WindGust();
            windGust.setGust(gustInMetersPS, VelocityUnits.MPS);

            HourlyWeather weather = currentDaysHourlyData.get(idx);
            weather.setWindGust(windGust);
        }
    }

    private static void addVisibilityData() {
        DomNodeList<DomNode> hourlyVisibility = currentDaysData.querySelectorAll(".step-visibility span");
        for (int idx = 0; idx<hourlyVisibility.size(); idx++) {
            String visSymbol = hourlyVisibility.get(idx).getNodeValue();
            VisibilityInfo visUnits = VisibilityInfo.getVisibilityUnit(visSymbol);

            NamedNodeMap attributes = hourlyVisibility.get(idx).getAttributes();
            String visibilityStr = attributes.getNamedItem("data-value").getNodeValue();
            double vis = Double.parseDouble(visibilityStr) / 100;

            Visibility visibility = new Visibility(vis, visUnits);
            HourlyWeather weather = currentDaysHourlyData.get(idx);
            weather.setVisibility(visibility);
        }
    }

    private static void addHumidityData() {
        DomNodeList<DomNode> hourlyHumidity = currentDaysData.querySelectorAll(".step-humidity span");
        for (int idx = 0; idx<hourlyHumidity.size(); idx++) {
            String percent = hourlyHumidity.get(idx).getAttributes().getNamedItem("data-value").getNodeValue();
            double humidity = Double.parseDouble(percent) / 100;

            HourlyWeather weather = currentDaysHourlyData.get(idx);
            weather.setHumidity(humidity);
        }
    }

    private static double convertPercentStringToProbability(String str) {
        str = str.trim();
        int startIndex = Character.isDigit(str.charAt(0)) ? 0 : 1;
        String withoutPSign = str.substring(startIndex, str.length()-1);
        return Double.parseDouble(withoutPSign)/100;
    }

    private static void addUvData() {
        DomNodeList<DomNode> hourlyUV  = currentDaysData.querySelectorAll(".step-uv td");
        for (int idx = 0; idx<hourlyUV.size(); idx++) {
            String scoreStr = hourlyUV.get(idx).getAttributes().getNamedItem("data-value").getNodeValue();
            int score = Integer.parseInt(scoreStr);

            UvInfo uvInfo = UvInfo.getUvFromIndex(score);
            UV uv = new UV(score, uvInfo);

            HourlyWeather weather = currentDaysHourlyData.get(idx);
            weather.setUv(uv);
        }
    }

    private static void addSunriseSunsetTimes() {
        String sunrise = currentDaysHighlights.querySelector(".sunrise time").getTextContent();
        String sunset = currentDaysHighlights.querySelector(".sunset time").getTextContent();

        currentDaysWeather.setSunrise(getTimeFromHHMM(sunrise));
        currentDaysWeather.setSunset(getTimeFromHHMM(sunset));
    }


    private static void addAirPollutionData() {
        DomNode spanContainer = currentDaysHighlights.querySelector(".weather-item span[data-type=aq]");
        try {
            String pollutionStr = spanContainer.getAttributes().getNamedItem("data-value").getNodeValue();
            int pollutionVal = Integer.parseInt(pollutionStr);
            AirPollutionInfo airPollutionInfo = AirPollutionInfo.getAirPollFromIndex(pollutionVal);
            AirPollution airPollution = new AirPollution(pollutionVal, airPollutionInfo);
            currentDaysWeather.setAirPollution(airPollution);
        } catch (NullPointerException e) {
            //only first 5 days have pollution data
        }
    }

    private static LocalTime getTimeFromHHMM(String HHMM) {
        HHMM = HHMM.trim();

        if (HHMM.toLowerCase().equals("now")) {
            LocalTime fullTime = LocalTime.now();
            return LocalTime.of(fullTime.getHour(), fullTime.getMinute());
        } else {
            String[] partsOfTime = HHMM.split(":");
            int hour = Integer.parseInt(partsOfTime[0]);
            int minute = Integer.parseInt(partsOfTime[1]);

            return LocalTime.of(hour, minute);
        }
    }

}
