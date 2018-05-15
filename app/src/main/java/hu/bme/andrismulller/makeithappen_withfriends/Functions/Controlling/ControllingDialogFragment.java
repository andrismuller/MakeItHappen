package hu.bme.andrismulller.makeithappen_withfriends.Functions.Controlling;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.common.base.Objects;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import hu.bme.andrismulller.makeithappen_withfriends.R;
import hu.bme.andrismulller.makeithappen_withfriends.TimePickerDialogFragment;
import hu.bme.andrismulller.makeithappen_withfriends.model.App;
import hu.bme.andrismulller.makeithappen_withfriends.model.Controlling;

public class ControllingDialogFragment extends DialogFragment {

    private EditText titleEditText;
    private EditText durationEditText;
    private ImageButton minusButton;
    private ImageButton plusButton;
    private Spinner durationUnitSpinner;
    private Button selectTimeButton;
    private AutoCompleteTextView urlTextView;
    private ImageButton addUrlButton;
    private Spinner appSpinner;
    private CheckBox internetCheckBox;
    private ListView alreadyAddedListView;

    ArrayAdapter<String> alreadyAddedAdapter;
    ArrayList<String> alreadyAddedList = new ArrayList<>();
    ArrayList<String> exampleUrls = new ArrayList<>();

    private double INCREMENT_VALUE = 0.5;
    private double DURATION_INITIAL_VALUE = 1.0;
    private ArrayList<String> urls = new ArrayList<>();
    private ArrayList<App> apps = new ArrayList<>();
    private static int TIMEPICK_REQUEST_CODE = 10;

    OnControllingAdded onControllingAdded;
    private long time;

    public interface OnControllingAdded {
        void onControllingAdded(Controlling controlling);
    }

    public static ControllingDialogFragment newInstance(){
        return new ControllingDialogFragment();
    }

    public ControllingDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
	    setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);

	    exampleUrls.add("https://facebook.com");
        exampleUrls.add("https://youtube.com");
        setCancelable(false);
    }

	@NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder=  new  AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.add_controlling))
                .setPositiveButton(R.string.add_controlling,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Calendar calendar = Calendar.getInstance();
                                ArrayList<String> appPackageNames = new ArrayList<String>();
                                for (int i = 0; i < apps.size(); ++i){
                                    appPackageNames.add(apps.get(i).getPackageName());
                                }
                                Controlling controlling = new Controlling(
                                        titleEditText.getText().toString(),
                                        urls,
                                        appPackageNames,
                                        internetCheckBox.isChecked(),
                                        Double.valueOf(durationEditText.getText().toString()),
                                        durationUnitSpinner.getSelectedItemPosition(),
                                        calendar.getTimeInMillis());
                                controlling.setMyId(controlling.save());

                                dialog.dismiss();

                                onControllingAdded.onControllingAdded(controlling);
                            }
                        }
                )
                .setNegativeButton(getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
	                            onControllingAdded.onControllingAdded(null);
                            }
                        }
                );
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialogfragment_controlling, null);

        titleEditText = view.findViewById(R.id.controlling_title_edittext);
        durationEditText = view.findViewById(R.id.controlling_duration_edittext);
        durationEditText.setText(String.valueOf(DURATION_INITIAL_VALUE));
        plusButton = view.findViewById(R.id.increment_duration_button);
        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double duration = Double.valueOf(durationEditText.getText().toString());
                duration += INCREMENT_VALUE;
                durationEditText.setText(String.valueOf(duration));
            }
        });
        minusButton = view.findViewById(R.id.decrement_duration_button);
        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double duration = Double.valueOf(durationEditText.getText().toString());
                if (duration > 1) {
	                duration -= INCREMENT_VALUE;
	                durationEditText.setText(String.valueOf(duration));
                } else {
                	Toast.makeText(getContext(), getString(R.string.cannot_be_lower), Toast.LENGTH_LONG).show();
                }
            }
        });
        durationUnitSpinner = view.findViewById(R.id.duration_unit_spinner);
        selectTimeButton = view.findViewById(R.id.select_time_button);
        selectTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                TimePickerDialogFragment timePickerDialogFragment = new TimePickerDialogFragment();
                timePickerDialogFragment.setTargetFragment(ControllingDialogFragment.this, TIMEPICK_REQUEST_CODE);
                timePickerDialogFragment.show(fm, getActivity().getString(R.string.pick_time));
            }
        });

        ArrayAdapter<String> urlAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.select_dialog_singlechoice, exampleUrls.toArray(new String[0]));
        urlTextView = view.findViewById(R.id.url_autotextview);
        urlTextView.setThreshold(1);
        urlTextView.setAdapter(urlAdapter);
        addUrlButton = view.findViewById(R.id.add_url_button);
        addUrlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                urls.add(urlTextView.getText().toString());
                alreadyAddedList.add(urlTextView.getText().toString());
                alreadyAddedAdapter.notifyDataSetChanged();
            }
        });
        appSpinner = view.findViewById(R.id.controlling_app_spinner);
        AppSpinnerAdapter appAdapter = new AppSpinnerAdapter(getContext());
        appSpinner.setAdapter(appAdapter);
        appSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
	                App app = (App) appSpinner.getSelectedItem();
	                apps.add(app);
	                alreadyAddedList.add(app.getLabel());
	                alreadyAddedAdapter.notifyDataSetChanged();
	                appSpinner.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        internetCheckBox = view.findViewById(R.id.internet_checkbox);
        alreadyAddedListView = view.findViewById(R.id.new_controlling_listview);
        alreadyAddedList.addAll(urls);
        ArrayList<String> appLabels = new ArrayList<String>();
        for (int i = 0; i < apps.size(); ++i){
            appLabels.add(apps.get(i).getLabel());
        }
        alreadyAddedList.addAll(appLabels);
        alreadyAddedAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, alreadyAddedList);
        alreadyAddedListView.setAdapter(alreadyAddedAdapter);

        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onControllingAdded = (OnControllingAdded)context;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Make sure fragment codes match up 
        if (requestCode == ControllingDialogFragment.TIMEPICK_REQUEST_CODE){
            Bundle extras = data.getExtras();
            if (extras != null) {
                time = extras.getLong("time",0);
            }
            String dateFormat = "yyyy.mm.dd. hh:mm";
            SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
            selectTimeButton.setText(getString(R.string.select_time) + " (" + formatter.format(time) + ")");
        }
    }
}
