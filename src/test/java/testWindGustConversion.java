import measurements.Wind;
import measurements.units.VelocityUnits;
import org.junit.Assert;
import org.junit.Test;

public class testWindGustConversion {
    private double[] kph = new double[]{60   , 112.654, 0, 50};
    private double[] mph = new double[]{37.28, 70     , 0, 31.07};

    @Test
    public void testGustKphToMph() {
        for (int i = 0; i<kph.length; i++) {
            Wind wind = new Wind();
            wind.setGust(kph[i], VelocityUnits.KPH);
            Assert.assertEquals(wind.getGust(VelocityUnits.MPH), mph[i], 0.1);
        }
    }
    @Test
    public void testGustKphToKph() {
        for (int i = 0; i<kph.length; i++) {
            Wind wind = new Wind();
            wind.setGust(kph[i], VelocityUnits.KPH);
            Assert.assertEquals(wind.getGust(VelocityUnits.KPH), kph[i], 0.1);
        }
    }

    @Test
    public void testGustMphToKph() {
        for (int i = 0; i<mph.length; i++) {
            Wind wind = new Wind();
            wind.setGust(mph[i], VelocityUnits.MPH);
            Assert.assertEquals(wind.getGust(VelocityUnits.KPH), kph[i], 0.1);
        }
    }
    @Test
    public void testGustMphToMph() {
        for (int i = 0; i<mph.length; i++) {
            Wind wind = new Wind();
            wind.setGust(mph[i], VelocityUnits.MPH);
            Assert.assertEquals(wind.getGust(VelocityUnits.MPH), mph[i], 0.1);
        }
    }
}
