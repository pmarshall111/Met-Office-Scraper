package measurements;

import units.VisibilityUnits;

public class Visibility {
    private double val;
    private VisibilityUnits units;

    public Visibility(double val, VisibilityUnits units) {
        this.val = val;
        this.units = units;
    }

    public String getRange() {
        return units.getRange();
    }
}
