package hu.bme.andrismulller.makeithappen_withfriends.Functions.Alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import hu.bme.andrismulller.makeithappen_withfriends.MyUtils.MyUtils;
import hu.bme.andrismulller.makeithappen_withfriends.model.Controlling;

public class AlarmReceiver extends WakefulBroadcastReceiver {

    private static final String TAG = "AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Alarm");

        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(2000);

        if (intent.getStringExtra("type").equals("alarm")) {
            Intent intent1 = new Intent();
            intent1.setClassName("hu.bme.andrismulller.makeithappen_withfriends", "hu.bme.andrismulller.makeithappen_withfriends.Functions.Alarm.AlarmActivity");
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent1.putExtra("note", intent.getStringExtra("note"));
            intent1.putExtra("id", intent.getLongExtra("id", -1));
            Log.i(TAG, "NOTE: " + intent.getStringExtra("note"));
            context.startActivity(intent1);
        } else if (intent.getStringExtra("type").equals("controlling")){
            MyUtils.turnOnWifi(context);

            Controlling controlling = Controlling.find(Controlling.class, "id = ?", intent.getStringExtra("id")).get(0);
            controlling.setActivated(false);
            controlling.save();
        }
    }
}
