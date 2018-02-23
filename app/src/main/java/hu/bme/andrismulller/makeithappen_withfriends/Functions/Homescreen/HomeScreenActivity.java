package hu.bme.andrismulller.makeithappen_withfriends.Functions.Homescreen;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.Calendar;

import hu.bme.andrismulller.makeithappen_withfriends.Functions.Alarm.AlarmReceiver;
import hu.bme.andrismulller.makeithappen_withfriends.MyUtils.MyUtils;
import hu.bme.andrismulller.makeithappen_withfriends.R;
import hu.bme.andrismulller.makeithappen_withfriends.model.Controlling;

public class HomeScreenActivity extends AppCompatActivity {

    private ViewPager pager;
    private PagerAdapter pagerAdapter;

    Controlling controlling;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        long id = getIntent().getLongExtra("id", -1);
        if (id > 0){
            controlling = Controlling.find(Controlling.class, "id = ?", String.valueOf(id)).get(0);
            if (controlling.isInternetBlocked()){
                MyUtils.turnOffWifi(getApplicationContext());
                registerReceiver(new WifiConnectionReceiver(), new IntentFilter("android.net.wifi.STATE_CHANGE"));
            }
            if (controlling.getDurationValue() > 0){
                Intent myIntent = new Intent(getApplicationContext(), AlarmReceiver.class);
                myIntent.putExtra("id", id);
                myIntent.putExtra("type", "controlling");
                PendingIntent alarmIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, myIntent, PendingIntent.FLAG_ONE_SHOT);
                AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis() + controlling.getDurationTimeInSec()*1000, alarmIntent);

            }
        }

        pager = (ViewPager) findViewById(R.id.homescreen_pager);
        pagerAdapter = new HomeScreenPagerAdapter(getSupportFragmentManager(), id);
        pager.setAdapter(pagerAdapter);

    }
}
