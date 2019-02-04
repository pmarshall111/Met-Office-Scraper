import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.petermarshall.DailyWeather;
import com.petermarshall.HourlyWeather;
import com.petermarshall.MetOfficeScraper;
import com.petermarshall.helpers.Locations;
import com.petermarshall.measurements.Conditions;
import com.petermarshall.measurements.units.TempUnits;
import com.petermarshall.measurements.units.VelocityUnits;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

//all in the same file so we don't have to fetch the met office page multiple times which takes about 30s each time.

public class testScraper {
    MetOfficeScraper metOfficeScraper;
    Locations testLocation = Locations.CHALFONT_ST_PETER;
    DailyWeather[] forecast;

    @Before
    public void setUp() throws Exception {
        metOfficeScraper = new MetOfficeScraper(testLocation);
        forecast = metOfficeScraper.getForecast();
    }

    @Test
    public void testHtmlUnitReturnsValidPage() {
        HtmlPage page = metOfficeScraper.getPage();

        Assert.assertTrue("Page is not null", page != null);
    }

    @Test
    public void testPageIsCorrectLocation() {
        String location = metOfficeScraper.getLocation();
        System.out.println(location);
        Assert.assertEquals("Scraped in the correct location", testLocation.metOfficeLocation, location);
    }

    @Test
    public void testCanGet7DaysWeather() {
        Assert.assertEquals("Forecast has 7 weather data classes", 7, forecast.length);
    }

    @Test
    public void testCanGetToday() {
        DailyWeather today = forecast[0];
        int hoursBetweenData = today.getHoursSeparation();

        Assert.assertEquals("Gets data every hour today", 1, hoursBetweenData);
    }

    @Test
    public void testCanGetTomorrow() {
        DailyWeather tomorrow = forecast[1];
        int hoursBetweenData = tomorrow.getHoursSeparation();

        Assert.assertEquals("Gets data every hour tomorrow", 1, hoursBetweenData);
    }

    @Test
    public void testGetsDataEvery3HoursAfterTomorrow() {
        for (int i = 2; i<forecast.length; i++) {
            int hoursBetweenData = forecast[i].getHoursSeparation();

            Assert.assertEquals("Gets data every 3 hours on days after. Day where error was " + i, 3, hoursBetweenData);
        }
    }

    @Test
    public void testHoursAreSorted() {
        for (DailyWeather dailyWeather: forecast) {
            LocalTime lastTime = null;
            for (HourlyWeather hourlyWeather : dailyWeather.getHourlyWeather()) {
                LocalTime currTime = hourlyWeather.getTime();
                if (lastTime != null) {
                    Assert.assertTrue("Hourly data not sorted for " + dailyWeather.getDay(), currTime.isAfter(lastTime));
                }

                lastTime = currTime;
            }
        }
    }

    @Test
    public void testDaysAreSorted() {
        LocalDate lastDate = null;
        for (DailyWeather dailyWeather: forecast) {
            LocalDate currDate = dailyWeather.getDay();
            if (lastDate != null) {
                Assert.assertTrue("Daily data not sorted for " + dailyWeather.getDay(), currDate.isAfter(lastDate));
            }

            lastDate = currDate;
        }
    }

    @Test
    public void testTodaysFirstHour() {
        DailyWeather today = forecast[0];
        HourlyWeather firstHour = today.getHourlyWeather().get(0);
        int hourHour = firstHour.getTime().getHour();
        int currHour = LocalTime.now().getHour();

        Assert.assertEquals(hourHour, currHour);
    }

    //outlook
    @Test
    public void testContainsOutlook() {
        for (DailyWeather dailyWeather: forecast) {
            for (HourlyWeather hourlyWeather : dailyWeather.getHourlyWeather()) {
                Conditions currCondition = hourlyWeather.getOutlook();

                Assert.assertNotEquals("Could not get outlook for " + dailyWeather.getDay() + " " + hourlyWeather.getTime(),
                        Conditions.NO_DATA, currCondition);
            }
        }
    }

