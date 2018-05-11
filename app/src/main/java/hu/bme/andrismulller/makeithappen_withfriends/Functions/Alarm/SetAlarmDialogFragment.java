package hu.bme.andrismulller.makeithappen_withfriends.Functions.Alarm;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;

import hu.bme.andrismulller.makeithappen_withfriends.R;
import hu.bme.andrismulller.makeithappen_withfriends.model.Alarm;

/**
 * A simple {@link Fragment} subclass.
 */
public class SetAlarmDialogFragment extends DialogFragment {

    EditText alarmNoteEditText;
    DatePicker alarmDatePicker;
    TimePicker alarmTimePicker;

    String note;
    int year;
    int month;
    int day;
    int hour;
    int minute;

    OnAlarmAddedListener onAlarmAddedListener;
	private long alarmType;

	public interface OnAlarmAddedListener {
        void onAlarmAdded(Alarm alarm, long alarmType);
    }

    public static SetAlarmDialogFragment newInstance(Long alarmType) {
        SetAlarmDialogFragment frag = new SetAlarmDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putLong("type", alarmType);
        frag.setArguments(bundle);
        return frag;
    }

    public SetAlarmDialogFragment() {
        // Required empty public constructor
    }

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		alarmType = getArguments().getLong("type");
	}

	@NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder=  new  AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.set_alarm))
                .setPositiveButton(R.string.set_alarm,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                note = alarmNoteEditText.getText().toString();

                                year = alarmDatePicker.getYear();
                                month = alarmDatePicker.getMonth();
                                day = alarmDatePicker.getDayOfMonth();

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    hour = alarmTimePicker.getHour();
                                    minute = alarmTimePicker.getMinute();
                                } else{
                                    hour = alarmTimePicker.getCurrentHour();
                                    minute = alarmTimePicker.getCurrentMinute();
                                }

                                Calendar calendar = Calendar.getInstance();
                                calendar.set(Calendar.YEAR, year);
                                calendar.set(Calendar.MONTH, month);
                                calendar.set(Calendar.DAY_OF_MONTH, day);
                                calendar.set(Calendar.HOUR_OF_DAY, hour);
                                calendar.set(Calendar.MINUTE, minute);

                                Alarm alarm = new Alarm(note, calendar.getTimeInMillis(),0, true, true , "Not implemented", 0);

                                dialog.dismiss();

                                onAlarmAddedListener.onAlarmAdded(alarm, alarmType);
                            }
                        }
                )
                .setNegativeButton(getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        }
                );

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialogfragment_set_alarm, null);

        alarmDatePicker = view.findViewById(R.id.alarm_datePicker);
        alarmNoteEditText = view.findViewById(R.id.alarm_note_editText);
        alarmTimePicker = view.findViewById(R.id.alarm_timePicker);

        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onAlarmAddedListener = (OnAlarmAddedListener)context;
    }
}
