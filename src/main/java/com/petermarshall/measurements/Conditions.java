package com.petermarshall.measurements;

//created condition class so we can later add svgs or small jpgs later to display on our app.
public enum Conditions {
    NO_DATA("N/A", "/binaries/content/gallery/mohippo/images/weather-guide/weather-symbols-jul17/no-data.png"),
    CLOUDY("Cloudy", "/binaries/content/gallery/mohippo/images/weather-guide/weather-symbols-jul17/cloudy.png"),
    OVERCAST("Overcast", "/binaries/content/gallery/mohippo/images/weather-guide/weather-symbols-jul17/overcast.png"),
    PARTLY_CLOUDY_NIGHT("Partly cloudy (night)", "/binaries/content/gallery/mohippo/images/weather-guide/weather-symbols-jul17/partly-cloudy-night.png"),
    SUNNY("Sunny day", "/binaries/content/gallery/mohippo/images/weather-guide/weather-symbols-jul17/sunny-day.png"),
    SUNNY_INTERVALS("Sunny intervals", "/binaries/content/gallery/mohippo/images/weather-guide/weather-symbols-jul17/sunny-intervals.png"),
    CLEAR_NIGHT("Clear night", "/binaries/content/gallery/mohippo/images/weather-guide/weather-symbols-jul17/clear-night.png"),
    MIST("Mist", "/binaries/content/gallery/mohippo/images/weather-guide/weather-symbols-jul17/mist.png"),
    FOG("Fog", "/binaries/content/gallery/mohippo/images/weather-guide/weather-symbols-jul17/fog.png"),
    LIGHT_RAIN_SHOWER_NIGHT("Light shower (night)", "/binaries/content/gallery/mohippo/images/weather-guide/weather-symbols-jul17/light-rain-shower-night.png"),
    LIGHT_RAIN_SHOWER("Light shower (day)", "/binaries/content/gallery/mohippo/images/weather-guide/weather-symbols-jul17/light-rain-shower-day.png"),
    LIGHT_RAIN("Light rain", "/binaries/content/gallery/mohippo/images/weather-guide/weather-symbols-jul17/light-rain.png"),
    DRIZZLE("Drizzle", "/binaries/content/gallery/mohippo/images/weather-guide/weather-symbols-jul17/drizzle.png"),
    HEAVY_RAIN_SHOWER_NIGHT("Heavy shower (night)", "/binaries/content/gallery/mohippo/images/weather-guide/weather-symbols-jul17/heavy-rain-shower-night.png"),
    HEAVY_RAIN_SHOWER("Heavy shower (day)", "/binaries/content/gallery/mohippo/images/weather-guide/weather-symbols-jul17/heavy-rain-shower-day.png"), //different between heavy rain shower and heavy rain appears to be that shower is over quickly so has a sun coming from behind the cloud.,
    HEAVY_RAIN("Heavy rain", "/binaries/content/gallery/mohippo/images/weather-guide/weather-symbols-jul17/heavy-rain.png"),
    SLEET_SHOWER_NIGHT("Sleet shower (night)", "/binaries/content/gallery/mohippo/images/weather-guide/weather-symbols-jul17/sleet-shower-night.png"),
    SLEET_SHOWER_DAY("Sleet shower (day)", "/binaries/content/gallery/mohippo/images/weather-guide/weather-symbols-jul17/sleet-shower-day.png"),
    SLEET("Sleet", "/binaries/content/gallery/mohippo/images/weather-guide/weather-symbols-jul17/sleet.png"),
    HAIL_SHOWER_NIGHT("Hail shower (night)", "/binaries/content/gallery/mohippo/images/weather-guide/weather-symbols-jul17/hail-shower-night.png"),
    HAIL_SHOWER_DAY("Hail shower (day)", "/binaries/content/gallery/mohippo/images/weather-guide/weather-symbols-jul17/hail-shower-day.png"),
    HAIL("Hail", "/binaries/content/gallery/mohippo/images/weather-guide/weather-symbols-jul17/hail.png"),
    LIGHT_SNOW_SHOWER_NIGHT("Light snow shower (night)", "/binaries/content/gallery/mohippo/images/weather-guide/weather-symbols-jul17/light-snow-shower-night.png"),
    LIGHT_SNOW_SHOWER_DAY("Light snow shower (day)", "/binaries/content/gallery/mohippo/images/weather-guide/weather-symbols-jul17/light-snow-shower-day.png"),
    LIGHT_SNOW("Light snow", "/binaries/content/gallery/mohippo/images/weather-guide/weather-symbols-jul17/light-snow.png"),
    HEAVY_SNOW_SHOWER_NIGHT("Heavy snow shower (night)", "/binaries/content/gallery/mohippo/images/weather-guide/weather-symbols-jul17/heavy-snow-shower-night.png"),
    HEAVY_SNOW_SHOWER_DAY("Heavy snow shower (day)", "/binaries/content/gallery/mohippo/images/weather-guide/weather-symbols-jul17/heavy-snow-shower-day.png"),
    HEAVY_SNOW("Heavy snow", "/binaries/content/gallery/mohippo/images/weather-guide/weather-symbols-jul17/heavy-snow.png"),
    THUNDER_SHOWER_NIGHT("Thunder shower (night)", "/binaries/content/gallery/mohippo/images/weather-guide/weather-symbols-jul17/thunder-shower-night.png"),
    THUNDER_SHOWER_DAY("Thunder shower (day)", "/binaries/content/gallery/mohippo/images/weather-guide/weather-symbols-jul17/thunder-shower-day.png"),
    THUNDER("Thunder", "/binaries/content/gallery/mohippo/images/weather-guide/weather-symbols-jul17/thunder.png"),
    DUST("Dust", "/binaries/content/gallery/mohippo/images/weather-guide/weather-symbols-jul17/dust.png"),
    TROPICAL_STORM("Tropical storm", "/binaries/content/gallery/mohippo/images/weather-guide/weather-symbols-jul17/tropical-storm.png");

    static final String baseUrl = "https://www.metoffice.gov.uk";

    public final String descriptor;
    private final String urlExtension;

    Conditions(String descriptor, String urlExtension) {
        this.descriptor = descriptor;
        this.urlExtension = urlExtension;
    }

    public String getImageUrl() {
        return Conditions.baseUrl + this.urlExtension;
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
