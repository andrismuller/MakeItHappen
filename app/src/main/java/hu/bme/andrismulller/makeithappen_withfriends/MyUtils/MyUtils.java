package hu.bme.andrismulller.makeithappen_withfriends.MyUtils;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.provider.ContactsContract;

/**
 * Created by Muller Andras on 2/19/2018.
 */

public class MyUtils {

    public static void turnOffWifi(Context applicationContext){
        WifiManager wifiManager = (WifiManager) applicationContext.getSystemService(applicationContext.WIFI_SERVICE);

        if (wifiManager != null && wifiManager.isWifiEnabled()){
            wifiManager.setWifiEnabled(false);
        }
    }

    public static void turnOnWifi(Context applicationContext){
        WifiManager wifiManager = (WifiManager) applicationContext.getSystemService(applicationContext.WIFI_SERVICE);

        if (wifiManager != null && !wifiManager.isWifiEnabled()){
            wifiManager.setWifiEnabled(true);
        }
    }
}
