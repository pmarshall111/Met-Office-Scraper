import com.petermarshall.helpers.MetOfficeUrl;
import org.junit.Assert;
import org.junit.Test;

public class testMetOfficeUrlGenerator {
    @Test
    public void testUrlGenerator() {
        String uxbridgeHash = "gcptm05c7";
        String feb2019MetOfficeUrl = "https://www.metoffice.gov.uk/public/weather/forecast/gcptm05c7";

        Assert.assertTrue("Our url contains the hash we're using", feb2019MetOfficeUrl.contains(uxbridgeHash));

        String actual = MetOfficeUrl.getUrl(uxbridgeHash);
        Assert.assertEquals("Generator gives the same as copied + pasted url", feb2019MetOfficeUrl, actual);
    }
}
