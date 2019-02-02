package units;

//created condition class so we can later add svgs or small jpgs later to display on our app.
public enum Conditions {
    NO_DATA("N/A"),
    CLOUDY("Cloudy"),
    OVERCAST("Overcast"),
    PARTLY_CLOUDY_NIGHT("Partly cloudy night"),
    SUNNY("Sunny day"),
    SUNNY_INTERVALS("Sunny intervals"),
    CLEAR_NIGHT("Clear night"),
    MIST("Mist"),
    FOG("Fog"),
    LIGHT_RAIN_SHOWER_NIGHT("Light rain shower night"),
    LIGHT_RAIN_SHOWER("Light rain shower day"),
    LIGHT_RAIN("Light rain"),
    DRIZZLE("Drizzle"),
    HEAVY_RAIN_SHOWER_NIGHT("Heavy rain shower night"),
    HEAVY_RAIN_SHOWER("Heavy rain shower day"), //different between heavy rain shower and heavy rain appears to be that shower is over quickly so has a sun coming from behind the cloud.,
    HEAVY_RAIN("Heavy rain"),
    SLEET_SHOWER_NIGHT("Sleet shower night"),
    SLEET_SHOWER_DAY("Sleet shower day"),
    SLEET("Sleet"),
    HAIL_SHOWER_NIGHT("Hail shower night"),
    HAIL_SHOWER_DAY("Hail shower day"),
    HAIL("Hail"),
    LIGHT_SNOW_SHOWER_NIGHT("Light snow shower night"),
    LIGHT_SNOW_SHOWER_DAY("Light snow shower day"),
    LIGHT_SNOW("Light snow"),
    HEAVY_SNOW_SHOWER_NIGHT("Heavy snow shower night"),
    HEAVY_SNOW_SHOWER_DAY("Heavy snow shower day"),
    HEAVY_SNOW("Heavy snow"),
    THUNDER_SHOWER_NIGHT("Thunder shower night"),
    THUNDER_SHOWER_DAY("Thunder shower day"),
    THUNDER("Thunder"),
    DUST("Dust"),
    TROPICAL_STORM("Tropical storm");


    //TODO move over the rest of these whilst eating https://www.metoffice.gov.uk/guide/weather/symbols

    public final String descriptor;

    Conditions(String descriptor) {
        this.descriptor = descriptor;
    }

    public static Conditions getCondition(String outlook) {
        Conditions[] cs = Conditions.values();
        for (Conditions c: cs) {
            if (c.descriptor.equals(outlook)) {
                return c;
            }
        }

        return Conditions.NO_DATA;
    }
}
