package hu.bme.andrismulller.makeithappen_withfriends.Functions.Messaging;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.Profile;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Calendar;

import hu.bme.andrismulller.makeithappen_withfriends.MainActivity;
import hu.bme.andrismulller.makeithappen_withfriends.R;
import hu.bme.andrismulller.makeithappen_withfriends.model.MyMessage;

public class SendMessageDialogFragment extends DialogFragment {

    private String userID;
    private String name;
    private DatabaseReference mFirebaseDatabaseReference;

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setName(String name) {
        this.name = name;
    }

    RecyclerView messagesRecyclerView;
    RecyclerView.LayoutManager layoutManager;
//    EarlierMessagesAdapter messagesAdapter;
    public static MessagesAdapter messagesAdapter;
    EditText sendMessageEdittext;
    ImageButton sendMessageButton;
    TextView nameTextView;
    ImageView profileImageView;
    ImageButton backButton;

    public String MESSAGES_CHILD = "";

    DatabaseReference messagesRef;

    SnapshotParser<MyMessage> parser;

    FirebaseRecyclerOptions<MyMessage> options;


    public SendMessageDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MESSAGES_CHILD = userID.compareTo(Profile.getCurrentProfile().getId()) < 0 ? userID + ";" + Profile.getCurrentProfile().getId() : Profile.getCurrentProfile().getId() + ";" + userID;

        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        messagesRef = mFirebaseDatabaseReference.child(MESSAGES_CHILD);

        parser = new SnapshotParser<MyMessage>() {
            @Override
            public MyMessage parseSnapshot(DataSnapshot dataSnapshot) {
                MyMessage friendlyMessage = dataSnapshot.getValue(MyMessage.class);
                if (friendlyMessage != null) {
                    friendlyMessage.setMyId(dataSnapshot.getKey());
                }
                return friendlyMessage;
            }
        };

        options = new FirebaseRecyclerOptions.Builder<MyMessage>()
                .setQuery(messagesRef, parser)
                .build();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialogfragment_send_message, null);
        messagesRecyclerView = view.findViewById(R.id.earlier_messages_recyclerview);
        layoutManager = new LinearLayoutManager(getContext());
        messagesRecyclerView.setLayoutManager(layoutManager);
//        messagesAdapter = new EarlierMessagesAdapter(userID, "me");
        messagesAdapter = new MessagesAdapter(options);
        messagesAdapter.startListening();
        messagesRecyclerView.setAdapter(messagesAdapter);
        sendMessageEdittext = view.findViewById(R.id.send_message_edittext);
        sendMessageButton = view.findViewById(R.id.send_message_button);
        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyMessage message = new MyMessage(sendMessageEdittext.getText().toString(), Calendar.getInstance().getTimeInMillis(),"me",userID, false);
                mFirebaseDatabaseReference.child(MESSAGES_CHILD).push().setValue(message);
                sendMessageEdittext.setText("");
                //mFirebaseAnalytics.logEvent(MESSAGE_SENT_EVENT, null);
            }
        });
        profileImageView = view.findViewById(R.id.send_message_friend_imageview);
        profileImageView.setImageBitmap(loadImageFromStorage(userID));
        nameTextView = view.findViewById(R.id.send_message_name_textview);
        nameTextView.setText(name);
        backButton = view.findViewById(R.id.send_message_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });

        return view;
    }

    private Bitmap loadImageFromStorage(String userId) {
        Bitmap b = null;
        try {
            File f=new File(MainActivity.IMAGE_DIR_PATH, userId + "_pic.jpg");
            b = BitmapFactory.decodeStream(new FileInputStream(f));
            return b;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return b;
    }

    @Override
    public void onResume() {
        super.onResume();
        messagesAdapter.startListening();
    }
}
