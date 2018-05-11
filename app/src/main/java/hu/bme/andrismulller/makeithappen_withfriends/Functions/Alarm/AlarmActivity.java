package hu.bme.andrismulller.makeithappen_withfriends.Functions.Alarm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import hu.bme.andrismulller.makeithappen_withfriends.MyUtils.Constants;
import hu.bme.andrismulller.makeithappen_withfriends.R;
import hu.bme.andrismulller.makeithappen_withfriends.model.Alarm;
import hu.bme.andrismulller.makeithappen_withfriends.model.Todo;

public class AlarmActivity extends AppCompatActivity {

    private static final String TAG = "AlarmActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        Intent intent = getIntent();
        String message = intent.getStringExtra("note");
        Log.i(TAG, "Note: " + message);

        long id = intent.getLongExtra("id", -1);
        if (id > 0 ){
            Alarm alarm = Alarm.findById(Alarm.class, id);
            alarm.setUsed(false);
            alarm.setRunning(false);
            alarm.save();

            if (alarm.getType() == Constants.ALARM_TYPE_TODO) {
	            try {
		            Todo todo = Todo.find(Todo.class, "alarmid = ?", alarm.getId().toString()).get(0);
					todo.setAlarm_id(-1);
					todo.save();
	            } catch (Exception e){
	            	e.printStackTrace();
	            }
            }
        }

        TextView noteTV = findViewById(R.id.alarm_note_tv);
        noteTV.setText(message);
    }
}
