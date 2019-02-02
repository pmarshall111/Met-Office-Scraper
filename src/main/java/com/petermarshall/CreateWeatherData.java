package com.petermarshall;

import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import measurements.Temperature;
import measurements.Wind;
import org.w3c.dom.NamedNodeMap;
import measurements.Conditions;
import measurements.units.TempUnits;
import measurements.units.VelocityUnits;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class CreateWeatherData {
    private CreateWeatherData() {
    }

    public static DailyWeather[] fromPage(HtmlPage page) {
        DailyWeather[] futureWeather = new DailyWeather[MetOfficeScraper.numbDaysOfWeather];

        String baseId = "divDayModule";

        for (int i = 0; i<MetOfficeScraper.numbDaysOfWeather; i++) {
            DomElement dayData = page.getElementById(baseId + i);

            DomNode dateNode = dayData.querySelector(".weatherDate .print");
            String dateWithParentheses = dateNode.getTextContent();
            LocalDate date = getDateFromTitle(dateWithParentheses);


            ArrayList<HourlyWeather> hourlyData = new ArrayList<>();

            DomNodeList<DomNode> rowTimesInDay = dayData.querySelectorAll(".weatherTime td");
            rowTimesInDay.forEach(timeRow -> {
                LocalTime time = getTimeFromRow(timeRow);
                HourlyWeather weather = new HourlyWeather(time);
                hourlyData.add(weather);
            });

            DomNodeList<DomNode> hourlyOutlook = dayData.querySelectorAll(".weatherWX td");
            for (int idx = 0; idx<hourlyOutlook.size(); idx++) {
                String outlook = hourlyOutlook.get(idx).getAttributes().getNamedItem("title").getTextContent();
                Conditions condition = Conditions.getCondition(outlook);

                HourlyWeather weather = hourlyData.get(idx);
                weather.setOutlook(condition);
            }

            DomNodeList<DomNode> hourlyChanceOfRain = dayData.querySelectorAll(".weatherRain td");
            for (int idx = 0; idx<hourlyChanceOfRain.size(); idx++) {
                String percent = hourlyChanceOfRain.get(idx).getTextContent();
                int startIndex = percent.charAt(0) == '<' ? 1 : 0;
                String withoutPSign = percent.substring(startIndex, percent.length()-1);
                double probability = Double.parseDouble(withoutPSign)/100;

                HourlyWeather weather = hourlyData.get(idx);
                weather.setChanceOfRain(probability);
            }

            DomNodeList<DomNode> hourlyTemp = dayData.querySelectorAll(".weatherTemp i");
            for (int idx = 0; idx<hourlyTemp.size(); idx++) {
                NamedNodeMap attributes = hourlyTemp.get(idx).getAttributes();
                String temp = attributes.getNamedItem("data-value-raw").getTextContent();
                String unit = attributes.getNamedItem("data-unit").getTextContent();
                double tempVal = Double.parseDouble(temp);
                TempUnits tempUnit = TempUnits.getTempUnit(unit);
                Temperature temperature = new Temperature(tempVal, tempUnit);

                HourlyWeather weather = hourlyData.get(idx);
                weather.setTemp(temperature);
            }

            DomNodeList<DomNode> hourlyFeelsLikeTemp = dayData.querySelectorAll(".waetherFeel td");
            for (int idx = 0; idx<hourlyFeelsLikeTemp.size(); i++) {
                NamedNodeMap attributes = hourlyFeelsLikeTemp.get(idx).getAttributes();
                String temp = attributes.getNamedItem("data-value-raw").getTextContent();
                String unit = attributes.getNamedItem("data-unit").getTextContent();
                double tempVal = Double.parseDouble(temp);
                TempUnits tempUnit = TempUnits.getTempUnit(unit);
                Temperature feelsLikeTemp = new Temperature(tempVal, tempUnit);

                HourlyWeather weather = hourlyData.get(idx);
                weather.setFeelsLikeTemp(feelsLikeTemp);
            }

            DomNodeList<DomNode> hourlyWind = dayData.querySelectorAll(".waetherWind td");
            for (int idx = 0; idx<hourlyWind.size(); i++) {
                DomNodeList<DomNode> windInfoGroup = hourlyWind.get(idx).getChildNodes();

                Wind wind = new Wind();
                for (DomNode windData: windInfoGroup) {
                    String className = windData.getClass().getName();

                    if (className.equals("direction")) {
                        wind.setDirection(windData.getTextContent());
                    } else {
                        NamedNodeMap attributes = hourlyWind.get(idx).getAttributes();
                        String windStr = attributes.getNamedItem("data-value-raw").getTextContent();
                        String unit = attributes.getNamedItem("data-unit").getTextContent();
                        double windVal = Double.parseDouble(windStr);
                        VelocityUnits velUnits = VelocityUnits.getVelocityUnit(windStr);

                        if (className.equals("gust")) {
                            wind.setGust(windVal, velUnits);
                        } else wind.setSpeed(windVal, velUnits);
                    }
                }

                HourlyWeather weather = hourlyData.get(idx);
                weather.setWind(wind);
            }

//
//            DomNodeList<DomNode> hourlyVisibility = dayData.querySelectorAll(".waetherVisibility td");
//            for (int idx = 0; idx<hourlyVisibility.size(); i++) {
//                String visSymbol = hourlyVisibility.get(idx).getTextContent();
//                VisibilityUnits visUnits = VisibilityUnits.getVisibilityUnit(visSymbol);
//
//                NamedNodeMap attributes = hourlyVisibility.get(idx).getAttributes();
//                String visibilityStr = attributes.getNamedItem("data-value-raw").getTextContent();
//                int vis = Integer.parseInt(visibilityStr);
//
//                Visibility visibility = new Visibility(vis, visUnits);
//                com.petermarshall.HourlyWeather weather = hourlyData.get(idx);
//                weather.setVisibility(visibility);
//            }
//
//
//            DomNodeList<DomNode> hourlyHumidity = dayData.querySelectorAll(".weatherHumidity td");
//            for (int idx = 0; idx<hourlyHumidity.size(); idx++) {
//                String percent = hourlyHumidity.get(idx).getTextContent();
//                int startIndex = percent.charAt(0) == '<' ? 1 : 0;
//                String withoutPSign = percent.substring(startIndex, percent.length()-1);
//                double probability = Double.parseDouble(withoutPSign)/100;
//
//                com.petermarshall.HourlyWeather weather = hourlyData.get(idx);
//                weather.setHumidity(probability);
//            }





            DailyWeather todaysWeather = new DailyWeather(date, hourlyData);
            futureWeather[i] = todaysWeather;
        }

        return futureWeather;
    }

    private static LocalTime getTimeFromRow(DomNode row) {
        String time = row.getTextContent();

        if (time.toLowerCase().equals("now")) {
            return LocalTime.now();
        } else {
            String[] partsOfTime = time.split(":");
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
