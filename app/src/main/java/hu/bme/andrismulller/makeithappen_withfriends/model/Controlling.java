package hu.bme.andrismulller.makeithappen_withfriends.model;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

import java.util.ArrayList;

/**
 * Created by Muller Andras on 9/23/2017.
 */

public class Controlling extends SugarRecord {
    private String name;
    private String urls;
    private String apps;
    private boolean internet;
    private double durationValue;
    private int durationUnit;
    private long startTime;
    private boolean activated;
    @Ignore
    private long myId;

    public Controlling(){}
    public Controlling(String name, ArrayList<String> urlsList, ArrayList<String> appsList, boolean internetBlocked, double durationValue, int durationUnit, long startTime){
        this.name = name;
        this.setUrls(urlsList);
        this.setApps(appsList);
        this.internet = internetBlocked;
        this.durationValue = durationValue;
        this.durationUnit = durationUnit;
        this.startTime = startTime;
        this.activated = false;
    }

    public long getMyId() {
        return myId;
    }

    public void setMyId(long myId) {
        this.myId = myId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrls() {
        return urls;
    }

    public void setUrls(ArrayList<String> urlsList) {
        String result = "";
        if (urlsList != null){
            for (int i = 0; i < urlsList.size(); ++i){
                result = result.concat(urlsList.get(i));
                result = result.concat(";");
            }
        }
        this.urls = result;
    }

    public String getApps() {
        return apps;
    }

    public void setApps(ArrayList<String> appsList) {
        String result = "";
        if (appsList != null){
            for (int i = 0; i < appsList.size(); ++i){
                result = result.concat(appsList.get(i));
                result = result.concat(";");
            }
        }
        this.apps = result;
    }

    public boolean isInternetBlocked() {
        return internet;
    }

    public void setInternetBlocked(boolean internetBlocked) {
        this.internet = internetBlocked;
    }

    public double getDurationValue() {
        return durationValue;
    }

    public void setDurationValue(int durationValue) {
        this.durationValue = durationValue;
    }

    public int getDurationUnit() {
        return durationUnit;
    }

    public void setDurationUnit(int durationUnit) {
        this.durationUnit = durationUnit;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public long getDurationTimeInSec(){
        long durationInSec = (long) durationValue * 60;
        if (durationUnit >= 1){
            durationInSec *= 60;
        } else if (durationUnit >= 2){
            durationInSec *= 24;
        } else if (durationUnit >= 3){
            durationInSec *= 7;
        }

        return durationInSec;
    }
}
