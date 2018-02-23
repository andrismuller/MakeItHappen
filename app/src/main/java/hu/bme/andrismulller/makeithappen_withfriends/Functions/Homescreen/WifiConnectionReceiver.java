package hu.bme.andrismulller.makeithappen_withfriends.Functions.Homescreen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import hu.bme.andrismulller.makeithappen_withfriends.MyUtils.MyUtils;

public class WifiConnectionReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        MyUtils.turnOffWifi(context);
    }
}