    @Test
    public void testCanGetOverallOutlookToday() {
        DailyWeather today = forecast[0];
        Conditions mostCommonCond = today.getAvgConditionToday();

        ArrayList<HourlyWeather> daytimeHours = today.getDaytimeHours();
        HashMap<Conditions, Integer> countOfConditions = new HashMap<>();
        for (HourlyWeather w: daytimeHours) {
            Conditions c = w.getOutlook();
            countOfConditions.putIfAbsent(c, 0);
            countOfConditions.put(c, countOfConditions.get(c) + 1);
        }

        HashSet<Conditions> commonCondition = new HashSet<>();
        int maxVal = -1;
        for (Map.Entry<Conditions, Integer> entry: countOfConditions.entrySet()) {
            if (entry.getValue() == maxVal) {
                commonCondition.add(entry.getKey());
            } else if (entry.getValue() > maxVal) {
                commonCondition.clear();
                commonCondition.add(entry.getKey());
            }
        }


        boolean testPasses = daytimeHours.size() == 0 || commonCondition.contains(mostCommonCond);
        if (!testPasses) {
            System.out.println("We calculate outlook to be one of these: ");
            for (Conditions c: commonCondition) {
                System.out.println(c.descriptor);
            }
        }
        Assert.assertTrue("Calculated as " + mostCommonCond.descriptor, testPasses);
    }


    //chance of rain
    @Test
    public void testContainsChanceOfRain() {
        for (DailyWeather dailyWeather: forecast) {
            for (HourlyWeather hourlyWeather : dailyWeather.getHourlyWeather()) {
                try {
                    double chanceOfRain = hourlyWeather.getChanceOfRain();
                    Assert.assertNotEquals(chanceOfRain,0,0); //will fail if double is default value - suspecting the value has not been set.
                } catch (NullPointerException e) {
                    Assert.fail("No data for " + dailyWeather.getDay() + " " + hourlyWeather.getTime());
                }
            }
        }
    }


    //temp
        @Test
    public void testContainsTemperature() {
        for (com.petermarshall.DailyWeather dailyWeather: forecast) {
            for (HourlyWeather hourlyWeather : dailyWeather.getHourlyWeather()) {
                try {
                    double temp = hourlyWeather.getTemp(TempUnits.CELSIUS);
                    Assert.assertNotEquals(temp,0,0); //will fail if double is default value - suspecting the value has not been set.
                } catch (NullPointerException e) {
                    Assert.fail("No data for " + dailyWeather.getDay() + " " + hourlyWeather.getTime());
                }
            }
        }
    }

    //feels like temp
    @Test
    public void testContainsFeelsLikeTemperature() {
        for (com.petermarshall.DailyWeather dailyWeather: forecast) {
            for (HourlyWeather hourlyWeather : dailyWeather.getHourlyWeather()) {
                try {
                    double temp = hourlyWeather.getFeelsLikeTemp(TempUnits.CELSIUS);
                    Assert.assertNotEquals(temp,0,0); //will fail if double is default value - suspecting the value has not been set.
                } catch (NullPointerException e) {
                    Assert.fail("No data for " + dailyWeather.getDay() + " " + hourlyWeather.getTime());
                }
            }
        }
    }

    //wind
    @Test
    public void testContainsWindDirection() {
        for (DailyWeather dailyWeather: forecast) {
            for (HourlyWeather hourlyWeather : dailyWeather.getHourlyWeather()) {
                try {
                String windDirection = hourlyWeather.getWindDirection();
                Assert.assertNotNull("null string, assuming ahs not been set", windDirection);
            } catch (NullPointerException e) {
                Assert.fail("Could not get data for " + dailyWeather.getDay() + hourlyWeather.getTime() + ". Potential problem with where setter can be called from.");
            }
            }
        }
    }

