package hu.bme.andrismulller.makeithappen_withfriends;


import android.*;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import hu.bme.andrismulller.makeithappen_withfriends.Functions.Weather.DownloadWeatherDataTask;
import hu.bme.andrismulller.makeithappen_withfriends.Functions.Weather.WeatherFragment;
import hu.bme.andrismulller.makeithappen_withfriends.MyUtils.Constants;
import hu.bme.andrismulller.makeithappen_withfriends.model.Controlling;
import hu.bme.andrismulller.makeithappen_withfriends.model.MyMessage;
import hu.bme.andrismulller.makeithappen_withfriends.model.Todo;
import hu.bme.andrismulller.makeithappen_withfriends.model.WeatherData;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements DownloadWeatherDataTask.OnWeatherDataArrivedListener {

    TextView todoTV;
    TextView controlTV;
    TextView requestTV;
    TextView weatherTV;

    List<Todo> todos;
    List<Controlling> controllings;
    List<MyMessage> requests;

    private LocationManager locationManager;
    private LocationListener locationListener;
    private Location currentLocation;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        todos = Todo.find(Todo.class, "is_done = 0");
        controllings = Controlling.find(Controlling.class, "activated = 1");
        requests = MyMessage.find(MyMessage.class, "is_request = 1");

        // Acquire a reference to the system Location Manager
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                currentLocation = location;
                locationManager.removeUpdates(locationListener);
                getWeather();
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

        getLocation();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        todoTV = view.findViewById(R.id.main_todo_tv);
        todoTV.setText(todos.size() + " " + getString(R.string.todos_you_have));
        controlTV = view.findViewById(R.id.main_control_tv);
        controlTV.setText(controllings.size() + " " + getString(R.string.controllings_you_have));
        requestTV = view.findViewById(R.id.main_request_tv);
        requestTV.setText(requests.size() + " " + getString(R.string.requests_you_have));
        weatherTV = view.findViewById(R.id.main_weather_tv);

        return view;
    }

    @Override
    public void onWeatherDataArrived(List<WeatherData> data) {
        if (data == null)
            return;
        weatherTV.setText(data.get(0).getPlace() + ": " + getString(R.string.temperature) + ": " + data.get(0).getTemperature()+ " °C " );
    }

    @AfterPermissionGranted(Constants.REQUEST_PERMISSION_LOCATION)
    private void getLocation(){
        // Register the listener with the Location Manager to receive location updates
        if (EasyPermissions.hasPermissions(
                getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION)) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        } else {
            EasyPermissions.requestPermissions(
                    this,
                    getString(R.string.need_location_permission),
                    Constants.REQUEST_PERMISSION_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION);
        }
    }

    private void getWeather(){
    	try {
		    SharedPreferences myPref = getActivity().getSharedPreferences(Constants.MY_PREFERENCE, Context.MODE_PRIVATE);
		    if (myPref.getLong(Constants.KEY_WEATHER_UPDATE_TIME, 0) < Calendar.getInstance().getTimeInMillis() - Constants.WEATHER_UPDATE_INTERVAL && currentLocation != null) {
			    DownloadWeatherDataTask downloadWeatherDataTask = new DownloadWeatherDataTask(this, Constants.REQUEST_WEATHER, getContext());
			    downloadWeatherDataTask.execute(Constants.BUDAPEST_WEATHER_URL.replace("q=Budapest,hu", "lat=" + currentLocation.getLatitude() + "&lon=" + currentLocation.getLongitude()));
		    } else if (myPref.getLong(Constants.KEY_WEATHER_UPDATE_TIME, 0) < Calendar.getInstance().getTimeInMillis() - Constants.WEATHER_UPDATE_INTERVAL) {
			    DownloadWeatherDataTask downloadWeatherDataTask = new DownloadWeatherDataTask(this, Constants.REQUEST_WEATHER, getContext());
			    downloadWeatherDataTask.execute(Constants.BUDAPEST_WEATHER_URL);
		    } else {
			    List<WeatherData> weatherDataList = WeatherData.find(WeatherData.class, "forecast = 0");
			    onWeatherDataArrived(weatherDataList);
		    }
	    }catch (Exception e){
    		e.printStackTrace();
	    }
    }
}
