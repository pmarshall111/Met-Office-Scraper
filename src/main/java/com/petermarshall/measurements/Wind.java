package measurements;

import com.petermarshall.CreateWeatherData;
import measurements.units.VelocityUnits;

import java.util.ArrayList;

//NOTE: would normally be put into measurements package, but Java does not allow access from above into sub-packages,
// so the options are either keeping it in the same package as com.petermarshall.CreateWeatherData, or making our setters public, which obviously isn't an option.

public class Wind {
    private String direction;

    private double speed;
    private VelocityUnits speedUnits;
    private double gust;
    private VelocityUnits gustUnits;

    public Wind() {
    }

    public boolean setDirection(String direction) {
        if (accessAllowed()) {
            this.direction = direction;
            return true;
        }
        return false;
    }

    public boolean setSpeed(double speed, VelocityUnits speedUnits) {
        if (accessAllowed()) {
            this.speed = speed;
            this.speedUnits = speedUnits;
            return true;
        }
        return false;
    }

    public boolean setGust(double gust, VelocityUnits gustUnits) {
        if (accessAllowed()) {
            this.gust = gust;
            this.gustUnits = gustUnits;
            return true;
        }
        return false;
    }

    /*
     * Used to allow us to put the class in measurements sub-package whilst still allowing com.petermarshall.CreateWeatherData to use the setters, but also
     * prohibiting access from anywhere else in the program.
     */
    private boolean accessAllowed() {
        ArrayList<String> ALLOWED_CLASSES = new ArrayList<String>(){{
            add(CreateWeatherData.class.getName());
            }};

        StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
        String currClass = this.getClass().getName();

        for (int i = 1; i<stElements.length; i++) {
            String stackClass = stElements[i].getClassName();
            if (!stackClass.equals(currClass)) {
                return ALLOWED_CLASSES.contains(stackClass);
            }
        }

        return true;
    }

    public String getDirection() {
        return direction;
    }

    public double getSpeed(VelocityUnits desiredUnits) {
        if (desiredUnits.equals(this.speedUnits)) {
            return speed;
        } else {
            return VelocityUnits.convert(speed, this.speedUnits, desiredUnits);
        }
    }

    public double getGust(VelocityUnits desiredUnits) {
        if (desiredUnits.equals(this.gustUnits)) {
            return gust;
        } else {
            return VelocityUnits.convert(gust, this.gustUnits, desiredUnits);
        }
    }
}
