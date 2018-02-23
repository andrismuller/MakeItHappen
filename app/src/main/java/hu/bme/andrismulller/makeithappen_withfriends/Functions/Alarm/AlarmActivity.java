package hu.bme.andrismulller.makeithappen_withfriends.Functions.Alarm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import hu.bme.andrismulller.makeithappen_withfriends.R;

public class AlarmActivity extends AppCompatActivity {

    private static final String TAG = "AlarmActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        Intent intent = getIntent();
        String message = intent.getStringExtra("note");
        Log.i(TAG, "Note: " + message);

        TextView noteTV = findViewById(R.id.alarm_note_tv);
        noteTV.setText(message);
    }
}
