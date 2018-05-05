package hu.bme.andrismulller.makeithappen_withfriends;

import android.*;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.orm.query.Condition;
import com.orm.query.Select;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hu.bme.andrismulller.makeithappen_withfriends.Functions.Alarm.AlarmFragment;
import hu.bme.andrismulller.makeithappen_withfriends.Functions.Alarm.SetAlarmDialogFragment;
import hu.bme.andrismulller.makeithappen_withfriends.Functions.Calendar.CalendarFragment;
import hu.bme.andrismulller.makeithappen_withfriends.Functions.Controlling.ControllingAdapter;
import hu.bme.andrismulller.makeithappen_withfriends.Functions.Controlling.ControllingDialogFragment;
import hu.bme.andrismulller.makeithappen_withfriends.Functions.Controlling.ControllingFragment;
import hu.bme.andrismulller.makeithappen_withfriends.Functions.Controlling.ShareControllingDialogFragment;
import hu.bme.andrismulller.makeithappen_withfriends.Functions.Homescreen.HomeScreenActivity;
import hu.bme.andrismulller.makeithappen_withfriends.Functions.Login.LoginFragment;
import hu.bme.andrismulller.makeithappen_withfriends.Functions.Messaging.MessagingFragment;
import hu.bme.andrismulller.makeithappen_withfriends.Functions.Note.NoteFragment;
import hu.bme.andrismulller.makeithappen_withfriends.Functions.Request.RequestFragment;
import hu.bme.andrismulller.makeithappen_withfriends.Functions.Todo.NewTodoDialogFragment;
import hu.bme.andrismulller.makeithappen_withfriends.Functions.Todo.TodoFragment;
import hu.bme.andrismulller.makeithappen_withfriends.Functions.Wallet.NewWalletDialogFragment;
import hu.bme.andrismulller.makeithappen_withfriends.Functions.Wallet.WalletFragment;
import hu.bme.andrismulller.makeithappen_withfriends.Functions.Weather.WeatherFragment;
import hu.bme.andrismulller.makeithappen_withfriends.MyUtils.Constants;
import hu.bme.andrismulller.makeithappen_withfriends.model.Controlling;
import hu.bme.andrismulller.makeithappen_withfriends.model.FacebookUser;
import hu.bme.andrismulller.makeithappen_withfriends.model.MyMessage;
import hu.bme.andrismulller.makeithappen_withfriends.model.WalletItem;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        TodoFragment.OnNewTodoListener, NewTodoDialogFragment.OnTodoAddedListener,
        SetAlarmDialogFragment.OnAlarmAddedListener, AlarmFragment.OnNewAlarmListener,
        LoginFragment.OnLoginListener
        , ControllingFragment.OnNewControllingListener
        , ControllingDialogFragment.OnControllingAdded
        , ControllingAdapter.OnControllingStartedListener
        , ControllingAdapter.OnSharingListener
        , NewWalletDialogFragment.OnWalletItemAddedListener, WalletFragment.OnNewWalletItemListener {

    private String fbToken;
    AccessTokenTracker accessTokenTracker;
    public static String IMAGE_DIR_PATH = "/data/user/0/hu.muller.andris.armando.makeithappen_withfriends/app_imageDir/";

    private static final String[] SCOPES = { CalendarScopes.CALENDAR_READONLY };
    GoogleAccountCredential mCredential;

    TextView navHeaderUserTV;
    ImageView navHeaderImgV;
    private DatabaseReference mRequestReference;
    private ChildEventListener mRequestReferenceListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        setContentView(R.layout.activity_main);

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "hu.muller.andris.armando.makeithappen_withfriends",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

        try {
            mRequestReference = FirebaseDatabase
                    .getInstance()
                    .getReference()
                    .child(Profile.getCurrentProfile().getId());
        } catch (Exception e){
            e.printStackTrace();
        }

        if (mRequestReference != null) {
            mRequestReferenceListener = mRequestReference.addChildEventListener(new ChildEventListener() {

                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    MyMessage request = dataSnapshot.getValue(MyMessage.class);
                    request.setId(request.save());
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header=navigationView.getHeaderView(0);
        navHeaderUserTV = header.findViewById(R.id.nav_header_user_name);
        navHeaderImgV = header.findViewById(R.id.nav_header_imageView);

        MainFragment mainFragment = new MainFragment();
        mainFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, mainFragment).commit();

        if (Profile.getCurrentProfile() != null){
            onLogin(null);
        }

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                if (currentAccessToken == null){
                    onLogout();
                }
            }
        };

        // Initialize credentials and service object.
        mCredential = GoogleAccountCredential.usingOAuth2(
                this, Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (findViewById(R.id.fragment_container) != null) {

            if (id == R.id.nav_controlling) {
                ControllingFragment controllingFragment = new ControllingFragment();
                controllingFragment.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, controllingFragment).commit();
            } else if (id == R.id.nav_friends) {
                if (Profile.getCurrentProfile() != null) {
                    MessagingFragment messagingFragment = new MessagingFragment();
                    messagingFragment.setArguments(getIntent().getExtras());
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, messagingFragment).commit();
                } else {
                    Snackbar.make(findViewById(android.R.id.content), getString(R.string.you_need_to_login_first), Snackbar.LENGTH_LONG)
                        .setAction(R.string.login, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                LoginFragment loginFragment = new LoginFragment();
                                loginFragment.setArguments(getIntent().getExtras());
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, loginFragment).commit();
                            }
                        }).show();
                }
            } else if (id == R.id.nav_todo) {
                TodoFragment todoFragment = new TodoFragment();
                todoFragment.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, todoFragment).commit();
            } else if (id == R.id.nav_calendar) {
                CalendarFragment calendarFragment = new CalendarFragment();
                calendarFragment.setArguments(getIntent().getExtras());
                calendarFragment.setCredential(mCredential);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, calendarFragment).commit();
            } else if (id == R.id.nav_alarm) {
                AlarmFragment alarmFragment = new AlarmFragment();
                alarmFragment.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, alarmFragment).commit();
            } else if (id == R.id.nav_main) {
                MainFragment mainFragment = new MainFragment();
                mainFragment.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mainFragment).commit();
            } else if (id == R.id.nav_note) {
                NoteFragment noteFragment = new NoteFragment();
                noteFragment.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, noteFragment).commit();
            } else if (id == R.id.nav_wallet) {
                WalletFragment walletFragment = new WalletFragment();
                walletFragment.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, walletFragment).commit();
            } else if (id == R.id.nav_login) {
                LoginFragment loginFragment = new LoginFragment();
                loginFragment.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, loginFragment).commit();
            } else if (id == R.id.nav_requests) {
                RequestFragment requestFragment = new RequestFragment();
                requestFragment.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, requestFragment).commit();
            } else if (id == R.id.nav_weather) {
                WeatherFragment weatherFragment = new WeatherFragment();
                weatherFragment.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, weatherFragment).commit();
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onNewTodo() {
        NewTodoDialogFragment newTodoDialogFragment = NewTodoDialogFragment.newInstance();
        newTodoDialogFragment.show(getSupportFragmentManager(), getString(R.string.new_todo));
    }

    @Override
    public void onTodoAdded() {
        Fragment page = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (page instanceof TodoFragment){
            TodoFragment todoFragment = (TodoFragment) page;
            todoFragment.update();
        }
    }

    @Override
    public void onAlarmAdded() {
        Fragment page = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (page instanceof AlarmFragment){
            AlarmFragment alarmFragment = (AlarmFragment)page;
            alarmFragment.update();
        }
    }

    @Override
    public void onNewAlarm() {
        SetAlarmDialogFragment setAlarmDialogFragment = SetAlarmDialogFragment.newInstance();
        setAlarmDialogFragment.show(getSupportFragmentManager(), getString(R.string.set_alarm));
    }

    @Override
    public void onLogin(LoginResult loginResult) {

        if (AccessToken.getCurrentAccessToken() != null) {
            GraphRequest request = GraphRequest.newMeRequest(
                    AccessToken.getCurrentAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            try {
                                navHeaderUserTV.setText(response.getJSONObject().get("name").toString());
                                GetFacebookProfilePicture getPicture = new GetFacebookProfilePicture(response.getJSONObject().getString("id"), true);
                                getPicture.execute(new String[]{"https://graph.facebook.com/" + response.getJSONObject().getString("id") + "/picture?type=small"});

                                JSONArray jsonArrayFriends = object.getJSONObject("friends").getJSONArray("data");
                                for (int i = 0; i < jsonArrayFriends.length(); ++i){
                                    JSONObject friend = jsonArrayFriends.getJSONObject(i);
                                    if (!existFriendInDatabase(friend.getString("name"))){
                                        getPicture = new GetFacebookProfilePicture(friend.getString("id"), false);
                                        getPicture.execute(new String[]{"https://graph.facebook.com/" + friend.getString("id") + "/picture?type=small"});
                                        FacebookUser facebookUser = new FacebookUser(friend.getString("name"), friend.getString("id"));
                                        facebookUser.setMyId(facebookUser.save());
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,link,friends");
            request.setParameters(parameters);
            request.executeAsync();
        }
    }

    public void onLogout() {

        navHeaderUserTV.setText(getString(R.string.logged_out));
        navHeaderImgV.setImageDrawable(getDrawable(R.drawable.ic_menu_camera));

        Snackbar.make(findViewById(android.R.id.content), "Logged out!", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    public void onNewControlling() {
        ControllingDialogFragment controllingDialogFragment = ControllingDialogFragment.newInstance();
        controllingDialogFragment.show(getSupportFragmentManager(), getString(R.string.add_controlling));
    }

    @Override
    public void onControllingStarted(Controlling controlling) {
        Intent homescreenActivity = new Intent(getApplicationContext(), HomeScreenActivity.class);
        homescreenActivity.putExtra("id", controlling.getId());
        getApplicationContext().startActivity(homescreenActivity);
    }

    @Override
    public void onControllingAdded(Controlling controlling) {
        Fragment page = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (page instanceof ControllingFragment){
            ControllingFragment fragment = (ControllingFragment) page;
            fragment.update(controlling);
        }
    }

    @Override
    public void onSharing(Controlling controlling) {
        ShareControllingDialogFragment shareControllingDialogFragment = new ShareControllingDialogFragment();
        shareControllingDialogFragment.setControlling(controlling);
        shareControllingDialogFragment.show(getSupportFragmentManager(), getString(R.string.share));

    }

    @Override
    public void onWalletItemAdded(WalletItem walletItem) {
        Fragment page = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (page instanceof WalletFragment){
            WalletFragment fragment = (WalletFragment) page;
            fragment.update(walletItem);
        }
    }

    @Override
    public void onNewWalletItem(boolean bevetel) {
        NewWalletDialogFragment newWalletDialogFragment = new NewWalletDialogFragment();
        newWalletDialogFragment.setBevetel(bevetel);
        newWalletDialogFragment.show(getSupportFragmentManager(), getString(R.string.new_wallet_item));
    }

    private class GetFacebookProfilePicture extends AsyncTask<String, Void, String> {
        private String userId;
        private boolean isItMe;

        public GetFacebookProfilePicture(String userId, boolean isItMe) {
            this.isItMe = isItMe;
            this.userId = userId;
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                URL imageURL = new URL(urls[0]);
                final Bitmap bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
                IMAGE_DIR_PATH = saveToInternalStorage(bitmap,userId);
                if (isItMe){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            navHeaderImgV.setImageBitmap(bitmap);
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return IMAGE_DIR_PATH;
        }

        @Override
        protected void onPostExecute(String result) {}
    }

    public boolean existFriendInDatabase(String name){
        List<FacebookUser> facebookUsers = Select.from(FacebookUser.class)
                .where(Condition.prop("user_name").eq(name))
                .list();
        if (facebookUsers != null && facebookUsers.size() > 0){
            return true;
        } else {
            return false;
        }
    }

    private String saveToInternalStorage(Bitmap bitmapImage, String userId){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        File mypath = new File(directory, userId + "_pic.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }
}
