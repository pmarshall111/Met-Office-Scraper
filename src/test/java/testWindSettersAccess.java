import measurements.Wind;
import org.junit.Assert;
import org.junit.Test;
import units.VelocityUnits;

//TODO: need to have a test in the scraper part to ensure that we can set data from createWeatherData.
public class testWindSettersAccess {
    Wind wind = new Wind();

    @Test
    public void testDirectionSetterBoolean() {
        boolean result = wind.setDirection("NW");
        Assert.assertTrue("Method should return false to tell us we did not update the setter", !result);
    }

    @Test
    public void testGustSetterBoolean() {
        boolean result = wind.setGust(3.2, VelocityUnits.MPH);
        Assert.assertTrue("Method should return false to tell us we did not update the setter", !result);
    }

    @Test
    public void testSpeedSetterBoolean() {
        boolean result = wind.setSpeed(1.5, VelocityUnits.MPH);
        Assert.assertTrue("Method should return false to tell us we did not update the setter", !result);
    }

    @Test
    public void testDirectionSetter() {
        wind.setDirection("NW");
        String result = wind.getDirection();
        Assert.assertNull("Getter should return null we did not update the setter", result);
    }

    @Test
    public void testGustSetter() {
        wind.setGust(3.2, VelocityUnits.MPH);
        try {
            double result = wind.getGust(VelocityUnits.MPH);
            Assert.fail("Gust should not have been able to have been set. Val of gust is " + result);
        } catch (NullPointerException e) {
        }
    }

    @Test
    public void testSpeedSetter() {
        wind.setSpeed(1.5, VelocityUnits.MPH);
        try {
            double result = wind.getSpeed(VelocityUnits.MPH);
            Assert.fail("Speed should not have been able to have been set. Val of speed is " + result);
        } catch (NullPointerException e) {
        }
    }


}
