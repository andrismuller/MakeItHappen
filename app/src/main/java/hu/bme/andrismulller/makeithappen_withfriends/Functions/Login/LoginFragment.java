package hu.bme.andrismulller.makeithappen_withfriends.Functions.Login;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import hu.bme.andrismulller.makeithappen_withfriends.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {


    private CallbackManager callbackManager;

    OnLoginListener onLoginListener;
    public interface OnLoginListener {
        void onLogin(LoginResult loginResult);
    }

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        callbackManager = new CallbackManager.Factory().create();
        LoginButton loginButton = view.findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "user_friends", "public_profile");
        loginButton.setFragment(this);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                onLoginListener.onLogin(loginResult);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException exception) {
                exception.printStackTrace();
            }
        });

        return view;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onLoginListener = (OnLoginListener) context;
    }
}
