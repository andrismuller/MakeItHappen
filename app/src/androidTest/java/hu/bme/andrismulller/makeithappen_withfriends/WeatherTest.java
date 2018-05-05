package hu.bme.andrismulller.makeithappen_withfriends;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import hu.bme.andrismulller.makeithappen_withfriends.Functions.Weather.DownloadWeatherDataTask;
import hu.bme.andrismulller.makeithappen_withfriends.MyUtils.Constants;
import hu.bme.andrismulller.makeithappen_withfriends.model.WeatherData;

import static org.junit.Assert.assertEquals;

/**
 * Created by Muller Andras on 4/22/2018.
 */

@RunWith(AndroidJUnit4.class)
public class WeatherTest implements DownloadWeatherDataTask.OnWeatherDataArrivedListener{

	List<WeatherData> weatherDataList;
	final CountDownLatch signal = new CountDownLatch(1);

	@Test
	public void downloadWeatherData() throws Exception {
		// Context of the app under test.
		Context appContext = InstrumentationRegistry.getTargetContext();

		DownloadWeatherDataTask downloadWeatherDataTask = new DownloadWeatherDataTask(this, Constants.REQUEST_WEATHER, appContext);
		downloadWeatherDataTask.execute(Constants.BUDAPEST_WEATHER_URL);

		signal.await();
		Assert.assertTrue( weatherDataList != null);
		Assert.assertTrue(weatherDataList.size() == 1);
		Assert.assertEquals(weatherDataList.get(0).getPlace(), "Budapest");
	}

	@Test
	public void downloadForecastData() throws Exception {
		// Context of the app under test.
		Context appContext = InstrumentationRegistry.getTargetContext();

		DownloadWeatherDataTask downloadWeatherDataTask = new DownloadWeatherDataTask(this, Constants.REQUEST_FORECAST, appContext);
		downloadWeatherDataTask.execute(Constants.FORECAST_URL);

		signal.await();
		Assert.assertTrue( weatherDataList != null);
		Assert.assertTrue(weatherDataList.size() == 40);
		Assert.assertEquals(weatherDataList.get(0).getPlace(), "Budapest");
	}

	@Override
	public void onWeatherDataArrived(List<WeatherData> data) {
		weatherDataList = data;
		signal.countDown();
	}
}
