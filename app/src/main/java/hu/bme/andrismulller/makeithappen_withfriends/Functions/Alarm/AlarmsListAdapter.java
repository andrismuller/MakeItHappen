package hu.bme.andrismulller.makeithappen_withfriends.Functions.Alarm;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import hu.bme.andrismulller.makeithappen_withfriends.MainActivity;
import hu.bme.andrismulller.makeithappen_withfriends.MyUtils.Constants;
import hu.bme.andrismulller.makeithappen_withfriends.R;
import hu.bme.andrismulller.makeithappen_withfriends.model.Alarm;

/**
 * Created by Muller Andras on 9/18/2017.
 */

public class AlarmsListAdapter extends BaseAdapter {
    private static List<Alarm> alarms = new ArrayList<>();
    private Context context;

    private LayoutInflater layoutInflater;
    private TextView timeTextView;
    private TextView noteTextView;
    private ImageButton deleteImgButton;
    private ImageButton editImgButton;
    private Button startCancelAlarmButton;

    public AlarmsListAdapter(Context applicationContext) {
        this.context = applicationContext;
        layoutInflater = (LayoutInflater.from(applicationContext));
        try {
            alarms = Alarm.find(Alarm.class, "is_used = 1 and type = ?", String.valueOf(Constants.ALARM_TYPE_CLOCK));
            for (int i = 0; i < alarms.size(); i++){
            	if (alarms.get(i).getTime() < Calendar.getInstance().getTimeInMillis()){
            		alarms.get(i).setRunning(false);
            		alarms.get(i).setUsed(false);
            		alarms.get(i).update();
            		alarms.remove(i);
	            }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getCount() {
        return alarms.size();
    }

    @Override
    public Object getItem(int i) {
        return alarms.get(i);
    }

    @Override
    public long getItemId(int i) {
        return alarms.get(i).hashCode();
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view = layoutInflater.inflate(R.layout.list_item_alarm, null);

        timeTextView = view.findViewById(R.id.alarmItem_time_textView);
        timeTextView.setText(getDate(alarms.get(i).getTime()));
        noteTextView = view.findViewById(R.id.alarmItem_note_textView);
        noteTextView.setText(alarms.get(i).getNote());

        startCancelAlarmButton = view.findViewById(R.id.start_cancel_alarm_button);
        startCancelAlarmButton.setText(alarms.get(i).isRunning() ? context.getString(R.string.cancel_alarm) : context.getString(R.string.start_alarm));
        startCancelAlarmButton.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View view) {
		        if (alarms.get(i).isRunning()){
		        	cancelAlarm(alarms.get(i));
		        	alarms.get(i).setRunning(false);
		        } else {
		        	startAlarm(alarms.get(i));
		        	alarms.get(i).setRunning(true);
		        }

		        alarms.get(i).save();
		        notifyDataSetChanged();
	        }
        });

        deleteImgButton = view.findViewById(R.id.alarmItem_delete_imgButton);
        deleteImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	if (alarms.get(i).isRunning()){
					cancelAlarm(alarms.get(i));
	            }

            	alarms.get(i).setRunning(false);
            	alarms.get(i).setUsed(false);
                alarms.get(i).save();

                alarms.remove(i);
                notifyDataSetChanged();
            }
        });
        editImgButton = view.findViewById(R.id.alarmItem_edit_imgButton);
        if (alarms.get(i).isRunning()){
        	view.setBackground(context.getDrawable(R.drawable.low_priority_background));
        } else {
        	view.setBackground(context.getDrawable(R.drawable.main_item_background));
        }

        return view;
    }

    private String getDate(long milliSeconds)
    {
        String dateFormat = "yyyy.MM.dd. hh:mm";
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.ENGLISH);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public void onAlarmAdded(Alarm alarm){
        Alarm mAlarm = Alarm.find(Alarm.class, "is_used = 0").get(0);
        if (mAlarm != null){
        	mAlarm.setUsed(true);
        	mAlarm.setRunning(true);
        	mAlarm.setMyId(alarm.getMyId());
        	mAlarm.setTime(alarm.getTime());
        	mAlarm.setNote(alarm.getNote());
        	mAlarm.setType(Constants.ALARM_TYPE_CLOCK);
        	mAlarm.save();

        	alarms.add(mAlarm);

        	startAlarm(mAlarm);
        }
        notifyDataSetChanged();
    }

    private void startAlarm(Alarm alarm){
	    Intent myIntent = new Intent(context, AlarmReceiver.class);
	    myIntent.putExtra("note", alarm.getNote());
	    myIntent.putExtra("id", alarm.getId());
	    myIntent.putExtra("type", "alarm");
	    PendingIntent alarmIntent = PendingIntent.getBroadcast(context, alarm.getUniqueID() , myIntent, PendingIntent.FLAG_ONE_SHOT);
	    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
	    Intent intentMain = new Intent(context, MainActivity.class);
	    PendingIntent pendingIntentMain = PendingIntent.getActivity(context, 0, intentMain, 0);
	    alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(alarm.getTime(), pendingIntentMain), alarmIntent);

	    Snackbar mySnackbar = Snackbar.make(((Activity)context).findViewById(R.id.fragment_alarm),
			    R.string.alarm_set, Snackbar.LENGTH_SHORT);
	    mySnackbar.show();
    }

    private void cancelAlarm(Alarm alarm){
	    Intent myIntent = new Intent(context, AlarmReceiver.class);
	    myIntent.putExtra("note", alarm.getNote());
	    myIntent.putExtra("id", alarm.getId());
	    myIntent.putExtra("type", "alarm");
	    PendingIntent alarmIntent = PendingIntent.getBroadcast(context, alarm.getUniqueID() , myIntent, PendingIntent.FLAG_ONE_SHOT);

	    alarmIntent.cancel();

	    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(alarmIntent);

	    Snackbar mySnackbar = Snackbar.make(((Activity)context).findViewById(R.id.fragment_alarm),
			    R.string.alarm_cancelled, Snackbar.LENGTH_SHORT);
	    mySnackbar.show();
    }
}
