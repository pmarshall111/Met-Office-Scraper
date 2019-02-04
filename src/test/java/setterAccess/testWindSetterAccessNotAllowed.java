package setterAccess;

import com.petermarshall.measurements.Wind;
import com.petermarshall.measurements.units.VelocityUnits;
import org.junit.Assert;
import org.junit.Test;

public class testWindSetterAccessNotAllowed {
    @Test
    public void testGustSetter() {
        double kph = 60;
        double mph = 37.28;
        try {
            Wind wind = new Wind();
            wind.setGust(kph, VelocityUnits.KPH);
            double gustInMph = wind.getGust(VelocityUnits.MPH);
            Assert.fail("Should not have been able to set the gust from outside CreateWeatherData class.");
        } catch (NullPointerException e) {
            //
        }
    }

    @Test
    public void testSpeedSetter() {
        double kph = 60;
        double mph = 37.28;
        try {
            Wind wind = new Wind();
            wind.setSpeed(kph, VelocityUnits.KPH);
            double speedInMph = wind.getSpeed(VelocityUnits.MPH);
            Assert.fail("Should not have been able to set the speed from outside CreateWeatherData class.");
        } catch (NullPointerException e) {
            //
        }
    }

    @Test
    public void testDirectionSetter() {
        String direction = "NW";

        Wind wind = new Wind();
        wind.setDirection(direction);
        String result = wind.getDirection();
        Assert.assertNull("Should not be able to set direction from outside CreateWeatherDara class.", result);
    }

    @Test
    public void testDirectionSetterBoolean() {
        Wind wind = new Wind();
        boolean result = wind.setDirection("NW");
        Assert.assertTrue("Method should return false to tell us we did not update the setter", !result);
    }

    @Test
    public void testGustSetterBoolean() {
        Wind wind = new Wind();
        boolean result = wind.setGust(3.2, VelocityUnits.MPH);
        Assert.assertTrue("Method should return false to tell us we did not update the setter", !result);
    }

    @Test
    public void testSpeedSetterBoolean() {
        Wind wind = new Wind();
        boolean result = wind.setSpeed(1.5, VelocityUnits.MPH);
        Assert.assertTrue("Method should return false to tell us we did not update the setter", !result);
    }
}