    @Test
    public void testContainsWindGust() {
        for (DailyWeather dailyWeather: forecast) {
            for (HourlyWeather hourlyWeather : dailyWeather.getHourlyWeather()) {
                try {
                    double gustVel = hourlyWeather.getWindGust(VelocityUnits.MPH);
                    Assert.assertNotEquals(gustVel,0,0); //will fail if double is default value - suspecting the value has not been set.
                } catch (NullPointerException e) {
                    Assert.fail("No data for " + dailyWeather.getDay() + " " + hourlyWeather.getTime() + ". Potential problem with where setter can be called from.");
                }
            }
        }
    }

    @Test
    public void testContainsWindSpeed() {
        for (DailyWeather dailyWeather: forecast) {
            for (HourlyWeather hourlyWeather : dailyWeather.getHourlyWeather()) {
                try {
                    double speedVel = hourlyWeather.getWindSpeed(VelocityUnits.MPH);
                    Assert.assertNotEquals(speedVel,0,0); //will fail if double is default value - suspecting the value has not been set.
                } catch (NullPointerException e) {
                    Assert.fail("No data for " + dailyWeather.getDay() + " " + hourlyWeather.getTime() + ". Potential problem with where setter can be called from.");
                }
            }
        }
    }

    //sunset && sunrise
    @Test
    public void testDayHasSunrise() {
        for (DailyWeather dailyWeather: forecast) {
            LocalTime sunrise = dailyWeather.getSunrise();
            Assert.assertNotNull("Sunrise should be set", sunrise);
        }
    }

    @Test
    public void testDayHasSunset() {
        for (DailyWeather dailyWeather: forecast) {
            LocalTime sunset = dailyWeather.getSunset();
            Assert.assertNotNull("Sunset should be set", sunset);
        }
    }

    @Test
    public void testCanGetDaytimeHours() {
        DailyWeather today = forecast[0];

        ArrayList<HourlyWeather> daytimeHours = today.getDaytimeHours();
        ArrayList<HourlyWeather> allHours = today.getHourlyWeather();
        LocalTime sunrise = today.getSunrise();
        LocalTime sunset = today.getSunset();

        allHours.forEach(hour -> {
            LocalTime currTime = hour.getTime();
            if (currTime.isAfter(sunrise) && currTime.isBefore(sunset)) {
                Assert.assertTrue("hours inside of sunrise + set not in daytime hours: " + hour.getTime(), daytimeHours.contains(hour));
            } else {
                Assert.assertFalse("hours includes a time outside of sunrise + set: " + hour.getTime() , daytimeHours.contains(hour));
            }
        });
    }


    //humidity
    @Test
    public void testHumidity() {
        for (DailyWeather dailyWeather: forecast) {
            for (HourlyWeather hourlyWeather : dailyWeather.getHourlyWeather()) {
                try {
                    double humidity = hourlyWeather.getHumidity();
                    Assert.assertNotEquals(humidity,0,0); //will fail if double is default value - suspecting the value has not been set.
                } catch (NullPointerException e) {
                    Assert.fail("No data for " + dailyWeather.getDay() + " " + hourlyWeather.getTime());
                }
            }
        }
    }

    //uv
    @Test
    public void testUvScore() {
        for (DailyWeather dailyWeather: forecast) {
            for (HourlyWeather hourlyWeather : dailyWeather.getHourlyWeather()) {
                try {
                    double uvScore = hourlyWeather.getUvScore();
                    //not using test for default value as a UV score of 0 is quite common. Could add 0.00001 to it but feels really hacky.
//                    Assert.assertNotEquals(uvScore,0,0); //will fail if double is default value - suspecting the value has not been set.
                } catch (NullPointerException e) {
                    Assert.fail("No data for " + dailyWeather.getDay() + " " + hourlyWeather.getTime());
                }
            }
        }
    }

    @Test
    public void testUvRecommendation() {
        for (DailyWeather dailyWeather: forecast) {
            for (HourlyWeather hourlyWeather : dailyWeather.getHourlyWeather()) {
                try {
                String uvRecommendation = hourlyWeather.getUvRecommendation();
                    Assert.assertNotNull("null string, assuming ahs not been set", uvRecommendation);
            } catch (NullPointerException e) {
                Assert.fail("Could not get data for " + dailyWeather.getDay() + hourlyWeather.getTime());
            }
            }
        }
    }

