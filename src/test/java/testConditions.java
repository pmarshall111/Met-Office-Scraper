import com.petermarshall.measurements.Conditions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;

public class testConditions {
    private static Conditions[] allConditions;
    private static String[] conditionLinks;

    @Before
    public void setUp() throws Exception {
        allConditions = Conditions.values();
        conditionLinks = new String[allConditions.length];

        for (int i = 0; i<allConditions.length; i++) {
            conditionLinks[i] = allConditions[i].getImageUrl();
        }
    }

    @Test
    public void testNumbOfConditions() {
        int NUMB_MET_OFFICE_CONDITIONS = 33;
        Assert.assertEquals(allConditions.length, NUMB_MET_OFFICE_CONDITIONS);
    }

    @Test
    public void testEachConditionHasImgUrl() {
        for (int i = 0; i<conditionLinks.length; i++) {
            Assert.assertNotNull(conditionLinks[i]);
        }
    }

    @Test
    public void allConditionsHaveUniquePictures() {
        HashSet<String> uniqueLinks = new HashSet<String>(Arrays.asList(conditionLinks));
        Assert.assertEquals(conditionLinks.length, uniqueLinks.size());
    }
}
