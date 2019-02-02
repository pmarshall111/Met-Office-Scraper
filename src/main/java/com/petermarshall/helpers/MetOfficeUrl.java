package com.petermarshall.helpers;

public class MetOfficeUrl {
    private static String BASE_URL = "https://www.metoffice.gov.uk/public/weather/forecast/";

    public static String getUrl(String locationHash) {
        return BASE_URL + locationHash;
    }

    private MetOfficeUrl() {}
}
