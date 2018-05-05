package hu.bme.andrismulller.makeithappen_withfriends.Functions.Weather;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
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

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class WeatherGraphActivity extends AppCompatActivity implements DownloadWeatherDataTask.OnWeatherDataArrivedListener {
    private static final String TAG = "WeatherGraphActivity";
    GraphView graph;
    ProgressBar mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_weather_graph);

        graph = findViewById(R.id.forecast_graph);
        mProgress = findViewById(R.id.forecast_progressbar);
        mProgress.setIndeterminate(true);
        mProgress.setVisibility(View.VISIBLE);

        SharedPreferences myPref = getSharedPreferences(Constants.MY_PREFERENCE, Context.MODE_PRIVATE);
        Log.d(TAG, "Forecast last updated: " + myPref.getLong(Constants.KEY_FORCAST_UPDATE_TIME, 0));
        if (myPref.getLong(Constants.KEY_FORCAST_UPDATE_TIME, 0) < Calendar.getInstance().getTimeInMillis() - Constants.WEATHER_UPDATE_INTERVAL) {
            DownloadWeatherDataTask downloadWeatherDataTask = new DownloadWeatherDataTask(this, Constants.REQUEST_FORECAST, getApplicationContext());
            downloadWeatherDataTask.execute(Constants.FORECAST_URL);
            WeatherData.deleteAll(WeatherData.class, "forecast = 1");
        } else {
            List<WeatherData> weatherDataList = WeatherData.find(WeatherData.class, "forecast = 1");
            onWeatherDataArrived(weatherDataList);
        }
    }

    @Override
    public void onWeatherDataArrived(List<WeatherData> data) {
        graph.removeAllSeries();
        if (data == null)
            return;
        DataPoint[] dataPoints = new DataPoint[data.size()];
        for (int i = 0; i < data.size(); ++i){
            dataPoints[i] = new DataPoint(new Date(Long.parseLong(data.get(i).getDateTimeTtc())), Double.parseDouble(data.get(i).getTemperature()));
        }
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints);
        PointsGraphSeries<DataPoint> pointSeries = new PointsGraphSeries<>(dataPoints);

        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    SimpleDateFormat sdf = new SimpleDateFormat("MM.dd\nhh:mm");
                    long date = (long)value;
                    Log.d(TAG, "Time: " + sdf.format(new Date(date * 1000)) + " --- millis: " + date);
                    return sdf.format(new Date(date * 1000));
                } else {
                    return super.formatLabel(value, isValueX) + " °C";
                }
            }
        });
        graph.getGridLabelRenderer().setNumHorizontalLabels(40);
        graph.getGridLabelRenderer().setNumVerticalLabels(10);

        graph.getViewport().setMinX(Double.parseDouble(data.get(0).getDateTimeTtc()));
        graph.getViewport().setMaxX(Double.parseDouble(data.get(data.size()-1).getDateTimeTtc()));
        graph.getViewport().setXAxisBoundsManual(true);

        graph.getGridLabelRenderer().setHumanRounding(false);

        graph.addSeries(series);
        graph.addSeries(pointSeries);

        mProgress.setProgress(View.GONE);
        mProgress.setVisibility(View.GONE);
    }
}
