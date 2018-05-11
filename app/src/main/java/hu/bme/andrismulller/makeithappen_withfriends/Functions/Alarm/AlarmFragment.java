package hu.bme.andrismulller.makeithappen_withfriends.Functions.Alarm;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import hu.bme.andrismulller.makeithappen_withfriends.MyUtils.Constants;
import hu.bme.andrismulller.makeithappen_withfriends.R;
import hu.bme.andrismulller.makeithappen_withfriends.model.Alarm;

public class AlarmFragment extends Fragment {
    public static final String TAG = "AlarmFragment";

    ListView alarmListView;
    AlarmsListAdapter alarmsListAdapter;
    OnNewAlarmListener onNewAlarmListener;

    public interface OnNewAlarmListener {
        void onNewAlarm(long todo);
    }

    public AlarmFragment() {
        // Required empty public constructor
    }

    public static AlarmFragment newInstance() {
        AlarmFragment fragment = new AlarmFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        alarmsListAdapter = new AlarmsListAdapter(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alarm, container, false);

        alarmListView = view.findViewById(R.id.alarm_listView);
        alarmListView.setAdapter(alarmsListAdapter);

        FloatingActionButton fab = view.findViewById(R.id.alarm_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (alarmsListAdapter.getCount()  < Constants.MAX_ALARM_NUMBER)
                    onNewAlarmListener.onNewAlarm(Constants.CLOCK_ALARM);
                else {
                    Toast.makeText(getContext(), getString(R.string.no_more_alarm), Toast.LENGTH_LONG).show();
                }

            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onNewAlarmListener = (OnNewAlarmListener) context;
    }

    public void update(Alarm alarm){
        alarmsListAdapter.onAlarmAdded(alarm);
    }

}
