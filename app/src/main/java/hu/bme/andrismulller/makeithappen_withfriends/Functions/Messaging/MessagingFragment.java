package hu.bme.andrismulller.makeithappen_withfriends.Functions.Messaging;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import hu.bme.andrismulller.makeithappen_withfriends.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessagingFragment extends Fragment {
    RecyclerView messagingRecyclerView;
    FriendsAdapter friendsAdapter;
    FloatingActionButton addFriendButton;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseAuth mAuth;

    public MessagingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        loginToFirebase();

        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messaging, container, false);

        messagingRecyclerView = view.findViewById(R.id.friends_recyclerview);
        layoutManager = new LinearLayoutManager(getContext());
        friendsAdapter = new FriendsAdapter(getActivity(), MessagingFragment.this);
        messagingRecyclerView.setLayoutManager(layoutManager);
        messagingRecyclerView.setAdapter(friendsAdapter);

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FriendsAdapter.SEND_MESSAGE_REQUEST_CODE){

        }
    }

    public void loginToFirebase(){
        if (AccessToken.getCurrentAccessToken() != null) {
            AuthCredential credential = FacebookAuthProvider.getCredential(AccessToken.getCurrentAccessToken().getToken());
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("FirebaseLogin", "signInWithCredential:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("FirebaseLogin", "signInWithCredential:failure", task.getException());
                                Toast.makeText(getContext(), "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}
