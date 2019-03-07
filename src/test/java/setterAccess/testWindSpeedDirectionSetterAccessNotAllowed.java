package setterAccess;

import com.petermarshall.measurements.WindGust;
import com.petermarshall.measurements.WindSpeedDirection;
import com.petermarshall.measurements.units.VelocityUnits;
import org.junit.Assert;
import org.junit.Test;

public class testWindSpeedDirectionSetterAccessNotAllowed {
    @Test
    public void testGustSetter() {
        double kph = 60;
        double mph = 37.28;
        try {
            WindGust windGust = new WindGust();
            windGust.setGust(kph, VelocityUnits.KPH);
            double gustInMph = windGust.getGust(VelocityUnits.MPH);
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
            WindSpeedDirection windSpeedDirection = new WindSpeedDirection();
            windSpeedDirection.setSpeed(kph, VelocityUnits.KPH);
            double speedInMph = windSpeedDirection.getSpeed(VelocityUnits.MPH);
            Assert.fail("Should not have been able to set the speed from outside CreateWeatherData class.");
        } catch (NullPointerException e) {
            //
        }
    }

    @Test
    public void testDirectionSetter() {
        String direction = "NW";

        WindSpeedDirection windSpeedDirection = new WindSpeedDirection();
        windSpeedDirection.setDirection(direction);
        String result = windSpeedDirection.getDirection();
        Assert.assertNull("Should not be able to set direction from outside CreateWeatherDara class.", result);
    }

    @Test
    public void testDirectionSetterBoolean() {
        WindSpeedDirection windSpeedDirection = new WindSpeedDirection();
        boolean result = windSpeedDirection.setDirection("NW");
        Assert.assertTrue("Method should return false to tell us we did not update the setter", !result);
    }

    @Test
    public void testGustSetterBoolean() {
        WindGust windGust = new WindGust();
        boolean result = windGust.setGust(3.2, VelocityUnits.MPH);
        Assert.assertTrue("Method should return false to tell us we did not update the setter", !result);
    }

    @Test
    public void testSpeedSetterBoolean() {
        WindSpeedDirection windSpeedDirection = new WindSpeedDirection();
        boolean result = windSpeedDirection.setSpeed(1.5, VelocityUnits.MPH);
        Assert.assertTrue("Method should return false to tell us we did not update the setter", !result);
    }
}
