package hu.bme.andrismulller.makeithappen_withfriends.Functions.Weather;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import hu.bme.andrismulller.makeithappen_withfriends.MyUtils.Constants;
import hu.bme.andrismulller.makeithappen_withfriends.R;
import hu.bme.andrismulller.makeithappen_withfriends.model.WeatherData;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class WeatherGraphActivity extends AppCompatActivity implements DownloadWeatherDataTask.OnWeatherDataArrivedListener {
    private static final String TAG = "WeatherGraphActivity";
	GraphView graph;
    ProgressBar mProgress;

	Location currentLocation;
	private LocationManager locationManager;
	private LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_weather_graph);

	    graph = findViewById(R.id.forecast_graph);

        mProgress = findViewById(R.id.forecast_progressbar);
        mProgress.setIndeterminate(true);
        mProgress.setVisibility(View.VISIBLE);

	    // Acquire a reference to the system Location Manager
	    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

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
    public void onWeatherDataArrived(List<WeatherData> data) {
    	graph.removeAllSeries();
        if (data == null)
            return;
        DataPoint[] dataPoints = new DataPoint[data.size()];
        for (int i = 0; i < data.size(); ++i){
            dataPoints[i] = new DataPoint(new Date(Long.parseLong(data.get(i).getDateTimeUtc())), Double.parseDouble(data.get(i).getTemperature()));
        }
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints);
        PointsGraphSeries<DataPoint> pointSeries = new PointsGraphSeries<>(dataPoints);

	    graph.getGridLabelRenderer().setHumanRounding(false);

	    graph.getViewport().setMinX(Double.parseDouble(data.get(0).getDateTimeUtc()));
	    graph.getViewport().setMaxX(Double.parseDouble(data.get(data.size()-1).getDateTimeUtc()));
	    graph.getViewport().setXAxisBoundsManual(true);

	    graph.getGridLabelRenderer().setNumVerticalLabels(10);
	    graph.getGridLabelRenderer().setNumHorizontalLabels(40);
	    graph.getGridLabelRenderer().setLabelFormatter(new DateLabelFormatter());

	    graph.addSeries(series);
	    graph.addSeries(pointSeries);

        mProgress.setProgress(View.GONE);
        mProgress.setVisibility(View.GONE);
    }

    private class DateLabelFormatter extends DefaultLabelFormatter{
	    final SimpleDateFormat sdf = new SimpleDateFormat("MM.dd\nhh:mm");

	    @Override
	    public String formatLabel(double value, boolean isValueX) {
		    if (isValueX) {
			    Date date = new Date((long)value * 1000);
			    Log.d(TAG, "label: " + sdf.format(date));
			    return sdf.format(date);
		    } else {
			    return super.formatLabel(value, isValueX) + " °C";
		    }
	    }
    }


	@AfterPermissionGranted(Constants.REQUEST_PERMISSION_LOCATION)
	private void getLocation(){
		// Register the listener with the Location Manager to receive location updates
		if (EasyPermissions.hasPermissions(
				this, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)) {
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
		SharedPreferences myPref = this.getSharedPreferences(Constants.MY_PREFERENCE, Context.MODE_PRIVATE);
		if (myPref.getLong(Constants.KEY_FORCAST_UPDATE_TIME, 0) < Calendar.getInstance().getTimeInMillis() - Constants.WEATHER_UPDATE_INTERVAL && currentLocation != null) {
			DownloadWeatherDataTask downloadWeatherDataTask = new DownloadWeatherDataTask(this, Constants.REQUEST_FORECAST, this);
			downloadWeatherDataTask.execute(Constants.FORECAST_URL.replace("q=Budapest,hu", "lat=" + currentLocation.getLatitude() + "&lon=" + currentLocation.getLongitude()));
		} else if (myPref.getLong(Constants.KEY_FORCAST_UPDATE_TIME, 0) < Calendar.getInstance().getTimeInMillis() - Constants.WEATHER_UPDATE_INTERVAL ){
			DownloadWeatherDataTask downloadWeatherDataTask = new DownloadWeatherDataTask(this, Constants.REQUEST_FORECAST, this);
			downloadWeatherDataTask.execute(Constants.FORECAST_URL);
		}
		else {
			List<WeatherData> weatherDataList = WeatherData.find(WeatherData.class, "forecast = 1");
			onWeatherDataArrived(weatherDataList);
		}
	}
}
