package setterAccess;

import org.junit.Assert;
import org.junit.Test;

public class NotAllowedClass {
    @Test
    public void testCanAccess() {
        TestAllowAccess testAllowAccess = new TestAllowAccess();
        boolean isAllowed = testAllowAccess.setSetter();
        Assert.assertFalse(isAllowed);
    }
}
