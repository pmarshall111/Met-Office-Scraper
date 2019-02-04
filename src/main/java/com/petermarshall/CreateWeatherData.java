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
    private static DailyWeather currentDaysWeather;
    private static ArrayList<HourlyWeather> currentDaysHourlyData;

    private CreateWeatherData() {
    }

    public static DailyWeather[] fromPage(HtmlPage page) {
        String baseId = "divDayModule";
        futureWeather = new DailyWeather[MetOfficeScraper.numbDaysOfWeather];

        for (int i = 0; i<MetOfficeScraper.numbDaysOfWeather; i++) {
            DomElement dayData = page.getElementById(baseId + i);

            setFieldsToCurrentDay(dayData, i);
            addHoursThatHaveData();
            addOutlook();
            addTemp();
            addFeelsLikeTemp();
            addChanceOfRainData();
            addWindData();
            addVisibilityData();
            addHumidityData();
            addUvData();
            addSunriseSunsetTimes();
            addAirPollutionData();
            //todo: add chance of rain
        }

        return futureWeather;
    }

    private static void addChanceOfRainData() {
        DomNodeList<DomNode> hourlyChanceOfRain = currentDaysData.querySelectorAll(".weatherRain td");
        for (int idx = 0; idx<hourlyChanceOfRain.size(); idx++) {
            String chanceString = hourlyChanceOfRain.get(idx).getTextContent();
            double probability = convertPercentStringToProbability(chanceString);

            HourlyWeather weather = currentDaysHourlyData.get(idx);
            weather.setChanceOfRain(probability);
        };
    }

    private static LocalDate getTodaysDate(DomElement dayData) {
        DomNode dateNode = dayData.querySelector(".weatherDate .print");
        String dateWithParentheses = dateNode.getTextContent();
        return getDateFromTitle(dateWithParentheses);
    }

    private static void setFieldsToCurrentDay(DomElement dayData, int index) {
        LocalDate date = getTodaysDate(dayData);
        currentDaysData = dayData;
        currentDaysHourlyData = new ArrayList<>();
        currentDaysWeather = new DailyWeather(date, currentDaysHourlyData);

        futureWeather[index] = currentDaysWeather;
    }

    private static void addHoursThatHaveData() {
        DomNodeList<DomNode> rowTimesInDay = currentDaysData.querySelectorAll(".weatherTime td");
        rowTimesInDay.forEach(timeRow -> {
            LocalTime time = getTimeFromHHMM(timeRow.getTextContent());
            HourlyWeather weather = new HourlyWeather(time);
            currentDaysHourlyData.add(weather);
        });
    }

    private static void addOutlook() {
        DomNodeList<DomNode> hourlyOutlook = currentDaysData.querySelectorAll(".weatherWX td");
        for (int idx = 0; idx<hourlyOutlook.size(); idx++) {
            String outlook = hourlyOutlook.get(idx).getAttributes().getNamedItem("title").getTextContent();
            Conditions condition = Conditions.getCondition(outlook);

            HourlyWeather weather = currentDaysHourlyData.get(idx);
            weather.setOutlook(condition);
        }
    }

    private static void addTemp() {
        DomNodeList<DomNode> hourlyTemp = currentDaysData.querySelectorAll(".weatherTemp i");
        for (int idx = 0; idx<hourlyTemp.size(); idx++) {
            NamedNodeMap attributes = hourlyTemp.get(idx).getAttributes();
            String temp = attributes.getNamedItem("data-value-raw").getTextContent();
            String unit = attributes.getNamedItem("data-unit").getTextContent();
            double tempVal = Double.parseDouble(temp);
            TempUnits tempUnit = TempUnits.getTempUnit(unit);
            Temperature temperature = new Temperature(tempVal, tempUnit);

            HourlyWeather weather = currentDaysHourlyData.get(idx);
            weather.setTemp(temperature);
        }
    }

    private static void addFeelsLikeTemp() {
        DomNodeList<DomNode> hourlyFeelsLikeTemp = currentDaysData.querySelectorAll(".weatherFeel td");
        for (int idx = 0; idx<hourlyFeelsLikeTemp.size(); idx++) {
            NamedNodeMap attributes = hourlyFeelsLikeTemp.get(idx).getAttributes();
            String temp = attributes.getNamedItem("data-value-raw").getTextContent();
            String unit = attributes.getNamedItem("data-unit").getTextContent();
            double tempVal = Double.parseDouble(temp);
            TempUnits tempUnit = TempUnits.getTempUnit(unit);
            Temperature feelsLikeTemp = new Temperature(tempVal, tempUnit);

            HourlyWeather weather = currentDaysHourlyData.get(idx);
            weather.setFeelsLikeTemp(feelsLikeTemp);
        }
    }

    private static void addWindData() {
        DomNodeList<DomNode> hourlyWind = currentDaysData.querySelectorAll(".weatherWind td");
        for (int idx = 0; idx<hourlyWind.size(); idx++) {
            DomNode currHourWind = hourlyWind.get(idx);

            DomNode directionNode = currHourWind.querySelector(".direction");
            DomNode speedNode = currHourWind.querySelector(".icon");
            DomNode gustNode = currHourWind.querySelector(".gust");

            Wind wind = new Wind();
            wind.setDirection(directionNode.getTextContent());
            wind.setSpeed( getRawVal(speedNode), getVelUnits(speedNode) );
            wind.setGust( getRawVal(gustNode), getVelUnits(gustNode) );

            HourlyWeather weather = currentDaysHourlyData.get(idx);
            weather.setWind(wind);
        }
    }

    private static VelocityUnits getVelUnits(DomNode node) {
        NamedNodeMap attributes = node.getAttributes();
        String unit = attributes.getNamedItem("data-unit").getTextContent();
        return VelocityUnits.getVelocityUnit(unit);
    }

    private static double getRawVal(DomNode node) {
        NamedNodeMap attributes = node.getAttributes();
        String windStr = attributes.getNamedItem("data-value-raw").getTextContent();
        return Double.parseDouble(windStr);
    }

    private static void addVisibilityData() {
        DomNodeList<DomNode> hourlyVisibility = currentDaysData.querySelectorAll(".weatherVisibility td");
        for (int idx = 0; idx<hourlyVisibility.size(); idx++) {
            String visSymbol = hourlyVisibility.get(idx).getTextContent();
            VisibilityInfo visUnits = VisibilityInfo.getVisibilityUnit(visSymbol);

            NamedNodeMap attributes = hourlyVisibility.get(idx).getAttributes();
            String visibilityStr = attributes.getNamedItem("data-value-raw").getTextContent();
            double vis = Double.parseDouble(visibilityStr) / 100;

            Visibility visibility = new Visibility(vis, visUnits);
            HourlyWeather weather = currentDaysHourlyData.get(idx);
            weather.setVisibility(visibility);
        }
    }

    private static void addHumidityData() {
        DomNodeList<DomNode> hourlyHumidity = currentDaysData.querySelectorAll(".weatherHumidity td");
        for (int idx = 0; idx<hourlyHumidity.size(); idx++) {
            String percent = hourlyHumidity.get(idx).getTextContent();
            double probability = convertPercentStringToProbability(percent);

            HourlyWeather weather = currentDaysHourlyData.get(idx);
            weather.setHumidity(probability);
        }
    }

    private static double convertPercentStringToProbability(String str) {
        str = str.trim();
        int startIndex = Character.isDigit(str.charAt(0)) ? 0 : 1;
        String withoutPSign = str.substring(startIndex, str.length()-1);
        return Double.parseDouble(withoutPSign)/100;
    }

    private static void addUvData() {
        DomNodeList<DomNode> hourlyUV  = currentDaysData.querySelectorAll(".weatherUV i");
        for (int idx = 0; idx<hourlyUV.size(); idx++) {
            String scoreStr = hourlyUV.get(idx).getTextContent().trim();
            if (scoreStr.length() == 0) {
                scoreStr = "0";
            }
            int score = Integer.parseInt(scoreStr);
            UvInfo uvInfo = UvInfo.getUvFromIndex(score);
            UV uv = new UV(score, uvInfo);

            HourlyWeather weather = currentDaysHourlyData.get(idx);
            weather.setUv(uv);
        }
    }

    private static void addSunriseSunsetTimes() {
        DomNodeList<DomNode> dayInfo = currentDaysData.querySelectorAll(".sunInner span");
        for (int idx = 0; idx<dayInfo.size(); idx++) {
            String text = dayInfo.get(idx).getTextContent().trim();
            if (text.toLowerCase().contains("sunset")) {
                String hhmm = getTimeFromSunString(text);
                currentDaysWeather.setSunset(getTimeFromHHMM(hhmm));
            } else if (text.toLowerCase().contains("sunrise")) {
                String hhmm = getTimeFromSunString(text);
                currentDaysWeather.setSunrise(getTimeFromHHMM(hhmm));
            }
        }
    }

    //format "Sunrise: 12:34"
    private static String getTimeFromSunString(String sunString) {
        return sunString.substring(sunString.length()-5);
    }

    private static void addAirPollutionData() {
        DomNodeList<DomNode> dayInfo = currentDaysData.querySelectorAll(".sunInner .icon");
        for (int idx = 0; idx<dayInfo.size(); idx++) {
            NamedNodeMap attributes = dayInfo.get(idx).getAttributes();
            String dataType = attributes.getNamedItem("data-type").getTextContent();
            if (dataType.equals("aq")) {
                String pollString = attributes.getNamedItem("data-value").getTextContent();
                int pollutionVal = Integer.parseInt(pollString);
                AirPollutionInfo airPollutionInfo = AirPollutionInfo.getAirPollFromIndex(pollutionVal);
                AirPollution airPollution = new AirPollution(pollutionVal, airPollutionInfo);

                currentDaysWeather.setAirPollution(airPollution);
            }
        }
    }

    private static LocalTime getTimeFromHHMM(String HHMM) {

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

    //input format is (Friday 1 February 2019)
    private static LocalDate getDateFromTitle(String date) {
        if (date.charAt(0) == '(' && date.charAt(date.length()-1) == ')') {
            date = date.substring(1, date.length()-1);
        }

        String[] partsOfDate = date.split(" ");
        int year = Integer.parseInt(partsOfDate[3]);
        int month = getMonthNumb(partsOfDate[2]);
        int day = Integer.parseInt(partsOfDate[1]);

        return LocalDate.of(year, month, day);
    }


    private static int getMonthNumb(String monthString) {
        int month;

        switch(monthString) {
            case "January":
                month = 1;
                break;
            case "February":
                month = 2;
                break;
            case "March":
                month = 3;
                break;
            case "April":
                month = 4;
                break;
            case "May":
                month = 5;
                break;
            case "June":
                month = 6;
                break;
            case "July":
                month = 7;
                break;
            case "August":
                month = 8;
                break;
            case "September":
                month = 9;
                break;
            case "October":
                month = 10;
                break;
            case "November":
                month = 11;
                break;
            case "December":
                month = 12;
                break;
            default:
                throw new RuntimeException("Could not change date string for month into number");
        }
        return month;
    }
}
