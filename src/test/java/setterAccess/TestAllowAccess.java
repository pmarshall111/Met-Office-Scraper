package setterAccess;

import com.petermarshall.helpers.AccessAllowed;

class TestAllowAccess {
    private AccessAllowed accessAllowed = new AccessAllowed(AllowedClass.class);

    TestAllowAccess() {
    }

    boolean setSetter() {
        if (accessAllowed.callerCanUseMethod()) {
            return true;
        } else {
            return false;
        }
    }

}