    @Test
    public void testGetTimesOutTheSun() {
        for (DailyWeather dailyWeather: forecast) {
            for (HourlyWeather hourlyWeather : dailyWeather.getHourlyWeather()) {
                try {
                    //is allowed to have size of 0
                    HashSet<LocalTime> timesOutTheSun = hourlyWeather.getTimesToStayOutTheSun();
                } catch (NullPointerException e) {
                    Assert.fail("Could not get data for " + dailyWeather.getDay() + hourlyWeather.getTime());
                }
            }
        }
    }


    //visibility
    @Test
    public void testVisibilityScore() {
        for (DailyWeather dailyWeather: forecast) {
            for (HourlyWeather hourlyWeather : dailyWeather.getHourlyWeather()) {
                try {
                    double visScore = hourlyWeather.getVisibilityScore();
                    Assert.assertNotEquals(visScore,0,0); //will fail if double is default value - suspecting the value has not been set.
                } catch (NullPointerException e) {
                    Assert.fail("No data for " + dailyWeather.getDay() + " " + hourlyWeather.getTime());
                }
            }
        }
    }

    @Test
    public void testVisibilityRangeString() {
        for (DailyWeather dailyWeather: forecast) {
            for (HourlyWeather hourlyWeather : dailyWeather.getHourlyWeather()) {
                try {
                    String range = hourlyWeather.getVisibilityRangeString();
                    Assert.assertNotNull("null string, assuming ahs not been set", range);
                } catch (NullPointerException e) {
                    Assert.fail("Could not get data for " + dailyWeather.getDay() + hourlyWeather.getTime());
                }
            }
        }
    }


    //air pollution
    @Test
    public void testCanGetAirPollutionScoreNext5Days() {
        for (int i = 0; i<5; i++) {
            DailyWeather dailyWeather = forecast[i];
            try {
                int airScore = dailyWeather.getAirScore();
                Assert.assertNotEquals(airScore,0,0); //will fail if double is default value - suspecting the value has not been set.
            } catch (NullPointerException e) {
                Assert.fail("No data for " + dailyWeather.getDay());
            }
        }
    }

    @Test
    public void testNoAirPollutionDays6And7() {
    //will fail if met office change their site and now allow air pollution data on days 6 and 7.
        try {
            DailyWeather sevenDaysFuture = forecast[6];
            int airScore7 = sevenDaysFuture.getAirScore();
            Assert.fail("We now have an air pollution data for day 7. Can update model.");
        } catch (NullPointerException e) {}

        try {
            DailyWeather sixDaysFuture = forecast[5];
            int airScore6 = sixDaysFuture.getAirScore();
            Assert.fail("We now have an air pollution data for day 6. Can update model.");
        } catch (NullPointerException e) {}
    }

    @Test
    public void testGetAirAdviceForGeneralPopFirst5Days() {
        for (int i = 0; i<5; i++) {
            DailyWeather dailyWeather = forecast[i];
            try {
            String advice = dailyWeather.getAirAdviceForGeneralPopulation();
                Assert.assertNotNull("null string, assuming ahs not been set", advice);
        } catch (NullPointerException e) {
            Assert.fail("Could not get data for " + dailyWeather.getDay());
        }
        }
    }

    @Test
    public void testGetAirAdviceForThoseAtRiskFirst5Days() {
        for (int i = 0; i<5; i++) {
            DailyWeather dailyWeather = forecast[i];
            try {
            String advice = dailyWeather.getAirAdviceForThoseAtRisk();
                Assert.assertNotNull("null string, assuming ahs not been set", advice);
        } catch (NullPointerException e) {
            Assert.fail("Could not get data for " + dailyWeather.getDay());
        }
        }
    }
}
