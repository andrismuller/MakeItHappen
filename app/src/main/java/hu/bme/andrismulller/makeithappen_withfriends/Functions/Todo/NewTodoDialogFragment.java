package hu.bme.andrismulller.makeithappen_withfriends.Functions.Todo;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import hu.bme.andrismulller.makeithappen_withfriends.R;
import hu.bme.andrismulller.makeithappen_withfriends.model.Todo;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewTodoDialogFragment extends DialogFragment {

    EditText titleET;
    EditText requiredET;
    EditText descriptionET;
    Spinner prioritySpinner;
    Spinner categorySpinner;
    Button setAlarmButton;

    OnTodoAddedListener onTodoAddedListener;
    public interface OnTodoAddedListener {
        void onTodoAdded();
    }

    public NewTodoDialogFragment() {
        // Required empty public constructor
    }

    public static NewTodoDialogFragment newInstance() {
        NewTodoDialogFragment frag = new NewTodoDialogFragment();
        return frag;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder=  new  AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.new_todo))
                .setPositiveButton(R.string.new_todo,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Todo todo = new Todo();
                                todo.setTitle(titleET.getText().toString());
                                todo.setRequiredInformation(requiredET.getText().toString());
                                todo.setDescription(descriptionET.getText().toString());
                                todo.setPriority(prioritySpinner.getSelectedItem().toString());
                                todo.setTodoCategory(categorySpinner.getSelectedItem().toString());
                                todo.setDone(false);
                                todo.setCreatedDate(System.currentTimeMillis());
                                todo.setAlarm_id(0);
                                todo.setMyId(todo.save());

                                dialog.dismiss();

                                onTodoAddedListener.onTodoAdded();

                                Snackbar mySnackbar = Snackbar.make(getActivity().findViewById(R.id.fragment_todo),
                                        R.string.alarm_set, Snackbar.LENGTH_SHORT);
                                mySnackbar.show();
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

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialogfragment_new_todo, null);

        descriptionET = view.findViewById(R.id.newtodo_description_edittext);
        titleET = view.findViewById(R.id.newtodo_title_edittext);
        requiredET = view.findViewById(R.id.newtodo_required_edittext);
        prioritySpinner = view.findViewById(R.id.newtodo_priority_spinner);
        categorySpinner = view.findViewById(R.id.newtodo_category_spinner);
        setAlarmButton = view.findViewById(R.id.newtodo_setalarm_button);
        setAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onTodoAddedListener = (OnTodoAddedListener) context;
    }
}
