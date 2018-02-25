package hu.bme.andrismulller.makeithappen_withfriends.Functions.Alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import hu.bme.andrismulller.makeithappen_withfriends.MyUtils.MyUtils;

public class AlarmReceiver extends WakefulBroadcastReceiver {

    private static final String TAG = "AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(TAG, "Alarm");

        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(2000);

        if (intent.getStringExtra("type").equals("alarm")) {
            Intent intent1 = new Intent();
            intent1.setClassName("hu.bme.andrismulller.makeithappen_withfriends", "hu.bme.andrismulller.makeithappen_withfriends.Functions.Alarm.AlarmActivity");
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent1.putExtra("note", intent.getStringExtra("note"));
            Log.i(TAG, "NOTE: " + intent.getStringExtra("note"));
            context.startActivity(intent1);
        } else if (intent.getStringExtra("type").equals("controlling")){
            MyUtils.turnOnWifi(context);


        }
    }
}
