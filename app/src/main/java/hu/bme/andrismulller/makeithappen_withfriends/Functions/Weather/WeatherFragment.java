package hu.bme.andrismulller.makeithappen_withfriends.Functions.Weather;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;

import hu.bme.andrismulller.makeithappen_withfriends.MainActivity;
import hu.bme.andrismulller.makeithappen_withfriends.MyUtils.Constants;
import hu.bme.andrismulller.makeithappen_withfriends.R;
import hu.bme.andrismulller.makeithappen_withfriends.model.WeatherData;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherFragment extends Fragment implements DownloadWeatherDataTask.OnWeatherDataArrivedListener {

	TextView tempTextView;
	TextView cityTextView;
	TextView descriptionTV;
	ImageView iconIV;
	Button forecastButton;
	ProgressBar weatherProgressBar;

	Location currentLocation;
	private LocationManager locationManager;
	private LocationListener locationListener;

	public WeatherFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weather, container, false);
        cityTextView = view.findViewById(R.id.weather_place_tv);
        tempTextView = view.findViewById(R.id.weather_temp_tv);
        descriptionTV = view.findViewById(R.id.weather_description_tv);
        iconIV = view.findViewById(R.id.weather_icon_iv);
        forecastButton = view.findViewById(R.id.weather_forecast_button);
        forecastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), WeatherGraphActivity.class);
                startActivity(intent);
            }
        });

        weatherProgressBar = view.findViewById(R.id.weather_progressbar);
        weatherProgressBar.setVisibility(View.VISIBLE);


        return view;
    }

    @Override
    public void onWeatherDataArrived(List<WeatherData> data) {
        if (data == null)
            return;
        tempTextView.setText(data.get(0).getTemperature());
        cityTextView.setText(data.get(0).getPlace());
        descriptionTV.setText(data.get(0).getDescription());
        Picasso.with(getContext()).load(Constants.WEATHER_ICON_URL + data.get(0).getIcon() + ".png").into(iconIV);
        weatherProgressBar.setVisibility(View.GONE);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode,
	                                       @NonNull String[] permissions,
	                                       @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		EasyPermissions.onRequestPermissionsResult(
				requestCode, permissions, grantResults, this);
	}

	@AfterPermissionGranted(Constants.REQUEST_PERMISSION_LOCATION)
	private void getLocation(){
		// Register the listener with the Location Manager to receive location updates
		if (EasyPermissions.hasPermissions(
				getActivity(), Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)) {
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
		} else {
			EasyPermissions.requestPermissions(
					this,
					getString(R.string.need_location_permission),
					Constants.REQUEST_PERMISSION_LOCATION,
					Manifest.permission.ACCESS_FINE_LOCATION,
					Manifest.permission.ACCESS_COARSE_LOCATION);
		}
	}

	private void getWeather(){
		if (getContext() != null) {
			SharedPreferences myPref = getContext().getSharedPreferences(Constants.MY_PREFERENCE, Context.MODE_PRIVATE);
			if (myPref != null) {
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
			}
		}
	}
}
