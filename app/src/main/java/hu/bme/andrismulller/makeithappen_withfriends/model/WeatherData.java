package hu.bme.andrismulller.makeithappen_withfriends.model;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

/**
 * Created by Muller Andras on 9/16/2017.
 */

public class WeatherData extends SugarRecord {
    String dateTimeUtc;
    String temperature;
    String description;
    String icon;
    String place;
    Boolean forecast;
    @Ignore
    long myId;

    public WeatherData() {
    }

    public WeatherData(String dateTimeUTC, String temperature, String description, String icon, String place, Boolean forecast) {
        this.dateTimeUtc = dateTimeUTC;
        this.temperature = temperature;
        this.description = description;
        this.icon = icon;
        this.place = place;
        this.forecast = forecast;
    }

    public String getDateTimeUtc() {
        return dateTimeUtc;
    }

    public void setDateTimeUtc(String dateTimeUtc) {
        this.dateTimeUtc = dateTimeUtc;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public long getMyId() {
        return myId;
    }

    public void setMyId(long myId) {
        this.myId = myId;
    }
}
