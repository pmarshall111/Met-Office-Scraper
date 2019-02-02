import units.TempUnits;
import measurements.Temperature;
import org.junit.Assert;
import org.junit.Test;

public class testTemperatureConversion {
    private double[] celsius =    new double[]{0 , 15, 32  , 19.4444};
    private double[] fahrenheit = new double[]{32, 59, 89.6, 67};


    @Test
    public void testFahrenheitToCelsius() {
        for (int i = 0; i<fahrenheit.length; i++) {
            Temperature t = new Temperature(fahrenheit[i], TempUnits.FAHRENHEIT);
            Assert.assertEquals(t.getTemp(TempUnits.CELSIUS), celsius[i], 0.1);
        }
    }

    @Test
    public void testCelsiusToFahrenheit() {
        for (int i = 0; i<fahrenheit.length; i++) {
            Temperature t = new Temperature(celsius[i], TempUnits.CELSIUS);
            Assert.assertEquals(t.getTemp(TempUnits.FAHRENHEIT), fahrenheit[i], 0.1);
        }
    }

    @Test
    public void testFahrenheitToFahrenheit() {
        for (double f: fahrenheit) {
            Temperature t = new Temperature(f, TempUnits.FAHRENHEIT);
            Assert.assertEquals(t.getTemp(TempUnits.FAHRENHEIT), f, 0.1);
        }
    }

    @Test
    public void testCelsiusToCelsius() {
        for (double c: celsius) {
            Temperature t = new Temperature(c, TempUnits.CELSIUS);
            Assert.assertEquals(t.getTemp(TempUnits.CELSIUS), c, 0.1);
        }
    }
}