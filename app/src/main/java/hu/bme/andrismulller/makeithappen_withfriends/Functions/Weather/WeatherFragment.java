package hu.bme.andrismulller.makeithappen_withfriends.Functions.Weather;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;

import hu.bme.andrismulller.makeithappen_withfriends.MainActivity;
import hu.bme.andrismulller.makeithappen_withfriends.MyUtils.Constants;
import hu.bme.andrismulller.makeithappen_withfriends.R;
import hu.bme.andrismulller.makeithappen_withfriends.model.WeatherData;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherFragment extends Fragment implements DownloadWeatherDataTask.OnWeatherDataArrivedListener{

    TextView tempTextView;
    TextView cityTextView;
    TextView descriptionTV;
    ImageView iconIV;
    Button forecastButton;

    public WeatherFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        SharedPreferences myPref = getContext().getSharedPreferences(Constants.MY_PREFERENCE, Context.MODE_PRIVATE);
        if (myPref.getLong(Constants.KEY_WEATHER_UPDATE_TIME, 0) < Calendar.getInstance().getTimeInMillis() - Constants.WEATHER_UPDATE_INTERVAL) {
            DownloadWeatherDataTask downloadWeatherDataTask = new DownloadWeatherDataTask(this, Constants.REQUEST_WEATHER, getContext());
            downloadWeatherDataTask.execute(Constants.BUDAPEST_WEATHER_URL);
        } else {
            List<WeatherData> weatherDataList = WeatherData.find(WeatherData.class, "forecast = 0");
            onWeatherDataArrived(weatherDataList);
        }

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
    }

}
