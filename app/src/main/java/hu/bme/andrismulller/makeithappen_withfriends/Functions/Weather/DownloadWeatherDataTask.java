package hu.bme.andrismulller.makeithappen_withfriends.Functions.Weather;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import hu.bme.andrismulller.makeithappen_withfriends.MainActivity;
import hu.bme.andrismulller.makeithappen_withfriends.MyUtils.Constants;
import hu.bme.andrismulller.makeithappen_withfriends.model.WeatherData;

/**
 * Created by Muller Andras on 9/11/2017.
 */

public class DownloadWeatherDataTask extends AsyncTask<String,Void,String> {
    private String result = "";
    private URL url;
    private HttpURLConnection urlConnection = null;
    private int requestType;
    Context context;

    private OnWeatherDataArrivedListener onWeatherDataArrivedListener;

    public interface OnWeatherDataArrivedListener{
        void onWeatherDataArrived(List<WeatherData> data);
    }

    public DownloadWeatherDataTask(OnWeatherDataArrivedListener weatherDataArrivedListener, int flag, Context context){
        this.onWeatherDataArrivedListener = weatherDataArrivedListener;
        requestType = flag;
        this.context = context;
    }

    @Override
    protected String doInBackground(String... urls) {

        try {
            url = new URL(urls[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream is = urlConnection.getInputStream();
            InputStreamReader reader = new InputStreamReader(is);

            int data = reader.read();
            while (data != -1){
                char current = (char) data;
                result += current;
                data = reader.read();
            }

            return result;

        } catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        try {
            JSONObject jsonObject = new JSONObject(result);
            List<WeatherData> weatherDataList = new ArrayList<>();
            String dateTimeUTC;
            String temperature;
            String placeName;
            String description;
            String icon;

            SharedPreferences myPref = context.getSharedPreferences(Constants.MY_PREFERENCE, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = myPref.edit();

            if (requestType == Constants.REQUEST_WEATHER) {
                JSONObject weatherData = new JSONObject(jsonObject.getString("main"));

                dateTimeUTC = jsonObject.getString("dt");
                temperature = weatherData.getString("temp");
                placeName = jsonObject.getString("name");
                description = jsonObject.getJSONArray("weather").getJSONObject(0).getString("description");
                icon = jsonObject.getJSONArray("weather").getJSONObject(0).getString("icon");

                WeatherData mWeatherObject = new WeatherData(dateTimeUTC, temperature, description, icon, placeName, false);
                mWeatherObject.setMyId(mWeatherObject.save());
                weatherDataList.add(mWeatherObject);

                editor.putLong(Constants.KEY_WEATHER_UPDATE_TIME, Calendar.getInstance().getTimeInMillis());
                editor.commit();
            } else if (requestType == Constants.REQUEST_FORECAST){
                JSONArray weatherData = new JSONArray(jsonObject.getString("list"));
                HashMap<String, String> hmWeatherData = new HashMap<>();
                placeName = jsonObject.getJSONObject("city").getString("name");

                for (int i = 0; i < weatherData.length(); ++i) {
                    JSONObject listItem = weatherData.getJSONObject(0);
                    dateTimeUTC = listItem.getString("dt");
                    temperature = listItem.getJSONObject("main").getString("temp");
                    description = listItem.getJSONArray("weather").getJSONObject(0).getString("description");
                    icon = listItem.getJSONArray("weather").getJSONObject(0).getString("icon");

                    WeatherData mWeatherObject = new WeatherData(dateTimeUTC, temperature, description, icon, placeName, true);
                    mWeatherObject.setMyId(mWeatherObject.save());
                    weatherDataList.add(mWeatherObject);
                }

                editor.putLong(Constants.KEY_FORCAST_UPDATE_TIME, Calendar.getInstance().getTimeInMillis());
                editor.commit();
            }

            onWeatherDataArrivedListener.onWeatherDataArrived(weatherDataList);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private double changeKelvinToCelsius(double kelvin){
        return kelvin - 273.15;
    }

}
