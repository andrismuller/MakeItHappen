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
import android.support.v4.app.Fragment;
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
        , NewWalletDialogFragment.OnWalletItemAdded, WalletFragment.OnNewWalletItemListener {

    private String fbToken;
    AccessTokenTracker accessTokenTracker;
    public static String IMAGE_DIR_PATH = "/data/user/0/hu.muller.andris.armando.makeithappen_withfriends/app_imageDir/";
    GoogleAccountCredential mCredential;
    ProgressDialog mProgress;

    private static final String BUTTON_TEXT = "Call Google Calendar API";
    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String[] SCOPES = { CalendarScopes.CALENDAR_READONLY };



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

        NoteFragment noteFragment = new NoteFragment();
        noteFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, noteFragment).commit();

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
                MessagingFragment messagingFragment = new MessagingFragment();
                messagingFragment.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, messagingFragment).commit();
            } else if (id == R.id.nav_todo) {
                TodoFragment todoFragment = new TodoFragment();
                todoFragment.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, todoFragment).commit();
            } else if (id == R.id.nav_calendar) {
                mProgress = new ProgressDialog(this);
                mProgress.setMessage("Calling Google Calendar API ...");

                getResultsFromApi();
            } else if (id == R.id.nav_alarm) {
                AlarmFragment alarmFragment = new AlarmFragment();
                alarmFragment.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, alarmFragment).commit();
            } else if (id == R.id.nav_main) {

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

    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case Constants.REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {
//                    mOutputText.setText(
//                            "This app requires Google Play Services. Please install " +
//                                    "Google Play Services on your device and relaunch this app.");
                } else {
                    getResultsFromApi();
                }
                break;
            case Constants.REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    String accountName =
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        SharedPreferences settings =
                                getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(PREF_ACCOUNT_NAME, accountName);
                        editor.apply();
                        mCredential.setSelectedAccountName(accountName);
                        getResultsFromApi();
                    }
                }
                break;
            case Constants.REQUEST_AUTHORIZATION:
                if (resultCode == RESULT_OK) {
                    getResultsFromApi();
                }
                break;
        }
    }

    private void getResultsFromApi() {
        if (! isGooglePlayServicesAvailable()) {
            acquireGooglePlayServices();
        } else if (mCredential.getSelectedAccountName() == null) {
            chooseAccount();
        } else if (! isDeviceOnline()) {

        } else {
            new MakeRequestTask(mCredential).execute();
        }
    }

    @AfterPermissionGranted(Constants.REQUEST_PERMISSION_GET_ACCOUNTS)
    private void chooseAccount() {
        if (EasyPermissions.hasPermissions(
                this, android.Manifest.permission.GET_ACCOUNTS)) {
            String accountName = this.getPreferences(Context.MODE_PRIVATE)
                    .getString(PREF_ACCOUNT_NAME, null);
            if (accountName != null) {
                mCredential.setSelectedAccountName(accountName);
                getResultsFromApi();
            } else {
                // Start a dialog from which the user can choose an account
                startActivityForResult(
                        mCredential.newChooseAccountIntent(),
                        Constants.REQUEST_ACCOUNT_PICKER);
            }
        } else {
            // Request the GET_ACCOUNTS permission via a user dialog
            EasyPermissions.requestPermissions(
                    this,
                    "This app needs to access your Google account (via Contacts).",
                    Constants.REQUEST_PERMISSION_GET_ACCOUNTS,
                    android.Manifest.permission.GET_ACCOUNTS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(
                requestCode, permissions, grantResults, this);
    }

    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    private void acquireGooglePlayServices() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
        }
    }

    void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(
                this,
                connectionStatusCode,
                Constants.REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

    private class MakeRequestTask extends AsyncTask<Void, Void, List<String>> {
        private com.google.api.services.calendar.Calendar mService = null;
        private Exception mLastError = null;

        MakeRequestTask(GoogleAccountCredential credential) {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new com.google.api.services.calendar.Calendar.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName("Google Calendar")
                    .build();
        }

        /**
         * Background task to call Google Calendar API.
         * @param params no parameters needed for this task.
         */
        @Override
        protected List<String> doInBackground(Void... params) {
            try {
                return getDataFromApi();
            } catch (Exception e) {
                mLastError = e;
                cancel(true);
                return null;
            }
        }

        /**
         * Fetch a list of the next 10 events from the primary calendar.
         * @return List of Strings describing returned events.
         * @throws IOException
         */
        private List<String> getDataFromApi() throws IOException {
            // List the next 10 events from the primary calendar.
            DateTime now = new DateTime(System.currentTimeMillis());
            List<String> eventStrings = new ArrayList<>();
            Events events = mService.events().list("primary")
                    .setMaxResults(10)
                    .setTimeMin(now)
                    .setOrderBy("startTime")
                    .setSingleEvents(true)
                    .execute();
            List<Event> items = events.getItems();

            for (Event event : items) {
                DateTime start = event.getStart().getDateTime();
                if (start == null) {
                    // All-day events don't have start times, so just use
                    // the start date.
                    start = event.getStart().getDate();
                }
                eventStrings.add(
                        String.format("%s (%s)", event.getSummary(), start));
            }
            return eventStrings;
        }


        @Override
        protected void onPreExecute() {
            mProgress.show();
        }

        @Override
        protected void onPostExecute(List<String> output) {
            mProgress.hide();
            if (output == null || output.size() == 0) {
//                mOutputText.setText("No results returned.");
            } else {
                CalendarFragment calendarFragment = new CalendarFragment();
                calendarFragment.setItems(output);
                calendarFragment.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, calendarFragment).commit();
            }
        }

        @Override
        protected void onCancelled() {
            mProgress.hide();
            if (mLastError != null) {
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServicesAvailabilityErrorDialog(
                            ((GooglePlayServicesAvailabilityIOException) mLastError)
                                    .getConnectionStatusCode());
                } else if (mLastError instanceof UserRecoverableAuthIOException) {
                    startActivityForResult(
                            ((UserRecoverableAuthIOException) mLastError).getIntent(),
                            Constants.REQUEST_AUTHORIZATION);
                } else {
//                    mOutputText.setText("The following error occurred:\n"
//                            + mLastError.getMessage());
                }
            } else {
//                mOutputText.setText("Request cancelled.");
            }
        }
    }
}
