package hu.bme.andrismulller.makeithappen_withfriends.Functions.Request;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.Profile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import hu.bme.andrismulller.makeithappen_withfriends.R;
import hu.bme.andrismulller.makeithappen_withfriends.model.MyMessage;


/**
 * A simple {@link Fragment} subclass.
 */
public class RequestFragment extends Fragment {

	public static final String REQUESTS = "requests";
	DatabaseReference requestsFromOthersRef;
    List<MyMessage> requestsFromOthersList;
	RequestListAdapter requestListAdapter;
	ChildEventListener myRequestChildListener;

	TextView infoTV;

    public RequestFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loginToFirebase();

        requestsFromOthersList = new ArrayList<>();

        if (Profile.getCurrentProfile() != null) {
        	myRequestChildListener = new ChildEventListener() {
		        @Override
		        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
			        requestsFromOthersList.add(dataSnapshot.getValue(MyMessage.class));
			        requestListAdapter.addRequest(dataSnapshot.getValue(MyMessage.class));
			        if (requestListAdapter.getCount() > 0)
				        infoTV.setText(requestListAdapter.getCount() + getString(R.string.requests_you_have));
		        }

		        @Override
		        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

		        }

		        @Override
		        public void onChildRemoved(DataSnapshot dataSnapshot) {
			        requestsFromOthersList.remove(dataSnapshot.getValue(MyMessage.class));
			        requestListAdapter.removeRequest(dataSnapshot.getValue(MyMessage.class));
			        if (requestListAdapter.getCount() < 0)
				        infoTV.setText(getString(R.string.no_requests));
		        }

		        @Override
		        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

		        }

		        @Override
		        public void onCancelled(DatabaseError databaseError) {

		        }
	        };

	        requestsFromOthersRef = FirebaseDatabase.getInstance().getReference().child(REQUESTS).child(Profile.getCurrentProfile().getId());
//	        requestsFromOthersRef.addChildEventListener(myRequestChildListener);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request, container, false);

        requestListAdapter = new RequestListAdapter(getContext());
        ListView requestListView = view.findViewById(R.id.request_listview);
        requestListView.setAdapter(requestListAdapter);

        infoTV = view.findViewById(R.id.request_information_tv);
        if (requestListAdapter.getCount() > 0) {
            infoTV.setText(requestListAdapter.getCount() + getString(R.string.requestsYouHave));
        } else {
            infoTV.setText(getString(R.string.no_requests));
        }

        return view;
    }

	public void loginToFirebase(){
    	final FirebaseAuth mAuth = FirebaseAuth.getInstance();
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

	@Override
	public void onStop() {
		super.onStop();
		if (requestsFromOthersRef != null){
			requestsFromOthersRef.removeEventListener(myRequestChildListener);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (requestsFromOthersRef != null){
			requestsFromOthersRef.addChildEventListener(myRequestChildListener);
		}
	}
}
