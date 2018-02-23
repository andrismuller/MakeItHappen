package hu.bme.andrismulller.makeithappen_withfriends.Functions.Alarm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

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

    public AlarmsListAdapter(Context applicationContext) {
        this.context = applicationContext;
        layoutInflater = (LayoutInflater.from(applicationContext));
//        for (int i = 0; i < alarms.size(); ++i){
//            alarms.get(i).delete();
//        }
        try {
            alarms = Alarm.listAll(Alarm.class);
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

        deleteImgButton = view.findViewById(R.id.alarmItem_delete_imgButton);
        deleteImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alarms.get(i).delete();

                alarms.remove(i);
                notifyDataSetChanged();
            }
        });
        editImgButton = view.findViewById(R.id.alarmItem_edit_imgButton);

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

    public void onAlarmAdded(){
        alarms = Alarm.listAll(Alarm.class);
        notifyDataSetChanged();
    }
}
