package hu.bme.andrismulller.makeithappen_withfriends;


import android.content.Context;
import android.content.SharedPreferences;
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

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        todos = Todo.find(Todo.class, "is_done = 0");
        controllings = Controlling.find(Controlling.class, "activated = 1");
        requests = MyMessage.find(MyMessage.class, "is_request = 1");
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

        SharedPreferences myPref = getContext().getSharedPreferences(Constants.MY_PREFERENCE, Context.MODE_PRIVATE);
        if (myPref.getLong(Constants.KEY_WEATHER_UPDATE_TIME, 0) < Calendar.getInstance().getTimeInMillis() - Constants.WEATHER_UPDATE_INTERVAL) {
            DownloadWeatherDataTask downloadWeatherDataTask = new DownloadWeatherDataTask(this, Constants.REQUEST_WEATHER, getContext());
            downloadWeatherDataTask.execute(Constants.BUDAPEST_WEATHER_URL);
            WeatherData.deleteAll(WeatherData.class,"forecast = 0");
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
        weatherTV.setText(data.get(0).getPlace() + ": " + getString(R.string.temperature) + " (°C): " + data.get(0).getTemperature());
    }
}
