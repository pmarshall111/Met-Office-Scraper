import measurements.Wind;
import units.VelocityUnits;
import org.junit.Assert;
import org.junit.Test;

public class testWindSpeedConversion {
    private double[] kph = new double[]{60   , 112.654, 0, 50};
    private double[] mph = new double[]{37.28, 70     , 0, 31.07};

    @Test
    public void testSpeedKphToMph() {
        for (int i = 0; i<kph.length; i++) {
            Wind wind = new Wind();
            wind.setSpeed(kph[i], VelocityUnits.KPH);
            Assert.assertEquals(wind.getSpeed(VelocityUnits.MPH), mph[i], 0.1);
        }
    }
    @Test
    public void testSpeedKphToKph() {
        for (int i = 0; i<kph.length; i++) {
            Wind wind = new Wind();
            wind.setSpeed(kph[i], VelocityUnits.KPH);
            Assert.assertEquals(wind.getSpeed(VelocityUnits.KPH), kph[i], 0.1);
        }
    }

    @Test
    public void testSpeedMphToKph() {
        for (int i = 0; i<mph.length; i++) {
            Wind wind = new Wind();
            wind.setSpeed(mph[i], VelocityUnits.MPH);
            Assert.assertEquals(wind.getSpeed(VelocityUnits.KPH), kph[i], 0.1);
        }
    }
    @Test
    public void testSpeedMphToMph() {
        for (int i = 0; i<mph.length; i++) {
            Wind wind = new Wind();
            wind.setSpeed(mph[i], VelocityUnits.MPH);
            Assert.assertEquals(wind.getSpeed(VelocityUnits.MPH), mph[i], 0.1);
        }
    }


}