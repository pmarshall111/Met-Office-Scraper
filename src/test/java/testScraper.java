import com.petermarshall.DailyWeather;
import com.petermarshall.HourlyWeather;
import com.petermarshall.MetOfficeScraper;
import helpers.Locations;
import measurements.Conditions;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

//todo: need a test for a fixed weather forecast in the past so we can verify we are assigning the right vals to each field.

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
    public void testCanGetHourlyDataToday() {
        DailyWeather today = forecast[0];
        int hoursBetweenData = today.getHoursSeparation();

        Assert.assertEquals("Gets data every hour today", 1, hoursBetweenData);
    }

    @Test
    public void testCanGetHourlyDataTomorrow() {
        DailyWeather tomorrow = forecast[1];
        int hoursBetweenData = tomorrow.getHoursSeparation();

        Assert.assertEquals("Gets data every hour tomorrow", 1, hoursBetweenData);
    }

    @Test
    public void testGetsDataEvery3HoursAfterTomorrow() {
        for (int i = 2; i<forecast.length; i++) {
            int hoursBetweenData = forecast[i].getHoursSeparation();

            Assert.assertEquals("Gets data every 3 hours on days after", 3, hoursBetweenData);
        }
    }

    @Test
    public void testHourlyDataContainsOutlook() {
        for (DailyWeather dailyWeather: forecast) {
            for (HourlyWeather hourlyWeather : dailyWeather.getHourlyWeather()) {
                Conditions currCondition = hourlyWeather.getOutlook();

                Assert.assertNotEquals("Could not get outlook for " + dailyWeather.getDay() + " " + hourlyWeather.getTime(),
                        Conditions.NO_DATA, currCondition);
            }
        }
    }

    @Test
    public void testHourlyDataContainsChanceOfRain() {
        for (DailyWeather dailyWeather: forecast) {
            for (HourlyWeather hourlyWeather : dailyWeather.getHourlyWeather()) {
                try {
                    double chanceOfRain = hourlyWeather.getChanceOfRain();
                } catch (NullPointerException e) {
                    Assert.fail("Could not get chance of rain for " + dailyWeather.getDay() + " " + hourlyWeather.getTime());
                }
            }
        }
    }

//    @Test
//    public void testHourlyDataContainsTemperature() {
//        for (com.petermarshall.DailyWeather dailyWeather: forecast) {
//            for (com.petermarshall.HourlyWeather hourlyWeather : dailyWeather.getHourlyWeather()) {
//                try {
//                    double temp = hourlyWeather.getTempInCelsius();
//                } catch (NullPointerException e) {
//                    Assert.fail("Could not get temperature for " + dailyWeather.getDay() + hourlyWeather.getTime());
//                }
//            }
//        }
//    }

    @Test
    public void testHourlyDataContainsWindDirection() {
        for (DailyWeather dailyWeather: forecast) {
            for (HourlyWeather hourlyWeather : dailyWeather.getHourlyWeather()) {

            }
        }
    }

    //    @Test
//    public void testCanGetOverallOutlookToday() {
//        //TODO: will require sunrise & sunset times so we can filter out the night and get the mode outlook.
//        com.petermarshall.DailyWeather today = forecast[0];
//        today.
//    }

    @Test
    public void testCanGetTomorrowsWeather() {

    }
}
