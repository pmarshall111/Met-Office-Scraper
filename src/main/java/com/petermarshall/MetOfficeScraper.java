package com.petermarshall;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.petermarshall.helpers.Locations;
import com.petermarshall.helpers.MetOfficeUrl;

public class MetOfficeScraper {

    final static int numbDaysOfWeather = 7;

    private final String locationHash;
    private HtmlPage page;
    private DailyWeather[] weatherData;

    public MetOfficeScraper(Locations locations) {
        this.locationHash = locations.metOfficeHash;

        setupPage();
        createWeatherData();
    }

    public static void main(String[] args) {
        MetOfficeScraper mo = new MetOfficeScraper(Locations.CHALFONT_ST_PETER);
        HtmlPage p = mo.getPage();
    }

    private void setupPage() {
        try (WebClient webClient = new WebClient()) {
            String url = MetOfficeUrl.getUrl(this.locationHash);

            webClient.getOptions().setCssEnabled(false);
            webClient.getOptions().setJavaScriptEnabled(false);
            page = webClient.getPage(url);
        } catch (Exception e) {
            page = null;
        }
    }

    public HtmlPage getPage() {
        return page;
    }

    public String getLocation() {
        String title = page.getTitleText();
        String[] splitToGetLocation = title.split(" weather");
        return splitToGetLocation[0];
    }

    private void createWeatherData() {
        weatherData = CreateWeatherData.fromPage(page);
    }

    public DailyWeather[] getForecast() {
        //TODO: possibly make this immutable? If we release as a public facing API or on GitHub it will have to be
        return weatherData;
    }
}
