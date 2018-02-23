package hu.bme.andrismulller.makeithappen_withfriends.model;

import android.graphics.drawable.Drawable;

import com.orm.SugarRecord;

/**
 * Created by Muller Andras on 9/24/2017.
 */

public class App extends SugarRecord {
    private String label;
    private String packageName;
    private Drawable icon;

    public App(){}

    public App(String label, String packageName, Drawable icon){
        this.label = label;
        this.packageName = packageName;
        this.icon = icon;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }


    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
