package hu.bme.andrismulller.makeithappen_withfriends;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;

public class TimePickerDialogFragment extends DialogFragment {

    private DatePicker datePicker;
    private TimePicker timePicker;
    long time;

    private OnTimePicked onTimePicked;
    public interface OnTimePicked {
        void onTimePicked(long time);
    }

    public static TimePickerDialogFragment newInstance(){
        return new TimePickerDialogFragment();
    }

    public TimePickerDialogFragment() {
        // Required empty public constructor
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder=  new  AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.pick_time))
                .setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                int year = datePicker.getYear();
                                int month = datePicker.getMonth();
                                int day = datePicker.getDayOfMonth();
                                int hour;
                                int minute;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    hour = timePicker.getHour();
                                    minute = timePicker.getMinute();
                                } else{
                                    hour = timePicker.getCurrentHour();
                                    minute = timePicker.getCurrentMinute();
                                }

                                Calendar calendar = Calendar.getInstance();
                                calendar.set(year, month, day, hour, minute);

                                dialog.dismiss();

//                                onTimePicked.onTimePicked(calendar.getTimeInMillis());
                                time = calendar.getTimeInMillis();
                                sendResult(10);
//                                Snackbar mySnackbar = Snackbar.make(getActivity().findViewById(R.id.),
//                                        R.string.alarm_set, Snackbar.LENGTH_SHORT);
//                                mySnackbar.show();
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

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialogfragment_time_picker, null);

        datePicker = view.findViewById(R.id.date_picker);
        timePicker = view.findViewById(R.id.time_picker);

        builder.setView(view);
        return builder.create();
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        onTimePicked = (OnTimePicked) context;
//    }

    private void sendResult(int REQUEST_CODE) {
        Intent intent = new Intent();
        intent.putExtra("time", time);
        getTargetFragment().onActivityResult(getTargetRequestCode(), REQUEST_CODE, intent);
    }
}
