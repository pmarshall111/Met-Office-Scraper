package conversions;

import com.petermarshall.measurements.units.VelocityUnits;
import org.junit.Assert;
import org.junit.Test;

public class testVelocityConversion {
    private double[] kph = new double[]{60   , 112.654, 0, 50};
    private double[] mph = new double[]{37.28, 70     , 0, 31.07};

    @Test
    public void testKphToMph() {
        for (int i = 0; i<kph.length; i++) {
            double kphToMph = VelocityUnits.convert(kph[i], VelocityUnits.KPH, VelocityUnits.MPH);
            Assert.assertEquals(kphToMph, mph[i], 0.1);
        }
    }
    @Test
    public void testKphToKph() {
        for (int i = 0; i<kph.length; i++) {
            double kphToKph = VelocityUnits.convert(kph[i], VelocityUnits.KPH, VelocityUnits.KPH);
            Assert.assertEquals(kphToKph, kph[i], 0.1);
        }
    }

    @Test
    public void testMphToKph() {
        for (int i = 0; i<mph.length; i++) {
            double mphToKph = VelocityUnits.convert(mph[i], VelocityUnits.MPH, VelocityUnits.KPH);
            Assert.assertEquals(mphToKph, kph[i], 0.1);
        }
    }
    @Test
    public void testMphToMph() {
        for (int i = 0; i<mph.length; i++) {
            double mphToMph = VelocityUnits.convert(mph[i], VelocityUnits.MPH, VelocityUnits.MPH);
            Assert.assertEquals(mphToMph, mph[i], 0.1);
        }
    }
}
