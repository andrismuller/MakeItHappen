package hu.bme.andrismulller.makeithappen_withfriends.Functions.Controlling;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.facebook.Profile;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import hu.bme.andrismulller.makeithappen_withfriends.MyUtils.Constants;
import hu.bme.andrismulller.makeithappen_withfriends.R;
import hu.bme.andrismulller.makeithappen_withfriends.model.Controlling;
import hu.bme.andrismulller.makeithappen_withfriends.model.FacebookUser;
import hu.bme.andrismulller.makeithappen_withfriends.model.MyMessage;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShareControllingDialogFragment extends DialogFragment {
    private String MESSAGES_CHILD;
    List<String> friendsNames;
    List<FacebookUser> friends;

    Spinner friendSpinner;
    EditText messageET;
    private Controlling controlling;

    DatabaseReference mFirebaseDatabaseReference;

    public ShareControllingDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();

        friendsNames = new ArrayList<>();
        friends = FacebookUser.listAll(FacebookUser.class);
        for (FacebookUser friend : friends){
            friendsNames.add(friend.getUserName());
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.request_page_title))
                .setPositiveButton(getString(R.string.share),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                MESSAGES_CHILD = friends.get(friendSpinner.getSelectedItemPosition()).getFacebookId();

                                MyMessage message = new MyMessage(messageET.getText() + Constants.DELIMITER_REQUEST + Constants.REQUEST_TYPE_CALL + Constants.DELIMITER_REQUEST + controlling.getStartTime(),
                                        Calendar.getInstance().getTimeInMillis(),
                                        friendSpinner.getSelectedItem().toString(), Profile.getCurrentProfile().getId(), true);
                                message.setId(message.save());

                                mFirebaseDatabaseReference.child(MESSAGES_CHILD).push().setValue(message);
                            }
                        })
                .setNegativeButton(getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialogfragment_share_controlling, null);

        friendSpinner = view.findViewById(R.id.share_controlling_friend_spinner);
        if (friendsNames != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                    android.R.layout.simple_spinner_item, friendsNames);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            friendSpinner.setAdapter(adapter);
        }

        messageET = view.findViewById(R.id.request_send_message_et);

        builder.setView(view);
        return builder.create();
    }

    public void setControlling(Controlling controlling) {
        this.controlling = controlling;
    }
}
